import * as Dialog from '@radix-ui/react-dialog';
import { TriangleAlert, X } from 'lucide-react';

interface ConfirmDialogProps {
  open: boolean;
  title: string;
  description: string;
  confirmLabel?: string;
  busy?: boolean;
  onOpenChange: (open: boolean) => void;
  onConfirm: () => void;
}

export function ConfirmDialog({
  open, title, description, confirmLabel = 'Excluir', busy = false, onOpenChange, onConfirm,
}: ConfirmDialogProps) {
  return (
    <Dialog.Root open={open} onOpenChange={onOpenChange}>
      <Dialog.Portal>
        <Dialog.Overlay className="dialog-overlay" />
        <Dialog.Content className="dialog-content dialog-content--small" aria-describedby="confirm-description">
          <div className="dialog-icon dialog-icon--danger"><TriangleAlert aria-hidden="true" /></div>
          <Dialog.Title>{title}</Dialog.Title>
          <Dialog.Description id="confirm-description">{description}</Dialog.Description>
          <Dialog.Close asChild>
            <button className="icon-button dialog-close" type="button" aria-label="Fechar"><X aria-hidden="true" /></button>
          </Dialog.Close>
          <div className="dialog-actions">
            <Dialog.Close asChild>
              <button className="button button--secondary" type="button" disabled={busy}>Cancelar</button>
            </Dialog.Close>
            <button className="button button--danger" type="button" onClick={onConfirm} disabled={busy}>
              {busy ? 'Excluindo…' : confirmLabel}
            </button>
          </div>
        </Dialog.Content>
      </Dialog.Portal>
    </Dialog.Root>
  );
}
