import { supabase } from '../../lib/supabase'

export default async function handler(req: any, res: any) {
  if (req.method !== 'POST') return res.status(405).json({ error: 'method not allowed' })
  try {
    const auth = req.headers['authorization'] as string | undefined
    const token = auth?.startsWith('Bearer ') ? auth.slice(7) : undefined
    if (!token) return res.status(401).json({ error: 'missing auth' })

    const user = await supabase.auth.getUser(token)
    const userId = (user as any)?.data?.user?.id
    if (!userId) return res.status(401).json({ error: 'invalid token' })

    const childId = req.body?.child_id as string
    const deviceId = req.body?.device_id as string
    const sessionEnd = req.body?.session_end as string | undefined
    if (!childId || !deviceId) return res.status(400).json({ error: 'child_id and device_id required' })

    const { data: rel } = await supabase
      .from('parent_child_map')
      .select('parent_id')
      .eq('parent_id', userId)
      .eq('child_id', childId)
      .limit(1)
    if (!rel || !rel[0]) return res.status(403).json({ error: 'not linked to child' })

    const { data: active } = await supabase
      .from('child_sessions')
      .select('id')
      .eq('child_id', childId)
      .eq('is_active', true)
      .eq('revoked', false)
      .limit(1)
    if (active && active[0]) return res.status(409).json({ error: 'active session exists' })

    const { error: insErr } = await supabase
      .from('child_sessions')
      .insert({ child_id: childId, device_id: deviceId, is_active: true, revoked: false, session_end: sessionEnd ?? null })
    if (insErr) return res.status(500).json({ error: 'failed to start session' })
    res.status(200).json({ ok: true })
  } catch (e) {
    res.status(500).json({ error: 'server error' })
  }
}
