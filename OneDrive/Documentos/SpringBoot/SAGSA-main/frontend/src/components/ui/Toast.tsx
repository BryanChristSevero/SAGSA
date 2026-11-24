import { CheckCircle2, Info, TriangleAlert, X } from 'lucide-react';
import { createContext, useCallback, useContext, useState, type ReactNode } from 'react';

export type ToastTone = 'success' | 'error' | 'info';

interface ToastItem {
  id: number;
  message: string;
  tone: ToastTone;
}

interface ToastContextValue {
  notify: (message: string, tone?: ToastTone) => void;
}

const ToastContext = createContext<ToastContextValue | null>(null);

export function ToastProvider({ children }: { children: ReactNode }) {
  const [toasts, setToasts] = useState<ToastItem[]>([]);

  const remove = useCallback((id: number) => {
    setToasts((current) => current.filter((item) => item.id !== id));
  }, []);

  const notify = useCallback((message: string, tone: ToastTone = 'success') => {
    const id = Date.now() + Math.floor(Math.random() * 1000);
    setToasts((current) => [...current, { id, message, tone }]);
    window.setTimeout(() => remove(id), 5000);
  }, [remove]);

  const icons = {
    success: <CheckCircle2 aria-hidden="true" />, error: <TriangleAlert aria-hidden="true" />, info: <Info aria-hidden="true" />,
  };

  return (
    <ToastContext.Provider value={{ notify }}>
      {children}
      <div className="toast-region" aria-live="polite" aria-label="Notificações">
        {toasts.map((toast) => (
          <div className={`toast toast--${toast.tone}`} key={toast.id} role="status">
            {icons[toast.tone]}
            <span>{toast.message}</span>
            <button className="icon-button" type="button" aria-label="Fechar notificação" onClick={() => remove(toast.id)}>
              <X aria-hidden="true" />
            </button>
          </div>
        ))}
      </div>
    </ToastContext.Provider>
  );
}

export function useToast() {
  const value = useContext(ToastContext);
  if (!value) throw new Error('useToast deve ser usado dentro de ToastProvider');
  return value;
}
