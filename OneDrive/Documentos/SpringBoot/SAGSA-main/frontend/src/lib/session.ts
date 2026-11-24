const TOKEN_KEY = 'sagsa.access-token';

export interface Session {
  token: string;
  email: string;
  expiresAt: number | null;
}

interface JwtPayload {
  sub?: string;
  exp?: number;
}

function decodePayload(token: string): JwtPayload | null {
  try {
    const encoded = token.split('.')[1];
    if (!encoded) return null;
    const normalized = encoded.replace(/-/g, '+').replace(/_/g, '/');
    const json = decodeURIComponent(
      Array.from(atob(normalized))
        .map((character) => `%${character.charCodeAt(0).toString(16).padStart(2, '0')}`)
        .join(''),
    );
    return JSON.parse(json) as JwtPayload;
  } catch {
    return null;
  }
}

export function readSession(): Session | null {
  const token = sessionStorage.getItem(TOKEN_KEY);
  if (!token) return null;
  const payload = decodePayload(token);
  const expiresAt = payload?.exp ? payload.exp * 1000 : null;
  if (!payload?.sub || (expiresAt !== null && expiresAt <= Date.now())) {
    clearSession();
    return null;
  }
  return { token, email: payload.sub, expiresAt };
}

export function saveSession(token: string): Session | null {
  sessionStorage.setItem(TOKEN_KEY, token);
  return readSession();
}

export function clearSession() {
  sessionStorage.removeItem(TOKEN_KEY);
}
