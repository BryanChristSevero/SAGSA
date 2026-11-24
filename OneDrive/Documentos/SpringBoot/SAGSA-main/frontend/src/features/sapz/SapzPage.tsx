import * as Dialog from '@radix-ui/react-dialog';
import { CalendarDays, Clock3, Eye, Plus, Sparkles, Trash2, UserRound, X } from 'lucide-react';
import { useCallback, useEffect, useMemo, useState, type FormEvent } from 'react';
import { ConfirmDialog } from '../../components/ui/ConfirmDialog';
import { EmptyState, ErrorState, LoadingState } from '../../components/ui/ContentState';
import { useToast } from '../../components/ui/Toast';
import { api, ApiError } from '../../lib/api';
import { formatDate, formatHours, shortText } from '../../lib/format';
import type { Modulo, Ppc, Sapz, Usuario } from '../../types/api';

type SapzForm = { titulo: string; descricao: string; desafio: string; cargaHorariaTotal: string; idModulo: string; usuarioId: string; };
const blankForm: SapzForm = { titulo: '', descricao: '', desafio: '', cargaHorariaTotal: '', idModulo: '', usuarioId: '' };

export function SapzPage() {
  const { notify } = useToast();
  const [sapzs, setSapzs] = useState<Sapz[]>([]);
  const [modules, setModules] = useState<Modulo[]>([]);
  const [ppcs, setPpcs] = useState<Ppc[]>([]);
  const [users, setUsers] = useState<Usuario[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [loadError, setLoadError] = useState<string | null>(null);
  const [formOpen, setFormOpen] = useState(false);
  const [form, setForm] = useState<SapzForm>(blankForm);
  const [formError, setFormError] = useState<string | null>(null);
  const [isSaving, setIsSaving] = useState(false);
  const [selected, setSelected] = useState<Sapz | null>(null);
  const [pendingDelete, setPendingDelete] = useState<Sapz | null>(null);
  const [isDeleting, setIsDeleting] = useState(false);

  const load = useCallback(async () => {
    setIsLoading(true); setLoadError(null);
    try {
      const [sapzResponse, moduleResponse, ppcResponse, userResponse] = await Promise.all([api.get<Sapz[]>('/api/sapzs'), api.get<Modulo[]>('/api/modulos'), api.get<Ppc[]>('/api/ppcs'), api.get<Usuario[]>('/api/usuarios')]);
      setSapzs(sapzResponse); setModules(moduleResponse); setPpcs(ppcResponse); setUsers(userResponse);
    } catch (error) { setLoadError(error instanceof ApiError ? error.message : 'Não foi possível carregar as SAPZs.'); } finally { setIsLoading(false); }
  }, []);

  useEffect(() => { void load(); }, [load]);
  const records = useMemo(() => [...sapzs].sort((a, b) => new Date(b.dataGeracao).getTime() - new Date(a.dataGeracao).getTime()), [sapzs]);

  function openForm() { setForm(blankForm); setFormError(null); setFormOpen(true); }
  function updateField(field: keyof SapzForm, value: string) { setForm((current) => ({ ...current, [field]: value })); }

  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (Object.values(form).some((value) => !value.trim())) { setFormError('Preencha todos os campos obrigatórios.'); return; }
    if (Number(form.cargaHorariaTotal) < 1) { setFormError('A carga horária total deve ser maior que zero.'); return; }
    setFormError(null); setIsSaving(true);
    try {
      const created = await api.post<Sapz>('/api/sapzs', { ...form, cargaHorariaTotal: Number(form.cargaHorariaTotal), idModulo: Number(form.idModulo), usuarioId: Number(form.usuarioId) });
      setSapzs((current) => [created, ...current]); setFormOpen(false); notify('SAPZ cadastrada com sucesso.');
    } catch (error) { setFormError(error instanceof ApiError ? error.message : 'Não foi possível cadastrar a SAPZ.'); } finally { setIsSaving(false); }
  }

  async function remove() {
    if (!pendingDelete) return;
    setIsDeleting(true);
    try { await api.delete(`/api/sapzs/${pendingDelete.id}`); setSapzs((current) => current.filter((item) => item.id !== pendingDelete.id)); setPendingDelete(null); notify('SAPZ excluída com sucesso.'); }
    catch (error) { notify(error instanceof ApiError ? error.message : 'Não foi possível excluir a SAPZ.', 'error'); }
    finally { setIsDeleting(false); }
  }

  return <section className="sapz-page">
    <div className="page-intro"><div><p>Registre situações de aprendizagem a partir dos módulos e responsáveis disponíveis.</p></div><button className="button button--primary" type="button" onClick={openForm}><Plus aria-hidden="true" /> Nova SAPZ</button></div>
    <div className="contract-note"><Sparkles aria-hidden="true" /><div><strong>Fluxo disponível na API</strong><p>A criação exige um módulo e um usuário. O PPC vinculado ao módulo fica visível na seleção; esta tela registra e consulta SAPZs, sem geração automática.</p></div></div>
    {isLoading ? <div className="surface"><LoadingState label="Carregando SAPZs e vínculos disponíveis…" /></div> : loadError ? <div className="surface"><ErrorState message={loadError} onRetry={() => void load()} /></div> : !records.length ? <div className="surface"><EmptyState title="Nenhuma SAPZ cadastrada" description="Registre uma situação de aprendizagem para ela aparecer aqui." action={<button className="button button--secondary" type="button" onClick={openForm}>Cadastrar SAPZ</button>} /></div> : <div className="sapz-grid">{records.map((sapz) => <article className="sapz-card" key={sapz.id}><div className="sapz-card-top"><span className="sapz-mark"><Sparkles aria-hidden="true" /></span><div className="sapz-actions"><button className="icon-button" type="button" onClick={() => setSelected(sapz)} aria-label="Ver detalhes da SAPZ"><Eye aria-hidden="true" /></button><button className="icon-button icon-button--danger" type="button" onClick={() => setPendingDelete(sapz)} aria-label="Excluir SAPZ"><Trash2 aria-hidden="true" /></button></div></div><h3>{sapz.titulo}</h3><p>{shortText(sapz.descricao, 140)}</p><div className="sapz-challenge"><strong>Desafio</strong><span>{shortText(sapz.desafio, 100)}</span></div><footer><span><Clock3 aria-hidden="true" />{formatHours(sapz.cargaHorariaTotal)}</span><span><UserRound aria-hidden="true" />{sapz.nomeUsuario ?? 'Usuário não informado'}</span></footer></article>)}</div>}
    <Dialog.Root open={formOpen} onOpenChange={(open) => { if (!isSaving) setFormOpen(open); }}><Dialog.Portal><Dialog.Overlay className="dialog-overlay" /><Dialog.Content className="dialog-content dialog-content--form"><div className="dialog-heading"><span className="eyebrow">NOVA SITUAÇÃO</span><Dialog.Title>Registrar SAPZ</Dialog.Title><Dialog.Description>Os dados são enviados diretamente para <code>/api/sapzs</code>.</Dialog.Description></div><Dialog.Close asChild><button className="icon-button dialog-close" type="button" disabled={isSaving} aria-label="Fechar"><X aria-hidden="true" /></button></Dialog.Close><form className="data-form" onSubmit={submit}>{formError && <div className="form-alert form-alert--error" role="alert">{formError}</div>}<div className="form-grid"><label className="field"><span>Título <b aria-hidden="true">*</b></span><input value={form.titulo} onChange={(event) => updateField('titulo', event.target.value)} required placeholder="Título da SAPZ" /></label><label className="field"><span>Carga horária total <b aria-hidden="true">*</b></span><input type="number" min="1" step="1" value={form.cargaHorariaTotal} onChange={(event) => updateField('cargaHorariaTotal', event.target.value)} required placeholder="Ex.: 24" /></label><label className="field"><span>Módulo <b aria-hidden="true">*</b></span><select value={form.idModulo} onChange={(event) => updateField('idModulo', event.target.value)} required><option value="">Selecione o módulo</option>{modules.map((item) => {
      const ppc = ppcs.find((candidate) => candidate.id === item.idPpc);
      const ppcLabel = ppc ? `${ppc.nomeCurso ?? 'PPC'} · ${ppc.anoVigencia}` : 'PPC não informado';
      return <option key={item.id} value={item.id}>{item.nomeModulo} · {ppcLabel}</option>;
    })}</select><small>{modules.length ? 'O PPC vinculado ao módulo aparece junto ao nome.' : 'Cadastre um módulo antes de criar a SAPZ.'}</small></label><label className="field"><span>Responsável <b aria-hidden="true">*</b></span><select value={form.usuarioId} onChange={(event) => updateField('usuarioId', event.target.value)} required><option value="">Selecione o usuário</option>{users.map((item) => <option key={item.idUsuario} value={item.idUsuario}>{item.nome} · {item.email}</option>)}</select><small>{users.length ? 'Usuário que será associado ao registro.' : 'Cadastre um usuário antes de criar a SAPZ.'}</small></label><label className="field field--wide"><span>Descrição <b aria-hidden="true">*</b></span><textarea rows={4} value={form.descricao} onChange={(event) => updateField('descricao', event.target.value)} required placeholder="Descreva a situação de aprendizagem." /></label><label className="field field--wide"><span>Desafio <b aria-hidden="true">*</b></span><textarea rows={4} value={form.desafio} onChange={(event) => updateField('desafio', event.target.value)} required placeholder="Descreva o desafio proposto." /></label></div><div className="dialog-actions"><Dialog.Close asChild><button className="button button--secondary" type="button" disabled={isSaving}>Cancelar</button></Dialog.Close><button className="button button--primary" type="submit" disabled={isSaving || !modules.length || !users.length}>{isSaving ? 'Registrando…' : 'Registrar SAPZ'}</button></div></form></Dialog.Content></Dialog.Portal></Dialog.Root>
    <Dialog.Root open={selected !== null} onOpenChange={(open) => { if (!open) setSelected(null); }}><Dialog.Portal><Dialog.Overlay className="dialog-overlay" /><Dialog.Content className="dialog-content dialog-content--small"><Dialog.Title>Detalhes da SAPZ</Dialog.Title><Dialog.Description>Informações registradas na API.</Dialog.Description><Dialog.Close asChild><button className="icon-button dialog-close" type="button" aria-label="Fechar"><X aria-hidden="true" /></button></Dialog.Close>{selected && <dl className="detail-list"><div><dt>Título</dt><dd>{selected.titulo}</dd></div><div><dt>Descrição</dt><dd>{selected.descricao}</dd></div><div><dt>Desafio</dt><dd>{selected.desafio}</dd></div><div><dt>Responsável</dt><dd>{selected.nomeUsuario ?? 'Não informado'}</dd></div><div><dt>Carga horária total</dt><dd>{formatHours(selected.cargaHorariaTotal)}</dd></div><div><dt>Data de geração</dt><dd><CalendarDays aria-hidden="true" /> {formatDate(selected.dataGeracao)}</dd></div></dl>}<div className="dialog-actions"><Dialog.Close asChild><button className="button button--secondary" type="button">Fechar</button></Dialog.Close></div></Dialog.Content></Dialog.Portal></Dialog.Root>
    <ConfirmDialog open={pendingDelete !== null} onOpenChange={(open) => { if (!open && !isDeleting) setPendingDelete(null); }} title="Excluir SAPZ?" description="As atividades associadas podem ser afetadas pela exclusão. Esta ação não pode ser desfeita." busy={isDeleting} onConfirm={() => void remove()} />
  </section>;
}
