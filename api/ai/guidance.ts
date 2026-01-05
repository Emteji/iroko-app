import { GoogleGenerativeAI } from '@google/generative-ai'
import { SYSTEM_PROMPTS } from './prompts.js'

const genAI = new GoogleGenerativeAI(process.env.GEMINI_API_KEY || '')

export default async function getGuidance(req: any, res: any) {
  if (req.method !== 'POST') return res.status(405).json({ error: 'Method not allowed' })
  
  const { taskName, childProfile } = req.body
  
  try {
    const model = genAI.getGenerativeModel({ model: 'gemini-pro' })
    
    let prompt = SYSTEM_PROMPTS.GUIDANCE_VOICE
      .replace('{{task_name}}', taskName || 'current activity')

    const result = await model.generateContent(prompt)
    const response = result.response
    const text = response.text()
    
    return res.status(200).json({ guidance: text })

  } catch (e: any) {
    console.error(e)
    return res.status(500).json({ error: e.message || 'AI Guidance Failed' })
  }
}
