import { ArrowRight, Eye, EyeOff, GraduationCap, LockKeyhole, Mail, ShieldCheck } from 'lucide-react';
import { useState, type FormEvent } from 'react';
import { api, ApiError } from '../../lib/api';
import type { LoginResponse } from '../../types/api';

interface LoginPageProps {
  notice?: string | null;
  onAuthenticated: (token: string) => void;
}

export function LoginPage({ notice, onAuthenticated }: LoginPageProps) {
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setError(null);
    const normalizedEmail = email.trim();
    if (!normalizedEmail || !/^\\S+@\\S+\\.\\S+$/.test(normalizedEmail)) {
      setError('Informe um e-mail válido.');
      return;
    }
    if (!senha || senha.length < 6) {
      setError('A senha deve ter pelo menos 6 caracteres.');
      return;
    }
    setIsSubmitting(true);
    try {
      const response = await api.post<LoginResponse>('/login', { email: normalizedEmail, senha });
      onAuthenticated(response.token);
    } catch (requestError) {
      setError(requestError instanceof ApiError ? requestError.message : 'Não foi possível realizar o login.');
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <main className="login-page">
      <section className="login-showcase" aria-label="Apresentação do SAGSA">
        <div className="login-brand"><span className="brand-mark">S</span><span><strong>SAGSA</strong><small>Sistema Acadêmico</small></span></div>
        <div className="showcase-copy"><span className="eyebrow">SENAI · GESTÃO ACADÊMICA</span><h1>Conhecimento organizado.<br /><em>Formação em movimento.</em></h1><p>Centralize a estrutura pedagógica e acompanhe o desenvolvimento acadêmico com clareza.</p></div>
        <div className="showcase-notes"><span><ShieldCheck aria-hidden="true" /> Acesso protegido por JWT</span><span><GraduationCap aria-hidden="true" /> Ambiente institucional</span></div>
      </section>
      <section className="login-panel">
        <div className="login-form-wrap">
          <div className="login-heading"><span className="eyebrow">ACESSO RESTRITO</span><h2>Boas-vindas de volta</h2><p>Entre com suas credenciais para acessar o SAGSA.</p></div>
          {(notice || error) && <div className={`form-alert ${error ? 'form-alert--error' : ''}`} role="alert">{error ?? notice}</div>}
          <form onSubmit={handleSubmit} noValidate>
            <label className="field"><span>E-mail institucional</span><div className="field-input"><Mail aria-hidden="true" /><input type="email" value={email} onChange={(event) => setEmail(event.target.value)} placeholder="nome@senai.br" autoComplete="email" required /></div></label>
            <label className="field"><span>Senha</span><div className="field-input"><LockKeyhole aria-hidden="true" /><input type={showPassword ? 'text' : 'password'} value={senha} onChange={(event) => setSenha(event.target.value)} placeholder="Sua senha" autoComplete="current-password" minLength={6} required /><button type="button" className="input-icon-button" onClick={() => setShowPassword((value) => !value)} aria-label={showPassword ? 'Ocultar senha' : 'Mostrar senha'}>{showPassword ? <EyeOff aria-hidden="true" /> : <Eye aria-hidden="true" />}</button></div></label>
            <button className="button button--primary login-submit" type="submit" disabled={isSubmitting}>{isSubmitting ? 'Autenticando…' : <>Entrar no sistema <ArrowRight aria-hidden="true" /></>}</button>
          </form>
          <p className="login-hint">O cadastro de usuários é disponibilizado pela própria API em <code>/api/usuarios</code>, conforme as permissões institucionais.</p>
        </div>
      </section>
    </main>
  );
}
