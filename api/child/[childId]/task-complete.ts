import { supabase } from '../../../backend-core/src/supabase/client'

export default async function handler(req: any, res: any) {
  if (req.method !== 'POST') return res.status(405).json({ error: 'method not allowed' })
  try {
    const childId = req.query.childId as string
    const deviceId = (req.headers['x-device-id'] as string) ?? ''
    if (!deviceId) return res.status(400).json({ error: 'device id required' })

    const { data: sessions, error: sessErr } = await supabase
      .from('child_sessions')
      .select('*')
      .eq('child_id', childId)
      .eq('is_active', true)
      .eq('revoked', false)
      .limit(1)

    if (sessErr) return res.status(500).json({ error: 'db error' })
    const s: any = sessions?.[0]
    const valid = s && s.device_id === deviceId && (!s.session_end || new Date(s.session_end) > new Date())
    if (!valid) return res.status(403).json({ error: 'invalid session/device' })

    const taskId = req.body?.task_id as string
    const proofUrl = req.body?.proof_url as string | undefined
    if (!taskId) return res.status(400).json({ error: 'task_id required' })

    const { error: insErr } = await supabase
      .from('task_completions')
      .insert({ task_id: taskId, child_id: childId, status: 'completed', proof_url: proofUrl })
    if (insErr) return res.status(500).json({ error: 'insert failed' })
    res.status(200).json({ ok: true })
  } catch (e) {
    res.status(500).json({ error: 'failed to complete task' })
  }
}

