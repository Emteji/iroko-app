import React, { useEffect, useState } from 'react';
import { Card } from '../../components/ui/Card';
import { useAdminStore } from '../../stores/adminStore';
import { MoreHorizontal, ShieldOff, CheckCircle } from 'lucide-react';

export default function UserManagement() {
  const { users, fetchUsers, suspendUser, activateUser, isLoading } = useAdminStore();
  const [filter, setFilter] = useState('all');

  useEffect(() => {
    fetchUsers();
  }, [fetchUsers]);

  const filteredUsers = users.filter(user => 
    filter === 'all' ? true : user.role === filter
  );

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-end">
        <div>
          <h2 className="text-2xl font-serif font-bold text-gray-900">User Management</h2>
          <p className="text-sm text-gray-500">Manage parent and child accounts.</p>
        </div>
        <div className="flex gap-2">
          <select 
            className="border border-gray-300 rounded-lg px-3 py-2 text-sm"
            value={filter}
            onChange={(e) => setFilter(e.target.value)}
          >
            <option value="all">All Roles</option>
            <option value="parent">Parents</option>
            <option value="child">Children</option>
            <option value="admin">Admins</option>
          </select>
        </div>
      </div>

      <Card className="p-0 overflow-hidden border border-gray-200 shadow-sm">
        <table className="w-full text-sm text-left">
          <thead className="bg-gray-50 text-gray-600 font-medium border-b border-gray-200">
            <tr>
              <th className="py-3 px-6">Email / ID</th>
              <th className="py-3 px-6">Role</th>
              <th className="py-3 px-6">Status</th>
              <th className="py-3 px-6">Joined</th>
              <th className="py-3 px-6 text-right">Actions</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100">
            {isLoading ? (
              <tr>
                <td colSpan={5} className="py-8 text-center text-gray-500">Loading users...</td>
              </tr>
            ) : filteredUsers.length === 0 ? (
              <tr>
                <td colSpan={5} className="py-8 text-center text-gray-500">No users found.</td>
              </tr>
            ) : (
              filteredUsers.map((user) => (
                <tr key={user.id} className="hover:bg-gray-50 transition-colors">
                  <td className="py-3 px-6 font-medium text-gray-900">
                    {user.email}
                    <div className="text-xs text-gray-400 font-normal">ID: {user.id}</div>
                  </td>
                  <td className="py-3 px-6">
                    <span className={`px-2 py-1 rounded-full text-xs font-medium ${
                      user.role === 'parent' ? 'bg-blue-100 text-blue-700' :
                      user.role === 'child' ? 'bg-purple-100 text-purple-700' :
                      'bg-gray-100 text-gray-700'
                    }`}>
                      {user.role.toUpperCase()}
                    </span>
                  </td>
                  <td className="py-3 px-6">
                    <span className={`flex items-center gap-1.5 text-xs font-medium ${
                      user.status === 'active' ? 'text-green-600' :
                      user.status === 'suspended' ? 'text-red-600' : 'text-orange-600'
                    }`}>
                      <div className={`w-1.5 h-1.5 rounded-full ${
                        user.status === 'active' ? 'bg-green-600' :
                        user.status === 'suspended' ? 'bg-red-600' : 'bg-orange-600'
                      }`} />
                      {user.status.charAt(0).toUpperCase() + user.status.slice(1)}
                    </span>
                  </td>
                  <td className="py-3 px-6 text-gray-500">
                    {new Date(user.joinedAt).toLocaleDateString()}
                  </td>
                  <td className="py-3 px-6 text-right">
                    <div className="flex justify-end gap-2">
                      {user.status === 'active' ? (
                        <button 
                          onClick={() => suspendUser(user.id)}
                          className="p-1.5 text-gray-400 hover:text-red-600 hover:bg-red-50 rounded transition-colors"
                          title="Suspend User"
                        >
                          <ShieldOff className="w-4 h-4" />
                        </button>
                      ) : (
                        <button 
                          onClick={() => activateUser(user.id)}
                          className="p-1.5 text-gray-400 hover:text-green-600 hover:bg-green-50 rounded transition-colors"
                          title="Activate User"
                        >
                          <CheckCircle className="w-4 h-4" />
                        </button>
                      )}
                      <button className="p-1.5 text-gray-400 hover:text-gray-900 hover:bg-gray-100 rounded transition-colors">
                        <MoreHorizontal className="w-4 h-4" />
                      </button>
                    </div>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </Card>
    </div>
  );
}
