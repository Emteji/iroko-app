import React, { useState } from 'react';
import { generateScenario, PersonaType } from '../services/geminiService';
import { CATEGORIES, ParentProfile } from '../types';
import { Sparkles, Moon, ArrowRight, User, Briefcase, Glasses, RefreshCw, Lock, Crown } from 'lucide-react';

const Personas: {id: PersonaType, name: string, role: string, icon: any, color: string, premium: boolean}[] = [
  { id: 'elder', name: 'Baba Iroko', role: 'The Ancient Wisdom', icon: User, color: 'text-iroko-gold', premium: false },
  { id: 'auntie', name: 'Auntie Nkechi', role: 'The Standard', icon: Glasses, color: 'text-iroko-clay', premium: true },
  { id: 'uncle', name: 'Uncle Femi', role: 'Street Smarts', icon: Briefcase, color: 'text-blue-400', premium: true }
];

interface Props {
   parent: ParentProfile;
   onTriggerPaywall: () => void;
}

const PalaverHut: React.FC<Props> = ({ parent, onTriggerPaywall }) => {
  const [step, setStep] = useState<'setup' | 'loading' | 'play' | 'feedback'>('setup');
  const [selectedPersona, setSelectedPersona] = useState<PersonaType>('elder');
  const [selectedCategory, setSelectedCategory] = useState(CATEGORIES[0].id);
  const [scenario, setScenario] = useState<any>(null);
  const [choice, setChoice] = useState<any>(null);

  const handlePersonaSelect = (p: typeof Personas[0]) => {
     if (p.premium && parent.subscriptionTier !== 'Chief') {
        onTriggerPaywall();
        return;
     }
     setSelectedPersona(p.id);
  };

  const handleCategorySelect = (catId: string) => {
     const lockedCategories = ['street-smarts', 'money'];
     if (lockedCategories.includes(catId) && parent.subscriptionTier !== 'Chief') {
        onTriggerPaywall();
        return;
     }
     setSelectedCategory(catId);
  };

  const startGame = async () => {
    setStep('loading');
    try {
       const data = await generateScenario(
         CATEGORIES.find(c => c.id === selectedCategory)?.label || 'General', 
         10, 
         selectedPersona,
         parent.locationContext // Pass the parent's environment context
       );
       setScenario(data);
       setStep('play');
    } catch (e) {
       setStep('setup');
    }
  };

  const makeChoice = (opt: any) => {
    setChoice(opt);
    setStep('feedback');
  };

  const reset = () => {
    setScenario(null);
    setChoice(null);
    setStep('setup');
  };

  if (step === 'setup') {
    return (
      <div className="max-w-2xl mx-auto py-8 animate-fade-in pb-24">
         <div className="text-center mb-10 bg-iroko-dark text-white rounded-[2.5rem] p-8 shadow-xl relative overflow-hidden">
            <div className="absolute inset-0 opacity-20 bg-[url('https://www.transparenttextures.com/patterns/stardust.png')] animate-pulse-slow"></div>
            <div className="relative z-10">
                <div className="w-16 h-16 bg-white/10 rounded-full flex items-center justify-center mx-auto mb-4 backdrop-blur-md border border-white/20">
                   <Moon size={32} className="text-iroko-gold" fill="currentColor" />
                </div>
                <h2 className="text-3xl font-display font-bold">The Palaver Hut</h2>
                <p className="text-white/60 mt-2">Tales by moonlight. Where wisdom meets real life.</p>
            </div>
         </div>

         <div className="space-y-6">
            <div className="bg-white p-6 rounded-[2rem] border border-stone-100 shadow-sm">
               <h3 className="font-bold text-stone-400 uppercase text-xs tracking-wider mb-4 pl-2">Choose the Judge</h3>
               <div className="flex gap-3 overflow-x-auto no-scrollbar pb-2">
                  {Personas.map(p => {
                     const isLocked = p.premium && parent.subscriptionTier !== 'Chief';
                     return (
                        <button 
                        key={p.id}
                        onClick={() => handlePersonaSelect(p)}
                        className={`relative flex-shrink-0 w-32 p-4 rounded-2xl border-2 transition-all flex flex-col items-center text-center gap-3 overflow-hidden ${selectedPersona === p.id ? 'border-iroko-dark bg-iroko-dark text-white' : 'border-stone-100 hover:border-iroko-earth'}`}
                        >
                           {/* Premium Badge */}
                           {p.premium && (
                             <div className={`absolute top-0 right-0 px-2 py-1 rounded-bl-lg text-[8px] font-bold uppercase tracking-wider flex items-center gap-1 z-10
                               ${isLocked ? 'bg-stone-200 text-stone-500' : 'bg-iroko-gold text-white'}`}>
                                {isLocked ? <Lock size={8} /> : <Crown size={8} fill="currentColor" />}
                             </div>
                           )}

                           <div className={`w-10 h-10 rounded-full flex items-center justify-center ${selectedPersona === p.id ? 'bg-white/20' : 'bg-stone-100'} ${p.color} ${isLocked ? 'grayscale opacity-50' : ''}`}>
                              <p.icon size={18} />
                           </div>
                           <div className={isLocked ? 'opacity-50' : ''}>
                              <div className="font-bold text-sm leading-tight">{p.name}</div>
                              <span className="text-[10px] text-stone-400">{p.role}</span>
                           </div>
                        </button>
                     );
                  })}
               </div>
            </div>

            <div className="bg-white p-6 rounded-[2rem] border border-stone-100 shadow-sm">
               <h3 className="font-bold text-stone-400 uppercase text-xs tracking-wider mb-4 pl-2">Choose the Test</h3>
               <div className="grid grid-cols-2 gap-3">
                  {CATEGORIES.map(c => {
                     const isLocked = ['street-smarts', 'money'].includes(c.id) && parent.subscriptionTier !== 'Chief';
                     return (
                        <button 
                        key={c.id}
                        onClick={() => handleCategorySelect(c.id)}
                        className={`relative px-4 py-4 rounded-xl font-bold text-sm text-left transition-all border overflow-hidden ${selectedCategory === c.id ? 'bg-iroko-earth text-white border-iroko-earth shadow-lg' : 'bg-stone-50 text-stone-500 border-transparent hover:bg-white hover:border-stone-200'}`}
                        >
                           <div className="flex justify-between items-center">
                              <span className={isLocked ? 'opacity-50' : ''}>{c.label}</span>
                              {isLocked ? (
                                 <div className="bg-stone-200 p-1.5 rounded-full text-stone-500">
                                    <Lock size={12}/>
                                 </div>
                              ) : (
                                 ['street-smarts', 'money'].includes(c.id) && <Crown size={14} className="text-iroko-gold" fill="currentColor"/>
                              )}
                           </div>
                        </button>
                     );
                  })}
               </div>
            </div>

            <button onClick={startGame} className="w-full py-5 bg-iroko-dark text-white rounded-2xl font-bold text-lg flex items-center justify-center gap-3 hover:scale-[1.02] active:scale-95 transition-all shadow-xl shadow-iroko-dark/20">
               <Sparkles size={20} className="animate-pulse" /> Enter the Hut
            </button>
         </div>
      </div>
    );
  }

  if (step === 'loading') {
     return (
        <div className="min-h-[60vh] flex flex-col items-center justify-center text-center animate-fade-in">
           <div className="w-20 h-20 bg-iroko-dark rounded-full flex items-center justify-center animate-bounce mb-6">
              <Moon size={40} className="text-iroko-gold" fill="currentColor" />
           </div>
           <h3 className="text-xl font-bold text-iroko-dark mb-2">Summoning {Personas.find(p => p.id === selectedPersona)?.name}...</h3>
           <p className="text-stone-400 text-sm">Reviewing the ancient scrolls.</p>
        </div>
     );
  }

  const personaObj = Personas.find(p => p.id === selectedPersona);

  return (
     <div className="bg-[#100C0A] min-h-[80vh] rounded-[2.5rem] overflow-hidden relative text-[#EBE5DA] shadow-2xl animate-slide-up mx-auto max-w-3xl border border-[#2C241B] mb-20">
        <div className="absolute inset-0 opacity-20 bg-[url('https://www.transparenttextures.com/patterns/dark-matter.png')] pointer-events-none"></div>
        
        {/* Header */}
        <div className="relative z-10 p-6 flex justify-between items-center bg-gradient-to-b from-black/40 to-transparent">
           <div className="flex items-center gap-3">
              <div className={`w-10 h-10 rounded-full bg-white/10 flex items-center justify-center ${personaObj?.color} border border-white/10`}>
                 {personaObj?.icon && <personaObj.icon size={20} />}
              </div>
              <div>
                 <h3 className="font-bold text-white">{personaObj?.name}</h3>
              </div>
           </div>
           <button onClick={reset} className="p-2 bg-white/5 rounded-full hover:bg-white/10 text-stone-400 transition-colors">
              <RefreshCw size={18} />
           </button>
        </div>

        {/* Story Content */}
        <div className="relative z-10 p-6 md:p-10 flex flex-col gap-8">
           
           <div className="animate-fade-in">
              <div className="flex gap-4">
                 <div className="w-1 bg-iroko-gold rounded-full opacity-50"></div>
                 <p className="text-xl md:text-2xl font-serif leading-relaxed text-stone-200">
                    "{scenario.situation}"
                 </p>
              </div>
           </div>

           {step === 'play' ? (
              <div className="space-y-4 mt-4 animate-slide-up" style={{animationDelay: '0.2s'}}>
                 {scenario.options.map((opt: any, idx: number) => (
                    <button
                       key={opt.id}
                       onClick={() => makeChoice(opt)}
                       className="w-full text-left p-6 rounded-2xl bg-white/5 border border-white/5 hover:bg-iroko-gold/10 hover:border-iroko-gold/30 transition-all group relative overflow-hidden"
                    >
                       <div className="flex gap-4 items-center relative z-10">
                          <span className="text-xs font-bold text-stone-500 uppercase tracking-widest group-hover:text-iroko-gold">Option {opt.id}</span>
                          <span className="text-lg font-medium">{opt.text}</span>
                       </div>
                    </button>
                 ))}
              </div>
           ) : (
              // Feedback Screen
              <div className="animate-slide-up bg-white/5 rounded-[2rem] p-8 border border-white/10 mt-4">
                 <div className="flex items-center gap-4 mb-6">
                    <div className="text-5xl animate-bounce">
                       {choice.score > 80 ? '👑' : choice.score > 40 ? '🤔' : '🤡'}
                    </div>
                    <div>
                       <h4 className="font-bold text-xl text-white">
                          {choice.score > 80 ? 'Wise Choice, Giant!' : choice.score > 40 ? 'Not bad, but listen...' : 'Ah! You fall my hand.'}
                       </h4>
                       <span className={`text-xs font-bold px-2 py-1 rounded bg-white/10 ${choice.score > 80 ? 'text-green-400' : 'text-orange-400'}`}>
                          +{choice.score} XP Earned
                       </span>
                    </div>
                 </div>
                 
                 <div className="bg-black/20 p-6 rounded-xl border-l-2 border-iroko-gold mb-8">
                    <p className="text-lg italic font-serif leading-relaxed text-stone-300">
                       "{choice.outcome}"
                    </p>
                 </div>

                 <button onClick={startGame} className="w-full py-4 bg-iroko-gold text-iroko-dark rounded-xl font-bold hover:bg-white transition-colors flex items-center justify-center gap-2">
                    Next Scenario <ArrowRight size={18} />
                 </button>
              </div>
           )}
        </div>
     </div>
  );
};

export default PalaverHut;