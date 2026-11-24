import { api } from '../../lib/api';
import { formatHours, shortText } from '../../lib/format';
import type { Atividade, Capacidade, Criterio, Curso, Entity, Id, Modulo, Ppc, Sapz, TipoPerfil, Usuario } from '../../types/api';

export type ResourceKey = 'courses' | 'ppcs' | 'modules' | 'capacities' | 'criteria' | 'activities' | 'users';
export type FormValues = Record<string, string>;

export interface LookupOption { value: string; label: string; }

export interface FormField {
  name: string;
  label: string;
  type: 'text' | 'textarea' | 'number' | 'email' | 'password' | 'select';
  required?: boolean;
  min?: number;
  placeholder?: string;
  hint?: string;
  options?: LookupOption[];
  lookup?: LookupKey | 'sapz';
}

export interface ResourceColumn<T> {
  key: string;
  label: string;
  value: (item: T) => string | number | null | undefined;
  display?: (item: T) => string;
  lookup?: LookupKey | 'sapz';
}

export interface ResourceDefinition<T extends object> {
  key: ResourceKey;
  singular: string;
  plural: string;
  description: string;
  endpoint: string;
  getId: (item: T) => Id;
  fields: FormField[];
  columns: ResourceColumn<T>[];
  searchText: (item: T) => string;
  toPayload: (values: FormValues) => object;
  detail: (item: T, lookups: Record<string, LookupOption[]>) => Array<{ label: string; value: string }>;
}

const number = (value: string) => Number(value);
const lookupLabel = (options: LookupOption[] | undefined, id: number | null, label: string) => (
  id == null ? 'Não informada' : options?.find((option) => option.value === String(id))?.label ?? `${label} #${id}`
);

