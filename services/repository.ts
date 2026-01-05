import { supabase, isSupabaseConfigured } from '../lib/supabaseClient';
import { ParentProfile, ChildProfile, Mission, ProgressLog, AgeCategory, CATEGORIES, Reward, LocationContext, AdminStats, Transaction, PaymentConfig } from '../types';

/**
 * IrokoRepository
 * Handles data fetching with graceful fallback to In-Memory Mocks if Supabase is not connected.
 */

class IrokoRepository {
  private static instance: IrokoRepository;

  // --- Mock Data Store (Fallback) ---
  private mockParents: Map<string, ParentProfile> = new Map();
  private mockChildren: Map<string, ChildProfile> = new Map();
  private mockMissions: Map<string, Mission> = new Map();
  private mockLogs: Map<string, ProgressLog> = new Map();
  private mockRewards: Map<string, Reward> = new Map();
  
  // Admin Mock Data
  private mockTransactions: Transaction[] = [];
  private paymentConfig: PaymentConfig = {
    provider: 'paystack',
    publicKey: '',
    secretKey: '',
    isLiveMode: false,
    isEnabled: false
  };

  private constructor() {
    if (!isSupabaseConfigured) {
      console.warn("⚠️ Supabase not configured. Using Mock Data Mode.");
      this.seedMockData();
    }
    // Load payment config from local storage for persistence in demo
    const savedConfig = localStorage.getItem('iroko_payment_config');
    if (savedConfig) {
      this.paymentConfig = JSON.parse(savedConfig);
    }
  }

  public static getInstance(): IrokoRepository {
    if (!IrokoRepository.instance) {
      IrokoRepository.instance = new IrokoRepository();
    }
    return IrokoRepository.instance;
  }

  // Helper for generating Black avatars
  private generateAvatarUrl(name: string): string {
    // SkinColor 614335 and ae5d29 are dark brown shades supported by DiceBear
    return `https://api.dicebear.com/7.x/avataaars/svg?seed=${name}&skinColor=614335,ae5d29`;
  }

  // --- Auth & User (Parent) Methods ---

  async getCurrentUser(): Promise<ParentProfile | null> {
    if (!isSupabaseConfigured) {
      return this.mockParents.get('user_123') || null;
    }
    const { data: { user } } = await supabase.auth.getUser();
    if (!user) return null;
    return this.getParent(user.id);
  }

  async getParent(id: string): Promise<ParentProfile | null> {
    if (!isSupabaseConfigured) return this.mockParents.get(id) || null;

    const { data, error } = await supabase
      .from('profiles')
      .select('*')
      .eq('id', id)
      .single();

    if (error) {
      // Create profile if missing (resilience)
      if (error.code === 'PGRST116') {
         return null; 
      }
      console.error('Error fetching parent:', error.message || error);
      return null;
    }

    if (!data) return null;

    return {
      id: data.id,
      name: data.name,
      email: data.email,
      subscriptionTier: data.subscription_tier,
      locationContext: data.location_context || 'home', // Default to home if missing
      childrenIds: [],
      createdAt: new Date(data.created_at).getTime()
    };
  }

  async createParent(parent: ParentProfile): Promise<void> {
    if (!isSupabaseConfigured) {
      this.mockParents.set(parent.id, parent);
      return;
    }
    await supabase.from('profiles').upsert({
      id: parent.id,
      name: parent.name,
      email: parent.email,
      subscription_tier: parent.subscriptionTier,
      location_context: parent.locationContext
    });
  }

  async updateParentProfile(id: string, updates: Partial<ParentProfile>): Promise<void> {
     if (!isSupabaseConfigured) {
        const p = this.mockParents.get(id);
        if(p) this.mockParents.set(id, { ...p, ...updates });
        return;
     }
     
     const dbUpdates: any = {};
     if (updates.name) dbUpdates.name = updates.name;
     if (updates.locationContext) dbUpdates.location_context = updates.locationContext;

     await supabase.from('profiles').update(dbUpdates).eq('id', id);
  }

  // --- Child Methods ---

  async getChildren(parentId: string): Promise<ChildProfile[]> {
    if (!isSupabaseConfigured) {
      return Array.from(this.mockChildren.values()).filter(c => c.parentId === parentId);
    }

    const { data, error } = await supabase
      .from('children')
      .select('*')
      .eq('parent_id', parentId);

    if (error || !data) return [];

    return data.map((c: any) => ({
      id: c.id,
      parentId: c.parent_id,
      name: c.name,
      age: c.age,
      gritPoints: c.grit_points,
      omoluabiPoints: c.omoluabi_points,
      currentLevel: c.current_level,
      avatarUrl: c.avatar_url
    }));
  }

