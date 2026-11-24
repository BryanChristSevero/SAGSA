const configuredUrl = import.meta.env.VITE_API_URL?.trim() ?? '';

export const API_BASE_URL = configuredUrl.replace(/\/$/, '');
