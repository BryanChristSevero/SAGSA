import { ArrowUpRight, BookOpen, BriefcaseBusiness, ClipboardList, FileText, GraduationCap, Layers3, Sparkles } from 'lucide-react';
import { useCallback, useEffect, useMemo, useState } from 'react';
import type { AppRoute } from '../../app-routes';
import { ErrorState, LoadingState } from '../../components/ui/ContentState';
import { api, ApiError } from '../../lib/api';
import { formatDate, formatHours, shortText } from '../../lib/format';
import type { Atividade, Curso, Modulo, Ppc, Sapz } from '../../types/api';

interface DashboardData {
  courses: Curso[];
  ppcs: Ppc[];
  modules: Modulo[];
  sapzs: Sapz[];
  activities: Atividade[];
}

interface DashboardPageProps { onNavigate: (route: AppRoute) => void; }

export function DashboardPage({ onNavigate }: DashboardPageProps) {
  const [data, setData] = useState<DashboardData | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const load = useCallback(async () => {
    setIsLoading(true); setError(null);
    try {
      const [courses, ppcs, modules, sapzs, activities] = await Promise.all([
        api.get<Curso[]>('/api/cursos'), api.get<Ppc[]>('/api/ppcs'), api.get<Modulo[]>('/api/modulos'), api.get<Sapz[]>('/api/sapzs'), api.get<Atividade[]>('/api/atividades'),
      ]);
      setData({ courses, ppcs, modules, sapzs, activities });
    } catch (requestError) {
      setError(requestError instanceof ApiError ? requestError.message : 'Não foi possível carregar os indicadores.');
    } finally { setIsLoading(false); }
  }, []);

  useEffect(() => { void load(); }, [load]);

  const recentSapzs = useMemo(() => data ? [...data.sapzs].sort((a, b) => new Date(b.dataGeracao).getTime() - new Date(a.dataGeracao).getTime()).slice(0, 4) : [], [data]);
  const cards = data ? [
    { label: 'Cursos', value: data.courses.length, icon: GraduationCap, route: 'courses' as const, hint: 'ofertas cadastradas' },
    { label: 'PPCs', value: data.ppcs.length, icon: FileText, route: 'ppcs' as const, hint: 'projetos pedagógicos' },
    { label: 'Módulos', value: data.modules.length, icon: Layers3, route: 'modules' as const, hint: 'componentes formativos' },
    { label: 'Atividades', value: data.activities.length, icon: ClipboardList, route: 'activities' as const, hint: 'registros planejados' },
  ] : [];

  return <section className="dashboard-page">
    <div className="page-intro dashboard-intro"><div><span className="eyebrow">PAINEL INSTITUCIONAL</span><h2>O que está acontecendo no SAGSA</h2><p>Uma visão objetiva da estrutura acadêmica registrada na API.</p></div><button className="button button--secondary" type="button" onClick={() => void load()}>Atualizar dados</button></div>
    {isLoading ? <div className="surface"><LoadingState label="Atualizando a visão geral…" /></div> : error ? <div className="surface"><ErrorState message={error} onRetry={() => void load()} /></div> : data && <>
      <div className="metric-grid">{cards.map(({ label, value, icon: Icon, route, hint }) => <button className="metric-card" type="button" key={label} onClick={() => onNavigate(route)}><span className="metric-icon"><Icon aria-hidden="true" /></span><span className="metric-copy"><strong>{value}</strong><span>{label}</span><small>{hint}</small></span><ArrowUpRight className="metric-arrow" aria-hidden="true" /></button>)}</div>
      <div className="dashboard-grid">
        <section className="surface activity-panel"><div className="panel-heading"><div><span className="eyebrow">REGISTROS RECENTES</span><h3>SAPZs cadastradas</h3></div><button className="text-button" type="button" onClick={() => onNavigate('sapz')}>Ver todas <ArrowUpRight aria-hidden="true" /></button></div>{recentSapzs.length ? <div className="recent-list">{recentSapzs.map((sapz) => <button type="button" className="recent-item" key={sapz.id} onClick={() => onNavigate('sapz')}><span className="recent-icon"><Sparkles aria-hidden="true" /></span><span><strong>{sapz.titulo}</strong><p>{shortText(sapz.descricao, 80)}</p><small>{formatDate(sapz.dataGeracao)} · {sapz.nomeUsuario ?? 'Usuário não informado'}</small></span><ArrowUpRight aria-hidden="true" /></button>)}</div> : <div className="inline-empty"><Sparkles aria-hidden="true" /><p>Nenhuma SAPZ foi cadastrada até o momento.</p><button className="button button--secondary" type="button" onClick={() => onNavigate('sapz')}>Cadastrar SAPZ</button></div>}</section>
        <aside className="surface quick-panel"><span className="eyebrow">ATALHOS</span><h3>Ações frequentes</h3><p>Vá direto para os próximos passos da estrutura pedagógica.</p><div className="quick-actions"><button type="button" onClick={() => onNavigate('courses')}><GraduationCap aria-hidden="true" /><span><strong>Novo curso</strong><small>Estruture a oferta</small></span><ArrowUpRight aria-hidden="true" /></button><button type="button" onClick={() => onNavigate('sapz')}><Sparkles aria-hidden="true" /><span><strong>Nova SAPZ</strong><small>Registre uma situação</small></span><ArrowUpRight aria-hidden="true" /></button><button type="button" onClick={() => onNavigate('activities')}><BriefcaseBusiness aria-hidden="true" /><span><strong>Nova atividade</strong><small>Planeje o percurso</small></span><ArrowUpRight aria-hidden="true" /></button></div></aside>
      </div>
      <div className="surface dashboard-summary"><div><span className="eyebrow">SÍNTESE DA BASE</span><h3>Estrutura disponível</h3></div><div className="summary-items"><span><BookOpen aria-hidden="true" /><strong>{data.modules.length}</strong> módulos para organizar</span><span><Sparkles aria-hidden="true" /><strong>{data.sapzs.length}</strong> SAPZs registradas</span><span><ClipboardList aria-hidden="true" /><strong>{formatHours(data.activities.reduce((total, activity) => total + activity.cargaHoraria, 0))}</strong> em atividades</span></div></div>
    </>}
  </section>;
}
