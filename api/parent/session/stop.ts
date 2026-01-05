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
    if (!childId) return res.status(400).json({ error: 'child_id required' })

    const { data: rel } = await supabase
      .from('parent_child_map')
      .select('parent_id')
      .eq('parent_id', userId)
      .eq('child_id', childId)
      .limit(1)
    if (!rel || !rel[0]) return res.status(403).json({ error: 'not linked to child' })

    const { error: updErr } = await supabase
      .from('child_sessions')
      .update({ is_active: false, session_end: new Date().toISOString() })
      .eq('child_id', childId)
      .eq('is_active', true)
      .eq('revoked', false)
    if (updErr) return res.status(500).json({ error: 'failed to stop session' })
    res.status(200).json({ ok: true })
  } catch (e) {
    res.status(500).json({ error: 'server error' })
  }
}
