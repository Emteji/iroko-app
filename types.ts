
// Core Domain Models for IROKO

export type SubscriptionTier = 'Villager' | 'Chief';
export type AgeCategory = '3-6' | '7-12' | '13-17';
export type MissionCategory = 'grit' | 'money' | 'respect' | 'street-smarts';
export type LocationContext = 'home' | 'diaspora'; // 'home' = Nigeria, 'diaspora' = Abroad

export const CATEGORIES = [
  { id: 'grit', label: 'Grit & Hustle', color: '#C27856' },
  { id: 'money', label: 'Money Sense', color: '#3A5F45' },
  { id: 'respect', label: 'Respect & Culture', color: '#8B5A2B' },
  { id: 'street-smarts', label: 'Street Smarts', color: '#2C241B' },
] as const;

export interface ParentProfile {
  id: string;
  name: string;
  email: string;
  subscriptionTier: SubscriptionTier;
  locationContext: LocationContext; // New field
  childrenIds: string[];
  createdAt: number;
}

export interface ChildProfile {
  id: string;
  parentId: string;
  name: string;
  age: number;
  gritPoints: number;
  omoluabiPoints: number; // Character/Integrity points
  currentLevel: string; // e.g., 'Novice', 'Apprentice', 'Giant'
  avatarUrl?: string;
}

export interface Mission {
  id: string;
  title: string;
  description: string;
  category: MissionCategory;
  ageCategory: AgeCategory;
  xpReward: number;
  steps: string[];
  practicalTip: string; // The "Wisdom Drop"
  isAiGenerated: boolean;
  createdAt: number;
  
  // Optional fields for UI compatibility
  duration?: string;
  storyOrProverb?: string;
}

export interface ProgressLog {
  id: string;
  childId: string;
  missionId: string;
  status: 'pending' | 'completed';
  proofUrl?: string; // Image URL for evidence
  parentVerified: boolean; // The checkmark
  completedAt: number; // Timestamp
}

export interface Reward {
  id: string;
  parentId: string;
  title: string;
  cost: number;
  emoji: string;
  createdAt: number;
}

// --- Admin & Payment Types ---

export interface Transaction {
  id: string;
  parentId: string;
  parentName: string;
  amount: number;
  currency: 'NGN' | 'USD';
  status: 'success' | 'failed' | 'pending';
  date: number;
  reference: string;
}

export interface PaymentConfig {
  provider: 'paystack';
  publicKey: string;
  secretKey: string; // In a real app, never send secret key to frontend. This is for the "Admin Input" demo.
  isLiveMode: boolean;
  isEnabled: boolean;
}

export interface AdminStats {
  totalParents: number;
  totalChildren: number;
  totalRevenue: number;
  activeMissions: number;
}