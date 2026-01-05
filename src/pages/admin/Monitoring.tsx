import React from 'react';
import { Card } from '../../components/ui/Card';

export default function SystemMonitoring() {
  return (
    <div className="space-y-6">
      <div>
        <h2 className="text-2xl font-serif font-bold text-gray-900">System Monitoring</h2>
        <p className="text-sm text-gray-500">Infrastructure health and performance metrics.</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <Card className="p-6 h-64 flex items-center justify-center bg-gray-50">
          <p className="text-gray-400">Server CPU / Memory Chart Placeholder</p>
        </Card>
        <Card className="p-6 h-64 flex items-center justify-center bg-gray-50">
          <p className="text-gray-400">Database Latency Chart Placeholder</p>
        </Card>
      </div>
    </div>
  );
}
