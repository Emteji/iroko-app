export interface User {
  id: string
  email: string
  full_name: string
  role: 'parent' | 'child' | 'admin'
  created_at: string
}

export interface Child {
  id: string
  user_id: string
  name: string
  date_of_birth?: string
  avatar_url?: string
  is_active: boolean
  created_at: string
  updated_at: string
}

export interface Task {
  id: string
  child_id: string
  title: string
  category: string
  description?: string
  scheduled_time?: string
  reward_points: number
  recurring_pattern: 'none' | 'daily' | 'weekly' | 'monthly'
  is_active: boolean
  created_at: string
  updated_at: string
}

export interface TaskCompletion {
  id: string
  task_id: string
  child_id: string
  completed_at: string
  metadata?: Record<string, any>
}

export interface ChildSession {
  id: string
  child_id: string
  device_id: string
  device_name?: string
  device_ip?: string
  started_at: string
  expires_at: string
  is_active: boolean
  created_at: string
}

export interface BehaviorLog {
  id: string
  child_id: string
  event_type: string
  metadata?: Record<string, any>
  created_at: string
}

export interface RewardWallet {
  id: string
  child_id: string
  balance: number
  total_earned: number
  total_spent: number
  created_at: string
  updated_at: string
}

export interface RewardTransaction {
  id: string
  wallet_id: string
  child_id: string
  amount: number
  type: 'earned' | 'spent'
  description: string
  created_at: string
}

export type TaskCategory = 
  | 'chores'
  | 'education' 
  | 'hygiene'
  | 'exercise'
  | 'social'
  | 'creative'
  | 'other'

export interface TaskWithChild extends Task {
  child: Child
}

export interface ChildWithStats extends Child {
  task_count: number
  completed_tasks_today: number
  pending_tasks_today: number
  current_session?: ChildSession
}
