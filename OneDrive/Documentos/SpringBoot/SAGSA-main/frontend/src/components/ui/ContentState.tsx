import { CircleAlert, Inbox, LoaderCircle } from 'lucide-react';

export function LoadingState({ label = 'Carregando dados…' }: { label?: string }) {
  return <div className="content-state"><LoaderCircle className="spin" aria-hidden="true" /><p>{label}</p></div>;
}

export function EmptyState({ title, description, action }: { title: string; description: string; action?: React.ReactNode }) {
  return <div className="content-state"><Inbox aria-hidden="true" /><h3>{title}</h3><p>{description}</p>{action}</div>;
}

export function ErrorState({ message, onRetry }: { message: string; onRetry: () => void }) {
  return <div className="content-state content-state--error"><CircleAlert aria-hidden="true" /><h3>Não foi possível carregar</h3><p>{message}</p><button className="button button--secondary" type="button" onClick={onRetry}>Tentar novamente</button></div>;
}
