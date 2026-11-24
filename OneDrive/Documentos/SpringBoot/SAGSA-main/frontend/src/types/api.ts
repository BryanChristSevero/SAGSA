export type Id = number;

export interface Curso {
  id: Id;
  nomeCurso: string;
  eixoTecnologico: string;
  cargaHorariaTotal: number;
}

export interface Ppc {
  id: Id;
  anoVigencia: number;
  nomeCurso: string | null;
}

export interface Modulo {
  id: Id;
  nomeModulo: string;
  descricao: string;
  idPpc: Id | null;
}

export interface Capacidade {
  id: Id;
  tipoCapacidade: string;
  descricao: string;
  idModulo: Id | null;
}

export interface Criterio {
  id: Id;
  descricao: string;
  idCapacidade: Id | null;
}

export interface Sapz {
  id: Id;
  titulo: string;
  descricao: string;
  desafio: string;
  dataGeracao: string;
  cargaHorariaTotal: number;
  nomeUsuario: string | null;
}

export interface Atividade {
  id: Id;
  descricaoAtividade: string;
  cargaHoraria: number;
  idSapz: Id | null;
}

export type TipoPerfil = 'ROLE_DOCENTE' | 'ROLE_ADMIN';

export interface Usuario {
  idUsuario: Id;
  nome: string;
  email: string;
  tipoPerfil: TipoPerfil;
}

export type Entity = Curso | Ppc | Modulo | Capacidade | Criterio | Sapz | Atividade | Usuario;

export interface LoginResponse {
  token: string;
}

export interface ApiErrorBody {
  message?: string;
  error?: string;
  status?: number;
  [field: string]: unknown;
}
