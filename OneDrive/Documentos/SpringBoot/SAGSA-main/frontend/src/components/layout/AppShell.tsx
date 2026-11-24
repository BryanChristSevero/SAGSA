import {
  BookOpen, BriefcaseBusiness, ChevronLeft, ChevronRight, ClipboardCheck, FileText, GraduationCap,
  LayoutDashboard, LogOut, Menu, Settings, Sparkles, Target, UserRound, Users, X,
} from 'lucide-react';
import { useEffect, useState, type ReactNode } from 'react';
import type { AppRoute } from '../../app-routes';
import { routeMeta } from '../../app-routes';

interface AppShellProps {
  route: AppRoute;
  email: string;
  onNavigate: (route: AppRoute) => void;
  onLogout: () => void;
  children: ReactNode;
}

const navGroups: Array<{ label: string; items: Array<{ route: AppRoute; label: string; icon: typeof LayoutDashboard }> }> = [
  { label: 'Visão geral', items: [{ route: 'dashboard', label: 'Dashboard', icon: LayoutDashboard }] },
  {
    label: 'Gestão acadêmica',
    items: [
      { route: 'courses', label: 'Cursos', icon: GraduationCap },
      { route: 'ppcs', label: 'PPCs', icon: FileText },
      { route: 'modules', label: 'Módulos', icon: BookOpen },
      { route: 'capacities', label: 'Capacidades', icon: Target },
      { route: 'criteria', label: 'Critérios de avaliação', icon: ClipboardCheck },
      { route: 'sapz', label: 'SAPZ', icon: Sparkles },
      { route: 'activities', label: 'Atividades', icon: BriefcaseBusiness },
    ],
  },
  {
    label: 'Administração',
    items: [
      { route: 'users', label: 'Usuários', icon: Users },
      { route: 'settings', label: 'Configurações', icon: Settings },
    ],
  },
];

export function AppShell({ route, email, onNavigate, onLogout, children }: AppShellProps) {
  const [collapsed, setCollapsed] = useState(false);
  const [mobileOpen, setMobileOpen] = useState(false);
  const meta = routeMeta[route];

  useEffect(() => {
    setMobileOpen(false);
  }, [route]);

  useEffect(() => {
    if (!mobileOpen) return;
    const closeOnEscape = (event: KeyboardEvent) => {
      if (event.key === 'Escape') setMobileOpen(false);
    };
    window.addEventListener('keydown', closeOnEscape);
    return () => window.removeEventListener('keydown', closeOnEscape);
  }, [mobileOpen]);

  return (
    <div className={`app-shell ${collapsed ? 'app-shell--collapsed' : ''}`}>
      {mobileOpen && <button className="mobile-scrim" type="button" aria-label="Fechar menu" onClick={() => setMobileOpen(false)} />}
      <aside className={`sidebar ${mobileOpen ? 'sidebar--open' : ''}`} aria-label="Navegação principal">
        <div className="brand-row">
          <button className="brand" type="button" onClick={() => onNavigate('dashboard')} aria-label="Ir para o dashboard do SAGSA">
            <span className="brand-mark">S</span>
            <span className="brand-copy"><strong>SAGSA</strong><small>SENAI • Acadêmico</small></span>
          </button>
          <button className="sidebar-collapse icon-button" type="button" onClick={() => setCollapsed((value) => !value)} aria-label={collapsed ? 'Expandir menu' : 'Recolher menu'}>
            {collapsed ? <ChevronRight aria-hidden="true" /> : <ChevronLeft aria-hidden="true" />}
          </button>
          <button className="sidebar-close icon-button" type="button" onClick={() => setMobileOpen(false)} aria-label="Fechar menu"><X aria-hidden="true" /></button>
        </div>
        <nav className="sidebar-nav">
          {navGroups.map((group) => (
            <div className="nav-group" key={group.label}>
              <span className="nav-label">{group.label}</span>
              {group.items.map(({ route: itemRoute, label, icon: Icon }) => (
                <button
                  type="button"
                  key={itemRoute}
                  title={collapsed ? label : undefined}
                  aria-current={route === itemRoute ? 'page' : undefined}
                  className={`nav-item ${route === itemRoute ? 'nav-item--active' : ''}`}
                  onClick={() => onNavigate(itemRoute)}
                >
                  <Icon aria-hidden="true" /> <span>{label}</span>
                </button>
              ))}
            </div>
          ))}
        </nav>
        <div className="sidebar-footer"><span className="status-dot" aria-hidden="true" /><span>Conexão protegida</span></div>
      </aside>
      <div className="app-main">
        <header className="header">
          <button className="mobile-menu icon-button" type="button" onClick={() => setMobileOpen(true)} aria-label="Abrir menu"><Menu aria-hidden="true" /></button>
          <div className="header-context"><div className="breadcrumbs"><span>{meta.section}</span><span aria-hidden="true">/</span><strong>{meta.title}</strong></div><h1>{meta.title}</h1></div>
          <div className="header-profile"><div className="profile-avatar" aria-hidden="true">{email.charAt(0).toUpperCase()}</div><div className="profile-copy"><strong>{email}</strong><span>Sessão autenticada</span></div><button className="icon-button" type="button" onClick={onLogout} aria-label="Sair do sistema" title="Sair"><LogOut aria-hidden="true" /></button></div>
        </header>
        <main className="page-content">{children}</main>
      </div>
    </div>
  );
}