  async createChild(parentId: string, name: string, age: number): Promise<ChildProfile | null> {
    const avatarUrl = this.generateAvatarUrl(name);

    if (!isSupabaseConfigured) {
      const newChild: ChildProfile = {
        id: crypto.randomUUID(),
        parentId,
        name,
        age,
        gritPoints: 0,
        omoluabiPoints: 0,
        currentLevel: 'Novice',
        avatarUrl: avatarUrl
      };
      this.mockChildren.set(newChild.id, newChild);
      return newChild;
    }

    const { data, error } = await supabase
      .from('children')
      .insert({
        parent_id: parentId,
        name,
        age,
        avatar_url: avatarUrl
      })
      .select()
      .single();

    if(error) {
      console.error("Error creating child", error.message || error);
      return null;
    }

    return {
       id: data.id,
      parentId: data.parent_id,
      name: data.name,
      age: data.age,
      gritPoints: data.grit_points,
      omoluabiPoints: data.omoluabi_points,
      currentLevel: data.current_level,
      avatarUrl: data.avatar_url
    }
  }

  async deleteChild(childId: string): Promise<void> {
    if (!isSupabaseConfigured) {
       this.mockChildren.delete(childId);
       return;
    }
    await supabase.from('children').delete().eq('id', childId);
  }

  async updateChildPoints(childId: string, grit: number, omoluabi: number): Promise<void> {
    if (!isSupabaseConfigured) {
       const child = this.mockChildren.get(childId);
       if(child) {
          child.gritPoints += grit;
          child.omoluabiPoints += omoluabi;
          if (child.gritPoints > 1000) child.currentLevel = "Odogwu (Giant)";
          else if (child.gritPoints > 500) child.currentLevel = "Warrior";
          else if (child.gritPoints > 200) child.currentLevel = "Apprentice";
          this.mockChildren.set(childId, child);
       }
       return;
    }

    const { data: child } = await supabase.from('children').select('grit_points, omoluabi_points').eq('id', childId).single();
    if (!child) return;

    const newGrit = (child.grit_points || 0) + grit;
    const newOmoluabi = (child.omoluabi_points || 0) + omoluabi;
    
    let currentLevel = "Novice";
    if (newGrit > 1000) currentLevel = "Odogwu (Giant)";
    else if (newGrit > 500) currentLevel = "Warrior";
    else if (newGrit > 200) currentLevel = "Apprentice";

    await supabase.from('children').update({
      grit_points: newGrit,
      omoluabi_points: newOmoluabi,
      current_level: currentLevel
    }).eq('id', childId);
  }

  async upgradeSubscription(parentId: string): Promise<void> {
    if (!isSupabaseConfigured) {
      const p = this.mockParents.get(parentId);
      if(p) {
        p.subscriptionTier = 'Chief';
        this.mockParents.set(parentId, p);
        
        // Log transaction for Admin
        this.mockTransactions.unshift({
           id: `txn_${Date.now()}`,
           parentId: p.id,
           parentName: p.name,
           amount: 24000,
           currency: 'NGN',
           status: 'success',
           date: Date.now(),
           reference: `REF-${Math.floor(Math.random() * 1000000)}`
        });
      }
      return;
    }
    await supabase.from('profiles').update({ subscription_tier: 'Chief' }).eq('id', parentId);
  }

  // --- Mission Repository Methods ---

  async getMissionsByAge(ageCategory: AgeCategory): Promise<Mission[]> {
    if (!isSupabaseConfigured) {
      return Array.from(this.mockMissions.values()).filter(m => m.ageCategory === ageCategory);
    }

    const { data, error } = await supabase.from('missions').select('*').eq('age_category', ageCategory);
    if (error || !data) return [];

    return data.map((m: any) => ({
      id: m.id,
      title: m.title,
      description: m.description,
      category: m.category,
      ageCategory: m.age_category,
      xpReward: m.xp_reward,
      steps: m.steps,
      practicalTip: m.practical_tip,
      isAiGenerated: m.is_ai_generated,
      createdAt: new Date(m.created_at).getTime(),
      duration: m.duration,
      storyOrProverb: m.story_or_proverb
    }));
  }

