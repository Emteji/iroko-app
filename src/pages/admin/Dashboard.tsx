import React from 'react';
import { Card } from '../../components/ui/Card';
import { 
  Users, 
  UserPlus, 
  AlertTriangle, 
  TrendingUp,
  Activity
} from 'lucide-react';

export default function AdminDashboard() {
  return (
    <div className="space-y-6">
      <div>
        <h2 className="text-2xl font-serif font-bold text-iroko-brown">System Overview</h2>
        <p className="text-sm text-gray-500">Live monitoring of IROKO platform health.</p>
      </div>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        {[
          { label: 'Active Families', value: '1,248', change: '+12%', icon: Users, color: 'text-iroko-brown', bg: 'bg-iroko-gold/20' },
          { label: 'New Trials', value: '84', change: '+5%', icon: UserPlus, color: 'text-iroko-forest', bg: 'bg-green-50' },
          { label: 'Active Alerts', value: '12', change: '-2', icon: AlertTriangle, color: 'text-orange-600', bg: 'bg-orange-50' },
          { label: 'AI Requests/min', value: '450', change: '+24%', icon: Activity, color: 'text-iroko-burntGold', bg: 'bg-purple-50' },
        ].map((stat, i) => (
          <Card key={i} className="p-4 border border-iroko-gold/20 shadow-sm bg-iroko-cream">
            <div className="flex items-start justify-between">
              <div>
                <p className="text-sm font-medium text-gray-500">{stat.label}</p>
                <h3 className="text-2xl font-bold mt-1 text-iroko-brown">{stat.value}</h3>
              </div>
              <div className={`p-2 rounded-lg ${stat.bg} ${stat.color}`}>
                <stat.icon className="w-5 h-5" />
              </div>
            </div>
            <div className="mt-4 flex items-center text-xs">
              <span className="text-iroko-forest font-medium flex items-center">
                <TrendingUp className="w-3 h-3 mr-1" />
                {stat.change}
              </span>
              <span className="text-gray-400 ml-2">vs last week</span>
            </div>
          </Card>
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Recent Activity */}
        <Card className="p-6 border border-iroko-gold/20 shadow-sm bg-white">
          <h3 className="text-lg font-bold text-iroko-brown mb-4 font-serif">Recent System Events</h3>
          <div className="space-y-4">
            {[
              { event: 'High Risk Alert: Child ID #8821 detected unsafe query', time: '2 mins ago', type: 'danger' },
              { event: 'New Premium Subscription: Family #9932', time: '15 mins ago', type: 'success' },
              { event: 'AI Model Re-calibration initiated', time: '1 hour ago', type: 'info' },
              { event: 'Suspicious Login Attempt blocked', time: '2 hours ago', type: 'warning' },
            ].map((item, i) => (
              <div key={i} className="flex items-start gap-3 pb-3 border-b border-gray-50 last:border-0">
                <div className={`w-2 h-2 mt-1.5 rounded-full ${
                  item.type === 'danger' ? 'bg-red-500' :
                  item.type === 'success' ? 'bg-iroko-forest' :
                  item.type === 'warning' ? 'bg-orange-500' : 'bg-iroko-gold'
                }`} />
                <div>
                  <p className="text-sm text-iroko-brown font-medium">{item.event}</p>
                  <p className="text-xs text-gray-400">{item.time}</p>
                </div>
              </div>
            ))}
          </div>
        </Card>

        {/* AI Health */}
        <Card className="p-6 border border-iroko-gold/20 shadow-sm bg-white">
          <h3 className="text-lg font-bold text-iroko-brown mb-4 font-serif">AI Core Performance</h3>
          <div className="space-y-4">
            <div>
              <div className="flex justify-between text-sm mb-1">
                <span className="text-gray-600">Response Latency</span>
                <span className="font-medium text-iroko-brown">124ms</span>
              </div>
              <div className="h-2 bg-gray-100 rounded-full overflow-hidden">
                <div className="h-full bg-iroko-forest w-[20%]" />
              </div>
            </div>
            <div>
              <div className="flex justify-between text-sm mb-1">
                <span className="text-gray-600">Token Usage (Daily Cap)</span>
                <span className="font-medium text-iroko-brown">45%</span>
              </div>
              <div className="h-2 bg-gray-100 rounded-full overflow-hidden">
                <div className="h-full bg-iroko-gold w-[45%]" />
              </div>
            </div>
            <div>
              <div className="flex justify-between text-sm mb-1">
                <span className="text-gray-600">Safety Filter Rate</span>
                <span className="font-medium text-iroko-brown">0.8%</span>
              </div>
              <div className="h-2 bg-gray-100 rounded-full overflow-hidden">
                <div className="h-full bg-purple-500 w-[1%]" />
              </div>
            </div>
          </div>
        </Card>
      </div>
    </div>
  );
}
