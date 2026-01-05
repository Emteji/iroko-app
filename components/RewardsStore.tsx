import React, { useEffect, useState } from 'react';
import { ShoppingBag, Plus, Trash2, Coins, Gift, AlertCircle } from 'lucide-react';
import { repository } from '../services/repository';
import { ChildProfile, ParentProfile, Reward } from '../types';

const RewardsStore = () => {
  const [loading, setLoading] = useState(true);
  const [parent, setParent] = useState<ParentProfile | null>(null);
  const [children, setChildren] = useState<ChildProfile[]>([]);
  const [selectedChild, setSelectedChild] = useState<ChildProfile | null>(null);
  const [rewards, setRewards] = useState<Reward[]>([]);
  const [isAdding, setIsAdding] = useState(false);
  const [newReward, setNewReward] = useState({ title: '', cost: 100, emoji: '🎁' });
  const [notification, setNotification] = useState<{msg: string, type: 'success' | 'error'} | null>(null);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    const p = await repository.getCurrentUser();
    if (p) {
      setParent(p);
      const kids = await repository.getChildren(p.id);
      setChildren(kids);
      if (kids.length > 0) setSelectedChild(kids[0]);
      
      const r = await repository.getRewards(p.id);
      setRewards(r);
    }
    setLoading(false);
  };

  const handleCreateReward = async () => {
     if(!parent || !newReward.title) return;
     await repository.createReward({
        parentId: parent.id,
        title: newReward.title,
        cost: newReward.cost,
        emoji: newReward.emoji
     });
     setIsAdding(false);
     setNewReward({ title: '', cost: 100, emoji: '🎁' });
     loadData();
  };

  const handleRedeem = async (reward: Reward) => {
     if(!selectedChild) return;
     const success = await repository.redeemReward(selectedChild.id, reward);
     if(success) {
        setNotification({ msg: `Successfully redeemed ${reward.title}!`, type: 'success' });
        loadData(); // Refresh balance
     } else {
        setNotification({ msg: "Not enough XP to buy this item.", type: 'error' });
     }
     setTimeout(() => setNotification(null), 3000);
  };

  if(loading) return <div className="p-12 text-center text-stone-400">Loading Market...</div>;

  return (
    <div className="max-w-4xl mx-auto space-y-8 pb-12 animate-fade-in">
       {/* Header */}
       <div className="flex flex-col md:flex-row justify-between items-end gap-6">
          <div>
            <div className="flex items-center gap-3 mb-2">
                <div className="w-10 h-10 bg-iroko-green rounded-xl flex items-center justify-center text-white shadow-lg shadow-iroko-green/20">
                    <ShoppingBag size={20} />
                </div>
                <h2 className="text-3xl font-display font-bold text-iroko-dark">The Village Market</h2>
            </div>
            <p className="text-stone-500 max-w-md">Teach the value of hard work. Kids spend their earned XP on rewards you approve.</p>
          </div>
          
          {/* Child Wallet */}
          {selectedChild && (
             <div className="bg-iroko-dark text-white px-6 py-4 rounded-2xl shadow-xl flex items-center gap-4 border border-white/10 relative overflow-hidden">
                <div className="absolute inset-0 bg-[url('https://www.transparenttextures.com/patterns/cubes.png')] opacity-10"></div>
                <div className="text-right relative z-10">
                   <p className="text-xs font-bold text-iroko-gold uppercase tracking-wider mb-1">{selectedChild.name}'s Wallet</p>
                   <p className="text-3xl font-display font-bold">{selectedChild.gritPoints + selectedChild.omoluabiPoints} XP</p>
                </div>
                <div className="w-12 h-12 bg-white/10 rounded-full flex items-center justify-center relative z-10">
                   <Coins className="text-iroko-gold" />
                </div>
             </div>
          )}
       </div>

       {/* Child Selector (If multiple) */}
       {children.length > 1 && (
         <div className="flex gap-2 overflow-x-auto pb-2">
            {children.map(child => (
               <button
                 key={child.id}
                 onClick={() => setSelectedChild(child)}
                 className={`px-4 py-2 rounded-full text-sm font-bold border transition-all ${selectedChild?.id === child.id ? 'bg-iroko-earth text-white border-iroko-earth' : 'bg-white text-stone-500 border-stone-200'}`}
               >
                 {child.name}
               </button>
            ))}
         </div>
       )}

       {/* Notification Toast */}
       {notification && (
         <div className={`fixed top-4 right-4 z-50 px-6 py-4 rounded-xl shadow-2xl flex items-center gap-3 animate-slide-up text-white font-bold ${notification.type === 'success' ? 'bg-green-600' : 'bg-red-500'}`}>
            {notification.type === 'success' ? <Gift size={20} /> : <AlertCircle size={20}/>}
            {notification.msg}
         </div>
       )}

       {/* Store Grid */}
       <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
          {/* Add New Card */}
          <button 
             onClick={() => setIsAdding(true)}
             className="min-h-[240px] border-2 border-dashed border-stone-300 rounded-[2rem] flex flex-col items-center justify-center text-stone-400 hover:border-iroko-earth hover:text-iroko-earth hover:bg-stone-50 transition-all group"
          >
             <div className="w-16 h-16 rounded-full bg-stone-100 group-hover:bg-iroko-earth/10 flex items-center justify-center mb-4 transition-colors">
                <Plus size={32} />
             </div>
             <span className="font-bold">Add New Reward</span>
          </button>

          {/* Items */}
          {rewards.map(reward => (
             <div key={reward.id} className="bg-white p-6 rounded-[2rem] shadow-sm border border-stone-100 relative group hover:shadow-xl transition-all hover:-translate-y-1">
                <div className="text-6xl mb-6 text-center group-hover:scale-110 transition-transform duration-300">{reward.emoji}</div>
                <h3 className="font-bold text-xl text-iroko-dark mb-2 text-center">{reward.title}</h3>
                
                <div className="flex items-center justify-center gap-1 text-iroko-earth font-bold text-lg mb-6 bg-stone-50 py-2 rounded-xl">
                   <Coins size={16} fill="currentColor" /> {reward.cost} XP
                </div>

                <button 
                   onClick={() => handleRedeem(reward)}
                   className="w-full py-3 bg-iroko-dark text-white rounded-xl font-bold hover:bg-iroko-green transition-colors flex items-center justify-center gap-2"
                >
                   Redeem Item
                </button>
             </div>
          ))}
       </div>

       {/* Add Reward Modal */}
       {isAdding && (
          <div className="fixed inset-0 bg-iroko-dark/50 backdrop-blur-sm z-50 flex items-center justify-center p-4">
             <div className="bg-white w-full max-w-sm rounded-[2rem] p-8 shadow-2xl animate-slide-up">
                <h3 className="text-2xl font-bold text-iroko-dark mb-6">Create Reward</h3>
                
                <div className="space-y-4">
                   <div>
                      <label className="text-xs font-bold text-stone-400 uppercase tracking-wider block mb-2">Item Name</label>
                      <input 
                         className="w-full p-4 bg-stone-50 rounded-xl font-bold text-iroko-dark border border-stone-100 focus:border-iroko-earth outline-none"
                         placeholder="e.g. Cinema Ticket"
                         value={newReward.title}
                         onChange={e => setNewReward({...newReward, title: e.target.value})}
                      />
                   </div>
                   
                   <div className="flex gap-4">
                      <div className="flex-1">
                         <label className="text-xs font-bold text-stone-400 uppercase tracking-wider block mb-2">Cost (XP)</label>
                         <input 
                            type="number"
                            className="w-full p-4 bg-stone-50 rounded-xl font-bold text-iroko-dark border border-stone-100 outline-none"
                            value={newReward.cost}
                            onChange={e => setNewReward({...newReward, cost: parseInt(e.target.value)})}
                         />
                      </div>
                      <div className="w-20">
                         <label className="text-xs font-bold text-stone-400 uppercase tracking-wider block mb-2">Icon</label>
                         <input 
                            className="w-full p-4 bg-stone-50 rounded-xl font-bold text-center text-xl border border-stone-100 outline-none"
                            value={newReward.emoji}
                            onChange={e => setNewReward({...newReward, emoji: e.target.value})}
                         />
                      </div>
                   </div>
                </div>

                <div className="flex gap-3 mt-8">
                   <button onClick={() => setIsAdding(false)} className="flex-1 py-4 text-stone-500 font-bold hover:bg-stone-50 rounded-xl">Cancel</button>
                   <button onClick={handleCreateReward} className="flex-1 py-4 bg-iroko-earth text-white font-bold rounded-xl shadow-lg shadow-iroko-earth/20 hover:scale-105 transition-transform">Create</button>
                </div>
             </div>
          </div>
       )}
    </div>
  );
};

export default RewardsStore;