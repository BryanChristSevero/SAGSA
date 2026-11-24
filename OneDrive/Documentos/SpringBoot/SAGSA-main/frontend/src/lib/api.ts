import { API_BASE_URL } from '../config/env';
import type { ApiErrorBody } from '../types/api';

type TokenGetter = () => string | null;
type UnauthorizedHandler = () => void;

let getToken: TokenGetter = () => null;
let onUnauthorized: UnauthorizedHandler | undefined;

export class ApiError extends Error {
  constructor(
    message: string,
    public readonly status: number,
    public readonly body?: ApiErrorBody,
  ) {
    super(message);
    this.name = 'ApiError';
  }
}

export function configureApi(auth: { getToken: TokenGetter; onUnauthorized?: UnauthorizedHandler }) {
  getToken = auth.getToken;
  onUnauthorized = auth.onUnauthorized;
}

function messageFromBody(body: unknown, fallback: string) {
  if (!body || typeof body !== 'object') return fallback;
  const parsed = body as ApiErrorBody;
  if (typeof parsed.message === 'string') return parsed.message;
  const validationMessages = Object.values(parsed).filter((value): value is string => typeof value === 'string');
  return validationMessages.length ? validationMessages.join(' ') : fallback;
}

async function request<T>(path: string, init: RequestInit = {}): Promise<T> {
  const token = getToken();
  const headers = new Headers(init.headers);
  headers.set('Accept', 'application/json');
  if (init.body) headers.set('Content-Type', 'application/json');
  if (token) headers.set('Authorization', `Bearer ${token}`);

  const controller = new AbortController();
  const timeout = window.setTimeout(() => controller.abort(), 15_000);

  let response: Response;
  let text: string;
  try {
    response = await fetch(`${API_BASE_URL}${path}`, { ...init, headers, signal: controller.signal });
    text = response.status === 204 ? '' : await response.text();
  } catch (error) {
    if (error instanceof DOMException && error.name === 'AbortError') {
      throw new ApiError('O servidor demorou para responder. Tente novamente.', 408);
    }
    throw new ApiError('Não foi possível comunicar com o servidor. Verifique a conexão e tente novamente.', 0);
  } finally {
    window.clearTimeout(timeout);
  }

  if (response.status === 204) return undefined as T;

  let body: unknown;
  try {
    body = text ? JSON.parse(text) : undefined;
  } catch {
    body = text;
  }

  if (!response.ok) {
    if (response.status === 401) onUnauthorized?.();
    const fallback = response.status === 401
      ? 'Sua sessão expirou. Entre novamente para continuar.'
      : 'Não foi possível concluir a operação.';
    throw new ApiError(messageFromBody(body, fallback), response.status, body as ApiErrorBody | undefined);
  }
  return body as T;
}

export const api = {
  get: <T>(path: string) => request<T>(path),
  post: <T>(path: string, data: unknown) => request<T>(path, { method: 'POST', body: JSON.stringify(data) }),
  delete: (path: string) => request<void>(path, { method: 'DELETE' }),
};
