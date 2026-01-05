import React from 'react';
import { Card } from '../../components/ui/Card';
import { Brain, Heart, Zap, AlertTriangle } from 'lucide-react';

export default function AIOversight() {
  const recentMoods = [
    { child: 'Joshua', mood: 'Happy', reason: 'Completed "Harvest" mission', time: '2 mins ago', color: 'text-yellow-500', bg: 'bg-yellow-50' },
    { child: 'Amara', mood: 'Frustrated', reason: 'Stuck on "Math" challenge', time: '10 mins ago', color: 'text-red-500', bg: 'bg-red-50' },
    { child: 'Liam', mood: 'Calm', reason: 'Listening to story', time: '15 mins ago', color: 'text-green-500', bg: 'bg-green-50' },
    { child: 'Sarah', mood: 'Excited', reason: 'Unlocked "Village" badge', time: '22 mins ago', color: 'text-purple-500', bg: 'bg-purple-50' },
  ];

  return (
    <div className="space-y-6">
      <div>
        <h2 className="text-2xl font-serif font-bold text-gray-900">AI Oversight</h2>
        <p className="text-sm text-gray-500">Review and audit AI interactions for safety and alignment.</p>
      </div>

      {/* AI Pulse */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
         <Card className="p-4 bg-iroko-brown text-white">
            <div className="flex items-center gap-4">
              <div className="p-3 bg-white/10 rounded-full">
                <Brain className="w-6 h-6 text-iroko-gold" />
              </div>
              <div>
                <p className="text-sm text-iroko-gold font-medium">Global Sentiment</p>
                <h3 className="text-2xl font-bold">Positive (82%)</h3>
              </div>
            </div>
         </Card>
         <Card className="p-4 bg-white border border-gray-200">
            <div className="flex items-center gap-4">
              <div className="p-3 bg-red-50 rounded-full">
                <AlertTriangle className="w-6 h-6 text-red-500" />
              </div>
              <div>
                <p className="text-sm text-gray-500 font-medium">Intervention Required</p>
                <h3 className="text-2xl font-bold text-gray-900">3 Cases</h3>
              </div>
            </div>
         </Card>
         <Card className="p-4 bg-white border border-gray-200">
            <div className="flex items-center gap-4">
              <div className="p-3 bg-blue-50 rounded-full">
                <Zap className="w-6 h-6 text-blue-500" />
              </div>
              <div>
                <p className="text-sm text-gray-500 font-medium">Real-time Interactions</p>
                <h3 className="text-2xl font-bold text-gray-900">128</h3>
              </div>
            </div>
         </Card>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Live Emotional Map */}
        <Card className="p-6">
          <h3 className="text-lg font-bold text-gray-900 mb-4 flex items-center gap-2">
            <Heart className="w-5 h-5 text-red-500" />
            Live Emotional Map
          </h3>
          <div className="space-y-4">
            {recentMoods.map((item, i) => (
              <div key={i} className="flex items-start justify-between p-3 rounded-lg border border-gray-100 hover:bg-gray-50 transition-colors">
                <div className="flex gap-3">
                  <div className={`w-10 h-10 rounded-full flex items-center justify-center font-bold ${item.bg} ${item.color}`}>
                    {item.child[0]}
                  </div>
                  <div>
                    <p className="font-medium text-gray-900">{item.child}</p>
                    <p className="text-sm text-gray-500">is feeling <span className={`font-medium ${item.color}`}>{item.mood}</span></p>
                    <p className="text-xs text-gray-400 mt-1">Context: {item.reason}</p>
                  </div>
                </div>
                <span className="text-xs text-gray-400 font-medium">{item.time}</span>
              </div>
            ))}
          </div>
        </Card>

        <Card className="p-6">
          <h3 className="text-lg font-bold text-gray-900 mb-4">Flagged Interactions Queue</h3>
          <div className="text-center py-12 text-gray-500 bg-gray-50 rounded-lg border border-dashed border-gray-200">
            <p>No high-risk interactions pending review.</p>
          </div>
        </Card>
      </div>
    </div>
  );
}
