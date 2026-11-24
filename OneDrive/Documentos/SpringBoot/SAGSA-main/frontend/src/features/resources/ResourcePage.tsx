import * as Dialog from '@radix-ui/react-dialog';
import { ArrowDownUp, Eye, Plus, Search, Trash2, X } from 'lucide-react';
import { useCallback, useEffect, useMemo, useRef, useState, type FormEvent } from 'react';
import { ConfirmDialog } from '../../components/ui/ConfirmDialog';
import { EmptyState, ErrorState, LoadingState } from '../../components/ui/ContentState';
import { useToast } from '../../components/ui/Toast';
import { api, ApiError } from '../../lib/api';
import { fetchLookup, getEmptyForm, type FormField, type FormValues, type LookupOption, type ResourceDefinition } from './resourceDefinitions';

const PAGE_SIZE = 8;

interface ResourcePageProps<T extends object> {
  resource: ResourceDefinition<T>;
}

function isBlank(value: string) {
  return value.trim().length === 0;
}

export function ResourcePage<T extends object>({ resource }: ResourcePageProps<T>) {
  const { notify } = useToast();
  const [items, setItems] = useState<T[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [loadError, setLoadError] = useState<string | null>(null);
  const [query, setQuery] = useState('');
  const [sortKey, setSortKey] = useState(resource.columns[0]?.key ?? '');
  const [sortDescending, setSortDescending] = useState(false);
  const [page, setPage] = useState(1);
  const [formOpen, setFormOpen] = useState(false);
  const [formValues, setFormValues] = useState<FormValues>(() => getEmptyForm(resource.fields));
  const [formError, setFormError] = useState<string | null>(null);
  const [isSaving, setIsSaving] = useState(false);
  const [lookupLoading, setLookupLoading] = useState(false);
  const [lookups, setLookups] = useState<Record<string, LookupOption[]>>({});
  const [lookupError, setLookupError] = useState<string | null>(null);
  const [selected, setSelected] = useState<T | null>(null);
  const [pendingDelete, setPendingDelete] = useState<T | null>(null);
  const [isDeleting, setIsDeleting] = useState(false);
  const loadVersion = useRef(0);

  useEffect(() => {
    setItems([]);
    setIsLoading(true);
    setLoadError(null);
    setQuery('');
    setSortKey(resource.columns[0]?.key ?? '');
    setSortDescending(false);
    setPage(1);
    setFormOpen(false);
    setSelected(null);
    setPendingDelete(null);
    setLookups({});
    setLookupError(null);
  }, [resource.key]);

  const loadItems = useCallback(async () => {
    const version = ++loadVersion.current;
    setIsLoading(true);
    setLoadError(null);
    try {
      const response = await api.get<T[]>(resource.endpoint);
      if (version !== loadVersion.current) return;
      setItems(response);
      const relatedLookups = [...new Set(resource.columns.flatMap((column) => column.lookup ? [column.lookup] : []))];
      if (relatedLookups.length) {
        try {
          const entries = await Promise.all(relatedLookups.map(async (key) => [key, await fetchLookup(key)] as const));
          if (version !== loadVersion.current) return;
          setLookups((current) => ({ ...current, ...Object.fromEntries(entries) }));
          setLookupError(null);
        } catch (error) {
          if (version !== loadVersion.current) return;
          setLookupError(error instanceof ApiError ? error.message : 'Não foi possível carregar os nomes dos registros relacionados.');
        }
      }
    } catch (error) {
      if (version !== loadVersion.current) return;
      setLoadError(error instanceof ApiError ? error.message : 'Não foi possível carregar os dados solicitados.');
    } finally {
      if (version === loadVersion.current) setIsLoading(false);
    }
  }, [resource]);

  useEffect(() => {
    void loadItems();
    return () => { loadVersion.current += 1; };
  }, [loadItems]);
  useEffect(() => { setPage(1); }, [query, sortKey, sortDescending]);

  const filteredItems = useMemo(() => {
    const normalizedQuery = query.trim().toLocaleLowerCase('pt-BR');
    const matching = normalizedQuery
      ? items.filter((item) => {
        const relatedText = resource.columns.flatMap((column) => {
          if (!column.lookup) return [];
          const value = column.value(item);
          return value == null ? [] : [lookups[column.lookup]?.find((option) => option.value === String(value))?.label ?? ''];
        }).join(' ');
        return `${resource.searchText(item)} ${relatedText}`.toLocaleLowerCase('pt-BR').includes(normalizedQuery);
      })
      : items;
    const column = resource.columns.find((item) => item.key === sortKey) ?? resource.columns[0];
    if (!column) return matching;
    return [...matching].sort((left, right) => {
      const leftValue = String(column.value(left) ?? '');
      const rightValue = String(column.value(right) ?? '');
      const result = leftValue.localeCompare(rightValue, 'pt-BR', { numeric: true, sensitivity: 'base' });
      return sortDescending ? -result : result;
    });
  }, [items, lookups, query, resource, sortDescending, sortKey]);
  useEffect(() => {
    setPage((current) => Math.min(current, Math.max(1, Math.ceil(filteredItems.length / PAGE_SIZE))));
  }, [filteredItems.length]);

  const pageCount = Math.max(1, Math.ceil(filteredItems.length / PAGE_SIZE));
  const visibleItems = filteredItems.slice((page - 1) * PAGE_SIZE, page * PAGE_SIZE);

  async function loadLookups() {
    const requiredLookups = [...new Set(resource.fields.flatMap((field) => field.lookup ? [field.lookup] : []))];
    if (!requiredLookups.length) return;
    setLookupLoading(true);
    try {
      const entries = await Promise.all(requiredLookups.map(async (key) => [key, await fetchLookup(key)] as const));
      setLookups((current) => ({ ...current, ...Object.fromEntries(entries) }));
    } catch (error) {
      setFormError(error instanceof ApiError ? error.message : 'Não foi possível carregar as opções relacionadas.');
    } finally {
      setLookupLoading(false);
    }
  }

  function openNewForm() {
    setFormValues(getEmptyForm(resource.fields));
    setFormError(null);
    setFormOpen(true);
    void loadLookups();
  }

  function validate() {
    const missing = resource.fields.find((field) => field.required && isBlank(formValues[field.name] ?? ''));
    if (missing) return `Preencha o campo obrigatório: ${missing.label}.`;
    const invalidNumber = resource.fields.find((field) => {
      if (field.type !== 'number' || !formValues[field.name]) return false;
      const value = Number(formValues[field.name]);
      return !Number.isFinite(value) || !Number.isInteger(value) || value < (field.min ?? -Infinity);
    });
    if (invalidNumber) return `${invalidNumber.label} deve ser um número inteiro maior ou igual a ${invalidNumber.min}.`;
    const password = resource.fields.find((field) => field.type === 'password' && field.required && (formValues[field.name] ?? '').length < 6);
    if (password) return 'A senha deve ter pelo menos 6 caracteres.';
    return null;
  }

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const validationError = validate();
    if (validationError) { setFormError(validationError); return; }
    setFormError(null);
    setIsSaving(true);
    try {
      const created = await api.post<T>(resource.endpoint, resource.toPayload(formValues));
      setItems((current) => [created, ...current]);
      setFormOpen(false);
      notify(`${resource.singular.charAt(0).toUpperCase()}${resource.singular.slice(1)} cadastrado com sucesso.`);
    } catch (error) {
      setFormError(error instanceof ApiError ? error.message : 'Não foi possível concluir o cadastro.');
    } finally {
      setIsSaving(false);
    }
  }

  async function handleDelete() {
    if (!pendingDelete) return;
    setIsDeleting(true);
    try {
      const id = resource.getId(pendingDelete);
      await api.delete(`${resource.endpoint}/${id}`);
      setItems((current) => current.filter((item) => resource.getId(item) !== id));
      setPendingDelete(null);
      notify(`${resource.singular.charAt(0).toUpperCase()}${resource.singular.slice(1)} excluído com sucesso.`);
    } catch (error) {
      notify(error instanceof ApiError ? error.message : 'Não foi possível excluir o registro.', 'error');
    } finally {
      setIsDeleting(false);
    }
  }

  function renderField(field: FormField) {
    const common = { id: `field-${field.name}`, name: field.name, value: formValues[field.name] ?? '', required: field.required, placeholder: field.placeholder, onChange: (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => setFormValues((current) => ({ ...current, [field.name]: event.target.value })) };
    const options = field.options ?? (field.lookup ? lookups[field.lookup] ?? [] : []);
    return (
      <label className={`field ${field.type === 'textarea' ? 'field--wide' : ''}`} key={field.name}>
        <span>{field.label}{field.required && <b aria-hidden="true"> *</b>}</span>
        {field.type === 'textarea' ? <textarea {...common} rows={4} /> : field.type === 'select' ? (
          <select {...common} disabled={lookupLoading}><option value="">Selecione uma opção</option>{options.map((option) => <option key={option.value} value={option.value}>{option.label}</option>)}</select>
        ) : <input {...common} type={field.type} min={field.min} step={field.type === 'number' ? 1 : undefined} />}
        {field.hint && <small>{field.hint}</small>}
      </label>
    );
  }

  return (
    <section className="resource-page">
      <div className="page-intro"><div><p>{resource.description}</p></div><button className="button button--primary" type="button" onClick={openNewForm}><Plus aria-hidden="true" /> Novo {resource.singular}</button></div>
      <div className="surface resource-surface">
        <div className="table-toolbar"><div className="search-field"><Search aria-hidden="true" /><input value={query} onChange={(event) => setQuery(event.target.value)} placeholder={`Buscar ${resource.plural.toLocaleLowerCase('pt-BR')}…`} aria-label={`Buscar ${resource.plural.toLocaleLowerCase('pt-BR')}`} /></div><label className="sort-select"><ArrowDownUp aria-hidden="true" /><span>Ordenar</span><select value={`${sortKey}:${sortDescending ? 'desc' : 'asc'}`} onChange={(event) => { const [key, direction] = event.target.value.split(':'); setSortKey(key); setSortDescending(direction === 'desc'); }}><>{resource.columns.map((column) => <option key={`${column.key}:asc`} value={`${column.key}:asc`}>{column.label} (A–Z)</option>)}{resource.columns.map((column) => <option key={`${column.key}:desc`} value={`${column.key}:desc`}>{column.label} (Z–A)</option>)}</></select></label></div>
        {lookupError && <div className="form-alert" role="status">{lookupError} Os vínculos serão exibidos por identificador até que estejam disponíveis.</div>}
        {isLoading ? <LoadingState /> : loadError ? <ErrorState message={loadError} onRetry={() => void loadItems()} /> : !visibleItems.length ? <EmptyState title={query ? 'Nenhum resultado encontrado' : `Nenhum ${resource.singular} cadastrado`} description={query ? 'Ajuste a busca para encontrar outro registro.' : `Cadastre o primeiro ${resource.singular} para começar.`} action={!query ? <button className="button button--secondary" type="button" onClick={openNewForm}>Cadastrar {resource.singular}</button> : undefined} /> : (
          <><div className="table-wrap"><table><thead><tr>{resource.columns.map((column) => <th key={column.key} scope="col">{column.label}</th>)}<th scope="col" className="table-actions-heading">Ações</th></tr></thead><tbody>{visibleItems.map((item) => <tr key={resource.getId(item)}>{resource.columns.map((column) => {
            const value = column.value(item);
            const relatedLabel = column.lookup && value != null
              ? lookups[column.lookup]?.find((option) => option.value === String(value))?.label
              : undefined;
            return <td key={column.key} data-label={column.label}>{relatedLabel ?? (column.display ? column.display(item) : String(value ?? '—'))}</td>;
          })}<td data-label="Ações" className="row-actions"><button className="icon-button" type="button" aria-label={`Visualizar ${resource.singular}`} title="Visualizar" onClick={() => setSelected(item)}><Eye aria-hidden="true" /></button><button className="icon-button icon-button--danger" type="button" aria-label={`Excluir ${resource.singular}`} title="Excluir" onClick={() => setPendingDelete(item)}><Trash2 aria-hidden="true" /></button></td></tr>)}</tbody></table></div><div className="pagination"><span>{filteredItems.length} {filteredItems.length === 1 ? 'registro' : 'registros'}</span><div><button className="button button--quiet" type="button" disabled={page === 1} onClick={() => setPage((current) => current - 1)}>Anterior</button><span>Página {Math.min(page, pageCount)} de {pageCount}</span><button className="button button--quiet" type="button" disabled={page >= pageCount} onClick={() => setPage((current) => current + 1)}>Próxima</button></div></div></>
        )}
      </div>
      <Dialog.Root open={formOpen} onOpenChange={(open) => { if (!isSaving) setFormOpen(open); }}><Dialog.Portal><Dialog.Overlay className="dialog-overlay" /><Dialog.Content className="dialog-content dialog-content--form"><div className="dialog-heading"><span className="eyebrow">NOVO REGISTRO</span><Dialog.Title>Cadastrar {resource.singular}</Dialog.Title><Dialog.Description>Os campos com <b>*</b> são obrigatórios.</Dialog.Description></div><Dialog.Close asChild><button className="icon-button dialog-close" type="button" disabled={isSaving} aria-label="Fechar"><X aria-hidden="true" /></button></Dialog.Close><form className="data-form" onSubmit={handleSubmit}>{formError && <div className="form-alert form-alert--error" role="alert">{formError}</div>}{lookupLoading && <div className="form-alert">Carregando opções relacionadas…</div>}<div className="form-grid">{resource.fields.map(renderField)}</div><div className="dialog-actions"><Dialog.Close asChild><button className="button button--secondary" type="button" disabled={isSaving}>Cancelar</button></Dialog.Close><button className="button button--primary" type="submit" disabled={isSaving || lookupLoading}>{isSaving ? 'Salvando…' : `Cadastrar ${resource.singular}`}</button></div></form></Dialog.Content></Dialog.Portal></Dialog.Root>
      <Dialog.Root open={selected !== null} onOpenChange={(open) => { if (!open) setSelected(null); }}><Dialog.Portal><Dialog.Overlay className="dialog-overlay" /><Dialog.Content className="dialog-content dialog-content--small"><Dialog.Title>Detalhes do registro</Dialog.Title><Dialog.Description>Informações retornadas pela API.</Dialog.Description><Dialog.Close asChild><button className="icon-button dialog-close" type="button" aria-label="Fechar"><X aria-hidden="true" /></button></Dialog.Close>{selected && <dl className="detail-list">{resource.detail(selected, lookups).map((item) => <div key={item.label}><dt>{item.label}</dt><dd>{item.value}</dd></div>)}</dl>}<div className="dialog-actions"><Dialog.Close asChild><button className="button button--secondary" type="button">Fechar</button></Dialog.Close></div></Dialog.Content></Dialog.Portal></Dialog.Root>
      <ConfirmDialog open={pendingDelete !== null} onOpenChange={(open) => { if (!open && !isDeleting) setPendingDelete(null); }} title={`Excluir ${resource.singular}?`} description="Esta ação remove o registro de forma permanente e não pode ser desfeita." busy={isDeleting} onConfirm={() => void handleDelete()} />
    </section>
  );
}
