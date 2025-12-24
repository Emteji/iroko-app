import React, { useState } from 'react'
import { supabase } from '../lib/supabase'
import { useNavigate } from 'react-router-dom'
import { LayoutDashboard } from 'lucide-react'

export const Login = () => {
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState('')
    const navigate = useNavigate()

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault()
        setLoading(true)
        setError('')

        try {
            const { error } = await supabase.auth.signInWithPassword({
                email,
                password,
            })

            if (error) throw error

            navigate('/dashboard')
        } catch (err: any) {
            setError(err.message)
        } finally {
            setLoading(false)
        }
    }

    return (
        <div className="flex min-h-screen w-full items-center justify-center bg-gray-50 p-4">
            {/* Decorative Background */}
            <div className="absolute inset-0 z-0 overflow-hidden">
                <div className="absolute -left-10 -top-10 h-64 w-64 rounded-full bg-green-200 opacity-20 blur-3xl"></div>
                <div className="absolute bottom-0 right-0 h-96 w-96 rounded-full bg-blue-200 opacity-20 blur-3xl"></div>
            </div>

            <div className="relative z-10 w-full max-w-sm overflow-hidden rounded-2xl bg-white shadow-xl ring-1 ring-gray-100">
                <div className="bg-white p-8">
                    <div className="mb-8 flex flex-col items-center">
                        <div className="mb-4 flex h-12 w-12 items-center justify-center rounded-xl bg-green-600 text-white shadow-lg shadow-green-200">
                            <LayoutDashboard className="h-6 w-6" />
                        </div>
                        <h2 className="text-2xl font-bold text-gray-900">Admin Portal</h2>
                        <p className="text-sm text-gray-500">Sign in to manage the village.</p>
                    </div>

                    {error && (
                        <div className="mb-6 rounded-lg bg-red-50 p-4 text-sm text-red-600 border border-red-100">
                            {error}
                        </div>
                    )}

                    <form onSubmit={handleLogin} className="space-y-5">
                        <div>
                            <label className="mb-1 block text-sm font-medium text-gray-900">Email Address</label>
                            <input
                                type="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                className="w-full rounded-lg border border-gray-200 bg-gray-50 p-2.5 text-sm font-medium text-gray-900 placeholder:text-gray-400 outline-none transition-all focus:border-green-500 focus:bg-white focus:ring-1 focus:ring-green-500"
                                placeholder="name@example.com"
                                required
                            />
                        </div>

                        <div>
                            <label className="mb-1 block text-sm font-medium text-gray-900">Password</label>
                            <input
                                type="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                className="w-full rounded-lg border border-gray-200 bg-gray-50 p-2.5 text-sm font-medium text-gray-900 placeholder:text-gray-400 outline-none transition-all focus:border-green-500 focus:bg-white focus:ring-1 focus:ring-green-500"
                                placeholder="••••••••"
                                required
                            />
                        </div>

                        <button
                            type="submit"
                            disabled={loading}
                            className="w-full rounded-lg bg-gray-900 py-2.5 text-sm font-bold text-white shadow-lg transition-all hover:bg-gray-800 hover:shadow-xl disabled:opacity-50"
                        >
                            {loading ? 'Authenticating...' : 'Sign In'}
                        </button>
                    </form>
                </div>
                <div className="bg-gray-50 px-8 py-4 text-center">
                    <p className="text-xs text-gray-400">Restricted Access. Secure Connection.</p>
                </div>
            </div>
        </div>
    )
}
