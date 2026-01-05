import React from 'react'
import { Outlet } from 'react-router-dom'
import { supabase } from '../lib/supabase'
import { PaystackButton } from '../components/PaystackButton'
import { Sidebar } from '../components/Sidebar'
import { StatsCard } from '../components/StatsCard'
import { RecentUsersTable } from '../components/RecentUsersTable'
import { Users, CreditCard, TrendingUp } from 'lucide-react'

// Layout Component (Shell)
export const DashboardLayout = () => {
    return (
        <div className="flex h-screen bg-gray-50 font-sans">
            <Sidebar />
            <main className="flex-1 overflow-y-auto">
                <header className="flex h-20 items-center justify-between bg-white px-8 shadow-sm">
                    <h2 className="text-2xl font-bold text-gray-900">Dashboard</h2>
                    <div className="flex items-center gap-4">
                        <div className="flex h-10 w-10 items-center justify-center rounded-full bg-gray-100">
                            {/* User Avatar Placeholder */}
                            <span className="font-bold text-gray-600">A</span>
                        </div>
                    </div>
                </header>
                <div className="p-8">
                    <Outlet />
                </div>
            </main>
        </div>
    )
}

// Overview Page Component
export const DashboardOverview = () => {
    const [userCount, setUserCount] = React.useState(0)

    React.useEffect(() => {
        const fetchStats = async () => {
            const { count } = await supabase
                .from('profiles')
                .select('*', { count: 'exact', head: true })
            if (count !== null) setUserCount(count)
        }
        fetchStats()
    }, [])

    return (
        <div className="space-y-8">
            {/* Stats Grid */}
            <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3">
                <StatsCard
                    title="Total Users"
                    value={userCount}
                    icon={Users}
                    trend="12%"
                    trendUp={true}
                    color="blue"
                />
                <StatsCard
                    title="Total Revenue"
                    value="₦0.00"
                    icon={CreditCard}
                    trend="0%"
                    trendUp={true}
                    color="green"
                />
                <StatsCard
                    title="Active Subs"
                    value="0"
                    icon={TrendingUp}
                    trend="5%"
                    trendUp={true}
                    color="purple"
                />
            </div>

            <div className="grid grid-cols-1 gap-8 lg:grid-cols-3">
                {/* Main Content Area (e.g., Table) */}
                <div className="lg:col-span-2">
                    <RecentUsersTable />
                </div>

                {/* Side Content Area (e.g., Quick Actions) */}
                <div className="space-y-6">
                    <div className="rounded-xl border border-gray-100 bg-white p-6 shadow-sm">
                        <h3 className="mb-4 font-semibold text-gray-900">Quick Actions</h3>
                        <p className="mb-6 text-sm text-gray-500">Initiate manual payments or overrides.</p>
                        <PaystackButton />
                    </div>
                </div>
            </div>
        </div>
    )
}
