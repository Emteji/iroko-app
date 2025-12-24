import React, { useEffect, useState } from 'react';
import { Radar, RadarChart, PolarGrid, PolarAngleAxis, PolarRadiusAxis, ResponsiveContainer, BarChart, Bar, XAxis, CartesianGrid } from 'recharts';
import { TrendingUp, Award, Calendar, Zap, RefreshCw, Trophy, Users, Lock, Crown } from 'lucide-react';
import { repository } from '../services/repository';
import { ChildProfile, ProgressLog, CATEGORIES, ParentProfile } from '../types';

interface Props {
   parent: ParentProfile;
   onTriggerPaywall: () => void;
}

const ProgressTracker: React.FC<Props> = ({ parent, onTriggerPaywall }) => {
  const [loading, setLoading] = useState(true);
  const [selectedChild, setSelectedChild] = useState<ChildProfile | null>(null);
  const [leaderboard, setLeaderboard] = useState<ChildProfile[]>([]);
  
  const [stats, setStats] = useState({
     lessonsDone: 0,
     totalScore: 0,
     currentStreak: 0
  });

  const [radarData, setRadarData] = useState<any[]>([]);
  const [activityData, setActivityData] = useState<any[]>([]);

  useEffect(() => {
    loadData();
  }, [parent.id]);

  const loadData = async () => {
     setLoading(true);
     const kids = await repository.getChildren(parent.id);
     if (kids.length > 0) {
         const child = kids[0]; // Default to first child for now
         setSelectedChild(child);
         
         // Fetch Logs
         const history = await repository.getChildProgress(child.id);

         // Fetch Leaderboard
         const leaders = await repository.getLeaderboard();
         setLeaderboard(leaders);

         // Calculate Stats
         const done = history.filter(h => h.status === 'completed');
         
         // --- Radar Chart Logic ---
         const rData = CATEGORIES.map(cat => {
             let base = 50; 
             if (cat.id === 'grit') base += (child.gritPoints % 100);
             if (cat.id === 'respect') base += (child.omoluabiPoints % 100);
             if (cat.id === 'money') base += (child.gritPoints / 2);
             
             return {
                subject: cat.label.split(' ')[0], // Short name
                A: Math.min(base, 150),
                fullMark: 150
             };
         });
         setRadarData(rData);

         // --- Activity Chart Logic ---
         const days = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
         const activityMap = new Array(7).fill(0);
         history.forEach(log => {
             const d = new Date(log.completedAt);
             activityMap[d.getDay()] += 1;
         });
         const aData = days.map((day, idx) => ({
             name: day,
             hours: activityMap[idx]
             
         }));
         setActivityData(aData);

         setStats({
             lessonsDone: done.length,
             totalScore: child.gritPoints + child.omoluabiPoints,
             currentStreak: calculateStreak(history)
         });
     }
     setLoading(false);
  };

  const calculateStreak = (history: ProgressLog[]) => {
      if(history.length === 0) return 0;
      return Math.min(history.length, 5); 
  };

  if(loading) return <div className="p-12 text-center text-stone-400">Loading Growth Data...</div>;

  if(!selectedChild) return <div className="p-12 text-center text-stone-400">No child profile found.</div>;

  return (
    <div className="space-y-8 animate-fade-in pb-12 max-w-5xl mx-auto">

      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center gap-4">
            <div className="w-12 h-12 bg-iroko-earth/10 rounded-2xl flex items-center justify-center text-iroko-earth">
            <TrendingUp size={24} />
            </div>
            <div>
            <h2 className="text-3xl font-display font-bold text-iroko-dark">Growth Journal</h2>
            <p className="text-stone-500">Tracking {selectedChild.name}'s journey.</p>
            </div>
        </div>
        <button onClick={loadData} className="p-2 bg-white rounded-full shadow-sm text-stone-400 hover:text-iroko-dark transition-colors">
            <RefreshCw size={20} />
        </button>
      </div>

      {/* Overview Cards */}
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
        <StatCard icon={Award} label="Lessons Done" value={stats.lessonsDone.toString()} color="text-iroko-clay" bg="bg-iroko-clay/10" />
        <StatCard icon={Zap} label="Current Streak" value={`${stats.currentStreak} Days`} color="text-iroko-earth" bg="bg-iroko-earth/10" />
        <StatCard icon={TrendingUp} label="Total Score" value={stats.totalScore.toString()} color="text-iroko-green" bg="bg-iroko-green/10" />
        <StatCard icon={Calendar} label="Current Level" value={selectedChild.currentLevel} color="text-iroko-dark" bg="bg-stone-100" />
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
        
        {/* Left Column: Charts */}
        <div className="space-y-8">
            {/* Radar Chart (Premium Feature) */}
            <div className="relative bg-white p-8 rounded-[2.5rem] shadow-sm border border-stone-100 flex flex-col items-center overflow-hidden">
              <div className="w-full flex justify-between items-center mb-6">
                <h3 className="text-lg font-bold text-iroko-dark">Strength Profile</h3>
                {parent.subscriptionTier === 'Chief' && (
                  <span className="text-xs font-bold bg-iroko-gold text-white px-3 py-1 rounded-full uppercase flex items-center gap-1">
                     <Crown size={10} fill="currentColor"/> Pro Analytics
                  </span>
                )}
              </div>
              
              <div className={`w-full h-[320px] transition-all duration-300 ${parent.subscriptionTier === 'Villager' ? 'blur-lg opacity-30 grayscale' : ''}`}>
                <ResponsiveContainer width="100%" height="100%">
                  <RadarChart cx="50%" cy="50%" outerRadius="70%" data={radarData}>
                    <PolarGrid stroke="#f0f0f0" strokeWidth={1.5} />
                    <PolarAngleAxis 
                      dataKey="subject" 
                      tick={{ fill: '#78716c', fontSize: 12, fontWeight: 600, fontFamily: 'Space Grotesk' }} 
                    />
                    <PolarRadiusAxis angle={30} domain={[0, 150]} tick={false} axisLine={false} />
                    <Radar
                      name="Score"
                      dataKey="A"
                      stroke="#8B5A2B"
                      strokeWidth={3}
                      fill="#8B5A2B"
                      fillOpacity={0.2}
                    />
                  </RadarChart>
                </ResponsiveContainer>
              </div>

              {/* Premium Overlay */}
              {parent.subscriptionTier === 'Villager' && (
                  <div className="absolute inset-0 z-10 flex flex-col items-center justify-center">
                     <div className="bg-iroko-dark text-white p-8 rounded-3xl shadow-2xl flex flex-col items-center text-center max-w-xs animate-slide-up relative overflow-hidden">
                        {/* Background Effect */}
                        <div className="absolute inset-0 bg-[url('https://www.transparenttextures.com/patterns/cubes.png')] opacity-10"></div>
                        
                        <div className="w-16 h-16 bg-gradient-to-br from-iroko-gold to-yellow-600 rounded-2xl flex items-center justify-center mb-4 text-white shadow-lg relative z-10 rotate-3">
                           <Lock size={32} />
                        </div>
                        <h4 className="font-display font-bold text-xl mb-2 relative z-10">Chief Access Only</h4>
                        <p className="text-sm text-stone-300 mb-6 relative z-10 leading-relaxed">
                           Unlock detailed analytics to see exactly where your child is thriving and where they need guidance.
                        </p>
                        <button 
                           onClick={onTriggerPaywall}
                           className="w-full bg-white text-iroko-dark font-bold px-6 py-3 rounded-xl text-sm uppercase tracking-wide hover:scale-105 transition-transform flex items-center justify-center gap-2 relative z-10"
                        >
                           <Crown size={16} className="text-iroko-gold fill-current" /> Upgrade Now
                        </button>
                     </div>
                  </div>
              )}
            </div>

            {/* Activity Chart */}
            <div className="bg-white p-8 rounded-[2.5rem] shadow-sm border border-stone-100 flex flex-col items-center">
              <div className="w-full flex justify-between items-center mb-6">
                <h3 className="text-lg font-bold text-iroko-dark">Consistency</h3>
                <span className="text-xs font-bold bg-stone-100 text-stone-500 px-3 py-1 rounded-full uppercase">Missions</span>
              </div>
              <div className="w-full h-[200px]">
                <ResponsiveContainer width="100%" height="100%">
                  <BarChart data={activityData} margin={{ top: 20, right: 0, left: -25, bottom: 0 }}>
                    <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#f5f5f5" />
                    <XAxis dataKey="name" axisLine={false} tickLine={false} tick={{fill: '#a8a29e', fontSize: 12}} dy={10} />
                    <Bar dataKey="hours" fill="#2C241B" radius={[8, 8, 8, 8]} barSize={24} activeBar={{ fill: '#8B5A2B' }} />
                  </BarChart>
                </ResponsiveContainer>
              </div>
            </div>
        </div>

        {/* Right Column: Leaderboard */}
        <div className="bg-[#2C241B] text-white p-8 rounded-[2.5rem] shadow-xl relative overflow-hidden flex flex-col">
           {/* Background Pattern */}
           <div className="absolute inset-0 opacity-10 bg-[url('https://www.transparenttextures.com/patterns/cubes.png')] pointer-events-none"></div>
           
           <div className="relative z-10 flex items-center gap-3 mb-8">
              <div className="w-10 h-10 bg-iroko-gold rounded-full flex items-center justify-center text-iroko-dark shadow-lg">
                <Trophy size={20} fill="currentColor" />
              </div>
              <div>
                <h3 className="text-2xl font-display font-bold">Village Top Giants</h3>
                <p className="text-iroko-gold text-sm font-medium">This Week's Leaders</p>
              </div>
           </div>

           <div className="relative z-10 flex-1 space-y-4">
             {leaderboard.map((student, idx) => (
                <div key={student.id} className={`flex items-center gap-4 p-4 rounded-2xl border transition-all ${student.id === selectedChild?.id ? 'bg-iroko-gold/20 border-iroko-gold' : 'bg-white/5 border-white/5 hover:bg-white/10'}`}>
                   <div className="w-8 font-display font-bold text-xl text-center text-stone-400">
                     {idx + 1}
                   </div>
                   <img src={student.avatarUrl} className="w-10 h-10 rounded-full bg-stone-700 border-2 border-stone-600" />
                   <div className="flex-1">
                      <h4 className="font-bold text-lg">{student.name}</h4>
                      <p className="text-xs text-stone-400">{student.currentLevel}</p>
                   </div>
                   <div className="text-right">
                      <span className="block font-bold text-iroko-gold">{student.gritPoints + student.omoluabiPoints}</span>
                      <span className="text-[10px] text-stone-500 uppercase">XP</span>
                   </div>
                </div>
             ))}
             
             {leaderboard.length === 0 && (
                <div className="text-center py-10 text-stone-500">
                   <Users size={32} className="mx-auto mb-2 opacity-50"/>
                   <p>Be the first to join the ranks!</p>
                </div>
             )}
           </div>

           <div className="relative z-10 mt-8 pt-6 border-t border-white/10 text-center">
              <p className="text-sm text-stone-400">
                 {selectedChild.name} is currently ranked <span className="text-white font-bold">#{leaderboard.findIndex(l => l.id === selectedChild?.id) + 1 || '-'}</span>
              </p>
           </div>
        </div>

      </div>
    </div>
  );
};

const StatCard = ({ icon: Icon, label, value, color, bg }: any) => (
  <div className="bg-white p-6 rounded-[2rem] shadow-sm border border-stone-100 flex flex-col items-center justify-center text-center group hover:shadow-md transition-shadow">
     <div className={`w-10 h-10 ${bg} rounded-full flex items-center justify-center mb-3 group-hover:scale-110 transition-transform`}>
       <Icon size={18} className={color} />
     </div>
     <span className="text-stone-400 text-[10px] font-bold uppercase tracking-wider mb-1">{label}</span>
     <span className={`text-2xl font-display font-bold ${color}`}>{value}</span>
  </div>
);

export default ProgressTracker;