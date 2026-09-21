package com.bankingsystem.qa.db.support;

import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Direct SQL access to the same database the application serves.
 *
 * These tests deliberately bypass the API for their assertions: an endpoint
 * reporting a correct balance proves the endpoint agrees with itself, not that
 * the row underneath is right. Everything here reads the stored state.
 */
public final class Database {

    private Database() {
    }

    /**
     * Converts the application's connection string into a JDBC URL.
     *
     * The app, psql and DBeaver all take {@code postgresql://user:pass@host/db},
     * but the JDBC driver wants credentials as properties and its own scheme,
     * so the same DATABASE_URL can be shared across all of them.
     */
    static String jdbcUrl(String databaseUrl) {
        URI uri = URI.create(databaseUrl);

        StringBuilder url = new StringBuilder("jdbc:postgresql://")
                .append(uri.getHost());

        if (uri.getPort() != -1) {
            url.append(':').append(uri.getPort());
        }

        url.append(uri.getPath());

        String query = uri.getQuery();

        if (query != null && !query.isBlank()) {
            // channel_binding is a libpq option the JDBC driver rejects.
            String jdbcSafe = String.join("&",
                    java.util.Arrays.stream(query.split("&"))
                            .filter(p -> !p.startsWith("channel_binding="))
                            .toList());

            if (!jdbcSafe.isBlank()) {
                url.append('?').append(jdbcSafe);
            }
        }

        return url.toString();
    }

    private static String[] credentials(String databaseUrl) {
        String userInfo = URI.create(databaseUrl).getUserInfo();

        if (userInfo == null) {
            return new String[]{null, null};
        }

        int separator = userInfo.indexOf(':');

        return separator < 0
                ? new String[]{userInfo, null}
                : new String[]{
                userInfo.substring(0, separator),
                userInfo.substring(separator + 1)
        };
    }

    public static Connection connect() {
        String databaseUrl = Config.databaseUrl();
        String[] credentials = credentials(databaseUrl);

        try {
            return DriverManager.getConnection(
                    jdbcUrl(databaseUrl), credentials[0], credentials[1]
            );
        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Could not connect to the database. Check DATABASE_URL.", e
            );
        }
    }

    /** Every row of a query, each as an ordered column-to-value map. */
    public static List<Map<String, Object>> rows(String sql, Object... params) {
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            bind(statement, params);

            try (ResultSet results = statement.executeQuery()) {
                ResultSetMetaData meta = results.getMetaData();
                List<Map<String, Object>> rows = new ArrayList<>();

                while (results.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();

                    for (int i = 1; i <= meta.getColumnCount(); i++) {
                        row.put(meta.getColumnLabel(i), results.getObject(i));
                    }

                    rows.add(row);
                }

                return rows;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Query failed: " + sql, e);
        }
    }

    public static Map<String, Object> row(String sql, Object... params) {
        List<Map<String, Object>> rows = rows(sql, params);
        return rows.isEmpty() ? null : rows.get(0);
    }

    /** First column of the first row, for counts and single values. */
    public static long number(String sql, Object... params) {
        Map<String, Object> row = row(sql, params);

        if (row == null || row.isEmpty()) {
            throw new IllegalStateException("No value returned by: " + sql);
        }

        Object value = row.values().iterator().next();
        return ((Number) value).longValue();
    }

    public static String text(String sql, Object... params) {
        Map<String, Object> row = row(sql, params);

        if (row == null || row.isEmpty()) {
            return null;
        }

        Object value = row.values().iterator().next();
        return value == null ? null : value.toString();
    }

    private static void bind(PreparedStatement statement, Object... params)
            throws SQLException {

        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
    }
}
