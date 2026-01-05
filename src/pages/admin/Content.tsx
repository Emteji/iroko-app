import React, { useState } from 'react';
import { Card } from '../../components/ui/Card';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { Plus, BookOpen, Layers, X, Save } from 'lucide-react';

export default function ContentManagement() {
  const [missions, setMissions] = useState([
    { id: 1, title: 'Morning Harvest', type: 'Routine', xp: 50, active: true },
    { id: 2, title: 'The Wise Elder', type: 'Story', xp: 100, active: true },
    { id: 3, title: 'River Crossing', type: 'Challenge', xp: 150, active: false },
  ]);

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [newMission, setNewMission] = useState({ title: '', type: 'Routine', xp: 50 });

  const handleCreate = () => {
    setMissions([...missions, { 
      id: missions.length + 1, 
      title: newMission.title, 
      type: newMission.type, 
      xp: Number(newMission.xp), 
      active: false 
    }]);
    setIsModalOpen(false);
    setNewMission({ title: '', type: 'Routine', xp: 50 });
  };

  return (
    <div className="space-y-6 relative">
      <div className="flex justify-between items-end">
        <div>
          <h2 className="text-2xl font-serif font-bold text-gray-900">Village Content</h2>
          <p className="text-sm text-gray-500">Manage missions, stories, and scenarios.</p>
        </div>
        <Button className="flex items-center gap-2" onClick={() => setIsModalOpen(true)}>
          <Plus className="w-4 h-4" />
          Create Mission
        </Button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {/* Stats */}
        <Card className="p-4 border-l-4 border-iroko-gold bg-white">
          <div className="flex justify-between items-start">
            <div>
              <p className="text-xs font-medium text-gray-500 uppercase">Active Missions</p>
              <h3 className="text-2xl font-bold text-iroko-brown mt-1">{missions.filter(m => m.active).length}</h3>
            </div>
            <BookOpen className="w-5 h-5 text-iroko-gold" />
          </div>
        </Card>
        <Card className="p-4 border-l-4 border-iroko-forest bg-white">
          <div className="flex justify-between items-start">
            <div>
              <p className="text-xs font-medium text-gray-500 uppercase">Story Modules</p>
              <h3 className="text-2xl font-bold text-iroko-brown mt-1">5</h3>
            </div>
            <Layers className="w-5 h-5 text-iroko-forest" />
          </div>
        </Card>
      </div>

      {/* Content List */}
      <Card className="p-0 overflow-hidden border border-gray-200 shadow-sm">
        <div className="p-4 border-b border-gray-100 bg-gray-50 flex justify-between items-center">
          <h3 className="font-bold text-gray-700">Mission Library</h3>
          <div className="flex gap-2">
             <input placeholder="Search..." className="text-sm border rounded px-2 py-1" />
          </div>
        </div>
        <table className="w-full text-sm text-left">
          <thead className="bg-white text-gray-500 font-medium border-b border-gray-100">
            <tr>
              <th className="py-3 px-6">Title</th>
              <th className="py-3 px-6">Type</th>
              <th className="py-3 px-6">XP Reward</th>
              <th className="py-3 px-6">Status</th>
              <th className="py-3 px-6 text-right">Actions</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100">
            {missions.map((m) => (
              <tr key={m.id} className="hover:bg-gray-50">
                <td className="py-3 px-6 font-medium text-gray-900">{m.title}</td>
                <td className="py-3 px-6">
                  <span className="px-2 py-1 bg-gray-100 rounded text-xs text-gray-600">{m.type}</span>
                </td>
                <td className="py-3 px-6 font-bold text-iroko-gold">{m.xp} XP</td>
                <td className="py-3 px-6">
                  <span className={`w-2 h-2 rounded-full inline-block mr-2 ${m.active ? 'bg-green-500' : 'bg-gray-300'}`} />
                  {m.active ? 'Active' : 'Draft'}
                </td>
                <td className="py-3 px-6 text-right">
                  <button className="text-blue-600 hover:underline">Edit</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </Card>

      {/* Create Modal */}
      {isModalOpen && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
          <div className="bg-white rounded-xl p-6 w-full max-w-md shadow-2xl">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-lg font-bold text-gray-900">Create New Mission</h3>
              <button onClick={() => setIsModalOpen(false)}><X className="w-5 h-5 text-gray-500" /></button>
            </div>
            
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Title</label>
                <Input 
                  value={newMission.title} 
                  onChange={(e) => setNewMission({...newMission, title: e.target.value})} 
                  placeholder="e.g. The First Harvest" 
                />
              </div>
              
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Type</label>
                <select 
                  className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm"
                  value={newMission.type}
                  onChange={(e) => setNewMission({...newMission, type: e.target.value})}
                >
                  <option value="Routine">Routine</option>
                  <option value="Story">Story</option>
                  <option value="Challenge">Challenge</option>
                </select>
              </div>
              
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">XP Reward</label>
                <Input 
                  type="number"
                  value={newMission.xp} 
                  onChange={(e) => setNewMission({...newMission, xp: Number(e.target.value)})} 
                />
              </div>

              <div className="pt-4 flex gap-3">
                <Button variant="outline" className="flex-1" onClick={() => setIsModalOpen(false)}>Cancel</Button>
                <Button className="flex-1" onClick={handleCreate}>
                  <Save className="w-4 h-4 mr-2" />
                  Save Mission
                </Button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
