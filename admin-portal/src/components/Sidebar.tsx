import { LayoutDashboard, Users, CreditCard, LogOut, Settings } from 'lucide-react'
import { Link, useLocation } from 'react-router-dom'
import { cn } from '../lib/utils'
import { supabase } from '../lib/supabase'
import { useNavigate } from 'react-router-dom'

export function Sidebar() {
    const location = useLocation()
    const navigate = useNavigate()

    const handleLogout = async () => {
        await supabase.auth.signOut()
        navigate('/')
    }

    const navigation = [
        { name: 'Overview', href: '/dashboard', icon: LayoutDashboard },
        { name: 'Users', href: '/dashboard/users', icon: Users },
        { name: 'Payments', href: '/dashboard/payments', icon: CreditCard },
        { name: 'Settings', href: '/dashboard/settings', icon: Settings },
    ]

    return (
        <aside className="flex h-screen w-72 flex-col border-r border-gray-200 bg-white shadow-sm">
            <div className="flex h-20 items-center border-b border-gray-100 px-8">
                <div className="flex items-center gap-2">
                    <div className="h-8 w-8 rounded-lg bg-green-600" />
                    <span className="text-xl font-bold text-gray-900">Admin/Portal</span>
                </div>
            </div>

            <nav className="flex-1 space-y-1 px-4 py-8">
                {navigation.map((item) => {
                    const Icon = item.icon
                    const isActive = location.pathname === item.href

                    return (
                        <Link
                            key={item.name}
                            to={item.href}
                            className={cn(
                                "group flex items-center rounded-xl px-4 py-3.5 text-sm font-medium transition-all duration-200",
                                isActive
                                    ? "bg-green-50 text-green-700 shadow-sm ring-1 ring-green-200"
                                    : "text-gray-600 hover:bg-gray-50 hover:text-gray-900"
                            )}
                        >
                            <Icon className={cn("mr-3 h-5 w-5", isActive ? "text-green-600" : "text-gray-400 group-hover:text-gray-600")} />
                            {item.name}
                        </Link>
                    )
                })}
            </nav>

            <div className="border-t border-gray-100 p-4">
                <button
                    onClick={handleLogout}
                    className="flex w-full items-center rounded-xl px-4 py-3 text-sm font-medium text-red-600 transition-colors hover:bg-red-50"
                >
                    <LogOut className="mr-3 h-5 w-5" />
                    Sign Out
                </button>
            </div>
        </aside>
    )
}
