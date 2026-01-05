import { createClient } from '@supabase/supabase-js'

// Use VITE_ prefix for shared env vars if needed, but for backend strict usage:
const url = process.env.VITE_SUPABASE_URL || process.env.SUPABASE_URL
const key = process.env.SUPABASE_SERVICE_ROLE_KEY || process.env.VITE_SUPABASE_ANON_KEY

if (!url || !key) {
  console.warn('Missing Supabase URL or Key')
}

export const supabase = createClient(url || '', key || '', {
  auth: { persistSession: false, autoRefreshToken: false }
})

