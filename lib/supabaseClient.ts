import { createClient } from '@supabase/supabase-js';

// Helper to safely access env vars across different environments (Vite vs CRA vs Node)
const getEnv = (key: string) => {
  if (typeof import.meta !== 'undefined' && (import.meta as any).env) {
    return (import.meta as any).env[key] || '';
  }
  if (typeof process !== 'undefined' && process.env) {
    return process.env[key] || '';
  }
  return '';
};

// Check for forced demo mode from localStorage (allows fallback if backend is broken)
const isForceDemo = typeof window !== 'undefined' && localStorage.getItem('iroko_force_demo') === 'true';

// Updated with your specific project details
const rawUrl = getEnv('VITE_SUPABASE_URL') || 'https://lmmidvcnltacprzuvqam.supabase.co';
const rawKey = getEnv('VITE_SUPABASE_ANON_KEY') || 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImxtbWlkdmNubHRhY3ByenV2cWFtIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjYyNzE0NTEsImV4cCI6MjA4MTg0NzQ1MX0.LVNOvU0nDElR-NxM7V2EUWNlmHRd_jibAr7knkHZbEM';

// Check if we have a valid configuration AND are not in forced demo mode
export const isSupabaseConfigured = 
  !isForceDemo &&
  rawUrl && 
  rawUrl.length > 0 && 
  rawUrl.startsWith('http') && 
  rawKey && 
  rawKey.length > 0;

export const supabase = createClient(rawUrl, rawKey);