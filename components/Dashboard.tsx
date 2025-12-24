import React, { useEffect, useState } from 'react';
import { ArrowRight, Lock, Check, Star, Zap, Upload, Loader2, PlayCircle, Trophy, Crown, ClipboardCheck, X, Clock, Trash2 } from 'lucide-react';
import { repository } from '../services/repository';
import { ParentProfile, ChildProfile, Mission } from '../types';

interface DashboardProps {
  parent: ParentProfile;
  onViewChange: (view: 'dashboard' | 'curriculum' | 'progress' | 'settings') => void;
  onTriggerPaywall: () => void;
  onRefreshProfile: () => void;
}

const Dashboard: React.FC<DashboardProps> = ({ parent, onViewChange, onTriggerPaywall, onRefreshProfile }) => {
  const [loading, setLoading] = useState(true);
  const [children, setChildren] = useState<ChildProfile[]>([]);
  const [selectedChild, setSelectedChild] = useState<ChildProfile | null>(null);
  const [activeMissions, setActiveMissions] = useState<Mission[]>([]);
  const [pendingApprovals, setPendingApprovals] = useState<any[]>([]);
  const [wisdom, setWisdom] = useState({ proverb: "The river that forgets its source will dry up.", origin: "Yoruba Proverb" });
  
  // Leaderboard Snippet State
  const [topStudents, setTopStudents] = useState<ChildProfile[]>([]);
  const [completionMode, setCompletionMode] = useState<string | null>(null); 
  const [isUploading, setIsUploading] = useState(false);
  const [proofFile, setProofFile] = useState<File | null>(null);
  const [proofPreview, setProofPreview] = useState<string | null>(null);

  // Helper to refresh data
  const fetchData = async () => {
    const kids = await repository.getChildren(parent.id);
    setChildren(kids);
    if (kids.length > 0 && !selectedChild) setSelectedChild(kids[0]);
    
    // Fetch Pending Approvals
    const approvals = await repository.getPendingApprovals(parent.id);
    setPendingApprovals(approvals);

    const leaders = await repository.getLeaderboard();
    setTopStudents(leaders.slice(0, 3));
    
    setWisdom(repository.getDailyWisdom());
    setLoading(false);
  };

  useEffect(() => {
    fetchData();
  }, [parent.id]);

  useEffect(() => {
    const fetchMissions = async () => {
      if (selectedChild) {
        const missions = await repository.getRecommendedMissions(selectedChild.id);
        setActiveMissions(missions);
      }
    };
    fetchMissions();
  }, [selectedChild, pendingApprovals]); // Refresh when approvals change

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      setProofFile(file);
      setProofPreview(URL.createObjectURL(file));
    }
  };

  const clearProof = () => {
    setProofFile(null);
    if (proofPreview) {
      URL.revokeObjectURL(proofPreview);
      setProofPreview(null);
    }
  };

  const handleMarkComplete = async () => {
    if (!selectedChild || !completionMode) return;
    setIsUploading(true);
    let proofUrl = undefined;
    
    try {
      if (proofFile) {
         const url = await repository.uploadProof(proofFile);
         if (url) proofUrl = url;
      }
      // Submits for approval now
      await repository.submitMission(selectedChild.id, completionMode, proofUrl);
    } catch (e) {
      console.error("Submission error", e);
    } finally {
      setCompletionMode(null);
      clearProof();
      setIsUploading(false);
      await fetchData();
    }
  };

  const handleApprove = async (logId: string) => {
     await repository.approveMission(logId);
     await fetchData(); // Refresh approvals list and child points
  };

  const handleReject = async (logId: string) => {
     await repository.rejectMission(logId);
     await fetchData();
  };

  if (loading) return <div className="min-h-[40vh] flex items-center justify-center text-iroko-earth/50 font-display animate-pulse">Gathering the Elders...</div>;
  if (!selectedChild) return <div className="text-center p-10 text-gray-500">Please add a child in your account settings.</div>;

  return (
    <div className="space-y-8 animate-fade-in relative">
      
      {/* Greeting & Child Toggle */}
      <section className="flex flex-col gap-4">
        <div className="flex justify-between items-center">
           <div>
             <p className="text-stone-400 text-xs font-bold uppercase tracking-widest mb-1">Welcome back,</p>
             <h1 className="text-3xl md:text-4xl font-display font-bold text-iroko-dark flex items-center gap-2">
                {parent.name.split(' ')[0]}
                {parent.subscriptionTier === 'Chief' && <Crown size={24} className="text-iroko-gold fill-current" />}
             </h1>
           </div>
           {/* Modern Child Switcher Pill */}
           {children.length > 1 && (
             <div className="bg-white p-1 rounded-full border border-stone-200 flex shadow-sm">
                {children.map(child => (
                   <button 
                     key={child.id}
                     onClick={() => setSelectedChild(child)}
                     className={`px-4 py-2 rounded-full text-xs font-bold transition-all ${selectedChild.id === child.id ? 'bg-iroko-dark text-white shadow-md' : 'text-stone-400 hover:text-iroko-earth'}`}
                   >
                      {child.name}
                   </button>
                ))}
             </div>
           )}
        </div>
      </section>

      {/* --- APPROVAL WIDGET (Chief's Desk) --- */}
      {pendingApprovals.length > 0 && (
         <section className="bg-white border-l-4 border-iroko-gold rounded-2xl shadow-xl p-6 animate-slide-up relative overflow-hidden">
            <div className="absolute top-0 right-0 p-4 opacity-10"><ClipboardCheck size={100} /></div>
            
            <h3 className="text-lg font-bold text-iroko-dark mb-4 flex items-center gap-2">
               <Crown size={20} className="text-iroko-gold fill-current"/>
               Chief's Desk: Approval Needed
            </h3>
            
            <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3 relative z-10">
               {pendingApprovals.map((log) => (
                  <div key={log.id} className="bg-stone-50 p-4 rounded-xl border border-stone-100">
                     <div className="flex items-center gap-3 mb-3">
                        <img src={log.avatarUrl} className="w-8 h-8 rounded-full bg-stone-200" alt="" />
                        <div>
                           <p className="text-sm font-bold text-iroko-dark">{log.childName}</p>
                           <p className="text-xs text-stone-500">Submitted just now</p>
                        </div>
                     </div>
                     <p className="text-sm font-medium mb-1">{log.missionTitle}</p>
                     <p className="text-xs text-iroko-green font-bold mb-3">+{log.xpReward} XP Reward</p>
                     
                     {log.proofUrl && (
                        <div className="mb-3">
                           <a href={log.proofUrl} target="_blank" rel="noopener noreferrer" className="text-xs text-blue-500 underline flex items-center gap-1">
                              <Upload size={10} /> View Proof
                           </a>
                        </div>
                     )}

                     <div className="flex gap-2 mt-2">
                        <button onClick={() => handleApprove(log.id)} className="flex-1 bg-iroko-dark text-white py-2 rounded-lg text-xs font-bold flex items-center justify-center gap-1 hover:bg-green-700">
                           <Check size={14} /> Approve
                        </button>
                        <button onClick={() => handleReject(log.id)} className="px-3 py-2 bg-white border border-stone-200 text-stone-500 rounded-lg hover:text-red-500 hover:border-red-200">
                           <X size={14} />
                        </button>
                     </div>
                  </div>
               ))}
            </div>
         </section>
      )}

      {/* Two Column Layout for Passport + Leaderboard */}
      <section className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {/* The "Passport" Card */}
        <div className="relative w-full rounded-[2.5rem] bg-iroko-dark overflow-hidden shadow-2xl shadow-iroko-dark/30 text-white p-8 group transition-all duration-500 hover:scale-[1.01]">
           {/* Background Elements */}
           <div className="absolute top-0 right-0 w-64 h-64 bg-iroko-gold rounded-full mix-blend-overlay blur-[80px] opacity-20 animate-pulse-slow"></div>
           <div className="absolute bottom-0 left-0 w-64 h-64 bg-iroko-clay rounded-full mix-blend-overlay blur-[80px] opacity-20"></div>
           <div className="absolute inset-0 bg-[url('https://www.transparenttextures.com/patterns/cubes.png')] opacity-10"></div>
           
           <div className="relative z-10 flex flex-col sm:flex-row items-center gap-6">
              <div className="relative">
                 <div className="w-20 h-20 rounded-3xl p-1 bg-gradient-to-br from-white/20 to-transparent backdrop-blur-md border border-white/20 shadow-lg rotate-3 group-hover:rotate-0 transition-transform duration-500">
                    <img src={selectedChild.avatarUrl} className="w-full h-full rounded-2xl bg-stone-800 object-cover" />
                 </div>
                 <div className="absolute -bottom-2 -right-2 bg-iroko-gold text-iroko-dark text-[10px] font-bold px-2 py-1 rounded-full border border-iroko-dark uppercase tracking-wider">
                    Lvl {Math.floor((selectedChild.gritPoints + selectedChild.omoluabiPoints)/100) + 1}
                 </div>
              </div>

              <div className="flex-1 text-center sm:text-left">
                 <div className="mb-4">
                    <p className="text-white/50 text-[10px] font-bold uppercase tracking-widest mb-1">Current Status</p>
                    <h2 className="text-2xl font-display font-bold text-white flex items-center justify-center sm:justify-start gap-2">
                       {selectedChild.currentLevel}
                    </h2>
                 </div>
                 
                 <div className="flex gap-3 justify-center sm:justify-start">
                    <div className="bg-white/10 backdrop-blur-md rounded-xl px-3 py-1.5 border border-white/5">
                       <p className="text-[10px] text-white/60 uppercase font-bold">Grit</p>
                       <p className="text-lg font-bold text-white">{selectedChild.gritPoints}</p>
                    </div>
                    <div className="bg-white/10 backdrop-blur-md rounded-xl px-3 py-1.5 border border-white/5">
                       <p className="text-[10px] text-white/60 uppercase font-bold">Culture</p>
                       <p className="text-lg font-bold text-white">{selectedChild.omoluabiPoints}</p>
                    </div>
                 </div>
              </div>
           </div>
        </div>

        {/* Mini Leaderboard Widget */}
        <div className="bg-white rounded-[2.5rem] p-6 border border-stone-100 shadow-sm flex flex-col relative overflow-hidden">
           <div className="flex items-center justify-between mb-4 relative z-10">
              <h3 className="font-bold text-iroko-dark flex items-center gap-2">
                 <Trophy size={18} className="text-iroko-gold" />
                 Village Leaders
              </h3>
              <button onClick={() => onViewChange('progress')} className="text-xs font-bold text-stone-400 hover:text-iroko-earth">See All</button>
           </div>
           
           <div className="space-y-3 relative z-10">
              {topStudents.map((student, idx) => (
                 <div key={student.id} className="flex items-center gap-3">
                    <span className={`w-5 text-center text-xs font-bold ${idx === 0 ? 'text-iroko-gold' : 'text-stone-400'}`}>#{idx + 1}</span>
                    <img src={student.avatarUrl} className="w-8 h-8 rounded-full bg-stone-100" />
                    <span className="text-sm font-medium text-iroko-dark truncate flex-1">{student.name}</span>
                    <span className="text-xs font-bold text-stone-400">{student.gritPoints + student.omoluabiPoints} XP</span>
                 </div>
              ))}
           </div>
           
           {/* Decor */}
           <div className="absolute -bottom-4 -right-4 w-24 h-24 bg-stone-50 rounded-full z-0"></div>
        </div>
      </section>

      {/* Daily Wisdom Scroll - Improved Visibility */}
      <section className="bg-[#FAF9F6] rounded-[2rem] p-6 border border-[#EBE5DA] shadow-sm relative overflow-hidden flex items-start gap-4">
         <div className="mt-1 bg-iroko-gold/10 p-2 rounded-full text-iroko-gold">
            <Star size={18} fill="currentColor" />
         </div>
         <div className="flex-1">
             <p className="font-serif italic text-lg text-iroko-dark leading-relaxed mb-2">"{wisdom.proverb}"</p>
             <p className="text-xs font-bold text-iroko-clay uppercase tracking-widest">— {wisdom.origin}</p>
         </div>
      </section>

      {/* Active Missions Stream */}
      <section>
        <div className="flex items-center justify-between mb-4 px-2">
           <h3 className="font-display font-bold text-xl text-iroko-dark">Mission Board</h3>
           {parent.subscriptionTier === 'Villager' && (
              <button onClick={onTriggerPaywall} className="text-[10px] font-bold bg-iroko-gold text-white px-3 py-1 rounded-full animate-pulse shadow-lg shadow-iroko-gold/30 hover:scale-105 transition-transform">
                 UPGRADE TO CHIEF
              </button>
           )}
        </div>

        <div className="space-y-6">
        {activeMissions.length > 0 ? activeMissions.map((mission, index) => {
           // Locked logic: If user is Villager, only the first mission (index 0) is unlocked.
           const isLocked = parent.subscriptionTier === 'Villager' && index > 0;
           
           // Check if this mission is pending for this child
           const isPending = pendingApprovals.some(p => p.childId === selectedChild.id && p.missionId === mission.id);

           return (
            <div key={mission.id} className="relative group">
              <div className={`bg-white rounded-[2.5rem] shadow-xl shadow-stone-200/50 overflow-hidden border border-stone-100 transition-all duration-300 ${isLocked ? 'blur-[2px] opacity-80 pointer-events-none' : ''}`}>
                  <div className="p-8">
                     <div className="flex justify-between items-start mb-4">
                        <span className="bg-iroko-green/10 text-iroko-green px-3 py-1 rounded-full text-xs font-bold uppercase tracking-wider border border-iroko-green/20">
                           {mission.category}
                        </span>
                        <span className="text-sm font-bold text-iroko-earth flex items-center gap-1">
                           <Zap size={16} fill="currentColor"/> +{mission.xpReward} XP
                        </span>
                     </div>
                     
                     <h3 className="text-2xl font-display font-bold text-iroko-dark mb-2 leading-tight">{mission.title}</h3>
                     <p className="text-stone-500 text-lg leading-relaxed mb-6 font-light">{mission.description}</p>

                     {/* Action Area */}
                     {!isLocked && (
                         isPending ? (
                            <div className="w-full py-4 bg-amber-50 text-amber-600 rounded-2xl font-bold text-lg flex items-center justify-center gap-3 border border-amber-100">
                               <Clock size={24} /> Pending Approval
                            </div>
                         ) : completionMode === mission.id ? (
                            <div className="bg-stone-50 p-6 rounded-2xl border-2 border-dashed border-stone-200 animate-fade-in">
                               <div className="flex flex-col gap-4">
                                  {/* File Input Area */}
                                  <div className="relative">
                                      {proofPreview ? (
                                          <div className="relative h-48 w-full rounded-xl overflow-hidden border border-stone-200 group bg-white">
                                              <img src={proofPreview} alt="Proof" className="w-full h-full object-cover" />
                                              <button 
                                                  onClick={clearProof}
                                                  className="absolute top-2 right-2 p-2 bg-black/50 hover:bg-red-500 text-white rounded-full transition-colors"
                                              >
                                                  <Trash2 size={16} />
                                              </button>
                                              <div className="absolute bottom-0 left-0 right-0 bg-black/60 text-white text-xs p-2 truncate">
                                                  {proofFile?.name}
                                              </div>
                                          </div>
                                      ) : (
                                          <label className="flex flex-col items-center justify-center h-32 bg-white rounded-xl border border-stone-200 cursor-pointer hover:border-iroko-earth transition-colors relative overflow-hidden group">
                                             <div className="absolute inset-0 bg-stone-50/50 group-hover:bg-transparent transition-colors"></div>
                                             <div className="relative z-10 flex flex-col items-center">
                                                <div className="w-10 h-10 rounded-full bg-stone-100 flex items-center justify-center mb-2 group-hover:bg-iroko-earth/10 group-hover:text-iroko-earth transition-colors">
                                                    <Upload size={18} className="text-stone-400 group-hover:text-iroko-earth" />
                                                </div>
                                                <span className="text-xs font-bold text-stone-400 uppercase group-hover:text-iroko-dark transition-colors">Upload Photo Proof (Optional)</span>
                                             </div>
                                             <input type="file" className="hidden" accept="image/*" onChange={handleFileChange} />
                                          </label>
                                      )}
                                  </div>
                                  
                                  <div className="flex gap-2">
                                     <button onClick={handleMarkComplete} disabled={isUploading} className="flex-1 bg-iroko-dark text-white py-3 rounded-xl font-bold text-sm flex items-center justify-center gap-2 hover:bg-iroko-earth transition-colors disabled:opacity-70 disabled:cursor-not-allowed">
                                        {isUploading ? <Loader2 className="animate-spin" /> : <Check />} {isUploading ? (proofFile ? "Uploading..." : "Submitting...") : "Submit for Approval"}
                                     </button>
                                     <button onClick={() => { setCompletionMode(null); clearProof(); }} className="px-4 py-3 bg-white border border-stone-200 rounded-xl font-bold text-stone-500 text-sm hover:text-iroko-dark hover:border-iroko-dark transition-colors">Cancel</button>
                                  </div>
                               </div>
                            </div>
                         ) : (
                            <button 
                               onClick={() => setCompletionMode(mission.id)}
                               className="w-full py-4 bg-iroko-dark text-white rounded-2xl font-bold text-lg flex items-center justify-center gap-3 hover:bg-iroko-earth transition-colors shadow-lg shadow-iroko-dark/20"
                            >
                               <PlayCircle size={24} /> Start Mission
                            </button>
                         )
                     )}
                  </div>
              </div>
              
              {/* Lock Overlay */}
              {isLocked && (
                <div 
                   onClick={onTriggerPaywall}
                   className="absolute inset-0 z-20 flex flex-col items-center justify-center cursor-pointer hover:bg-black/5 transition-colors rounded-[2.5rem]"
                >
                    <div className="bg-iroko-dark text-white px-6 py-4 rounded-2xl shadow-2xl flex items-center gap-3 animate-float border border-white/10">
                        <Lock size={20} className="text-iroko-gold" />
                        <div>
                           <p className="font-bold text-sm">Premium Mission</p>
                           <p className="text-[10px] text-stone-400">Chief Access Only</p>
                        </div>
                    </div>
                </div>
              )}
            </div>
           );
        }) : (
           <div className="bg-white rounded-[2.5rem] p-12 text-center border border-dashed border-stone-200">
              <div className="w-16 h-16 bg-green-50 text-green-600 rounded-full flex items-center justify-center mx-auto mb-4">
                 <Check size={32} strokeWidth={3} />
              </div>
              <h3 className="text-xl font-bold text-iroko-dark">All Caught Up!</h3>
              <p className="text-stone-400 mb-6 text-sm">You are raising a giant. Rest now.</p>
              <button onClick={() => onViewChange('curriculum')} className="text-iroko-earth font-bold text-sm hover:underline">
                 Generate Custom Lesson &rarr;
              </button>
           </div>
        )}
        </div>
      </section>
    </div>
  );
};

export default Dashboard;