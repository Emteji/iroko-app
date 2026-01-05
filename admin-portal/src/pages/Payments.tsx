import { CreditCard, Search, ArrowUpRight, ArrowDownLeft } from 'lucide-react'

export const Payments = () => {
    // Mock data for payments
    const transactions = [
        { id: 'tx_01', user: 'Parent One', amount: '5,000', type: 'Subscription', status: 'Success', date: '2024-03-12' },
        { id: 'tx_02', user: 'Parent Two', amount: '2,500', type: 'Renewal', status: 'Pending', date: '2024-03-11' },
        { id: 'tx_03', user: 'Parent Three', amount: '10,000', type: 'Annual', status: 'Success', date: '2024-03-10' },
        { id: 'tx_04', user: 'Parent Four', amount: '5,000', type: 'Subscription', status: 'Failed', date: '2024-03-09' },
    ]

    return (
        <div className="space-y-8">
            <div className="flex items-center justify-between">
                <div>
                    <h2 className="text-2xl font-bold text-gray-900">Payment History</h2>
                    <p className="text-gray-500">View and manage all transactions.</p>
                </div>
                <button className="flex items-center gap-2 rounded-lg bg-gray-900 px-4 py-2 text-sm font-medium text-white transition-colors hover:bg-gray-800">
                    <ArrowDownLeft className="h-4 w-4" /> Export Report
                </button>
            </div>

            {/* Stats */}
            <div className="grid grid-cols-1 gap-6 md:grid-cols-3">
                <div className="rounded-xl border border-gray-100 bg-white p-6 shadow-sm">
                    <div className="flex items-center justify-between">
                        <p className="text-sm font-medium text-gray-500">Total Revenue</p>
                        <div className="rounded-lg bg-green-50 p-2 text-green-600"><CreditCard className="h-5 w-5" /></div>
                    </div>
                    <p className="mt-2 text-3xl font-bold text-gray-900">₦2,450,000</p>
                </div>
                <div className="rounded-xl border border-gray-100 bg-white p-6 shadow-sm">
                    <div className="flex items-center justify-between">
                        <p className="text-sm font-medium text-gray-500">Pending</p>
                        <div className="rounded-lg bg-orange-50 p-2 text-orange-600"><ArrowUpRight className="h-5 w-5" /></div>
                    </div>
                    <p className="mt-2 text-3xl font-bold text-gray-900">₦45,000</p>
                </div>
                <div className="rounded-xl border border-gray-100 bg-white p-6 shadow-sm">
                    <div className="flex items-center justify-between">
                        <p className="text-sm font-medium text-gray-500">Success Rate</p>
                        <div className="rounded-lg bg-blue-50 p-2 text-blue-600"><CreditCard className="h-5 w-5" /></div>
                    </div>
                    <p className="mt-2 text-3xl font-bold text-gray-900">98.2%</p>
                </div>
            </div>

            {/* Table */}
            <div className="rounded-xl border border-gray-100 bg-white shadow-sm">
                <div className="flex items-center justify-between border-b border-gray-100 px-6 py-4">
                    <div className="relative w-full max-w-sm">
                        <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-gray-400" />
                        <input
                            type="text"
                            placeholder="Search transaction ID..."
                            className="w-full rounded-lg border border-gray-200 py-2 pl-10 pr-4 text-sm outline-none focus:border-green-500 focus:ring-1 focus:ring-green-500"
                        />
                    </div>
                    <button className="text-sm font-medium text-gray-600 hover:text-gray-900">Filter</button>
                </div>
                <div className="overflow-x-auto">
                    <table className="w-full text-left text-sm">
                        <thead className="bg-gray-50 text-gray-500">
                            <tr>
                                <th className="px-6 py-3 font-medium">Tx ID</th>
                                <th className="px-6 py-3 font-medium">User</th>
                                <th className="px-6 py-3 font-medium">Type</th>
                                <th className="px-6 py-3 font-medium">Date</th>
                                <th className="px-6 py-3 font-medium">Amount</th>
                                <th className="px-6 py-3 font-medium">Status</th>
                            </tr>
                        </thead>
                        <tbody className="divide-y divide-gray-100">
                            {transactions.map((tx) => (
                                <tr key={tx.id} className="hover:bg-gray-50">
                                    <td className="px-6 py-4 font-mono text-gray-500">{tx.id}</td>
                                    <td className="px-6 py-4 font-medium text-gray-900">{tx.user}</td>
                                    <td className="px-6 py-4 text-gray-600">{tx.type}</td>
                                    <td className="px-6 py-4 text-gray-500">{tx.date}</td>
                                    <td className="px-6 py-4 font-medium text-gray-900">₦{tx.amount}</td>
                                    <td className="px-6 py-4">
                                        <span className={`inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-medium ${tx.status === 'Success' ? 'bg-green-50 text-green-700' :
                                                tx.status === 'Pending' ? 'bg-orange-50 text-orange-700' :
                                                    'bg-red-50 text-red-700'
                                            }`}>
                                            {tx.status}
                                        </span>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    )
}
