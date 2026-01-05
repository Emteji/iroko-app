
export default async function handler(req: any, res: any) {
  if (req.method !== 'POST') return res.status(405).json({ error: 'method not allowed' })
  try {
    const result = { text: 'Observations processed', risk: 0 }
    res.status(200).json(result)
  } catch (e) {
    res.status(500).json({ error: 'failed to generate report' })
  }
}