export const resources: { [K in ResourceKey]: ResourceDefinition<ResourceTypeMap[K]> } = {
  courses: {
    key: 'courses', singular: 'curso', plural: 'Cursos', description: 'Estruture a oferta formativa e seus eixos tecnológicos.', endpoint: '/api/cursos', getId: (item) => item.id,
    fields: [
      { name: 'nomeCurso', label: 'Nome do curso', type: 'text', required: true, placeholder: 'Ex.: Técnico em Desenvolvimento de Sistemas' },
      { name: 'eixoTecnologico', label: 'Eixo tecnológico', type: 'text', required: true, placeholder: 'Ex.: Informação e Comunicação' },
      { name: 'cargaHorariaTotal', label: 'Carga horária total', type: 'number', required: true, min: 1, placeholder: 'Ex.: 1200', hint: 'Informe o valor em horas.' },
    ],
    columns: [
      { key: 'nomeCurso', label: 'Curso', value: (item) => item.nomeCurso },
      { key: 'eixoTecnologico', label: 'Eixo tecnológico', value: (item) => item.eixoTecnologico },
      { key: 'cargaHorariaTotal', label: 'Carga horária', value: (item) => item.cargaHorariaTotal, display: (item) => formatHours(item.cargaHorariaTotal) },
    ],
    searchText: (item) => `${item.nomeCurso} ${item.eixoTecnologico}`,
    toPayload: (values) => ({ ...values, cargaHorariaTotal: number(values.cargaHorariaTotal) }),
    detail: (item) => [{ label: 'Nome do curso', value: item.nomeCurso }, { label: 'Eixo tecnológico', value: item.eixoTecnologico }, { label: 'Carga horária total', value: formatHours(item.cargaHorariaTotal) }],
  },
  ppcs: {
    key: 'ppcs', singular: 'PPC', plural: 'PPCs', description: 'Mantenha os Projetos Pedagógicos de Curso vinculados à oferta.', endpoint: '/api/ppcs', getId: (item) => item.id,
    fields: [
      { name: 'idCurso', label: 'Curso', type: 'select', required: true, lookup: 'courses', hint: 'A API exige um curso já cadastrado.' },
      { name: 'anoVigencia', label: 'Ano de vigência', type: 'number', required: true, min: 1, placeholder: 'Ex.: 2026' },
    ],
    columns: [{ key: 'nomeCurso', label: 'Curso', value: (item) => item.nomeCurso, display: (item) => item.nomeCurso ?? 'Não informado' }, { key: 'anoVigencia', label: 'Vigência', value: (item) => item.anoVigencia }],
    searchText: (item) => `${item.nomeCurso ?? ''} ${item.anoVigencia}`,
    toPayload: (values) => ({ idCurso: number(values.idCurso), anoVigencia: number(values.anoVigencia) }),
    detail: (item) => [{ label: 'Curso', value: item.nomeCurso ?? 'Não informado' }, { label: 'Ano de vigência', value: String(item.anoVigencia) }],
  },
  modules: {
    key: 'modules', singular: 'módulo', plural: 'Módulos', description: 'Organize os componentes formativos de cada PPC.', endpoint: '/api/modulos', getId: (item) => item.id,
    fields: [
      { name: 'idPpc', label: 'PPC', type: 'select', required: true, lookup: 'ppcs', hint: 'O vínculo com o PPC também aparece na listagem.' },
      { name: 'nomeModulo', label: 'Nome do módulo', type: 'text', required: true, placeholder: 'Ex.: Fundamentos de Programação' },
      { name: 'descricao', label: 'Descrição', type: 'textarea', required: true, placeholder: 'Descreva o escopo do módulo.' },
    ],
    columns: [{ key: 'nomeModulo', label: 'Módulo', value: (item) => item.nomeModulo }, { key: 'descricao', label: 'Descrição', value: (item) => item.descricao, display: (item) => shortText(item.descricao) }, { key: 'idPpc', label: 'PPC', value: (item) => item.idPpc, display: (item) => item.idPpc ? `PPC #${item.idPpc}` : 'Não informado', lookup: 'ppcs' }],
    searchText: (item) => `${item.nomeModulo} ${item.descricao}`,
    toPayload: (values) => ({ idPpc: number(values.idPpc), nomeModulo: values.nomeModulo, descricao: values.descricao }),
    detail: (item, lookups) => [{ label: 'Nome do módulo', value: item.nomeModulo }, { label: 'Descrição', value: item.descricao }, { label: 'PPC vinculado', value: lookupLabel(lookups.ppcs, item.idPpc, 'PPC') }],
  },
  capacities: {
    key: 'capacities', singular: 'capacidade', plural: 'Capacidades', description: 'Registre as capacidades associadas aos módulos acadêmicos.', endpoint: '/api/capacidades', getId: (item) => item.id,
    fields: [
      { name: 'idModulo', label: 'Módulo', type: 'select', required: true, lookup: 'modules', hint: 'O vínculo com o módulo também aparece na listagem.' },
      { name: 'tipoCapacidade', label: 'Tipo de capacidade', type: 'text', required: true, placeholder: 'Ex.: Técnica, social ou organizativa' },
      { name: 'descricao', label: 'Descrição', type: 'textarea', required: true, placeholder: 'Descreva a capacidade.' },
    ],
    columns: [{ key: 'tipoCapacidade', label: 'Tipo', value: (item) => item.tipoCapacidade }, { key: 'descricao', label: 'Descrição', value: (item) => item.descricao, display: (item) => shortText(item.descricao) }, { key: 'idModulo', label: 'Módulo', value: (item) => item.idModulo, display: (item) => item.idModulo ? `Módulo #${item.idModulo}` : 'Não informado', lookup: 'modules' }],
    searchText: (item) => `${item.tipoCapacidade} ${item.descricao}`,
    toPayload: (values) => ({ idModulo: number(values.idModulo), tipoCapacidade: values.tipoCapacidade, descricao: values.descricao }),
    detail: (item, lookups) => [{ label: 'Tipo de capacidade', value: item.tipoCapacidade }, { label: 'Descrição', value: item.descricao }, { label: 'Módulo vinculado', value: lookupLabel(lookups.modules, item.idModulo, 'Módulo') }],
  },
  criteria: {
    key: 'criteria', singular: 'critério de avaliação', plural: 'Critérios de avaliação', description: 'Defina evidências objetivas para acompanhar as capacidades.', endpoint: '/api/criterios', getId: (item) => item.id,
    fields: [
      { name: 'idCapacidade', label: 'Capacidade', type: 'select', required: true, lookup: 'capacities' },
      { name: 'descricao', label: 'Descrição do critério', type: 'textarea', required: true, placeholder: 'Ex.: Desenvolve a solução seguindo os requisitos.' },
    ],
    columns: [{ key: 'descricao', label: 'Critério', value: (item) => item.descricao, display: (item) => shortText(item.descricao) }, { key: 'idCapacidade', label: 'Capacidade vinculada', value: (item) => item.idCapacidade, display: (item) => item.idCapacidade ? `Capacidade #${item.idCapacidade}` : 'Não informada', lookup: 'capacities' }],
    searchText: (item) => `${item.descricao} ${item.idCapacidade ?? ''}`,
    toPayload: (values) => ({ idCapacidade: number(values.idCapacidade), descricao: values.descricao }),
    detail: (item, lookups) => [{ label: 'Descrição', value: item.descricao }, { label: 'Capacidade vinculada', value: lookupLabel(lookups.capacities, item.idCapacidade, 'Capacidade') }],
  },
  activities: {
    key: 'activities', singular: 'atividade', plural: 'Atividades', description: 'Planeje atividades vinculadas às SAPZs cadastradas.', endpoint: '/api/atividades', getId: (item) => item.id,
    fields: [
      { name: 'idSapz', label: 'SAPZ', type: 'select', required: true, lookup: 'sapz' },
      { name: 'descricaoAtividade', label: 'Descrição da atividade', type: 'textarea', required: true, placeholder: 'Descreva a atividade a ser desenvolvida.' },
      { name: 'cargaHoraria', label: 'Carga horária', type: 'number', required: true, min: 1, placeholder: 'Ex.: 8', hint: 'Informe o valor em horas.' },
    ],
    columns: [{ key: 'descricaoAtividade', label: 'Atividade', value: (item) => item.descricaoAtividade, display: (item) => shortText(item.descricaoAtividade) }, { key: 'cargaHoraria', label: 'Carga horária', value: (item) => item.cargaHoraria, display: (item) => formatHours(item.cargaHoraria) }, { key: 'idSapz', label: 'SAPZ vinculada', value: (item) => item.idSapz, display: (item) => item.idSapz ? `SAPZ #${item.idSapz}` : 'Não informada', lookup: 'sapz' }],
    searchText: (item) => `${item.descricaoAtividade} ${item.idSapz ?? ''}`,
    toPayload: (values) => ({ idSapz: number(values.idSapz), descricaoAtividade: values.descricaoAtividade, cargaHoraria: number(values.cargaHoraria) }),
    detail: (item, lookups) => [{ label: 'Descrição', value: item.descricaoAtividade }, { label: 'Carga horária', value: formatHours(item.cargaHoraria) }, { label: 'SAPZ vinculada', value: lookupLabel(lookups.sapz, item.idSapz, 'SAPZ') }],
  },
  users: {
    key: 'users', singular: 'usuário', plural: 'Usuários', description: 'Administre os perfis que acessam o ambiente SAGSA.', endpoint: '/api/usuarios', getId: (item) => item.idUsuario,
    fields: [
      { name: 'nome', label: 'Nome completo', type: 'text', required: true, placeholder: 'Nome do usuário' },
      { name: 'email', label: 'E-mail', type: 'email', required: true, placeholder: 'nome@senai.br' },
      { name: 'senha', label: 'Senha inicial', type: 'password', required: true, placeholder: 'Mínimo de 6 caracteres', hint: 'A senha é enviada somente no cadastro.' },
      { name: 'tipoPerfil', label: 'Perfil', type: 'select', required: true, options: [{ value: 'ROLE_DOCENTE', label: 'Docente' }, { value: 'ROLE_ADMIN', label: 'Administrador' }] },
    ],
    columns: [{ key: 'nome', label: 'Usuário', value: (item) => item.nome }, { key: 'email', label: 'E-mail', value: (item) => item.email }, { key: 'tipoPerfil', label: 'Perfil', value: (item) => item.tipoPerfil, display: (item) => item.tipoPerfil === 'ROLE_ADMIN' ? 'Administrador' : 'Docente' }],
    searchText: (item) => `${item.nome} ${item.email} ${item.tipoPerfil}`,
    toPayload: (values) => ({ nome: values.nome, email: values.email, senha: values.senha, tipoPerfil: values.tipoPerfil as TipoPerfil }),
    detail: (item) => [{ label: 'Nome', value: item.nome }, { label: 'E-mail', value: item.email }, { label: 'Perfil', value: item.tipoPerfil === 'ROLE_ADMIN' ? 'Administrador' : 'Docente' }],
  },
};

