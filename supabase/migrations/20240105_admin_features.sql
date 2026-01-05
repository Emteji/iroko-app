-- 1. MOODS Table (for Child App Emotional Check-ins)
create table if not exists public.moods (
  id uuid default uuid_generate_v4() primary key,
  child_id uuid references public.children(id) not null,
  mood_label text not null, -- e.g. 'Happy', 'Sad', 'Angry'
  note text,
  created_at timestamp with time zone default timezone('utc'::text, now()) not null
);

-- 2. MISSION TEMPLATES (for Admin Content Management)
create table if not exists public.mission_templates (
  id uuid default uuid_generate_v4() primary key,
  title text not null,
  description text,
  type text not null check (type in ('Routine', 'Story', 'Challenge')),
  xp_reward integer default 10,
  min_age integer,
  is_active boolean default false,
  created_by uuid references public.users(id),
  created_at timestamp with time zone default timezone('utc'::text, now()) not null
);

-- 3. RLS Policies for Admin Access

-- Function to check if user is admin
create or replace function public.is_admin()
returns boolean as $$
begin
  return exists (
    select 1 from public.users
    where id = auth.uid()
    and role = 'admin'
  );
end;
$$ language plpgsql security definer;

-- Enable RLS
alter table public.moods enable row level security;
alter table public.mission_templates enable row level security;

-- Moods Policies
create policy "Children can insert own moods" on public.moods for insert with check (
  exists (
    select 1 from public.children where id = child_id
    -- In a real app, we'd check if the auth.uid() matches the child's linked user_id or device_id
  )
);

create policy "Parents can view their children's moods" on public.moods for select using (
  exists (
    select 1 from public.parent_child_map
    where parent_child_map.child_id = moods.child_id
    and parent_child_map.parent_id = auth.uid()
  )
);

create policy "Admins can view all moods" on public.moods for select using (is_admin());

-- Mission Templates Policies
create policy "Admins can manage templates" on public.mission_templates for all using (is_admin());
create policy "Everyone can view active templates" on public.mission_templates for select using (is_active = true);