  async getRecommendedMissions(childId: string): Promise<Mission[]> {
    let child;
    
    if (!isSupabaseConfigured) {
       child = this.mockChildren.get(childId);
    } else {
       const cRes = await supabase.from('children').select('*').eq('id', childId).single();
       child = cRes.data;
       
       await this.checkAndSeedMissions();
    }
    
    if (!child) return [];

    let targetAgeCat: AgeCategory = '7-12';
    if (child.age <= 6) targetAgeCat = '3-6';
    else if (child.age >= 13) targetAgeCat = '13-17';

    const missions = await this.getMissionsByAge(targetAgeCat);

    // Get both pending and completed logs
    const progress = await this.getChildProgress(childId);
    
    // We filter out anything that is pending OR completed
    const activeIds = new Set(progress.filter(p => p.status === 'completed' || p.status === 'pending').map(p => p.missionId));
    
    const activeMissions = missions.filter(m => !activeIds.has(m.id));

    return activeMissions;
  }

  // --- Approval Workflow Methods ---

  async getPendingApprovals(parentId: string): Promise<(ProgressLog & { missionTitle: string, childName: string, xpReward: number, avatarUrl: string })[]> {
     const children = await this.getChildren(parentId);
     const childIds = children.map(c => c.id);
     
     if (childIds.length === 0) return [];

     let logs: ProgressLog[] = [];
     if (!isSupabaseConfigured) {
        logs = Array.from(this.mockLogs.values()).filter(l => childIds.includes(l.childId) && l.status === 'pending');
     } else {
        const { data } = await supabase.from('progress_logs').select('*').in('child_id', childIds).eq('status', 'pending');
        if (data) {
           logs = data.map((l: any) => ({
             id: l.id,
             childId: l.child_id,
             missionId: l.mission_id,
             status: l.status,
             proofUrl: l.proof_url,
             parentVerified: l.parent_verified,
             completedAt: new Date(l.completed_at).getTime()
           }));
        }
     }

     // Enrich Data
     const enrichedLogs = [];
     for (const log of logs) {
        const child = children.find(c => c.id === log.childId);
        
        let mission: Mission | undefined;
        if (!isSupabaseConfigured) {
           mission = this.mockMissions.get(log.missionId);
        } else {
           const { data } = await supabase.from('missions').select('*').eq('id', log.missionId).single();
           if(data) mission = {
              id: data.id, title: data.title, description: data.description, category: data.category,
              ageCategory: data.age_category, xpReward: data.xp_reward, steps: data.steps,
              practicalTip: data.practical_tip, isAiGenerated: data.is_ai_generated, createdAt: 0
           };
        }

        if (child && mission) {
           enrichedLogs.push({
              ...log,
              childName: child.name,
              avatarUrl: child.avatarUrl || '',
              missionTitle: mission.title,
              xpReward: mission.xpReward
           });
        }
     }

     return enrichedLogs;
  }

  async submitMission(childId: string, missionId: string, proof?: string): Promise<void> {
    if (!isSupabaseConfigured) {
       const log: ProgressLog = {
         id: crypto.randomUUID(),
         childId, missionId, status: 'pending', parentVerified: false, proofUrl: proof, completedAt: Date.now()
       };
       this.mockLogs.set(log.id, log);
       return;
    }

    const { error } = await supabase.from('progress_logs').insert({
      child_id: childId,
      mission_id: missionId,
      status: 'pending',
      parent_verified: false,
      proof_url: proof,
      completed_at: new Date().toISOString()
    });

    if (error) console.error("Submit error", error);
  }

  async approveMission(logId: string): Promise<void> {
     let log: ProgressLog | undefined;
     let mission: Mission | undefined;

     // 1. Fetch Log Details
     if (!isSupabaseConfigured) {
        log = this.mockLogs.get(logId);
        if (log) {
           log.status = 'completed';
           log.parentVerified = true;
           this.mockLogs.set(logId, log);
           mission = this.mockMissions.get(log.missionId);
        }
     } else {
        const { data: logData } = await supabase.from('progress_logs').select('*').eq('id', logId).single();
        if (logData) {
            await supabase.from('progress_logs').update({ status: 'completed', parent_verified: true }).eq('id', logId);
            
            const { data: missionData } = await supabase.from('missions').select('*').eq('id', logData.mission_id).single();
            if(missionData) {
               mission = {
                  id: missionData.id, title: missionData.title, description: missionData.description,
                  category: missionData.category, ageCategory: missionData.age_category, xpReward: missionData.xp_reward,
                  steps: missionData.steps, practicalTip: missionData.practical_tip, isAiGenerated: false, createdAt: 0
               };
               log = { id: logId, childId: logData.child_id, missionId: logData.mission_id, status: 'completed', parentVerified: true, completedAt: 0 };
            }
        }
     }

     // 2. Award Points
     if (log && mission) {
        const gritEarned = mission.xpReward;
        let omoluabiEarned = 10;
        if (mission.category === 'respect') {
           omoluabiEarned += Math.floor(mission.xpReward * 0.5);
        }
        await this.updateChildPoints(log.childId, gritEarned, omoluabiEarned);
     }
  }

