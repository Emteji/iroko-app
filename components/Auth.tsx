import React, { useState } from 'react';
import { supabase, isSupabaseConfigured } from '../lib/supabaseClient';
import { Loader2, ArrowRight, Lock, Mail, User, ArrowLeft } from 'lucide-react';
import { repository } from '../services/repository';
import { Logo } from './Logo';

type AuthMode = 'signin' | 'signup' | 'forgot';

export const Auth = () => {
  const [loading, setLoading] = useState(false);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [name, setName] = useState('');
  const [authMode, setAuthMode] = useState<AuthMode>('signin');
  const [message, setMessage] = useState<{ type: 'error' | 'success', text: string } | null>(null);

  const handleAuth = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setMessage(null);

    // DEMO MODE LOGIN
    if (!isSupabaseConfigured) {
      await new Promise(r => setTimeout(r, 1000));
      
      if (authMode === 'forgot') {
        setMessage({ type: 'success', text: 'Demo: Password reset link sent.' });
        setLoading(false);
        return;
      }

      localStorage.setItem('iroko_demo_user', 'true');
      if (authMode === 'signup') {
        await repository.createParent({
           id: 'user_123',
           name: name || 'Demo Parent',
           email: email,
           subscriptionTier: 'Villager',
           locationContext: 'home',
           childrenIds: [],
           createdAt: Date.now()
        });
      }
      window.location.reload();
      return;
    }

    try {
      if (authMode === 'forgot') {
        const { error } = await supabase.auth.resetPasswordForEmail(email, {
          redirectTo: window.location.origin,
        });
        if (error) throw error;
        setMessage({ type: 'success', text: 'Check your email for the password reset link.' });
      } 
      else if (authMode === 'signup') {
        const { data, error } = await supabase.auth.signUp({
          email,
          password,
          options: { data: { display_name: name } }
        });
        if (error) throw error;
        
        if (data.user) {
           const { error: profileError } = await supabase.from('profiles').insert({
             id: data.user.id,
             name: name,
             email: email,
             subscription_tier: 'Villager'
           });
           
           if (profileError) console.warn("Profile creation warning:", profileError.message);
        }
        setMessage({ type: 'success', text: 'Account created! Please check your email to verify.' });
      } 
      else {
        const { error } = await supabase.auth.signInWithPassword({ email, password });
        if (error) throw error;
      }
    } catch (error: any) {
      setMessage({ type: 'error', text: error.message });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen w-full bg-iroko-dark flex flex-col md:flex-row relative overflow-hidden">
      
      {/* --- Mobile Top / Desktop Left: Imagery --- */}
      <div className="w-full h-[45vh] md:h-screen md:w-5/12 relative bg-iroko-earth overflow-hidden shrink-0">
         {/* Updated Image: A warm, high-quality portrait of a Black mother and child sharing a moment */}
         <img 
            src="https://images.unsplash.com/photo-1615814521949-06399eb6646b?q=80&w=2574&auto=format&fit=crop" 
            className="w-full h-full object-cover object-center animate-fade-in transition-transform duration-[10s] hover:scale-105 bg-iroko-earth"
            alt="African mother and child sharing a warm moment"
            loading="eager"
            onError={(e) => {
              // Fallback
              e.currentTarget.src = "https://images.unsplash.com/photo-1548232979-6c557ee14752?q=80&w=2670&auto=format&fit=crop";
            }}
         />
         {/* Gradient Overlay for Text Readability - Deep warm tones */}
         <div className="absolute inset-0 bg-gradient-to-t from-iroko-earth/95 via-black/20 to-transparent md:bg-gradient-to-r md:from-iroko-earth/90 md:via-black/20 md:to-transparent"></div>
         
         <div className="absolute bottom-10 left-6 md:top-12 md:left-12 z-10 pr-6">
            <div className="flex items-center gap-3 mb-4">
               <div className="bg-iroko-gold p-2 rounded-xl shadow-lg">
                 <Logo className="w-8 h-8" variant="dark" />
               </div>
               <span className="text-white font-display font-bold text-xl tracking-widest">IROKO</span>
            </div>
            <h1 className="text-4xl md:text-5xl font-display font-bold text-white leading-tight mb-2 drop-shadow-md">
              Raise a <br/><span className="text-iroko-gold">Giant.</span>
            </h1>
            <p className="text-stone-200 text-sm md:text-base font-medium max-w-xs drop-shadow-sm">
              The app for Nigerian parents building the next generation of leaders.
            </p>
         </div>
      </div>

      {/* --- Mobile Bottom Sheet / Desktop Right: Form --- */}
      <div className="flex-1 bg-white relative z-20 -mt-6 rounded-t-[2.5rem] md:mt-0 md:rounded-none px-6 py-10 md:p-16 flex flex-col justify-center shadow-[0_-10px_40px_rgba(0,0,0,0.3)] md:shadow-none animate-slide-up">
         
         <div className="max-w-md mx-auto w-full">
            {/* Toggle Header */}
            <div className="flex items-center gap-8 mb-8 border-b border-stone-100 pb-1">
                <button 
                  onClick={() => setAuthMode('signin')}
                  className={`text-lg font-bold pb-3 transition-all ${authMode === 'signin' ? 'text-iroko-dark border-b-2 border-iroko-earth' : 'text-stone-300'}`}
                >
                  Sign In
                </button>
                <button 
                  onClick={() => setAuthMode('signup')}
                  className={`text-lg font-bold pb-3 transition-all ${authMode === 'signup' ? 'text-iroko-dark border-b-2 border-iroko-earth' : 'text-stone-300'}`}
                >
                  Join Village
                </button>
            </div>

            <h3 className="text-2xl font-display font-bold text-iroko-dark mb-2">
               {authMode === 'signin' ? 'Welcome Back, Odogwu.' : authMode === 'signup' ? 'Start the Journey.' : 'Recover Account'}
            </h3>
            <p className="text-stone-500 mb-8 text-sm">
               {authMode === 'signin' ? 'Your child\'s future is waiting.' : 'It takes a village. Let us verify you.'}
            </p>

            <form onSubmit={handleAuth} className="space-y-4">
                
                {authMode === 'signup' && (
                  <div className="bg-stone-50 rounded-2xl p-4 flex items-center gap-3 border border-stone-100 focus-within:ring-2 focus-within:ring-iroko-earth/20 focus-within:border-iroko-earth transition-all">
                     <User className="text-stone-400" size={20} />
                     <input 
                        type="text" 
                        placeholder="Parent's Name" 
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        className="bg-transparent border-none outline-none w-full font-medium text-iroko-dark placeholder:text-stone-400"
                        required
                     />
                  </div>
                )}

                <div className="bg-stone-50 rounded-2xl p-4 flex items-center gap-3 border border-stone-100 focus-within:ring-2 focus-within:ring-iroko-earth/20 focus-within:border-iroko-earth transition-all">
                   <Mail className="text-stone-400" size={20} />
                   <input 
                      type="email" 
                      placeholder="Email Address" 
                      value={email}
                      onChange={(e) => setEmail(e.target.value)}
                      className="bg-transparent border-none outline-none w-full font-medium text-iroko-dark placeholder:text-stone-400"
                      required
                   />
                </div>

                {authMode !== 'forgot' && (
                  <div className="bg-stone-50 rounded-2xl p-4 flex items-center gap-3 border border-stone-100 focus-within:ring-2 focus-within:ring-iroko-earth/20 focus-within:border-iroko-earth transition-all">
                     <Lock className="text-stone-400" size={20} />
                     <input 
                        type="password" 
                        placeholder="Password" 
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        className="bg-transparent border-none outline-none w-full font-medium text-iroko-dark placeholder:text-stone-400"
                        required
                     />
                  </div>
                )}

                {authMode === 'signin' && (
                  <div className="flex justify-end">
                     <button type="button" onClick={() => setAuthMode('forgot')} className="text-xs font-bold text-stone-400 hover:text-iroko-earth py-2">Forgot Password?</button>
                  </div>
                )}

                {message && (
                  <div className={`p-4 rounded-xl text-xs font-bold text-center ${message.type === 'error' ? 'bg-red-50 text-red-500' : 'bg-green-50 text-green-600'}`}>
                    {message.text}
                  </div>
                )}

                <button 
                  type="submit" 
                  disabled={loading}
                  className="w-full py-5 bg-iroko-dark text-white rounded-2xl font-bold text-lg hover:bg-iroko-earth transition-all flex items-center justify-center gap-2 shadow-xl shadow-iroko-dark/20 mt-4 active:scale-95"
                >
                  {loading ? <Loader2 className="animate-spin" /> : (
                    <>
                      {authMode === 'signin' ? 'Sign In' : authMode === 'signup' ? 'Create Account' : 'Send Link'} 
                      <ArrowRight size={20} />
                    </>
                  )}
                </button>

                {authMode === 'forgot' && (
                   <button onClick={() => setAuthMode('signin')} className="w-full py-3 text-stone-400 font-bold hover:text-iroko-dark transition-colors flex items-center justify-center gap-2 text-sm">
                     <ArrowLeft size={16} /> Back to Login
                   </button>
                )}
            </form>

            {!isSupabaseConfigured && (
                <div className="mt-8 pt-4 border-t border-stone-100 text-center">
                  <span className="inline-block px-3 py-1 bg-iroko-gold/10 text-iroko-earth text-[10px] font-bold rounded-full uppercase tracking-wider">
                    Demo Mode Active
                  </span>
                  <p className="text-xs text-stone-400 mt-2">No backend connected. Login with any details.</p>
                </div>
            )}
         </div>
      </div>
    </div>
  );
};