export interface ResourceTypeMap {
  courses: Curso;
  ppcs: Ppc;
  modules: Modulo;
  capacities: Capacidade;
  criteria: Criterio;
  activities: Atividade;
  users: Usuario;
}

export async function fetchLookup(key: LookupKey): Promise<LookupOption[]> {
  switch (key) {
    case 'courses': return (await api.get<Curso[]>('/api/cursos')).map((item) => ({ value: String(item.id), label: item.nomeCurso }));
    case 'ppcs': return (await api.get<Ppc[]>('/api/ppcs')).map((item) => ({ value: String(item.id), label: `${item.nomeCurso ?? 'Curso não informado'} · ${item.anoVigencia}` }));
    case 'modules': return (await api.get<Modulo[]>('/api/modulos')).map((item) => ({ value: String(item.id), label: item.nomeModulo }));
    case 'capacities': return (await api.get<Capacidade[]>('/api/capacidades')).map((item) => ({ value: String(item.id), label: `${item.tipoCapacidade} · ${shortText(item.descricao, 55)}` }));
    case 'sapz': return (await api.get<Sapz[]>('/api/sapzs')).map((item) => ({ value: String(item.id), label: item.titulo }));
  }
}

type LookupKey = 'courses' | 'ppcs' | 'modules' | 'capacities' | 'sapz';

export function getEmptyForm(fields: FormField[]): FormValues {
  return Object.fromEntries(fields.map((field) => [field.name, '']));
}
