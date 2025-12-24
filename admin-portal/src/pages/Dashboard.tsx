import React from 'react'
import { useNavigate, Outlet, Link } from 'react-router-dom'
import { supabase } from '../lib/supabase'
import { PaystackButton } from '../components/PaystackButton'

export const DashboardLayout = () => {
    const navigate = useNavigate()

    const handleLogout = async () => {
        await supabase.auth.signOut()
        navigate('/')
    }

    return (
        <div className="flex h-screen bg-gray-100">
            {/* Sidebar */}
            <aside className="w-64 bg-green-800 text-white">
                <div className="p-4">
                    <h1 className="text-xl font-bold">Admin Panel</h1>
                </div>
                <nav className="mt-8 space-y-2 px-4">
                    <Link to="/dashboard" className="block rounded px-4 py-2 hover:bg-green-700">Overview</Link>
                    <Link to="/dashboard/users" className="block rounded px-4 py-2 hover:bg-green-700">Users</Link>
                    <Link to="/dashboard/payments" className="block rounded px-4 py-2 hover:bg-green-700">Payments</Link>
                </nav>
                <div className="absolute bottom-0 w-64 p-4">
                    <button
                        onClick={handleLogout}
                        className="w-full rounded bg-red-600 px-4 py-2 hover:bg-red-700"
                    >
                        Logout
                    </button>
                </div>
            </aside>

            {/* Main Content */}
            <main className="flex-1 overflow-auto p-8">
                <Outlet />
            </main>
        </div>
    )
}

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
        <div>
            <h2 className="mb-6 text-3xl font-bold text-gray-800">Dashboard Overview</h2>
            <div className="grid grid-cols-1 gap-6 md:grid-cols-3">
                {/* Stats Cards */}
                <div className="rounded-lg bg-white p-6 shadow">
                    <h3 className="text-gray-500">Total Users</h3>
                    <p className="text-3xl font-bold">{userCount}</p>
                </div>
                <div className="rounded-lg bg-white p-6 shadow">
                    <h3 className="text-gray-500">Revenue</h3>
                    <p className="text-3xl font-bold">₦0.00</p>
                </div>
                <div className="rounded-lg bg-white p-6 shadow">
                    <h3 className="text-gray-500">Active Subscriptions</h3>
                    <p className="text-3xl font-bold">0</p>
                </div>
            </div>

            <div className="mt-8 rounded-lg bg-white p-6 shadow">
                <h3 className="mb-4 text-lg font-bold">Quick Actions</h3>
                <PaystackButton />
            </div>
        </div>
    )
}
