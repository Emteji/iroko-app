import { Save, Bell, Shield, CreditCard } from 'lucide-react'

export const Settings = () => {
    return (
        <div className="max-w-4xl space-y-8">
            <div>
                <h2 className="text-2xl font-bold text-gray-900">Settings</h2>
                <p className="text-gray-500">Manage your admin preferences and security.</p>
            </div>

            <div className="rounded-xl border border-gray-100 bg-white p-6 shadow-sm">
                <h3 className="mb-4 flex items-center gap-2 font-semibold text-gray-900">
                    <Shield className="h-5 w-5 text-gray-400" />
                    Security
                </h3>
                <div className="space-y-4">
                    <div className="flex items-center justify-between rounded-lg border border-gray-100 p-4">
                        <div>
                            <p className="font-medium text-gray-900">Two-Factor Authentication</p>
                            <p className="text-sm text-gray-500">Add an extra layer of security to your account.</p>
                        </div>
                        <label className="relative inline-flex cursor-pointer items-center">
                            <input type="checkbox" value="" className="peer sr-only" />
                            <div className="peer h-6 w-11 rounded-full bg-gray-200 after:absolute after:left-[2px] after:top-[2px] after:h-5 after:w-5 after:rounded-full after:border after:border-gray-300 after:bg-white after:transition-all after:content-[''] peer-checked:bg-green-600 peer-checked:after:translate-x-full peer-checked:after:border-white peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-green-300"></div>
                        </label>
                    </div>
                    <div className="flex items-center justify-between rounded-lg border border-gray-100 p-4">
                        <div>
                            <p className="font-medium text-gray-900">Password Requirements</p>
                            <p className="text-sm text-gray-500">Require special characters for admin passwords.</p>
                        </div>
                        <label className="relative inline-flex cursor-pointer items-center">
                            <input type="checkbox" value="" className="peer sr-only" defaultChecked />
                            <div className="peer h-6 w-11 rounded-full bg-gray-200 after:absolute after:left-[2px] after:top-[2px] after:h-5 after:w-5 after:rounded-full after:border after:border-gray-300 after:bg-white after:transition-all after:content-[''] peer-checked:bg-green-600 peer-checked:after:translate-x-full peer-checked:after:border-white peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-green-300"></div>
                        </label>
                    </div>
                </div>
            </div>

            <div className="rounded-xl border border-gray-100 bg-white p-6 shadow-sm">
                <h3 className="mb-4 flex items-center gap-2 font-semibold text-gray-900">
                    <CreditCard className="h-5 w-5 text-gray-400" />
                    Payment Configuration
                </h3>
                <div className="space-y-4">
                    <div className="grid gap-6 md:grid-cols-2">
                        <div>
                            <label className="mb-2 block text-sm font-medium text-gray-700">Paystack Public Key</label>
                            <input
                                type="text"
                                placeholder="pk_test_..."
                                className="w-full rounded-lg border border-gray-200 bg-gray-50 p-2.5 text-sm text-gray-900 outline-none focus:border-green-500 focus:ring-1 focus:ring-green-500"
                            />
                        </div>
                        <div>
                            <label className="mb-2 block text-sm font-medium text-gray-700">Paystack Secret Key</label>
                            <input
                                type="password"
                                placeholder="sk_test_..."
                                className="w-full rounded-lg border border-gray-200 bg-gray-50 p-2.5 text-sm text-gray-900 outline-none focus:border-green-500 focus:ring-1 focus:ring-green-500"
                            />
                        </div>
                    </div>
                    <div className="flex items-center justify-between rounded-lg border border-gray-100 p-4">
                        <div>
                            <p className="font-medium text-gray-900">Live Mode</p>
                            <p className="text-sm text-gray-500">Enable real payments (process real cards).</p>
                        </div>
                        <label className="relative inline-flex cursor-pointer items-center">
                            <input type="checkbox" value="" className="peer sr-only" />
                            <div className="peer h-6 w-11 rounded-full bg-gray-200 after:absolute after:left-[2px] after:top-[2px] after:h-5 after:w-5 after:rounded-full after:border after:border-gray-300 after:bg-white after:transition-all after:content-[''] peer-checked:bg-green-600 peer-checked:after:translate-x-full peer-checked:after:border-white peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-green-300"></div>
                        </label>
                    </div>
                </div>
            </div>

            <div className="rounded-xl border border-gray-100 bg-white p-6 shadow-sm">
                <h3 className="mb-4 flex items-center gap-2 font-semibold text-gray-900">
                    <Bell className="h-5 w-5 text-gray-400" />
                    Notifications
                </h3>
                <div className="grid gap-6 md:grid-cols-2">
                    <div>
                        <label className="mb-2 block text-sm font-medium text-gray-700">Email Notifications</label>
                        <select className="w-full rounded-lg border border-gray-200 bg-gray-50 p-2.5 text-sm outline-none focus:border-green-500 focus:ring-1 focus:ring-green-500">
                            <option>All Activity</option>
                            <option>Critical Alerts Only</option>
                            <option>None</option>
                        </select>
                    </div>
                </div>
            </div>

            <div className="flex justify-end pt-4">
                <button className="flex items-center gap-2 rounded-lg bg-gray-900 px-6 py-2.5 font-medium text-white transition-all hover:bg-gray-800">
                    <Save className="h-4 w-4" /> Save Changes
                </button>
            </div>
        </div>
    )
}
