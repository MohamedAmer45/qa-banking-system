/*
 * The application exposes a few functions on `window` so its own inline
 * handlers can call them. Tests reach for two: `navigate`, to open a view
 * without going through the sidebar (which is role-filtered since BUG-UI-002),
 * and `toast`, to raise a status message without having to cause one.
 */
declare global {
  interface Window {
    navigate?: (view: string) => void | Promise<void>;
    toast?: (title: string, message?: string, kind?: string) => void;
  }
}

export {};
