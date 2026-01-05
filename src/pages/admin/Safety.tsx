import React, { useEffect } from 'react';
import { Card } from '../../components/ui/Card';
import { useAdminStore } from '../../stores/adminStore';
import { AlertTriangle, CheckCircle } from 'lucide-react';

export default function SafetyAbuse() {
  const { flaggedEvents, fetchUsers, resolveEvent } = useAdminStore();

  useEffect(() => {
    fetchUsers();
  }, [fetchUsers]);

  const criticalEvents = flaggedEvents.filter(e => e.severity === 'critical' && e.status === 'open');
  const openEvents = flaggedEvents.filter(e => e.status === 'open');

  return (
    <div className="space-y-6">
      <div>
        <h2 className="text-2xl font-serif font-bold text-gray-900">Safety & Abuse</h2>
        <p className="text-sm text-gray-500">Handle reports and critical safety incidents.</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Card className="p-6 border-l-4 border-red-500">
          <h3 className="text-lg font-bold text-gray-900">Critical Incidents</h3>
          <p className="text-3xl font-bold mt-2 text-red-600">{criticalEvents.length}</p>
          <p className="text-xs text-gray-400 mt-1">Requires immediate action</p>
        </Card>
        <Card className="p-6 border-l-4 border-orange-500">
          <h3 className="text-lg font-bold text-gray-900">Open Reports</h3>
          <p className="text-3xl font-bold mt-2 text-orange-600">{openEvents.length}</p>
          <p className="text-xs text-gray-400 mt-1">Pending review</p>
        </Card>
        <Card className="p-6 border-l-4 border-blue-500">
          <h3 className="text-lg font-bold text-gray-900">Automated Flags</h3>
          <p className="text-3xl font-bold mt-2 text-blue-600">
             {flaggedEvents.filter(e => e.status === 'open').length}
          </p>
          <p className="text-xs text-gray-400 mt-1">AI detected patterns</p>
        </Card>
      </div>

      <Card className="p-6">
        <h3 className="text-lg font-bold text-gray-900 mb-4">Recent Flags</h3>
        <div className="space-y-4">
            {flaggedEvents.map(event => (
                <div key={event.id} className="flex items-start justify-between border-b border-gray-100 pb-4 last:border-0">
                    <div className="flex gap-4">
                        <div className={`p-2 rounded-full ${
                            event.severity === 'critical' ? 'bg-red-100 text-red-600' : 
                            event.severity === 'medium' ? 'bg-orange-100 text-orange-600' : 
                            'bg-yellow-100 text-yellow-600'
                        }`}>
                            <AlertTriangle className="w-5 h-5" />
                        </div>
                        <div>
                            <p className="font-medium text-gray-900">{event.description}</p>
                            <p className="text-sm text-gray-500">User ID: {event.userId} • {new Date(event.timestamp).toLocaleString()}</p>
                        </div>
                    </div>
                    {event.status === 'open' ? (
                        <button 
                            onClick={() => resolveEvent(event.id)}
                            className="text-sm bg-gray-100 hover:bg-gray-200 text-gray-700 px-3 py-1.5 rounded-lg transition-colors font-medium"
                        >
                            Mark Resolved
                        </button>
                    ) : (
                        <span className="flex items-center text-green-600 text-sm font-medium">
                            <CheckCircle className="w-4 h-4 mr-1" />
                            Resolved
                        </span>
                    )}
                </div>
            ))}
            {flaggedEvents.length === 0 && (
                <p className="text-center text-gray-500 py-8">No safety flags found.</p>
            )}
        </div>
      </Card>
    </div>
  );
}
