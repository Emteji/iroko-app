import React from 'react';
import { 
  Users, 
  Calendar, 
  Award, 
  Shield, 
  Activity,
  ChevronRight,
  Menu,
  Bell,
  Search,
  Star,
  Zap,
  BookOpen
} from 'lucide-react';
import { Card } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';

export default function Dashboard() {
  return (
    <div className="min-h-screen bg-iroko-white font-sans text-iroko-brown">
      {/* Header */}
      <header className="bg-white border-b border-iroko-brown/10 sticky top-0 z-10">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
          <div className="flex items-center gap-4">
            <button className="p-2 hover:bg-gray-100 rounded-full lg:hidden">
              <Menu className="w-6 h-6" />
            </button>
            <h1 className="text-2xl font-serif font-bold text-iroko-brown tracking-wide">IROKO</h1>
          </div>
          
          <div className="flex items-center gap-4">
            <div className="hidden md:flex items-center gap-2 bg-iroko-cream px-3 py-1.5 rounded-full border border-iroko-brown/10">
              <div className="w-2 h-2 rounded-full bg-green-500 animate-pulse" />
              <span className="text-xs font-medium text-iroko-brown">System Active</span>
            </div>
            <button className="p-2 hover:bg-iroko-cream rounded-full relative">
              <Bell className="w-5 h-5" />
              <span className="absolute top-1.5 right-1.5 w-2 h-2 bg-iroko-gold rounded-full border border-white" />
            </button>
            <div className="w-8 h-8 rounded-full bg-iroko-brown text-iroko-gold flex items-center justify-center font-serif font-bold border-2 border-iroko-gold">
              P
            </div>
          </div>
        </div>
      </header>

      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Welcome Section */}
        <div className="mb-8">
          <h2 className="text-3xl font-serif font-bold text-iroko-brown mb-2">Good Afternoon, Parent</h2>
          <p className="text-iroko-brown/70">Here is the governance overview for your village.</p>
        </div>

        {/* Child Hero Card (Kofi) */}
        <div className="mb-8 relative overflow-hidden rounded-3xl shadow-xl">
          {/* Background Gradient */}
          <div className="absolute inset-0 bg-gradient-to-br from-iroko-forest to-green-900" />
          
          {/* Decorative Circles */}
          <div className="absolute top-0 right-0 w-64 h-64 bg-white/5 rounded-full -translate-y-1/2 translate-x-1/4" />
          <div className="absolute bottom-0 left-0 w-48 h-48 bg-iroko-gold/10 rounded-full translate-y-1/2 -translate-x-1/4" />

          <div className="relative p-6 md:p-8 text-white">
            <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-6">
              <div className="flex items-center gap-4">
                <div className="w-16 h-16 rounded-full border-2 border-iroko-gold/50 bg-white/10 flex items-center justify-center text-2xl font-serif">
                  K
                </div>
                <div>
                  <h3 className="text-2xl font-serif font-bold">Kofi</h3>
                  <div className="flex items-center gap-2 text-white/80 text-sm">
                    <span className="bg-white/20 px-2 py-0.5 rounded-full">Age 8</span>
                    <span>•</span>
                    <span className="flex items-center gap-1">
                      <div className="w-1.5 h-1.5 rounded-full bg-green-400" />
                      Online | Focused
                    </span>
                  </div>
                </div>
              </div>

              <div className="flex items-center gap-8 w-full md:w-auto">
                <div className="flex-1 md:flex-none text-center md:text-right">
                  <div className="text-sm text-white/60 mb-1">Daily Discipline</div>
                  <div className="text-2xl font-bold font-serif text-iroko-gold">82%</div>
                </div>
                <div className="w-px h-10 bg-white/20 hidden md:block" />
                <div className="flex-1 md:flex-none text-center md:text-right">
                  <div className="text-sm text-white/60 mb-1">Tasks Done</div>
                  <div className="text-2xl font-bold font-serif">5/6</div>
                </div>
              </div>
            </div>

            <div className="mt-8">
              <div className="flex justify-between text-sm mb-2 text-white/80">
                <span>Level 4 Progress</span>
                <span>1,240 / 1,500 XP</span>
              </div>
              <div className="h-2 bg-black/20 rounded-full overflow-hidden">
                <div className="h-full bg-iroko-gold w-[82%] rounded-full" />
              </div>
            </div>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Left Column (Key Metrics) */}
          <div className="lg:col-span-2 space-y-8">
            {/* Quick Actions Grid */}
            <section>
              <h3 className="text-lg font-bold text-iroko-brown mb-4 flex items-center gap-2">
                <Zap className="w-5 h-5 text-iroko-gold" />
                Quick Actions
              </h3>
              <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
                {[
                  { label: 'Assign Mission', icon: <Activity />, color: 'bg-blue-50 text-blue-700' },
                  { label: 'AI Guide', icon: <Search />, color: 'bg-purple-50 text-purple-700' },
                  { label: 'Restrict Access', icon: <Shield />, color: 'bg-red-50 text-red-700' },
                  { label: 'View Reports', icon: <BookOpen />, color: 'bg-green-50 text-green-700' },
                ].map((action, i) => (
                  <button 
                    key={i}
                    className={`${action.color} p-4 rounded-xl flex flex-col items-center justify-center gap-3 hover:shadow-md transition-all border border-transparent hover:border-black/5 aspect-square sm:aspect-auto sm:h-32`}
                  >
                    <div className="p-2 bg-white/60 rounded-full">{action.icon}</div>
                    <span className="text-sm font-medium text-center">{action.label}</span>
                  </button>
                ))}
              </div>
            </section>

            {/* Today's Focus */}
            <section>
              <h3 className="text-lg font-bold text-iroko-brown mb-4 flex items-center gap-2">
                <Star className="w-5 h-5 text-iroko-gold" />
                Today's Focus
              </h3>
              <Card className="bg-gradient-to-r from-iroko-brown to-iroko-clay text-white p-6 border-none shadow-lg">
                <div className="flex items-start gap-4">
                  <div className="p-3 bg-white/10 rounded-full">
                    <Award className="w-8 h-8 text-iroko-gold" />
                  </div>
                  <div>
                    <h4 className="text-xl font-serif font-bold mb-2">Discipline & Responsibility</h4>
                    <p className="text-white/80 mb-4">
                      Focus on completing morning routines without reminders. Consistency builds character.
                    </p>
                    <Button variant="secondary" className="bg-white/10 hover:bg-white/20 text-white border-white/20">
                      View Daily Plan
                    </Button>
                  </div>
                </div>
              </Card>
            </section>
          </div>

          {/* Right Column (Notifications/Status) */}
          <div className="space-y-8">
            {/* Attention Needed */}
            <section>
              <h3 className="text-lg font-bold text-iroko-brown mb-4">Attention Needed</h3>
              <div className="space-y-3">
                <div className="bg-iroko-cream border border-iroko-brown/10 p-4 rounded-xl flex items-start gap-3 shadow-sm">
                  <div className="p-2 bg-white rounded-full text-green-600">
                    <Activity className="w-4 h-4" />
                  </div>
                  <div className="flex-1">
                    <h4 className="font-bold text-sm text-iroko-brown">Mission Ready for Review</h4>
                    <p className="text-xs text-iroko-brown/70 mt-1">Kofi completed "Clean Room"</p>
                  </div>
                  <ChevronRight className="w-4 h-4 text-iroko-brown/40" />
                </div>

                <div className="bg-orange-50 border border-orange-100 p-4 rounded-xl flex items-start gap-3 shadow-sm">
                  <div className="p-2 bg-white rounded-full text-orange-600">
                    <Shield className="w-4 h-4" />
                  </div>
                  <div className="flex-1">
                    <h4 className="font-bold text-sm text-iroko-brown">Screen Time Warning</h4>
                    <p className="text-xs text-iroko-brown/70 mt-1">30 mins remaining today</p>
                  </div>
                  <ChevronRight className="w-4 h-4 text-iroko-brown/40" />
                </div>
              </div>
            </section>

            {/* Village Context */}
            <section>
              <h3 className="text-lg font-bold text-iroko-brown mb-4">Village Context</h3>
              <Card className="p-4 border-iroko-brown/10 bg-white shadow-sm">
                <div className="space-y-4">
                  <div className="flex justify-between items-center pb-3 border-b border-gray-100">
                    <span className="text-sm text-gray-500">Environment</span>
                    <span className="font-medium text-iroko-brown">Urban</span>
                  </div>
                  <div className="flex justify-between items-center pb-3 border-b border-gray-100">
                    <span className="text-sm text-gray-500">Values</span>
                    <span className="font-medium text-iroko-brown">Respect, Grit</span>
                  </div>
                  <div className="flex justify-between items-center">
                    <span className="text-sm text-gray-500">Language</span>
                    <span className="font-medium text-iroko-brown">Bilingual</span>
                  </div>
                </div>
                <Button className="w-full mt-4 bg-iroko-brown text-white hover:bg-iroko-clay">
                  Update Context
                </Button>
              </Card>
            </section>
          </div>
        </div>
      </main>
    </div>
  )
}
