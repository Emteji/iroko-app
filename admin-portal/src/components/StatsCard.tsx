import { type LucideIcon } from 'lucide-react'
import { cn } from '../lib/utils'

interface StatsCardProps {
    title: string
    value: string | number
    icon: LucideIcon
    trend?: string
    trendUp?: boolean
    color?: 'blue' | 'green' | 'purple' | 'orange'
}

export function StatsCard({ title, value, icon: Icon, trend, trendUp, color = 'blue' }: StatsCardProps) {
    const colorStyles = {
        blue: 'bg-blue-50 text-blue-600',
        green: 'bg-green-50 text-green-600',
        purple: 'bg-purple-50 text-purple-600',
        orange: 'bg-orange-50 text-orange-600',
    }

    return (
        <div className="rounded-xl border border-gray-100 bg-white p-6 shadow-sm transition-all hover:shadow-md">
            <div className="flex items-center justify-between">
                <div>
                    <p className="text-sm font-medium text-gray-500">{title}</p>
                    <p className="mt-2 text-3xl font-bold text-gray-900">{value}</p>
                </div>
                <div className={cn("rounded-lg p-3", colorStyles[color])}>
                    <Icon className="h-6 w-6" />
                </div>
            </div>
            {trend && (
                <div className="mt-4 flex items-center text-sm">
                    <span className={cn("font-medium", trendUp ? "text-green-600" : "text-red-600")}>
                        {trendUp ? '↑' : '↓'} {trend}
                    </span>
                    <span className="ml-2 text-gray-400">vs last month</span>
                </div>
            )}
        </div>
    )
}
