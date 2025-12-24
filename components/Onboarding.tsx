import React, { useState } from 'react';
import { ChevronRight, MapPin, Globe } from 'lucide-react';
import { Logo } from './Logo';
import { repository } from '../services/repository';
import { LocationContext } from '../types';

interface OnboardingProps {
  onComplete: () => void;
}

const slides = [
  {
    id: 1,
    image: 'https://images.unsplash.com/photo-1503919545889-aef636e10ad4?q=80&w=2670&auto=format&fit=crop', 
    title: "School teaches Math.\nWe teach Life.",
    description: "While school handles the algebra, Iroko helps you build your child's character, grit, and street smarts."
  },
  {
    id: 2,
    image: 'https://images.unsplash.com/photo-1515964893922-a7fc1c58c287?q=80&w=2574&auto=format&fit=crop', 
    title: "Resilience is a muscle.\nTrain it daily.",
    description: "Through small, practical missions, your child learns the value of hard work, money, and patience."
  },
  {
    id: 3,
    image: 'https://images.unsplash.com/photo-1531983412531-1f49a365ffed?q=80&w=2670&auto=format&fit=crop', 
    title: "Rooted in Culture.\nReady for the World.",
    description: "Modern parenting tools blended with the timeless wisdom of our elders. Let's build a giant."
  },
  // New Slide for Context Selection
  {
    id: 4,
    image: 'https://images.unsplash.com/photo-1543714909-3cb3d508e752?q=80&w=2670&auto=format&fit=crop',
    title: "Where is your Village?",
    description: "We tailor the curriculum based on your environment."
  }
];

export const Onboarding: React.FC<OnboardingProps> = ({ onComplete }) => {
  const [current, setCurrent] = useState(0);
  const [isAnimating, setIsAnimating] = useState(false);
  const [selectedLocation, setSelectedLocation] = useState<LocationContext | null>(null);

  const handleNext = async () => {
    // If on location slide, ensure selection
    if (current === slides.length - 1) {
       if (!selectedLocation) return;
       
       // Update Profile with Location
       const user = await repository.getCurrentUser();
       if (user) {
          await repository.updateParentProfile(user.id, { locationContext: selectedLocation });
       }
       onComplete();
       return;
    }

    setIsAnimating(true);
    setTimeout(() => {
      setCurrent(current + 1);
      setIsAnimating(false);
    }, 300);
  };

  const slide = slides[current];

  return (
    <div className="fixed inset-0 bg-iroko-dark text-white z-[100] overflow-hidden font-sans">
      
      {/* Background Layer */}
      {slides.map((s, index) => (
         <div 
           key={s.id}
           className={`absolute inset-0 transition-opacity duration-700 ease-in-out ${index === current ? 'opacity-100' : 'opacity-0'}`}
         >
            <img src={s.image} className="w-full h-full object-cover" alt="" />
            <div className="absolute inset-0 bg-gradient-to-t from-black/90 via-black/40 to-transparent"></div>
         </div>
      ))}

      {/* Top Bar with Logo */}
      <div className="absolute top-0 left-0 right-0 p-6 z-20 flex justify-between items-center safe-area-top">
         <div className="bg-white/10 backdrop-blur-md p-2 rounded-xl border border-white/10">
            <Logo className="w-8 h-8" variant="gold" />
         </div>
         {current < slides.length - 1 && (
             <button 
                onClick={() => {
                   setCurrent(slides.length - 1); // Skip to last slide to force selection
                }}
                className="px-4 py-2 bg-black/20 backdrop-blur-md rounded-full text-stone-200 font-bold text-xs uppercase tracking-widest hover:bg-black/40 transition-colors border border-white/10"
             >
                Skip
             </button>
         )}
      </div>

      {/* Content Layer */}
      <div className="relative z-10 flex flex-col h-full justify-end p-8 pb-16 md:p-16 max-w-2xl mx-auto safe-area-bottom">
        
        {/* Progress Indicators */}
        <div className="flex gap-2 mb-8">
           {slides.map((_, idx) => (
             <div key={idx} className={`h-1.5 rounded-full transition-all duration-500 ${idx === current ? 'w-8 bg-iroko-gold' : 'w-2 bg-white/20'}`} />
           ))}
        </div>

        {/* Text Area */}
        <div className={`transition-all duration-500 transform ${isAnimating ? 'translate-y-4 opacity-0' : 'translate-y-0 opacity-100'} mb-8`}>
           <h1 className="text-4xl md:text-5xl font-display font-bold mb-4 leading-[1.1] text-white whitespace-pre-line drop-shadow-lg">
              {slide.title}
           </h1>
           <p className="text-lg text-stone-200 font-medium leading-relaxed max-w-md drop-shadow-md">
              {slide.description}
           </p>

           {/* Location Selection UI (Only on last slide) */}
           {current === slides.length - 1 && (
              <div className="grid grid-cols-2 gap-4 mt-8">
                 <button 
                   onClick={() => setSelectedLocation('home')}
                   className={`p-4 rounded-2xl border-2 flex flex-col items-center gap-3 transition-all ${selectedLocation === 'home' ? 'bg-iroko-gold border-iroko-gold text-iroko-dark' : 'bg-white/10 border-white/20 hover:bg-white/20'}`}
                 >
                    <div className="w-12 h-12 bg-green-900 rounded-full flex items-center justify-center text-white text-xl">🇳🇬</div>
                    <div>
                       <span className="block font-bold text-sm">Home</span>
                       <span className="text-xs opacity-80">(Nigeria)</span>
                    </div>
                 </button>

                 <button 
                   onClick={() => setSelectedLocation('diaspora')}
                   className={`p-4 rounded-2xl border-2 flex flex-col items-center gap-3 transition-all ${selectedLocation === 'diaspora' ? 'bg-iroko-gold border-iroko-gold text-iroko-dark' : 'bg-white/10 border-white/20 hover:bg-white/20'}`}
                 >
                    <div className="w-12 h-12 bg-blue-900 rounded-full flex items-center justify-center text-white text-xl">🌍</div>
                    <div>
                       <span className="block font-bold text-sm">Diaspora</span>
                       <span className="text-xs opacity-80">(Abroad)</span>
                    </div>
                 </button>
              </div>
           )}
        </div>

        {/* Controls */}
        <div className="flex items-center justify-end">
           <button 
             onClick={handleNext}
             disabled={current === slides.length - 1 && !selectedLocation}
             className={`w-20 h-20 bg-iroko-gold text-iroko-dark rounded-full flex items-center justify-center hover:scale-105 active:scale-95 transition-all shadow-[0_0_30px_rgba(198,156,58,0.4)] ${current === slides.length - 1 && !selectedLocation ? 'opacity-50 grayscale cursor-not-allowed' : ''}`}
             aria-label="Next Slide"
           >
             <ChevronRight size={32} strokeWidth={3} />
           </button>
        </div>
      </div>
    </div>
  );
};
