import { RecentUsersTable } from '../components/RecentUsersTable'
import { Plus } from 'lucide-react'

export const Users = () => {
    return (
        <div className="space-y-8">
            <div className="flex items-center justify-between">
                <div>
                    <h2 className="text-2xl font-bold text-gray-900">User Management</h2>
                    <p className="text-gray-500">Manage parent accounts and permissions.</p>
                </div>
                <button className="flex items-center gap-2 rounded-lg bg-green-600 px-4 py-2 text-sm font-medium text-white transition-colors hover:bg-green-700">
                    <Plus className="h-4 w-4" /> Add User
                </button>
            </div>

            {/* Reusing the robust table component we already built */}
            <RecentUsersTable />
        </div>
    )
}
