import React, { useState } from 'react';
import { 
  Star, 
  CheckCircle, 
  Clock, 
  Trophy, 
  Smile, 
  Gamepad2,
  BookOpen,
  Menu
} from 'lucide-react';
import { Card } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';

export default function ChildDashboard() {
  const [activeTab, setActiveTab] = useState('tasks');

  // Mock Data
  const childName = "Kofi";
  const xp = 1240;
  const nextLevelXp = 1500;
  const level = 4;
  const streak = 5;

  const tasks = [
    { id: 1, title: "Make Bed", points: 50, status: 'completed', time: '8:00 AM' },
    { id: 2, title: "Homework: Math", points: 100, status: 'pending', time: '4:00 PM' },
    { id: 3, title: "Read for 20 mins", points: 75, status: 'pending', time: 'Anytime' },
  ];

  const rewards = [
    { id: 1, title: "1 Hour TV", cost: 500, icon: "📺" },
    { id: 2, title: "Ice Cream", cost: 800, icon: "🍦" },
    { id: 3, title: "New Game", cost: 2000, icon: "🎮" },
  ];

  return (
    <div className="min-h-screen bg-indigo-50 font-sans text-gray-800 pb-20">
      {/* Top Bar (Gamified) */}
      <header className="bg-indigo-600 text-white p-4 sticky top-0 z-10 shadow-md">
        <div className="flex justify-between items-center">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 bg-indigo-400 rounded-full border-2 border-white flex items-center justify-center font-bold text-lg">
              {childName.charAt(0)}
            </div>
            <div>
              <h1 className="font-bold text-lg">Hi, {childName}!</h1>
              <div className="flex items-center gap-1 text-xs text-indigo-200">
                <span className="bg-white/20 px-1.5 py-0.5 rounded">Lvl {level}</span>
                <span>Warrior</span>
              </div>
            </div>
          </div>
          
          <div className="flex items-center gap-3">
            <div className="flex flex-col items-end">
              <span className="font-bold text-yellow-300 flex items-center gap-1">
                <Star className="w-4 h-4 fill-yellow-300" />
                {xp}
              </span>
              <div className="w-20 h-1.5 bg-black/20 rounded-full mt-1">
                <div 
                  className="h-full bg-yellow-300 rounded-full" 
                  style={{ width: `${(xp / nextLevelXp) * 100}%` }} 
                />
              </div>
            </div>
          </div>
        </div>
      </header>

      <main className="p-4 space-y-6">
        {/* Streak Banner */}
        <div className="bg-gradient-to-r from-orange-400 to-red-500 text-white p-4 rounded-2xl shadow-lg flex items-center justify-between">
          <div>
            <h2 className="font-bold text-lg">🔥 {streak} Day Streak!</h2>
            <p className="text-white/80 text-sm">Keep it up to earn a bonus!</p>
          </div>
          <Trophy className="w-10 h-10 text-yellow-200" />
        </div>

        {/* Action Tabs */}
        <div className="flex bg-white p-1 rounded-xl shadow-sm">
          <button 
            onClick={() => setActiveTab('tasks')}
            className={`flex-1 py-2 text-sm font-bold rounded-lg transition-colors ${activeTab === 'tasks' ? 'bg-indigo-100 text-indigo-600' : 'text-gray-400'}`}
          >
            My Missions
          </button>
          <button 
            onClick={() => setActiveTab('rewards')}
            className={`flex-1 py-2 text-sm font-bold rounded-lg transition-colors ${activeTab === 'rewards' ? 'bg-indigo-100 text-indigo-600' : 'text-gray-400'}`}
          >
            Rewards
          </button>
        </div>

        {/* Content Area */}
        {activeTab === 'tasks' ? (
          <div className="space-y-3">
            {tasks.map(task => (
              <Card key={task.id} className={`p-4 border-l-4 ${task.status === 'completed' ? 'border-green-500 bg-green-50/50' : 'border-indigo-500'} flex items-center justify-between`}>
                <div className="flex items-center gap-3">
                  <div className={`p-2 rounded-full ${task.status === 'completed' ? 'bg-green-100 text-green-600' : 'bg-indigo-100 text-indigo-600'}`}>
                    {task.status === 'completed' ? <CheckCircle className="w-5 h-5" /> : <BookOpen className="w-5 h-5" />}
                  </div>
                  <div>
                    <h3 className={`font-bold ${task.status === 'completed' ? 'text-gray-400 line-through' : 'text-gray-800'}`}>
                      {task.title}
                    </h3>
                    <p className="text-xs text-gray-500 flex items-center gap-1">
                      <Clock className="w-3 h-3" /> {task.time}
                    </p>
                  </div>
                </div>
                <div className="font-bold text-indigo-600 bg-indigo-50 px-2 py-1 rounded text-sm">
                  +{task.points} XP
                </div>
              </Card>
            ))}
            
            <Button className="w-full bg-indigo-600 hover:bg-indigo-700 text-white py-6 rounded-xl shadow-lg shadow-indigo-200 mt-4">
              I'm Bored! (Ask AI)
            </Button>
          </div>
        ) : (
          <div className="grid grid-cols-2 gap-3">
            {rewards.map(reward => (
              <Card key={reward.id} className="p-4 flex flex-col items-center text-center gap-2 border-2 border-transparent hover:border-indigo-200 transition-all">
                <div className="text-4xl mb-1">{reward.icon}</div>
                <h3 className="font-bold text-sm leading-tight">{reward.title}</h3>
                <Button 
                  size="sm" 
                  disabled={xp < reward.cost}
                  className={`w-full ${xp >= reward.cost ? 'bg-green-500 hover:bg-green-600' : 'bg-gray-200 text-gray-400'}`}
                >
                  {reward.cost} XP
                </Button>
              </Card>
            ))}
          </div>
        )}
      </main>

      {/* Bottom Nav */}
      <nav className="fixed bottom-0 left-0 right-0 bg-white border-t border-gray-200 px-6 py-3 flex justify-between items-center text-xs font-medium text-gray-400">
        <button className="flex flex-col items-center gap-1 text-indigo-600">
          <Gamepad2 className="w-6 h-6" />
          Play
        </button>
        <button className="flex flex-col items-center gap-1 hover:text-indigo-600">
          <Smile className="w-6 h-6" />
          Mood
        </button>
        <button className="flex flex-col items-center gap-1 hover:text-indigo-600">
          <Menu className="w-6 h-6" />
          Menu
        </button>
      </nav>
    </div>
  );
}
