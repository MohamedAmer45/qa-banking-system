export class Environment {
  static get isCI(): boolean {
    return process.env.CI === "true";
  }

  static get baseUrl(): string {
    return (
      process.env.BASE_URL ??
      "http://localhost:3000"
    );
  }
}

