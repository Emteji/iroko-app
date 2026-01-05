import { create } from 'zustand'

export interface AdminUser {
  id: string
  email: string
  role: 'parent' | 'child' | 'admin'
  status: 'active' | 'suspended' | 'pending'
  joinedAt: string
  familyId?: string
}

export interface FlaggedEvent {
  id: string
  userId: string
  severity: 'low' | 'medium' | 'high' | 'critical'
  description: string
  timestamp: string
  status: 'open' | 'resolved'
}

interface AdminState {
  users: AdminUser[]
  flaggedEvents: FlaggedEvent[]
  isLoading: boolean
  
  // Actions
  fetchUsers: () => Promise<void>
  suspendUser: (userId: string) => Promise<void>
  activateUser: (userId: string) => Promise<void>
  resolveEvent: (eventId: string) => void
}

// Mock data for "Fully Built" simulation until Supabase is populated
const MOCK_USERS: AdminUser[] = [
  { id: '1', email: 'parent.doe@example.com', role: 'parent', status: 'active', joinedAt: '2023-10-12', familyId: 'fam_1' },
  { id: '2', email: 'child.doe@example.com', role: 'child', status: 'active', joinedAt: '2023-10-12', familyId: 'fam_1' },
  { id: '3', email: 'bad.actor@example.com', role: 'parent', status: 'suspended', joinedAt: '2023-11-01', familyId: 'fam_2' },
  { id: '4', email: 'new.mom@example.com', role: 'parent', status: 'pending', joinedAt: '2024-01-04', familyId: 'fam_3' },
]

const MOCK_EVENTS: FlaggedEvent[] = [
  { id: 'e1', userId: '2', severity: 'medium', description: 'Attempted to access restricted site: tiktok.com', timestamp: '2024-01-04T10:00:00Z', status: 'open' },
  { id: 'e2', userId: '2', severity: 'low', description: 'Screen time limit exceeded override attempt', timestamp: '2024-01-04T14:30:00Z', status: 'open' },
]

export const useAdminStore = create<AdminState>((set) => ({
  users: [],
  flaggedEvents: [],
  isLoading: false,

  fetchUsers: async () => {
    set({ isLoading: true })
    // Simulate API call
    setTimeout(() => {
      set({ users: MOCK_USERS, flaggedEvents: MOCK_EVENTS, isLoading: false })
    }, 800)
  },

  suspendUser: async (userId) => {
    set(state => ({
      users: state.users.map(u => u.id === userId ? { ...u, status: 'suspended' } : u)
    }))
  },

  activateUser: async (userId) => {
    set(state => ({
      users: state.users.map(u => u.id === userId ? { ...u, status: 'active' } : u)
    }))
  },

  resolveEvent: (eventId) => {
    set(state => ({
      flaggedEvents: state.flaggedEvents.map(e => e.id === eventId ? { ...e, status: 'resolved' } : e)
    }))
  }
}))
