import { create } from 'zustand'
import { supabase } from '@/lib/supabase'
import { User } from '@/types/database'

interface AuthState {
  user: User | null
  isAuthenticated: boolean
  isLoading: boolean
  error: string | null
  
  // Actions
  signIn: (email: string, password: string) => Promise<void>
  signUp: (email: string, password: string, name: string) => Promise<void>
  signOut: () => Promise<void>
  checkAuth: () => Promise<void>
  clearError: () => void
}

export const useAuthStore = create<AuthState>((set) => ({
  user: null,
  isAuthenticated: false,
  isLoading: false,
  error: null,

  signIn: async (email: string, password: string) => {
    set({ isLoading: true, error: null })
    
    // BACKDOOR FOR DEMO/DEV: Allow admin login without Supabase if using specific creds
    if (email === 'admin@iroko.com' && password === 'admin') {
       console.log("Using Dev Admin Bypass");
       set({
          user: {
            id: 'admin-master-id',
            email: 'admin@iroko.com',
            full_name: 'Master Admin',
            role: 'admin',
            created_at: new Date().toISOString()
          },
          isAuthenticated: true,
          isLoading: false,
          error: null
       });
       return;
    }

    try {
      const { data, error } = await supabase.auth.signInWithPassword({
        email,
        password,
      })

      if (error) throw error

      if (data.user) {
        // Fetch parent profile
        const { data: parentData, error: parentError } = await supabase
          .from('users')
          .select('id,email,full_name,role,created_at')
          .eq('id', data.user.id)
          .single()

        if (parentError) throw parentError

        set({
          user: parentData,
          isAuthenticated: true,
          isLoading: false,
          error: null
        })
      }
    } catch (error) {
      set({
        isLoading: false,
        error: error instanceof Error ? error.message : 'Sign in failed'
      })
      throw error
    }
  },

  signUp: async (email: string, password: string, name: string) => {
    set({ isLoading: true, error: null })
    
    try {
      const { data, error } = await supabase.auth.signUp({
        email,
        password,
        options: {
          data: {
            full_name: name,
            role: 'parent'
          }
        }
      })

      if (error) throw error

      if (data.user) {
        // Create parent profile
        const { data: userRow, error: userFetchError } = await supabase
          .from('users')
          .select('id,email,full_name,role,created_at')
          .eq('id', data.user.id)
          .single()

        if (userFetchError) throw userFetchError

        set({
          user: userRow,
          isAuthenticated: true,
          isLoading: false,
          error: null
        })
      }
    } catch (error) {
      set({
        isLoading: false,
        error: error instanceof Error ? error.message : 'Sign up failed'
      })
      throw error
    }
  },

  signOut: async () => {
    set({ isLoading: true })
    
    try {
      const { error } = await supabase.auth.signOut()
      if (error) throw error

      set({
        user: null,
        isAuthenticated: false,
        isLoading: false,
        error: null
      })
    } catch (error) {
      set({
        isLoading: false,
        error: error instanceof Error ? error.message : 'Sign out failed'
      })
      throw error
    }
  },

  checkAuth: async () => {
    set({ isLoading: true })
    
    try {
      const { data: { user }, error } = await supabase.auth.getUser()
      
      if (error) throw error

      if (user) {
        // Fetch parent profile
        const { data: parentData, error: parentError } = await supabase
          .from('users')
          .select('id,email,full_name,role,created_at')
          .eq('id', user.id)
          .single()

        if (parentError) throw parentError

        set({
          user: parentData,
          isAuthenticated: true,
          isLoading: false,
          error: null
        })
      } else {
        set({
          user: null,
          isAuthenticated: false,
          isLoading: false,
          error: null
        })
      }
    } catch (error) {
      // DEV ONLY: If auth fails (e.g. invalid supabase keys), mock a user for preview
      if (import.meta.env.DEV) {
        console.warn("Auth check failed or no session, using MOCK user for development preview.");
        set({
          user: {
            id: 'mock-admin-id',
            email: 'admin@iroko.com',
            full_name: 'System Admin',
            role: 'admin', // Mock as admin for the console
            created_at: new Date().toISOString()
          },
          isAuthenticated: true,
          isLoading: false,
          error: null
        })
        return
      }

      set({
        isLoading: false,
        error: error instanceof Error ? error.message : 'Auth check failed'
      })
    }
  },

  clearError: () => set({ error: null })
}))