  async rejectMission(logId: string): Promise<void> {
     if (!isSupabaseConfigured) {
        this.mockLogs.delete(logId);
        return;
     }
     await supabase.from('progress_logs').delete().eq('id', logId);
  }

  async markMissionComplete(childId: string, missionId: string, proof?: string): Promise<void> {
     return this.submitMission(childId, missionId, proof);
  }

  async getLeaderboard(): Promise<ChildProfile[]> {
    if (!isSupabaseConfigured) {
      return Array.from(this.mockChildren.values())
        .sort((a, b) => (b.gritPoints + b.omoluabiPoints) - (a.gritPoints + a.omoluabiPoints))
        .slice(0, 5);
    }
    const { data, error } = await supabase
      .from('children')
      .select('*')
      .order('grit_points', { ascending: false })
      .limit(10);
    if (error || !data) return [];
    const mapped = data.map((c: any) => ({
      id: c.id,
      parentId: c.parent_id,
      name: c.name,
      age: c.age,
      gritPoints: c.grit_points,
      omoluabiPoints: c.omoluabi_points,
      currentLevel: c.current_level,
      avatarUrl: c.avatar_url
    }));
    return mapped.sort((a, b) => (b.gritPoints + b.omoluabiPoints) - (a.gritPoints + a.omoluabiPoints));
  }

  async getRewards(parentId: string): Promise<Reward[]> {
    if (!isSupabaseConfigured) {
      return Array.from(this.mockRewards.values()).filter(r => r.parentId === parentId);
    }
    const { data, error } = await supabase.from('rewards').select('*').eq('parent_id', parentId).order('cost', { ascending: true });
    if(error || !data) return [];

    return data.map((r: any) => ({
      id: r.id,
      parentId: r.parent_id,
      title: r.title,
      cost: r.cost,
      emoji: r.emoji,
      createdAt: new Date(r.created_at).getTime()
    }));
  }

  async createReward(reward: Omit<Reward, 'id'|'createdAt'>): Promise<void> {
    if(!isSupabaseConfigured) {
       const newR = { ...reward, id: crypto.randomUUID(), createdAt: Date.now() };
       this.mockRewards.set(newR.id, newR);
       return;
    }
    await supabase.from('rewards').insert({
       parent_id: reward.parentId,
       title: reward.title,
       cost: reward.cost,
       emoji: reward.emoji
    });
  }

  async redeemReward(childId: string, reward: Reward): Promise<boolean> {
     let child: ChildProfile | undefined;
     
     if (!isSupabaseConfigured) {
        child = this.mockChildren.get(childId);
        if (!child) return false;
        const total = child.gritPoints + child.omoluabiPoints;
        if (total < reward.cost) return false;
        const half = Math.floor(reward.cost / 2);
        const remainder = reward.cost - half;
        child.gritPoints = Math.max(0, child.gritPoints - half);
        child.omoluabiPoints = Math.max(0, child.omoluabiPoints - remainder);
        this.mockChildren.set(childId, child);
        return true;
     }

     const { data: cData } = await supabase.from('children').select('*').eq('id', childId).single();
     if(!cData) return false;
     
     const currentGrit = cData.grit_points;
     const currentOmoluabi = cData.omoluabi_points;
     const total = currentGrit + currentOmoluabi;

     if (total < reward.cost) return false;

     let takeGrit = Math.floor(reward.cost / 2);
     let takeOmoluabi = reward.cost - takeGrit;

     if (takeGrit > currentGrit) {
        const diff = takeGrit - currentGrit;
        takeGrit = currentGrit;
        takeOmoluabi += diff;
     } else if (takeOmoluabi > currentOmoluabi) {
        const diff = takeOmoluabi - currentOmoluabi;
        takeOmoluabi = currentOmoluabi;
        takeGrit += diff;
     }

     const { error: logError } = await supabase.from('redemptions').insert({
        child_id: childId,
        reward_id: reward.id,
        cost_at_time: reward.cost
     });

     if (logError) return false;

     await supabase.from('children').update({
        grit_points: currentGrit - takeGrit,
        omoluabi_points: currentOmoluabi - takeOmoluabi
     }).eq('id', childId);

     return true;
  }

