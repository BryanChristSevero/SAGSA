export type AppRoute =
  | 'dashboard'
  | 'courses'
  | 'ppcs'
  | 'modules'
  | 'capacities'
  | 'criteria'
  | 'sapz'
  | 'activities'
  | 'users'
  | 'settings';

export const routeMeta: Record<AppRoute, { title: string; section: string }> = {
  dashboard: { title: 'Visão geral', section: 'SAGSA' },
  courses: { title: 'Cursos', section: 'Gestão acadêmica' },
  ppcs: { title: 'PPCs', section: 'Gestão acadêmica' },
  modules: { title: 'Módulos', section: 'Gestão acadêmica' },
  capacities: { title: 'Capacidades', section: 'Gestão acadêmica' },
  criteria: { title: 'Critérios de avaliação', section: 'Gestão acadêmica' },
  sapz: { title: 'SAPZ', section: 'Gestão acadêmica' },
  activities: { title: 'Atividades', section: 'Gestão acadêmica' },
  users: { title: 'Usuários', section: 'Administração' },
  settings: { title: 'Configurações', section: 'Administração' },
};

export function getRouteFromHash(): AppRoute {
  try {
    const candidate = decodeURIComponent(window.location.hash.replace(/^#\/?/, '').split(/[?]/)[0]) as AppRoute;
    return candidate in routeMeta ? candidate : 'dashboard';
  } catch {
    return 'dashboard';
  }
}
