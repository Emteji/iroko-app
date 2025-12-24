import React, { useState, useEffect } from 'react';
import { Home, Sparkles, TrendingUp, Edit2, Save, ShoppingBag, Moon, LogOut, Plus, AlertCircle, User, Crown, Link as LinkIcon, ExternalLink } from 'lucide-react';
import Dashboard from './components/Dashboard';
import CurriculumGenerator from './components/CurriculumGenerator';
import ProgressTracker from './components/ProgressTracker';
import RewardsStore from './components/RewardsStore';
import PalaverHut from './components/PalaverHut';
import { PatternBackground } from './components/PatternBackground';
import { Auth } from './components/Auth';
import { Onboarding } from './components/Onboarding';
import { ErrorBoundary } from './components/ErrorBoundary';
import { supabase, isSupabaseConfigured } from './lib/supabaseClient';
import { repository } from './services/repository';
import { ParentProfile, ChildProfile } from './types';
import { Logo } from './components/Logo';
import { Paywall } from './components/Paywall';


// Admin is no longer part of the main 'ViewState' to enforce separation
type ViewState = 'dashboard' | 'curriculum' | 'progress' | 'market' | 'palaver' | 'settings';

function App() {
  const [session, setSession] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [parent, setParent] = useState<ParentProfile | null>(null);
  const [children, setChildren] = useState<ChildProfile[]>([]);
  const [hasChildren, setHasChildren] = useState(false);
  const [currentView, setCurrentView] = useState<ViewState>('dashboard');
  const [errorState, setErrorState] = useState<string | null>(null);
  const [showPaywall, setShowPaywall] = useState(false);

  // Settings State
  const [isEditingProfile, setIsEditingProfile] = useState(false);
  const [editName, setEditName] = useState('');

  // Onboarding State
  const [showOnboarding, setShowOnboarding] = useState(false);

  // Child Creation State
  const [isCreatingChild, setIsCreatingChild] = useState(false);
  const [newChildName, setNewChildName] = useState('');
  const [newChildAge, setNewChildAge] = useState(8);

  useEffect(() => {
    const hasSeenOnboarding = localStorage.getItem('iroko_has_seen_onboarding');

    if (!isSupabaseConfigured) {
      const demoUser = localStorage.getItem('iroko_demo_user');
      if (demoUser) {
        setSession({ user: { id: 'user_123', email: 'demo@iroko.ng' } });
        checkUserProfile('user_123', !hasSeenOnboarding);
      } else {
        setLoading(false);
      }
      return;
    }

    supabase.auth.getSession().then(({ data, error }) => {
      if (!error && data.session) {
        setSession(data.session);
        checkUserProfile(data.session.user.id, !hasSeenOnboarding);
      } else {
        setLoading(false);
      }
    });

    const { data: { subscription } } = supabase.auth.onAuthStateChange((_event, session) => {
      setSession(session);
      if (session) checkUserProfile(session.user.id, !hasSeenOnboarding);
      else {
        setLoading(false);
        setParent(null);
      }
    });

    return () => subscription.unsubscribe();
  }, []);

  const checkUserProfile = async (userId: string, shouldShowOnboardingIfNew: boolean) => {
    try {
      let p = await repository.getParent(userId);

      // Auto-Recovery
      if (!p) {
        console.warn("Profile missing for authenticated user. Attempting recovery...");
        const { data: { user } } = await supabase.auth.getUser();

        if (user) {
          const recoveryProfile: ParentProfile = {
            id: user.id,
            name: user.user_metadata?.display_name || user.email?.split('@')[0] || 'Parent',
            email: user.email || '',
            subscriptionTier: 'Villager',
            locationContext: 'home',
            childrenIds: [],
            createdAt: Date.now()
          };

          try {
            await repository.createParent(recoveryProfile);
            await new Promise(r => setTimeout(r, 1000));
            p = await repository.getParent(userId);
          } catch (err) {
            console.error("Profile recovery failed:", err);
          }
        }
      }

      if (!p) {
        setErrorState("We couldn't load your profile. This might be a connection issue.");
        setLoading(false);
        return;
      }

      setParent(p);
      if (p) setEditName(p.name);

      const kids = await repository.getChildren(p.id);
      setChildren(kids);
      const hasKids = kids.length > 0;
      setHasChildren(hasKids);

      if (!hasKids && shouldShowOnboardingIfNew) {
        setShowOnboarding(true);
      }
    } catch (e: any) {
      console.error("Critical error in profile check:", e);
      setErrorState(e.message || "An unexpected error occurred.");
    } finally {
      setLoading(false);
    }
  };

  const refreshProfile = async () => {
    if (parent) {
      const p = await repository.getParent(parent.id);
      setParent(p);
    }
  };

  const handleCreateChild = async () => {
    if (!parent) return;
    await repository.createChild(parent.id, newChildName, newChildAge);
    const kids = await repository.getChildren(parent.id);
    setChildren(kids);
    setHasChildren(true);
    setIsCreatingChild(false);
    if (!hasChildren) window.location.reload();
  };

  const handleSignOut = async () => {
    if (isSupabaseConfigured) await supabase.auth.signOut();
    else localStorage.removeItem('iroko_demo_user');

    localStorage.removeItem('iroko_force_demo');

    setSession(null);
    setParent(null);
    setErrorState(null);
    window.location.reload();
  };

  const updateProfileName = async () => {
    if (!parent) return;
    await repository.updateParentProfile(parent.id, { name: editName });
    const p = await repository.getParent(parent.id);
    if (p) setParent(p);
    setIsEditingProfile(false);
  };

  // Admin logic removed - cleaned up for production build

  if (loading) return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-iroko-light text-iroko-dark font-display">
      <div className="w-20 h-20 mb-6 animate-pulse">
        <Logo className="w-full h-full" variant="gold" />
      </div>
      <div className="w-16 h-16 border-4 border-iroko-gold border-t-transparent rounded-full animate-spin mb-4"></div>
      <p className="text-lg font-bold animate-pulse">Summoning the Village...</p>
    </div>
  );

  if (!session) return <Auth />;

  if (errorState) return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-iroko-light p-6 text-center">
      <div className="w-16 h-16 bg-red-100 text-red-500 rounded-full flex items-center justify-center mb-4">
        <AlertCircle size={32} />
      </div>
      <h2 className="text-2xl font-bold text-iroko-dark mb-2">Small Wahala</h2>
      <p className="text-stone-500 mb-8 max-w-md">{errorState}</p>

      <button onClick={() => window.location.reload()} className="bg-iroko-dark text-white px-6 py-3 rounded-xl font-bold mb-3 w-full max-w-xs">
        Try Again
      </button>
      <button onClick={handleSignOut} className="text-stone-400 font-bold hover:text-iroko-dark text-sm">
        Sign Out
      </button>
    </div>
  );

  if (showOnboarding) return <Onboarding onComplete={() => {
    localStorage.setItem('iroko_has_seen_onboarding', 'true');
    setShowOnboarding(false);
  }} />;

  if (!hasChildren && !loading && !isCreatingChild) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-iroko-light p-6 relative overflow-hidden">
        <div className="absolute inset-0 z-0 opacity-[0.05] pointer-events-none"><PatternBackground /></div>
        <div className="max-w-md w-full bg-white/80 backdrop-blur-xl rounded-[2.5rem] p-10 shadow-2xl text-center relative z-10 animate-slide-up border border-white">
          <div className="w-20 h-20 bg-iroko-earth rounded-3xl mx-auto flex items-center justify-center mb-6 shadow-xl shadow-iroko-earth/30">
            <Logo className="w-10 h-10" variant="light" />
          </div>
          <h2 className="text-3xl font-display font-bold text-iroko-dark mb-3">One Last Step</h2>
          <p className="text-stone-500 mb-8 leading-relaxed">Who are we raising to be a giant today?</p>

          <div className="space-y-6 text-left">
            <div>
              <label className="block text-xs font-bold text-iroko-dark mb-2 uppercase tracking-wide ml-2">Child's Name</label>
              <input
                value={newChildName} onChange={e => setNewChildName(e.target.value)}
                className="w-full p-5 bg-stone-50 border-none rounded-2xl outline-none focus:bg-white focus:ring-2 focus:ring-iroko-earth/20 transition-all font-display text-lg placeholder:text-stone-300 shadow-inner"
                placeholder="e.g. Tunde"
              />
            </div>
            <div>
              <div className="flex justify-between items-center mb-4">
                <label className="block text-xs font-bold text-iroko-dark uppercase tracking-wide ml-2">Age: {newChildAge}</label>
              </div>
              <input
                type="range" min="3" max="17" value={newChildAge} onChange={e => setNewChildAge(parseInt(e.target.value))}
                className="w-full h-2 bg-stone-200 rounded-lg appearance-none cursor-pointer accent-iroko-earth"
              />
            </div>
            <button
              onClick={handleCreateChild}
              disabled={!newChildName.trim()}
              className="w-full bg-iroko-dark text-white py-5 rounded-2xl font-bold text-lg mt-4 shadow-xl shadow-iroko-dark/20 hover:scale-[1.02] active:scale-95 transition-all"
            >
              Start Journey
            </button>
          </div>
        </div>
      </div>
    )
  }

  // Settings Component
  const SettingsView = () => (
    <div className="max-w-xl mx-auto pt-4 pb-24 animate-fade-in">
      <h2 className="text-3xl font-display font-bold text-iroko-dark mb-6 text-center">My Profile</h2>
      <div className="bg-white p-8 rounded-[2.5rem] shadow-xl text-left border border-stone-100 mb-8">
        <div className="flex items-center gap-4 mb-8 pb-8 border-b border-stone-100">
          <div className="w-16 h-16 bg-iroko-light rounded-full flex items-center justify-center text-2xl font-bold text-iroko-earth shrink-0 relative">
            {parent?.name.charAt(0)}
            {parent?.subscriptionTier === 'Chief' && (
              <div className="absolute -bottom-1 -right-1 bg-iroko-gold p-1.5 rounded-full border-2 border-white shadow-sm">
                <Crown size={12} className="text-white fill-current" />
              </div>
            )}
          </div>
          <div className="flex-1">
            {isEditingProfile ? (
              <div className="flex gap-2">
                <input value={editName} onChange={(e) => setEditName(e.target.value)} className="w-full p-2 bg-stone-50 rounded-lg border border-stone-200" />
                <button onClick={updateProfileName} className="p-2 bg-iroko-dark text-white rounded-lg"><Save size={18} /></button>
              </div>
            ) : (
              <div className="flex items-center gap-2">
                <h3 className="text-xl font-bold text-iroko-dark">{parent?.name}</h3>
                <button onClick={() => setIsEditingProfile(true)} className="text-stone-400 hover:text-iroko-dark"><Edit2 size={14} /></button>
              </div>
            )}

            <div className="flex items-center gap-2 mt-1">
              <p className="text-stone-400 text-sm">{parent?.email}</p>
              <span className={`px-2 py-0.5 rounded-full text-[10px] font-bold uppercase ${parent?.subscriptionTier === 'Chief' ? 'bg-iroko-gold text-white' : 'bg-stone-200 text-stone-500'}`}>
                {parent?.subscriptionTier}
              </span>
            </div>

            {parent?.subscriptionTier === 'Villager' && (
              <button onClick={() => setShowPaywall(true)} className="mt-3 text-xs font-bold text-iroko-gold hover:underline flex items-center gap-1">
                <Crown size={12} /> Upgrade to Chief
              </button>
            )}
          </div>
        </div>

        <div className="mb-8">
          <h4 className="text-xs font-bold uppercase text-stone-400 mb-4 tracking-wider">Children</h4>
          <div className="space-y-3">
            {children.map(child => (
              <div key={child.id} className="flex items-center justify-between p-4 bg-stone-50 rounded-2xl border border-stone-100">
                <div className="flex items-center gap-3">
                  <img src={child.avatarUrl} className="w-10 h-10 rounded-full bg-stone-200" alt={child.name} />
                  <div>
                    <p className="font-bold text-iroko-dark">{child.name}</p>
                    <p className="text-xs text-stone-500">Age {child.age} • Level {Math.floor((child.gritPoints + child.omoluabiPoints) / 100) + 1}</p>
                  </div>
                </div>
              </div>
            ))}
          </div>
          <button onClick={() => setIsCreatingChild(true)} className="w-full mt-4 py-3 rounded-xl border-2 border-dashed border-stone-200 text-stone-400 font-bold flex items-center justify-center gap-2 hover:border-iroko-earth hover:text-iroko-earth transition-colors">
            <Plus size={18} /> Add Another Child
          </button>
        </div>

        <button onClick={handleSignOut} className="w-full py-4 bg-red-50 text-red-500 rounded-2xl font-bold flex items-center justify-center gap-2 mb-8">
          <LogOut size={20} /> Sign Out
        </button>
      </div>
    </div>
  );

  return (
    <ErrorBoundary>
      <div className="min-h-screen bg-iroko-light pb-24 md:pb-0 md:pl-24">

        {showPaywall && parent && (
          <Paywall
            parentId={parent.id}
            onClose={() => setShowPaywall(false)}
            onUpgradeSuccess={async () => {
              await refreshProfile();
              setShowPaywall(false);
            }}
          />
        )}

        <div className="p-6 md:p-8 max-w-7xl mx-auto">
          {currentView === 'dashboard' && parent && (
            <Dashboard
              parent={parent}
              onViewChange={setCurrentView}
              onTriggerPaywall={() => setShowPaywall(true)}
              onRefreshProfile={refreshProfile}
            />
          )}
          {currentView === 'curriculum' && parent && (
            <CurriculumGenerator
              parent={parent}
              onTriggerPaywall={() => setShowPaywall(true)}
            />
          )}
          {currentView === 'progress' && parent && (
            <ProgressTracker
              parent={parent}
              onTriggerPaywall={() => setShowPaywall(true)}
            />
          )}
          {currentView === 'market' && parent && (
            <RewardsStore parent={parent} />
          )}
          {currentView === 'palaver' && parent && (
            <PalaverHut
              parent={parent}
              onTriggerPaywall={() => setShowPaywall(true)}
            />
          )}
          {currentView === 'settings' && <SettingsView />}
        </div>

        <nav className="fixed bottom-0 left-0 right-0 bg-white border-t border-stone-100 p-4 md:top-0 md:bottom-auto md:left-0 md:right-auto md:w-24 md:h-screen md:border-r md:border-t-0 flex md:flex-col items-center justify-around md:justify-start md:gap-8 z-40 shadow-lg md:shadow-none safe-area-bottom">
          <div className="hidden md:block p-4 mb-4">
            <Logo className="w-8 h-8" variant="dark" />
          </div>

          <NavButton icon={Home} active={currentView === 'dashboard'} onClick={() => setCurrentView('dashboard')} label="Home" />
          <NavButton icon={Sparkles} active={currentView === 'curriculum'} onClick={() => setCurrentView('curriculum')} label="Learn" />
          <NavButton icon={Moon} active={currentView === 'palaver'} onClick={() => setCurrentView('palaver')} label="Tales" />
          <NavButton icon={TrendingUp} active={currentView === 'progress'} onClick={() => setCurrentView('progress')} label="Growth" />
          <NavButton icon={ShoppingBag} active={currentView === 'market'} onClick={() => setCurrentView('market')} label="Market" />

          <div className="hidden md:block mt-auto pb-8 text-center space-y-4">
            {parent?.subscriptionTier === 'Villager' && (
              <button onClick={() => setShowPaywall(true)} className="w-8 h-8 rounded-full bg-iroko-gold text-white flex items-center justify-center animate-pulse hover:scale-110 transition-transform shadow-lg" title="Upgrade">
                <Crown size={16} fill="currentColor" />
              </button>
            )}
            <NavButton icon={User} active={currentView === 'settings'} onClick={() => setCurrentView('settings')} label="Profile" />
          </div>
          <div className="md:hidden">
            <button onClick={() => setCurrentView('settings')} className={`p-2 rounded-xl transition-all ${currentView === 'settings' ? 'text-iroko-dark bg-stone-100' : 'text-stone-400'}`}>
              <User size={24} />
            </button>
          </div>
        </nav>
      </div>
    </ErrorBoundary>
  );
}

const NavButton = ({ icon: Icon, active, onClick, label }: any) => (
  <button
    onClick={onClick}
    className={`flex flex-col items-center gap-1 p-2 rounded-xl transition-all ${active ? 'text-iroko-dark scale-110' : 'text-stone-400 hover:text-iroko-earth'}`}
  >
    <div className={`p-2 rounded-full ${active ? 'bg-iroko-gold text-iroko-dark shadow-md' : ''}`}>
      <Icon size={20} strokeWidth={active ? 2.5 : 2} />
    </div>
    <span className="text-[10px] font-bold md:hidden">{label}</span>
  </button>
);

export default App;