import { useCallback, useEffect, useState } from 'react';
import type { AppRoute } from './app-routes';
import { getRouteFromHash, routeMeta } from './app-routes';
import { AppShell } from './components/layout/AppShell';
import { ToastProvider } from './components/ui/Toast';
import { configureApi } from './lib/api';
import { clearSession, readSession, saveSession, type Session } from './lib/session';
import { DashboardPage } from './features/dashboard/DashboardPage';
import { LoginPage } from './features/auth/LoginPage';
import { ResourcePage } from './features/resources/ResourcePage';
import { resources } from './features/resources/resourceDefinitions';
import { SapzPage } from './features/sapz/SapzPage';
import { SettingsPage } from './features/settings/SettingsPage';

function AppContent() {
  const [session, setSession] = useState<Session | null>(() => readSession());
  const [route, setRoute] = useState<AppRoute>(() => getRouteFromHash());
  const [loginNotice, setLoginNotice] = useState<string | null>(null);

  const logout = useCallback((reason?: string) => {
    clearSession();
    setSession(null);
    setLoginNotice(reason ?? 'Você saiu do SAGSA com segurança.');
  }, []);

  useEffect(() => {
    configureApi({ getToken: () => session?.token ?? null, onUnauthorized: () => logout('Sua sessão expirou. Entre novamente para continuar.') });
  }, [logout, session]);

  useEffect(() => {
    if (!session?.expiresAt) return;
    const remaining = session.expiresAt - Date.now();
    if (remaining <= 0) {
      logout('Sua sessão expirou. Entre novamente para continuar.');
      return;
    }
    const timeout = window.setTimeout(() => {
      logout('Sua sessão expirou. Entre novamente para continuar.');
    }, remaining);
    return () => window.clearTimeout(timeout);
  }, [logout, session]);

  useEffect(() => {
    const updateRoute = () => setRoute(getRouteFromHash());
    window.addEventListener('hashchange', updateRoute);
    return () => window.removeEventListener('hashchange', updateRoute);
  }, []);

  useEffect(() => {
    document.title = `${routeMeta[route].title} · SAGSA`;
  }, [route]);

  const navigate = useCallback((nextRoute: AppRoute) => {
    const hash = `#/${nextRoute}`;
    if (window.location.hash !== hash) window.location.hash = hash;
    setRoute(nextRoute);
  }, []);

  function handleAuthenticated(token: string) {
    const nextSession = saveSession(token);
    if (!nextSession) { setLoginNotice('Não foi possível validar a sessão recebida. Tente novamente.'); return; }
    setLoginNotice(null);
    setSession(nextSession);
    navigate('dashboard');
  }

  if (!session) return <LoginPage notice={loginNotice} onAuthenticated={handleAuthenticated} />;

  const content = {
    dashboard: <DashboardPage onNavigate={navigate} />,
    courses: <ResourcePage resource={resources.courses} />,
    ppcs: <ResourcePage resource={resources.ppcs} />,
    modules: <ResourcePage resource={resources.modules} />,
    capacities: <ResourcePage resource={resources.capacities} />,
    criteria: <ResourcePage resource={resources.criteria} />,
    activities: <ResourcePage resource={resources.activities} />,
    users: <ResourcePage resource={resources.users} />,
    sapz: <SapzPage />,
    settings: <SettingsPage />,
  } satisfies Record<AppRoute, React.ReactNode>;

  return <AppShell route={route} email={session.email} onNavigate={navigate} onLogout={() => logout()}>{content[route]}</AppShell>;
}

export default function App() {
  return <ToastProvider><AppContent /></ToastProvider>;
}
