import { MoreHorizontal, User } from 'lucide-react'

export function RecentUsersTable() {
    // This will eventually be replaced by real data passed as props or fetched here
    const users = [
        { id: 1, name: 'Demo User 1', email: 'user1@example.com', role: 'User', status: 'Active', date: '2024-03-10' },
        { id: 2, name: 'Demo User 2', email: 'user2@example.com', role: 'User', status: 'Inactive', date: '2024-03-09' },
        { id: 3, name: 'Admin User', email: 'admin@iroko.com', role: 'Admin', status: 'Active', date: '2024-03-08' },
    ]

    return (
        <div className="rounded-xl border border-gray-100 bg-white shadow-sm">
            <div className="flex items-center justify-between border-b border-gray-100 px-6 py-4">
                <h3 className="font-semibold text-gray-900">Recent Users</h3>
                <button className="text-sm font-medium text-green-600 hover:text-green-700">View All</button>
            </div>
            <div className="overflow-x-auto">
                <table className="w-full text-left text-sm">
                    <thead className="bg-gray-50 text-gray-500">
                        <tr>
                            <th className="px-6 py-3 font-medium">User</th>
                            <th className="px-6 py-3 font-medium">Role</th>
                            <th className="px-6 py-3 font-medium">Status</th>
                            <th className="px-6 py-3 font-medium">Joined</th>
                            <th className="px-6 py-3 font-medium"></th>
                        </tr>
                    </thead>
                    <tbody className="divide-y divide-gray-100">
                        {users.map((user) => (
                            <tr key={user.id} className="group hover:bg-gray-50">
                                <td className="px-6 py-4">
                                    <div className="flex items-center gap-3">
                                        <div className="flex h-10 w-10 items-center justify-center rounded-full bg-gray-100 text-gray-500">
                                            <User className="h-5 w-5" />
                                        </div>
                                        <div>
                                            <p className="font-medium text-gray-900">{user.name}</p>
                                            <p className="text-gray-500">{user.email}</p>
                                        </div>
                                    </div>
                                </td>
                                <td className="px-6 py-4">
                                    <span className="inline-flex items-center rounded-full bg-blue-50 px-2.5 py-0.5 text-xs font-medium text-blue-700">
                                        {user.role}
                                    </span>
                                </td>
                                <td className="px-6 py-4">
                                    <span className={`inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-medium ${user.status === 'Active'
                                            ? 'bg-green-50 text-green-700'
                                            : 'bg-gray-100 text-gray-700'
                                        }`}>
                                        <span className={`mr-1.5 h-1.5 w-1.5 rounded-full ${user.status === 'Active' ? 'bg-green-600' : 'bg-gray-500'
                                            }`} />
                                        {user.status}
                                    </span>
                                </td>
                                <td className="px-6 py-4 text-gray-500">{user.date}</td>
                                <td className="px-6 py-4 text-right">
                                    <button className="text-gray-400 hover:text-gray-600">
                                        <MoreHorizontal className="h-5 w-5" />
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    )
}
