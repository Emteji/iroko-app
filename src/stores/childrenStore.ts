import { create } from 'zustand'
import { supabase } from '@/lib/supabase'
import { Child, ChildWithStats, Task, ChildSession } from '@/types/database'

interface ChildrenState {
  children: ChildWithStats[]
  isLoading: boolean
  error: string | null
  
  // Actions
  fetchChildren: () => Promise<void>
  createChild: (childData: Omit<Child, 'id' | 'created_at' | 'updated_at'>) => Promise<void>
  updateChild: (id: string, updates: Partial<Child>) => Promise<void>
  deleteChild: (id: string) => Promise<void>
  clearError: () => void
}

export const useChildrenStore = create<ChildrenState>((set, get) => ({
  children: [],
  isLoading: false,
  error: null,

  fetchChildren: async () => {
    set({ isLoading: true, error: null })
    
    try {
      const { data: { user } } = await supabase.auth.getUser()
      if (!user) throw new Error('Not authenticated')

      // Fetch children with stats
      const { data: childrenData, error: childrenError } = await supabase
        .from('children')
        .select(`
          id,
          name,
          dob,
          created_at,
          link:parent_child_map!inner(parent_id,child_id),
          tasks:tasks(count),
          completed_tasks:task_completions(count),
          active_session:child_sessions!inner(is_active)
        `)
        .eq('link.parent_id', user.id)
        .eq('active_session.is_active', true)

      if (childrenError) throw childrenError

      // Transform data to match our types
      const transformedChildren = childrenData.map((child: any) => ({
        ...child,
        task_count: child.tasks?.[0]?.count || 0,
        completed_tasks_today: child.completed_tasks?.[0]?.count || 0,
        pending_tasks_today: 0,
        current_session: child.active_session?.[0] || undefined
      }))

      set({
        children: transformedChildren,
        isLoading: false,
        error: null
      })
    } catch (error) {
      set({
        isLoading: false,
        error: error instanceof Error ? error.message : 'Failed to fetch children'
      })
    }
  },

  createChild: async (childData) => {
    set({ isLoading: true, error: null })
    
    try {
      const { data: { user } } = await supabase.auth.getUser()
      if (!user) throw new Error('Not authenticated')

      const { data, error } = await supabase
        .from('children')
        .insert([{
          ...childData,
          is_active: true
        }])
        .select()
        .single()

      if (error) throw error

      // Add to local state
      const newChild: ChildWithStats = {
        ...data,
        task_count: 0,
        completed_tasks_today: 0,
        pending_tasks_today: 0
      }

      set(state => ({
        children: [...state.children, newChild],
        isLoading: false,
        error: null
      }))
    } catch (error) {
      set({
        isLoading: false,
        error: error instanceof Error ? error.message : 'Failed to create child'
      })
      throw error
    }
  },

  updateChild: async (id, updates) => {
    set({ isLoading: true, error: null })
    
    try {
      const { data, error } = await supabase
        .from('children')
        .update(updates)
        .eq('id', id)
        .select()
        .single()

      if (error) throw error

      // Update local state
      set(state => ({
        children: state.children.map(child => 
          child.id === id ? { ...child, ...data } : child
        ),
        isLoading: false,
        error: null
      }))
    } catch (error) {
      set({
        isLoading: false,
        error: error instanceof Error ? error.message : 'Failed to update child'
      })
      throw error
    }
  },

  deleteChild: async (id) => {
    set({ isLoading: true, error: null })
    
    try {
      const { error } = await supabase
        .from('children')
        .delete()
        .eq('id', id)

      if (error) throw error

      // Remove from local state
      set(state => ({
        children: state.children.filter(child => child.id !== id),
        isLoading: false,
        error: null
      }))
    } catch (error) {
      set({
        isLoading: false,
        error: error instanceof Error ? error.message : 'Failed to delete child'
      })
      throw error
    }
  },

  clearError: () => set({ error: null })
}))
