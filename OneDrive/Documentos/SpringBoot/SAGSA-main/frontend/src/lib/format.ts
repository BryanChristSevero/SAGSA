export function formatHours(value: number | null | undefined) {
  if (value === null || value === undefined) return '—';
  return `${value} h`;
}

export function formatDate(value: string | null | undefined) {
  if (!value) return '—';
  const date = new Date(value);
  return Number.isNaN(date.getTime())
    ? value
    : new Intl.DateTimeFormat('pt-BR', { dateStyle: 'medium', timeStyle: 'short' }).format(date);
}

export function shortText(value: string, length = 88) {
  return value.length > length ? `${value.slice(0, length).trimEnd()}…` : value;
}
