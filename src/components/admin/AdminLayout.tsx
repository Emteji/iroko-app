import React from 'react';
import { NavLink, Outlet, useNavigate } from 'react-router-dom';
import { 
  LayoutDashboard, 
  Activity, 
  Brain, 
  ShieldAlert, 
  Users, 
  LogOut,
  Settings,
  BookOpen
} from 'lucide-react';
import { useAuthStore } from '../../stores/authStore';

export default function AdminLayout() {
  const signOut = useAuthStore((state) => state.signOut);
  const navigate = useNavigate();

  const handleSignOut = async () => {
    await signOut();
    navigate('/login');
  };

  const navItems = [
    { icon: LayoutDashboard, label: 'Overview', to: '/admin/dashboard' },
    { icon: Activity, label: 'System Health', to: '/admin/monitoring' },
    { icon: Brain, label: 'AI Oversight', to: '/admin/ai' },
    { icon: BookOpen, label: 'Content & Missions', to: '/admin/content' },
    { icon: ShieldAlert, label: 'Safety & Abuse', to: '/admin/safety' },
    { icon: Users, label: 'User Management', to: '/admin/users' },
  ];

  return (
    <div className="min-h-screen bg-gray-50 flex font-sans">
      {/* Sidebar */}
      <aside className="w-64 bg-white border-r border-gray-200 flex flex-col fixed h-full z-10">
        <div className="p-6 border-b border-gray-100">
          <h1 className="text-xl font-serif font-bold text-iroko-brown tracking-wide">IROKO <span className="text-iroko-gold text-xs font-sans font-medium">ADMIN</span></h1>
        </div>
        
        <nav className="flex-1 p-4 space-y-1">
          {navItems.map((item) => (
            <NavLink
              key={item.to}
              to={item.to}
              className={({ isActive }) =>
                `flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium transition-colors ${
                  isActive 
                    ? 'bg-iroko-brown text-white' 
                    : 'text-gray-600 hover:bg-gray-50 hover:text-iroko-brown'
                }`
              }
            >
              <item.icon className="w-4 h-4" />
              {item.label}
            </NavLink>
          ))}
        </nav>

        <div className="p-4 border-t border-gray-100 space-y-1">
          <button className="flex w-full items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium text-gray-600 hover:bg-gray-50 hover:text-iroko-brown">
            <Settings className="w-4 h-4" />
            Settings
          </button>
          <button 
            onClick={handleSignOut}
            className="flex w-full items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium text-red-600 hover:bg-red-50"
          >
            <LogOut className="w-4 h-4" />
            Sign Out
          </button>
        </div>
      </aside>

      {/* Main Content */}
      <main className="flex-1 ml-64 p-8">
        <Outlet />
      </main>
    </div>
  );
}
