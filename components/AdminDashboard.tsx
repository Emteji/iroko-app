import React, { useEffect, useState } from 'react';
import { repository } from '../services/repository';
import { AdminStats, Transaction, PaymentConfig } from '../types';
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, CartesianGrid } from 'recharts';
import { Users, CreditCard, Activity, Save, Shield, Settings, AlertTriangle, CheckCircle, Search, Eye, EyeOff, LogOut } from 'lucide-react';
import { Logo } from './Logo';

interface AdminDashboardProps {
    onExit?: () => void;
}

export const AdminDashboard: React.FC<AdminDashboardProps> = ({ onExit }) => {
  const [activeTab, setActiveTab] = useState<'overview' | 'transactions' | 'payment'>('overview');
  const [stats, setStats] = useState<AdminStats | null>(null);
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [config, setConfig] = useState<PaymentConfig>({
      provider: 'paystack', publicKey: '', secretKey: '', isLiveMode: false, isEnabled: false
  });
  const [loading, setLoading] = useState(true);
  const [saveStatus, setSaveStatus] = useState<'idle' | 'saving' | 'saved'>('idle');
  const [showSecret, setShowSecret] = useState(false);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    const s = await repository.getAdminStats();
    const t = await repository.getTransactions();
    const c = repository.getPaymentConfig();
    
    setStats(s);
    setTransactions(t);
    setConfig(c);
    setLoading(false);
  };

  const handleSaveConfig = async () => {
    setSaveStatus('saving');
    // Simulate API call persistence
    await new Promise(r => setTimeout(r, 1000));
    await repository.savePaymentConfig(config);
    setSaveStatus('saved');
    setTimeout(() => setSaveStatus('idle'), 3000);
  };

  if (loading) return <div className="min-h-screen flex items-center justify-center text-iroko-earth font-bold">Accessing Secure Records...</div>;

  return (
    <div className="min-h-screen bg-stone-50 pb-20">
      {/* Top Bar */}
      <div className="bg-iroko-dark text-white p-6 sticky top-0 z-30 shadow-md">
         <div className="max-w-7xl mx-auto flex justify-between items-center">
             <div className="flex items-center gap-3">
                <Logo className="w-8 h-8" variant="gold" />
                <div>
                   <h1 className="text-xl font-display font-bold">Village Admin</h1>
                   <p className="text-[10px] text-stone-400 uppercase tracking-widest">Control Center</p>
                </div>
             </div>
             <div className="flex items-center gap-4">
                <span className="hidden md:inline-flex items-center gap-2 bg-white/10 px-3 py-1 rounded-full text-xs font-bold border border-white/10">
                   <Shield size={12} className="text-green-400" /> Secure Admin Session
                </span>
                {onExit && (
                    <button onClick={onExit} className="bg-white/10 p-2 rounded-lg hover:bg-white/20 transition-colors text-white" title="Exit Admin">
                        <LogOut size={16} />
                    </button>
                )}
             </div>
         </div>
      </div>

      <div className="max-w-7xl mx-auto p-6 md:p-8">
         {/* Tabs */}
         <div className="flex gap-4 mb-8 border-b border-stone-200 overflow-x-auto pb-1 no-scrollbar">
            <TabButton active={activeTab === 'overview'} onClick={() => setActiveTab('overview')} icon={Activity} label="Overview" />
            <TabButton active={activeTab === 'transactions'} onClick={() => setActiveTab('transactions')} icon={CreditCard} label="Transactions" />
            <TabButton active={activeTab === 'payment'} onClick={() => setActiveTab('payment')} icon={Settings} label="Payment Settings" />
         </div>

         {/* Content */}
         <div className="animate-fade-in">
            {activeTab === 'overview' && stats && (
               <div className="space-y-8">
                  {/* Stats Grid */}
                  <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
                     <StatCard label="Total Parents" value={stats.totalParents} icon={Users} />
                     <StatCard label="Total Children" value={stats.totalChildren} icon={Users} />
                     <StatCard label="Total Revenue" value={`₦${(stats.totalRevenue).toLocaleString()}`} icon={CreditCard} highlight />
                     <StatCard label="Active Missions" value={stats.activeMissions} icon={Activity} />
                  </div>

                  {/* Charts */}
                  <div className="bg-white p-8 rounded-2xl shadow-sm border border-stone-100">
                     <h3 className="font-bold text-iroko-dark mb-6">Revenue Growth (Simulated)</h3>
                     <div className="h-64 w-full">
                        <ResponsiveContainer width="100%" height="100%">
                           <BarChart data={[
                              { month: 'Jan', amount: 120000 },
                              { month: 'Feb', amount: 180000 },
                              { month: 'Mar', amount: 240000 },
                              { month: 'Apr', amount: stats.totalRevenue }
                           ]}>
                              <CartesianGrid strokeDasharray="3 3" vertical={false} />
                              <XAxis dataKey="month" tick={{fill: '#a8a29e'}} axisLine={false} tickLine={false} />
                              <YAxis tick={{fill: '#a8a29e'}} axisLine={false} tickLine={false} tickFormatter={(value) => `₦${value/1000}k`} />
                              <Tooltip cursor={{fill: '#f5f5f4'}} contentStyle={{borderRadius: '12px', border: 'none', boxShadow: '0 10px 15px -3px rgba(0, 0, 0, 0.1)'}} />
                              <Bar dataKey="amount" fill="#8B4513" radius={[4, 4, 0, 0]} />
                           </BarChart>
                        </ResponsiveContainer>
                     </div>
                  </div>
               </div>
            )}

            {activeTab === 'transactions' && (
               <div className="bg-white rounded-2xl shadow-sm border border-stone-100 overflow-hidden">
                  <div className="p-6 border-b border-stone-100 flex justify-between items-center flex-wrap gap-4">
                     <h3 className="font-bold text-iroko-dark">Recent Payments</h3>
                     <div className="bg-stone-50 p-2 rounded-lg flex items-center gap-2 w-full md:w-64">
                        <Search size={16} className="text-stone-400" />
                        <input placeholder="Search reference..." className="bg-transparent border-none outline-none text-sm w-full" />
                     </div>
                  </div>
                  <div className="overflow-x-auto">
                     <table className="w-full text-left text-sm min-w-[600px]">
                        <thead className="bg-stone-50 text-stone-500 font-bold uppercase text-xs">
                           <tr>
                              <th className="p-4">Reference</th>
                              <th className="p-4">Parent</th>
                              <th className="p-4">Amount</th>
                              <th className="p-4">Status</th>
                              <th className="p-4">Date</th>
                           </tr>
                        </thead>
                        <tbody className="divide-y divide-stone-100">
                           {transactions.map(txn => (
                              <tr key={txn.id} className="hover:bg-stone-50 transition-colors">
                                 <td className="p-4 font-mono text-xs">{txn.reference}</td>
                                 <td className="p-4 font-medium text-iroko-dark">{txn.parentName}</td>
                                 <td className="p-4 font-bold">{txn.currency} {txn.amount.toLocaleString()}</td>
                                 <td className="p-4">
                                    <span className={`px-2 py-1 rounded-full text-[10px] font-bold uppercase ${txn.status === 'success' ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}`}>
                                       {txn.status}
                                    </span>
                                 </td>
                                 <td className="p-4 text-stone-500">{new Date(txn.date).toLocaleDateString()}</td>
                              </tr>
                           ))}
                           {transactions.length === 0 && (
                              <tr>
                                 <td colSpan={5} className="p-8 text-center text-stone-400">No transactions found. Upgrade a user to see data here.</td>
                              </tr>
                           )}
                        </tbody>
                     </table>
                  </div>
               </div>
            )}

            {activeTab === 'payment' && (
               <div className="max-w-2xl mx-auto">
                  <div className="bg-white p-8 rounded-2xl shadow-sm border border-stone-100">
                     <div className="flex items-center gap-4 mb-8">
                        <div className="w-12 h-12 bg-blue-50 rounded-xl flex items-center justify-center text-blue-600">
                           <CreditCard size={24} />
                        </div>
                        <div>
                           <h3 className="text-xl font-bold text-iroko-dark">Paystack Integration</h3>
                           <p className="text-stone-500 text-sm">Configure how parents pay for the "Chief" subscription.</p>
                        </div>
                     </div>

                     <div className="space-y-6">
                        {/* Toggle */}
                        <div className="flex items-center justify-between p-4 bg-stone-50 rounded-xl border border-stone-100 cursor-pointer" onClick={() => setConfig({...config, isEnabled: !config.isEnabled})}>
                           <div>
                              <p className="font-bold text-iroko-dark">Enable Payments</p>
                              <p className="text-xs text-stone-500">Show upgrade options to users</p>
                           </div>
                           <button 
                              className={`w-12 h-6 rounded-full transition-colors relative ${config.isEnabled ? 'bg-green-500' : 'bg-stone-300'}`}
                           >
                              <div className={`w-4 h-4 bg-white rounded-full absolute top-1 transition-all ${config.isEnabled ? 'left-7' : 'left-1'}`}></div>
                           </button>
                        </div>

                         <div className="flex items-center justify-between p-4 bg-stone-50 rounded-xl border border-stone-100 cursor-pointer" onClick={() => setConfig({...config, isLiveMode: !config.isLiveMode})}>
                           <div>
                              <p className="font-bold text-iroko-dark">Live Mode</p>
                              <p className="text-xs text-stone-500">Switch between Test and Live keys</p>
                           </div>
                           <button 
                              className={`w-12 h-6 rounded-full transition-colors relative ${config.isLiveMode ? 'bg-iroko-dark' : 'bg-orange-400'}`}
                           >
                              <div className={`w-4 h-4 bg-white rounded-full absolute top-1 transition-all ${config.isLiveMode ? 'left-7' : 'left-1'}`}></div>
                           </button>
                        </div>

                        <div>
                           <label className="block text-xs font-bold text-stone-400 uppercase tracking-wider mb-2">Public Key</label>
                           <input 
                              type="text" 
                              value={config.publicKey}
                              onChange={(e) => setConfig({...config, publicKey: e.target.value})}
                              placeholder="pk_test_..." 
                              className="w-full p-4 bg-stone-50 border border-stone-200 rounded-xl font-mono text-sm outline-none focus:border-iroko-earth transition-colors"
                           />
                        </div>

                        <div>
                           <label className="block text-xs font-bold text-stone-400 uppercase tracking-wider mb-2">Secret Key</label>
                           <div className="relative">
                              <input 
                                 type={showSecret ? "text" : "password"}
                                 value={config.secretKey}
                                 onChange={(e) => setConfig({...config, secretKey: e.target.value})}
                                 placeholder="sk_test_..." 
                                 className="w-full p-4 bg-stone-50 border border-stone-200 rounded-xl font-mono text-sm outline-none focus:border-iroko-earth transition-colors"
                              />
                              <button onClick={() => setShowSecret(!showSecret)} className="absolute right-4 top-4 text-stone-400 hover:text-iroko-dark">
                                 {showSecret ? <EyeOff size={16}/> : <Eye size={16}/>}
                              </button>
                           </div>
                           <p className="text-[10px] text-orange-500 mt-2 flex items-center gap-1">
                              <AlertTriangle size={10} /> Never share this key. It is stored locally for this admin demo.
                           </p>
                        </div>

                        <button 
                           onClick={handleSaveConfig}
                           disabled={saveStatus === 'saving'}
                           className="w-full py-4 bg-iroko-dark text-white rounded-xl font-bold flex items-center justify-center gap-2 hover:bg-iroko-earth transition-colors"
                        >
                           {saveStatus === 'saving' ? 'Saving...' : saveStatus === 'saved' ? 'Saved Successfully!' : 'Save Configuration'}
                           {saveStatus === 'saved' ? <CheckCircle size={18} /> : <Save size={18} />}
                        </button>
                     </div>
                  </div>
               </div>
            )}
         </div>
      </div>
    </div>
  );
};

const TabButton = ({ active, onClick, icon: Icon, label }: any) => (
   <button 
      onClick={onClick}
      className={`flex items-center gap-2 px-6 py-4 border-b-2 transition-all whitespace-nowrap ${active ? 'border-iroko-earth text-iroko-dark font-bold' : 'border-transparent text-stone-400 hover:text-iroko-earth'}`}
   >
      <Icon size={18} /> {label}
   </button>
);

const StatCard = ({ label, value, icon: Icon, highlight }: any) => (
   <div className={`p-6 rounded-2xl shadow-sm border border-stone-100 flex flex-col justify-between h-32 ${highlight ? 'bg-iroko-dark text-white' : 'bg-white text-iroko-dark'}`}>
      <div className="flex justify-between items-start">
         <span className={`text-xs font-bold uppercase tracking-wider ${highlight ? 'text-stone-400' : 'text-stone-400'}`}>{label}</span>
         <Icon size={20} className={highlight ? 'text-iroko-gold' : 'text-stone-300'} />
      </div>
      <span className="text-3xl font-display font-bold">{value}</span>
   </div>
);