import React, { useState } from 'react';
import { Check, X, Star, ShieldCheck, Zap, Crown, CreditCard } from 'lucide-react';
import { repository } from '../services/repository';

interface PaywallProps {
  onClose: () => void;
  onUpgradeSuccess: () => void;
  parentId: string;
}

export const Paywall: React.FC<PaywallProps> = ({ onClose, onUpgradeSuccess, parentId }) => {
  const [isProcessing, setIsProcessing] = useState(false);
  const [billingCycle, setBillingCycle] = useState<'monthly' | 'yearly'>('yearly');

  const handleSubscribe = async () => {
    setIsProcessing(true);
    
    // Check for Admin Configured Payment Gateway
    const config = repository.getPaymentConfig();

    if (config.isEnabled && config.provider === 'paystack' && config.publicKey) {
       // SIMULATE PAYSTACK POPUP
       // In a real implementation, you would use 'react-paystack' here with config.publicKey
       console.log(`Initializing Paystack (${config.isLiveMode ? 'Live' : 'Test'}) with key: ${config.publicKey}`);
       
       await new Promise(resolve => setTimeout(resolve, 2000));
       
       // Allow user to see the "popup" experience (Simulated)
       const confirmed = window.confirm(`[Paystack Simulation]\n\nProcessing payment of N${billingCycle === 'monthly' ? '2,500' : '24,000'} via Paystack.\n\nClick OK to simulate success.`);
       
       if (confirmed) {
          await repository.upgradeSubscription(parentId);
          onUpgradeSuccess();
       } else {
          setIsProcessing(false);
       }
    } else {
       // Fallback Mock (Old Behavior)
       await new Promise(resolve => setTimeout(resolve, 2000));
       await repository.upgradeSubscription(parentId);
       onUpgradeSuccess();
    }
  };

  const prices = {
    monthly: 2500,
    yearly: 24000 // 20% discount roughly (2000/mo)
  };

  return (
    <div className="fixed inset-0 z-[100] flex items-center justify-center p-4 overflow-y-auto">
      <div className="absolute inset-0 bg-iroko-dark/90 backdrop-blur-md transition-opacity" onClick={onClose} />
      
      <div className="relative bg-[#1A1512] text-white rounded-[2.5rem] shadow-2xl max-w-4xl w-full overflow-hidden animate-slide-up flex flex-col md:flex-row border border-white/10 my-8 md:my-0">
        
        {/* Left Side: Value Prop (Visual) */}
        <div className="md:w-5/12 bg-iroko-earth relative p-8 md:p-12 flex flex-col justify-between overflow-hidden">
           {/* Background Pattern */}
           <div className="absolute inset-0 opacity-20 bg-[url('https://www.transparenttextures.com/patterns/cubes.png')] mix-blend-overlay"></div>
           <div className="absolute top-0 right-0 w-64 h-64 bg-iroko-gold rounded-full filter blur-[100px] opacity-30"></div>

           <div className="relative z-10">
              <div className="inline-flex items-center gap-2 bg-black/20 backdrop-blur-md px-3 py-1 rounded-full border border-white/10 text-xs font-bold uppercase tracking-widest mb-6">
                 <Crown size={14} className="text-iroko-gold" />
                 <span>Premium Access</span>
              </div>
              <h2 className="text-4xl md:text-5xl font-display font-bold leading-none mb-4">
                Raise a <br/><span className="text-iroko-gold">Giant.</span>
              </h2>
              <p className="text-white/80 text-lg leading-relaxed">
                Unlock the full village. Give your child the unfair advantage of ancient wisdom meeting modern street smarts.
              </p>
           </div>

           <div className="relative z-10 mt-12">
              <div className="bg-black/20 backdrop-blur-sm rounded-2xl p-4 border border-white/10">
                 <div className="flex gap-1 text-iroko-gold mb-2">
                    <Star fill="currentColor" size={14} /><Star fill="currentColor" size={14} /><Star fill="currentColor" size={14} /><Star fill="currentColor" size={14} /><Star fill="currentColor" size={14} />
                 </div>
                 <p className="text-sm font-medium italic text-white/90 mb-2">"My son Tunde used to waste money. Now he negotiates prices in the market. This app is the village we lost."</p>
                 <p className="text-xs font-bold uppercase tracking-widest text-white/50">— Mrs. Adebayo, Lagos</p>
              </div>
           </div>
        </div>

        {/* Right Side: Pricing & Form */}
        <div className="md:w-7/12 bg-white text-iroko-dark p-8 md:p-12 relative">
           <button onClick={onClose} className="absolute top-6 right-6 p-2 bg-stone-100 rounded-full hover:bg-stone-200 transition-colors">
              <X size={20} className="text-stone-500" />
           </button>

           <div className="mb-8 text-center">
              <h3 className="text-2xl font-display font-bold mb-6">Choose your Plan</h3>
              
              {/* Toggle */}
              <div className="inline-flex bg-stone-100 p-1 rounded-full relative">
                 <div className={`absolute top-1 bottom-1 w-1/2 bg-white rounded-full shadow-sm transition-all duration-300 ${billingCycle === 'monthly' ? 'left-1' : 'left-[48.5%]'}`}></div>
                 <button 
                   onClick={() => setBillingCycle('monthly')}
                   className={`relative z-10 px-6 py-2 text-sm font-bold rounded-full transition-colors ${billingCycle === 'monthly' ? 'text-iroko-dark' : 'text-stone-400'}`}
                 >
                    Monthly
                 </button>
                 <button 
                   onClick={() => setBillingCycle('yearly')}
                   className={`relative z-10 px-6 py-2 text-sm font-bold rounded-full transition-colors flex items-center gap-2 ${billingCycle === 'yearly' ? 'text-iroko-dark' : 'text-stone-400'}`}
                 >
                    Yearly <span className="bg-green-100 text-green-700 text-[9px] px-2 py-0.5 rounded-full">SAVE 20%</span>
                 </button>
              </div>
           </div>

           <div className="text-center mb-8">
              <div className="flex items-baseline justify-center gap-1">
                 <span className="text-5xl font-display font-bold text-iroko-dark">₦{billingCycle === 'monthly' ? '2,500' : '2,000'}</span>
                 <span className="text-stone-400 font-medium">/ month</span>
              </div>
              {billingCycle === 'yearly' && <p className="text-xs text-stone-400 mt-2">Billed ₦24,000 yearly</p>}
           </div>

           <div className="space-y-4 mb-8">
              <FeatureItem text="Unlimited AI Mentor Access (Baba, Auntie, Uncle)" />
              <FeatureItem text="Full 50+ Mission Curriculum" />
              <FeatureItem text="Advanced Growth Analytics" />
              <FeatureItem text="Priority Support from the Village" />
           </div>

           <button
             onClick={handleSubscribe}
             disabled={isProcessing}
             className="w-full py-5 bg-iroko-dark text-white rounded-2xl font-bold text-lg hover:bg-iroko-earth hover:scale-[1.02] active:scale-95 transition-all flex items-center justify-center gap-3 shadow-xl shadow-iroko-dark/20 relative overflow-hidden group"
           >
             {isProcessing ? (
               <span className="animate-pulse">Connecting to Paystack...</span>
             ) : (
               <>
                 <span>Become a Chief</span>
                 <Zap className="group-hover:fill-current transition-colors" size={20} />
               </>
             )}
           </button>

           <div className="mt-6 flex items-center justify-center gap-2 text-xs text-stone-400">
              <CreditCard size={14} />
              <span>Secured by Paystack. Cancel anytime.</span>
           </div>
        </div>
      </div>
    </div>
  );
};

const FeatureItem = ({ text }: { text: string }) => (
  <div className="flex items-center gap-3 bg-stone-50 p-3 rounded-xl border border-stone-100">
    <div className="w-5 h-5 rounded-full bg-green-100 flex items-center justify-center shrink-0">
      <Check size={12} className="text-green-600" strokeWidth={3} />
    </div>
    <span className="text-iroko-dark font-medium text-sm">{text}</span>
  </div>
);