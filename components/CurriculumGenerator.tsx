import React, { useState } from 'react';
import { generateLessonPlan, PersonaType } from '../services/geminiService';
import { Sparkles, Send, Clock, MessageCircle, ArrowLeft, Bookmark, Brain, Heart, Coins, Map, User, Briefcase, Glasses, Lock, Crown } from 'lucide-react';
import { CATEGORIES, ParentProfile } from '../types';

interface Props {
  parent: ParentProfile;
  onTriggerPaywall: () => void;
}

interface LessonPlan {
  title: string;
  duration: string;
  objective: string;
  storyOrProverb: string;
  discussionPoints: string[];
  practicalActivity: {
    name: string;
    instructions: string[];
  };
  streetSmartTip: string;
}

const CategoryIcon = ({ id, active }: { id: string, active: boolean }) => {
  const color = active ? "text-white" : "text-iroko-dark/50";
  switch(id) {
    case 'grit': return <Brain className={color} />;
    case 'money': return <Coins className={color} />;
    case 'respect': return <Heart className={color} />;
    case 'street-smarts': return <Map className={color} />;
    default: return <Sparkles className={color} />;
  }
};

const Personas: {id: PersonaType, name: string, role: string, icon: any, color: string, desc: string, premium: boolean}[] = [
  { 
    id: 'elder', 
    name: 'Baba Iroko', 
    role: 'The Wise Elder', 
    icon: User, 
    color: 'bg-iroko-earth', 
    desc: 'Patient wisdom, proverbs, and deep roots.',
    premium: false
  },
  { 
    id: 'auntie', 
    name: 'Auntie Nkechi', 
    role: 'The Disciplinarian', 
    icon: Glasses, 
    color: 'bg-iroko-clay', 
    desc: 'Strict on manners, hygiene, and respect.',
    premium: true
  },
  { 
    id: 'uncle', 
    name: 'Uncle Femi', 
    role: 'Lagos Hustler', 
    icon: Briefcase, 
    color: 'bg-iroko-dark', 
    desc: 'Street smarts, money moves, and ambition.',
    premium: true
  }
];

