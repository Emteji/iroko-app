-- Enable UUID extension
create extension if not exists "uuid-ossp";

-- 1. USERS
create table public.users (
  id uuid references auth.users not null primary key,
  email text unique not null,
  role text not null check (role in ('parent', 'child', 'admin')),
  created_at timestamp with time zone default timezone('utc'::text, now()) not null
);

-- 2. CHILDREN
create table public.children (
  id uuid default uuid_generate_v4() primary key,
  user_id uuid references public.users(id), -- Optional link to auth user if child has login
  name text not null,
  dob date not null,
  age_band text not null,
  grit_score integer default 0,
  responsibility_score integer default 0,
  discipline_score integer default 0,
  created_at timestamp with time zone default timezone('utc'::text, now()) not null
);

-- 3. PARENT_CHILD_MAP
create table public.parent_child_map (
  parent_id uuid references public.users(id) not null,
  child_id uuid references public.children(id) not null,
  primary key (parent_id, child_id)
);

-- 4. VILLAGE_CONTEXT
create table public.village_context (
  id uuid default uuid_generate_v4() primary key,
  child_id uuid references public.children(id) not null,
  origin_region text,
  origin_state text,
  origin_town text,
  tribe text,
  primary_language text,
  secondary_languages text[],
  environment_type text check (environment_type in ('rural', 'urban', 'diaspora')),
  value_weights jsonb default '{}'::jsonb,
  risk_exposure jsonb default '{}'::jsonb,
  learning_bias jsonb default '{}'::jsonb,
  created_at timestamp with time zone default timezone('utc'::text, now()) not null
);

-- 5. TASKS
create table public.tasks (
  id uuid default uuid_generate_v4() primary key,
  child_id uuid references public.children(id) not null,
  parent_id uuid references public.users(id), -- AI generated tasks might have null parent initially or linked to system
  category text not null,
  instruction text not null,
  difficulty text not null,
  time_window jsonb, -- e.g. {"start": "08:00", "end": "10:00"}
  created_at timestamp with time zone default timezone('utc'::text, now()) not null
);

-- 6. TASK_COMPLETIONS
create table public.task_completions (
  id uuid default uuid_generate_v4() primary key,
  task_id uuid references public.tasks(id) not null,
  child_id uuid references public.children(id) not null,
  status text not null check (status in ('pending', 'submitted', 'approved', 'rejected')),
  proof text, -- URL to image/video or text response
  completed_at timestamp with time zone default timezone('utc'::text, now()) not null
);

-- 7. MISSIONS
create table public.missions (
  id uuid default uuid_generate_v4() primary key,
  child_id uuid references public.children(id) not null,
  context_tags text[],
  objective text not null,
  difficulty text not null,
  created_at timestamp with time zone default timezone('utc'::text, now()) not null
);

-- 8. MISSION_ATTEMPTS
create table public.mission_attempts (
  id uuid default uuid_generate_v4() primary key,
  mission_id uuid references public.missions(id) not null,
  child_id uuid references public.children(id) not null,
  response text,
  created_at timestamp with time zone default timezone('utc'::text, now()) not null
);

-- 9. BEHAVIOR_LOGS
create table public.behavior_logs (
  id uuid default uuid_generate_v4() primary key,
  child_id uuid references public.children(id) not null,
  type text not null,
  details jsonb,
  created_at timestamp with time zone default timezone('utc'::text, now()) not null
);

-- 10. REWARDS_WALLET
create table public.rewards_wallet (
  id uuid default uuid_generate_v4() primary key,
  child_id uuid references public.children(id) unique not null,
  points_balance integer default 0
);

-- 11. REWARD_TRANSACTIONS
create table public.reward_transactions (
  id uuid default uuid_generate_v4() primary key,
  child_id uuid references public.children(id) not null,
  parent_id uuid references public.users(id),
  type text not null check (type in ('earn', 'spend')),
  points integer not null,
  status text not null check (status in ('pending', 'approved', 'rejected')),
  created_at timestamp with time zone default timezone('utc'::text, now()) not null
);

-- 12. DEVICE_POLICIES
create table public.device_policies (
  id uuid default uuid_generate_v4() primary key,
  child_id uuid references public.children(id) not null,
  app_rules jsonb default '[]'::jsonb,
  browser_rules jsonb default '[]'::jsonb,
  screen_time_rules jsonb default '{}'::jsonb,
  created_at timestamp with time zone default timezone('utc'::text, now()) not null
);

-- 13. RESTRICTION_EVENTS
create table public.restriction_events (
  id uuid default uuid_generate_v4() primary key,
  child_id uuid references public.children(id) not null,
  trigger text not null,
  action text not null,
  created_at timestamp with time zone default timezone('utc'::text, now()) not null
);

-- 14. AI_REPORTS
create table public.ai_reports (
  id uuid default uuid_generate_v4() primary key,
  child_id uuid references public.children(id) not null,
  summary text,
  risk_score integer,
  created_at timestamp with time zone default timezone('utc'::text, now()) not null
);

-- 15. SUBSCRIPTIONS
create table public.subscriptions (
  id uuid default uuid_generate_v4() primary key,
  user_id uuid references public.users(id) not null,
  plan text not null check (plan in ('free', 'standard', 'premium')),
  status text not null,
  billing_provider text,
  created_at timestamp with time zone default timezone('utc'::text, now()) not null
);

-- RLS POLICIES (Simplified for initial setup, strict in production)
alter table public.users enable row level security;
alter table public.children enable row level security;
alter table public.parent_child_map enable row level security;
-- ... enable for all

-- Policy: Parents can see their own data
create policy "Users can view own data" on public.users for select using (auth.uid() = id);

-- Policy: Parents can see children they are mapped to
create policy "Parents can view mapped children" on public.children for select using (
  exists (
    select 1 from public.parent_child_map 
    where parent_child_map.child_id = children.id 
    and parent_child_map.parent_id = auth.uid()
  )
);

-- Policy: Admins can view all (requires admin role check function or claim)
-- For now, assuming admin role is stored in users table metadata or via claim