  async getChildProgress(childId: string): Promise<ProgressLog[]> {
    if (!isSupabaseConfigured) {
      return Array.from(this.mockLogs.values()).filter(l => l.childId === childId);
    }
    
    const { data, error } = await supabase.from('progress_logs').select('*').eq('child_id', childId);
    if (error || !data) return [];

    return data.map((l: any) => ({
      id: l.id,
      childId: l.child_id,
      missionId: l.mission_id,
      status: l.status,
      proofUrl: l.proof_url,
      parentVerified: l.parent_verified,
      completedAt: new Date(l.completed_at).getTime()
    }));
  }

  async uploadProof(file: File): Promise<string | null> {
    if (!isSupabaseConfigured) return "https://via.placeholder.com/300";
    const fileExt = file.name.split('.').pop();
    const fileName = `${Math.random()}.${fileExt}`;
    const filePath = `${fileName}`;
    const { error: uploadError } = await supabase.storage.from('mission-proofs').upload(filePath, file);
    if (uploadError) {
      console.error("Upload error", uploadError.message || uploadError);
      return null;
    }
    const { data } = supabase.storage.from('mission-proofs').getPublicUrl(filePath);
    return data.publicUrl;
  }

  getDailyWisdom(): { proverb: string, origin: string } {
    const proverbs = [
        { proverb: "The river that forgets its source will dry up.", origin: "Yoruba Proverb" },
        { proverb: "If you want to go fast, go alone. If you want to go far, go together.", origin: "African Proverb" },
        { proverb: "A child who washes his hands clean eats with elders.", origin: "Igbo Proverb" },
        { proverb: "It takes a village to raise a child.", origin: "Igbo/Yoruba" },
        { proverb: "When the roots are deep, there is no reason to fear the wind.", origin: "African Proverb" },
        { proverb: "Knowledge is like a garden: if it is not cultivated, it cannot be harvested.", origin: "African Proverb" },
        { proverb: "One falsehood spoils a thousand truths.", origin: "Ashanti Proverb" }
    ];
    const day = new Date().getDate();
    return proverbs[day % proverbs.length];
  }

  // --- Admin & Payment Methods ---

  async getAdminStats(): Promise<AdminStats> {
     // In a real app, this would be a secure Edge Function call or a restricted View
     if (!isSupabaseConfigured) {
        return {
           totalParents: this.mockParents.size,
           totalChildren: this.mockChildren.size,
           totalRevenue: this.mockTransactions.reduce((acc, t) => acc + t.amount, 0),
           activeMissions: this.mockLogs.size
        };
     }
     
     // Simulated stats for Supabase mode (as we can't easily count all rows without admin key in client)
     return {
        totalParents: 120, // Mock
        totalChildren: 245, // Mock
        totalRevenue: 540000, // Mock NGN
        activeMissions: 890
     };
  }

  async getTransactions(): Promise<Transaction[]> {
     if (!isSupabaseConfigured) {
        return this.mockTransactions;
     }
     return []; // Real transaction fetching would require backend
  }

  async savePaymentConfig(config: PaymentConfig): Promise<void> {
     this.paymentConfig = config;
     // Persist to local storage for demo persistence
     localStorage.setItem('iroko_payment_config', JSON.stringify(config));
  }

  getPaymentConfig(): PaymentConfig {
     return this.paymentConfig;
  }

  private async checkAndSeedMissions() {
    if(!isSupabaseConfigured) return;
    try {
      const { count, error } = await supabase.from('missions').select('*', { count: 'exact', head: true });
      if (error) {
         console.warn("Could not check missions table for seeding:", error.message);
         return;
      }
      if (count === 0) {
        console.log("Seeding Database...");
        await supabase.from('missions').insert(this.getSeedMissions());
      }
    } catch (e) {
      console.error("Seeding error:", e);
    }
  }