const CurriculumGenerator: React.FC<Props> = ({ parent, onTriggerPaywall }) => {
  const [step, setStep] = useState<'input' | 'loading' | 'result'>('input');
  const [topic, setTopic] = useState('');
  const [age, setAge] = useState<number>(8);
  const [selectedCategory, setSelectedCategory] = useState<string>(CATEGORIES[0].id);
  const [selectedPersona, setSelectedPersona] = useState<PersonaType>('elder');
  const [lessonPlan, setLessonPlan] = useState<LessonPlan | null>(null);

  const handlePersonaSelect = (persona: typeof Personas[0]) => {
     if (persona.premium && parent.subscriptionTier !== 'Chief') {
        onTriggerPaywall();
        return;
     }
     setSelectedPersona(persona.id);
  };

  const handleGenerate = async () => {
    if (!topic.trim()) return;
    
    setStep('loading');
    try {
      const result = await generateLessonPlan(
        `${selectedCategory} - ${topic}`,
        age,
        "Parent wants to teach a lesson today.",
        selectedPersona,
        parent.locationContext // Pass the parent's environment context
      );
      setLessonPlan(result);
      setStep('result');
    } catch (e) {
      console.error(e);
      setStep('input');
      alert("We couldn't reach the Council of Elders just now. Please try again.");
    }
  };

  if (step === 'loading') {
    const personaObj = Personas.find(p => p.id === selectedPersona);
    return (
      <div className="flex flex-col items-center justify-center min-h-[50vh] text-center space-y-8 animate-fade-in max-w-md mx-auto">
        <div className="relative">
          <div className={`w-24 h-24 border-4 border-opacity-20 rounded-full animate-spin border-t-current text-${personaObj?.color.replace('bg-', '')}`}></div>
          <div className="absolute inset-0 flex items-center justify-center text-4xl animate-pulse">
            {selectedPersona === 'elder' && '👴🏾'}
            {selectedPersona === 'auntie' && '👩🏾'}
            {selectedPersona === 'uncle' && '👨🏾'}
          </div>
        </div>
        <div>
          <h3 className="text-2xl font-display font-bold text-iroko-dark mb-2">{personaObj?.name} is thinking...</h3>
          <p className="text-stone-500 font-light">Crafting a lesson on {CATEGORIES.find(c => c.id === selectedCategory)?.label}.</p>
        </div>
      </div>
    );
  }

  if (step === 'result' && lessonPlan) {
    const personaObj = Personas.find(p => p.id === selectedPersona);
    
    return (
      <div className="max-w-3xl mx-auto pb-12 animate-slide-up">
        {/* Navigation */}
        <button 
          onClick={() => setStep('input')}
          className="mb-8 group flex items-center gap-2 text-stone-500 hover:text-iroko-dark transition-colors px-4 py-2"
        >
          <div className="w-8 h-8 rounded-full bg-white border border-stone-200 flex items-center justify-center group-hover:border-iroko-dark transition-colors">
            <ArrowLeft size={16} />
          </div>
          <span className="font-medium">Create New Lesson</span>
        </button>

        {/* The "Letter" Card */}
        <div className="bg-[#FFFCF8] rounded-[3rem] shadow-2xl shadow-stone-300/50 overflow-hidden relative border border-stone-100">
          
          {/* Subtle Paper Texture */}
          <div className="absolute inset-0 opacity-40 mix-blend-multiply bg-[url('https://www.transparenttextures.com/patterns/cream-paper.png')] pointer-events-none"></div>

          {/* Header Section */}
          <div className="p-8 md:p-12 border-b border-stone-100/50 relative z-10">
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 mb-6">
               <div className="flex gap-2">
                 <span className="inline-flex items-center gap-2 bg-iroko-earth text-white px-4 py-2 rounded-full text-xs font-bold uppercase tracking-wider shadow-md shadow-iroko-earth/20">
                   <CategoryIcon id={selectedCategory} active={true} />
                   {CATEGORIES.find(c => c.id === selectedCategory)?.label}
                 </span>
                 <span className={`inline-flex items-center gap-2 ${personaObj?.color} text-white px-4 py-2 rounded-full text-xs font-bold uppercase tracking-wider shadow-md`}>
                   {personaObj?.name}
                 </span>
               </div>
               <span className="inline-flex items-center gap-2 text-stone-500 font-medium text-sm bg-white/50 px-3 py-1 rounded-full border border-stone-100">
                 <Clock size={14}/> {lessonPlan.duration} Activity
               </span>
            </div>
            
            <h2 className="text-3xl md:text-5xl font-display font-bold text-iroko-dark mb-4 leading-[1.1]">
              {lessonPlan.title}
            </h2>
            <p className="text-xl text-stone-600 font-serif italic border-l-2 border-iroko-gold pl-4">
              "Goal: {lessonPlan.objective}"
            </p>
          </div>

          <div className="p-8 md:p-12 space-y-10 relative z-10 bg-gradient-to-b from-transparent to-[#F9F6F0]/50">
            
            {/* Story Section */}
            <section className="bg-white rounded-[2rem] p-8 shadow-sm border border-stone-50">
              <div className="flex items-center gap-3 mb-4">
                <span className="text-2xl">📜</span>
                <h3 className="font-bold text-iroko-dark text-sm uppercase tracking-widest">
                  {selectedPersona === 'elder' ? 'The Ancient Wisdom' : selectedPersona === 'uncle' ? 'The Street Code' : 'The Golden Rule'}
                </h3>
              </div>
              <p className="text-lg md:text-xl text-iroko-dark/80 leading-relaxed font-serif">
                "{lessonPlan.storyOrProverb}"
              </p>
            </section>

            {/* Discussion Points */}
            <section>
              <div className="flex items-center gap-3 mb-6 pl-2">
                <div className="w-8 h-8 rounded-full bg-iroko-green/10 flex items-center justify-center text-iroko-green">
                   <MessageCircle size={16} />
                </div>
                <h3 className="font-bold text-iroko-dark text-sm uppercase tracking-widest">Ask Your Child</h3>
              </div>
              <div className="grid gap-4">
                {lessonPlan.discussionPoints.map((point, idx) => (
                  <div key={idx} className="bg-white p-6 rounded-2xl border border-stone-100 shadow-sm hover:shadow-md transition-shadow flex gap-5 items-start">
                    <span className="text-iroko-green/40 font-bold text-2xl font-display leading-none">0{idx + 1}</span>
                    <span className="text-stone-700 font-medium text-lg leading-snug">{point}</span>
                  </div>
                ))}
              </div>
            </section>

            {/* Practical Activity */}
            <section className="bg-iroko-clay/5 rounded-[2.5rem] p-8 md:p-10 border border-iroko-clay/10 relative overflow-hidden">
               <div className="absolute -right-10 -top-10 w-40 h-40 bg-iroko-clay/10 rounded-full blur-3xl"></div>
               <h3 className="relative z-10 font-bold text-iroko-dark text-sm uppercase tracking-widest mb-6 flex items-center gap-2">
                 <span className="text-xl">⚡️</span> Action: {lessonPlan.practicalActivity.name}
               </h3>
               <div className="relative z-10 space-y-4">
                  {lessonPlan.practicalActivity.instructions.map((step, idx) => (
                    <div key={idx} className="flex gap-4 items-start">
                      <div className="mt-2 w-2 h-2 rounded-full bg-iroko-clay shrink-0"></div>
                      <span className="text-iroko-dark/90 text-lg leading-relaxed">{step}</span>
                    </div>
                  ))}
               </div>
            </section>

            {/* Street Smart Tip */}
            <section className={`p-8 md:p-10 rounded-[2.5rem] text-center relative overflow-hidden shadow-xl text-white ${personaObj?.color}`}>
               <div className="absolute inset-0 opacity-10 bg-[url('https://www.transparenttextures.com/patterns/cubes.png')]"></div>
               <div className="relative z-10">
                 <h4 className="text-white/70 text-[10px] font-bold uppercase tracking-widest mb-4">{personaObj?.name}'s Final Word</h4>
                 <p className="text-xl md:text-2xl font-display font-bold leading-tight">"{lessonPlan.streetSmartTip}"</p>
               </div>
            </section>
            
            <button className="w-full py-5 rounded-2xl border-2 border-iroko-dark text-iroko-dark font-bold text-lg hover:bg-iroko-dark hover:text-white transition-all flex items-center justify-center gap-2 group">
              <Bookmark size={20} className="group-hover:fill-current" /> Save Lesson to Journal
            </button>
          </div>
        </div>
      </div>
    );
  }

  // Input View (Wizard Style)
  return (
    <div className="max-w-xl mx-auto py-8">
      <div className="text-center mb-10">
        <h2 className="text-4xl font-display font-bold text-iroko-dark mb-3">Council of Elders</h2>
        <p className="text-stone-500 text-lg font-light">Who should teach this lesson?</p>
      </div>

      <div className="space-y-8">
        
        {/* Step 1: Persona Selection */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
           {Personas.map(persona => {
             const isLocked = persona.premium && parent.subscriptionTier !== 'Chief';
             
             return (
               <button
                 key={persona.id}
                 onClick={() => handlePersonaSelect(persona)}
                 className={`relative p-4 rounded-2xl border-2 transition-all duration-300 flex flex-col items-center text-center gap-2 overflow-hidden group
                   ${selectedPersona === persona.id 
                     ? `border-${persona.color.replace('bg-', '')} bg-stone-50 scale-105 shadow-lg` 
                     : 'border-transparent bg-white hover:bg-stone-50'}`}
               >
                  {/* Premium Badge Logic */}
                  {persona.premium && (
                    <div className={`absolute top-0 right-0 px-2 py-1 rounded-bl-xl text-[9px] font-bold uppercase tracking-wider flex items-center gap-1
                      ${isLocked ? 'bg-stone-200 text-stone-500' : 'bg-iroko-gold text-white'}`}>
                       {isLocked ? <Lock size={8} /> : <Crown size={8} fill="currentColor" />}
                       {isLocked ? 'Locked' : 'Chief'}
                    </div>
                  )}

                  <div className={`w-12 h-12 rounded-full ${persona.color} flex items-center justify-center text-white shadow-md mt-2 ${isLocked ? 'grayscale opacity-70' : ''}`}>
                     <persona.icon size={20} />
                  </div>
                  <div className={isLocked ? 'opacity-60' : ''}>
                    <h3 className={`font-bold text-sm ${selectedPersona === persona.id ? 'text-iroko-dark' : 'text-stone-500'}`}>{persona.name}</h3>
                    <p className="text-[10px] font-bold uppercase tracking-wider text-stone-400">{persona.role}</p>
                  </div>
                  
                  {/* Selected Indicator */}
                  {selectedPersona === persona.id && !isLocked && (
                     <div className="absolute top-2 left-2 bg-green-500 text-white p-1 rounded-full shadow-sm"><div className="w-2 h-2 bg-white rounded-full"></div></div>
                  )}

                  {/* Lock Overlay for Interaction Hint */}
                  {isLocked && (
                     <div className="absolute inset-0 bg-white/50 backdrop-blur-[1px] opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center">
                        <span className="bg-iroko-dark text-white text-[10px] font-bold px-2 py-1 rounded-full shadow-lg">Upgrade to Unlock</span>
                     </div>
                  )}
               </button>
             );
           })}
        </div>

        {/* Info Box */}
        <div className="bg-stone-50 p-4 rounded-xl text-xs text-stone-500 text-center leading-relaxed border border-stone-100">
           {Personas.find(p => p.id === selectedPersona)?.desc}
        </div>
        
        {/* Step 2: Age */}
        <div className="bg-white p-6 rounded-[2rem] shadow-sm border border-stone-100">
          <label className="flex items-center justify-between text-sm font-bold text-stone-400 uppercase tracking-wide mb-6">
             <span>Child's Age</span>
             <span className="text-iroko-dark bg-stone-100 px-3 py-1 rounded-full">{age} years</span>
          </label>
          <input 
            type="range" 
            min="4" 
            max="16" 
            value={age} 
            onChange={(e) => setAge(parseInt(e.target.value))}
            className="w-full h-4 bg-stone-100 rounded-lg appearance-none cursor-pointer accent-iroko-earth hover:accent-iroko-dark transition-all"
          />
          <div className="flex justify-between mt-3 text-xs font-medium text-stone-400">
            <span>Young Child</span>
            <span>Teenager</span>
          </div>
        </div>

        {/* Step 3: Category */}
        <div>
          <label className="block text-sm font-bold text-stone-400 uppercase tracking-wide mb-4 pl-4">What does this teach?</label>
          <div className="grid grid-cols-2 gap-4">
            {CATEGORIES.map(cat => (
              <button
                key={cat.id}
                onClick={() => setSelectedCategory(cat.id)}
                className={`p-5 rounded-[1.5rem] text-left transition-all duration-300 relative overflow-hidden group
                  ${selectedCategory === cat.id 
                    ? 'bg-iroko-dark text-white shadow-xl shadow-iroko-dark/20 scale-[1.02]' 
                    : 'bg-white text-stone-500 hover:bg-stone-50 border border-stone-100'}`}
              >
                <div className="flex items-center gap-3 mb-2">
                  <CategoryIcon id={cat.id} active={selectedCategory === cat.id} />
                </div>
                <span className="block font-bold text-lg">{cat.label}</span>
              </button>
            ))}
          </div>
        </div>

        {/* Step 4: Topic */}
        <div className="bg-white p-2 rounded-[2rem] shadow-sm border border-stone-100 focus-within:ring-2 focus-within:ring-iroko-earth/10 focus-within:border-iroko-earth transition-all">
          <textarea
            value={topic}
            onChange={(e) => setTopic(e.target.value)}
            placeholder="e.g. Tunde lied about breaking the plate..."
            className="w-full p-6 rounded-[1.8rem] bg-stone-50/50 border-none outline-none min-h-[160px] resize-none text-xl text-iroko-dark placeholder:text-stone-300 transition-all font-medium"
          />
        </div>

        <button
          onClick={handleGenerate}
          disabled={!topic.trim()}
          className="w-full py-6 bg-iroko-dark text-white rounded-[1.8rem] font-bold text-xl flex items-center justify-center gap-3 hover:bg-iroko-earth transition-all shadow-xl shadow-iroko-dark/20 disabled:opacity-50 disabled:shadow-none hover:scale-[1.02] active:scale-95"
        >
          <Sparkles size={24} className={topic.trim() ? "animate-pulse" : ""} /> 
          Ask {Personas.find(p => p.id === selectedPersona)?.name.split(' ')[0]}
        </button>

      </div>
    </div>
  );
};

export default CurriculumGenerator;