  private seedMockData() {
    console.log("🌱 Seeding Mock Data");
    this.mockParents.set('user_123', {
      id: 'user_123',
      name: 'Demo Parent',
      email: 'demo@iroko.ng',
      subscriptionTier: 'Villager',
      locationContext: 'home',
      childrenIds: ['child_1'],
      createdAt: Date.now()
    });
    const demoAvatar = this.generateAvatarUrl('Tunde');
    this.mockChildren.set('child_1', {
      id: 'child_1',
      parentId: 'user_123',
      name: 'Tunde',
      age: 8,
      gritPoints: 120,
      omoluabiPoints: 95,
      currentLevel: 'Apprentice',
      avatarUrl: demoAvatar
    });
    
    // Seed transactions
    this.mockTransactions.push(
       { id: 'txn_1', parentId: 'user_123', parentName: 'Demo Parent', amount: 24000, currency: 'NGN', status: 'success', date: Date.now() - 100000, reference: 'REF-88392' },
       { id: 'txn_2', parentId: 'user_999', parentName: 'Chisom Okeke', amount: 24000, currency: 'NGN', status: 'success', date: Date.now() - 500000, reference: 'REF-11223' },
       { id: 'txn_3', parentId: 'user_888', parentName: 'Fatima Musa', amount: 2500, currency: 'NGN', status: 'failed', date: Date.now() - 900000, reference: 'REF-99112' }
    );

    const r1 = { id: 'r1', parentId: 'user_123', title: '1 Hour TV Time', cost: 150, emoji: '📺', createdAt: Date.now() };
    const r2 = { id: 'r2', parentId: 'user_123', title: 'Chicken Republic Meal', cost: 500, emoji: '🍗', createdAt: Date.now() };
    const r3 = { id: 'r3', parentId: 'user_123', title: 'New Football', cost: 1000, emoji: '⚽️', createdAt: Date.now() };
    this.mockRewards.set(r1.id, r1);
    this.mockRewards.set(r2.id, r2);
    this.mockRewards.set(r3.id, r3);
    const missions = this.getSeedMissions();
    missions.forEach((m: any) => {
       const mission = {
         ...m,
         ageCategory: m.age_category,
         xpReward: m.xp_reward,
         practicalTip: m.practical_tip,
         isAiGenerated: false,
         createdAt: Date.now()
       };
       this.mockMissions.set(mission.id, mission as Mission);
    });
  }

  private getSeedMissions() {
    return [
        {
          title: 'The Market Test',
          description: 'Go to the market with N1000. Buy ingredients for stew. Return with change.',
          category: 'money',
          age_category: '7-12',
          xp_reward: 100,
          steps: ['Collect N1000 from parent', 'Navigate to the market', 'Buy Pepper (N200), Onions (N200)', 'Bring back exact change'],
          practical_tip: 'A fool and his money are soon parted. Count your change before you leave the stall.',
          duration: '2 Hours'
        },
        {
          title: 'The No-Screen Sunday',
          description: '24 hours without iPad/Phone. Boredom breeds creativity.',
          category: 'grit',
          age_category: '7-12',
          xp_reward: 150,
          steps: ['Hand over devices at 8am', 'Find a book to read', 'Build something with hands'],
          practical_tip: 'Your mind is sharper when it is not being entertained by others.',
          duration: '24 Hours'
        },
        {
          title: 'The Greeting',
          description: 'Prostrate/Kneel or properly greet 3 elders today with eye contact.',
          category: 'respect',
          age_category: '7-12',
          xp_reward: 50,
          steps: ['Identify 3 elders', 'Greet properly (Good Morning Ma/Sir)', 'Wait for response before moving'],
          practical_tip: 'Charisma opens doors, but character keeps them open.',
          duration: '1 Day'
        },
        {
          title: 'The Savings Box',
          description: 'Decorate a physical "Kolo" (save box). Put N50 inside.',
          category: 'money',
          age_category: '7-12',
          xp_reward: 75,
          steps: ['Find an empty container', 'Seal it', 'Decorate it', 'Deposit first N50'],
          practical_tip: 'Small drops of water make a mighty ocean.',
          duration: '1 Hour'
        },
        {
          title: 'The Uncle Pitch',
          description: 'Ask an uncle for a favor, but offer value in return (e.g., wash his car).',
          category: 'street-smarts',
          age_category: '7-12',
          xp_reward: 200,
          steps: ['Identify a need the uncle has', 'Propose the trade', 'Execute the job well'],
          practical_tip: 'Never beg. Trade value for value.',
          duration: '3 Hours'
        }
      ];
  }
}

export const repository = IrokoRepository.getInstance();