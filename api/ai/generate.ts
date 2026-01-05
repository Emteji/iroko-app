import { GoogleGenerativeAI } from '@google/generative-ai'
import { SYSTEM_PROMPTS } from './prompts.js'

const genAI = new GoogleGenerativeAI(process.env.GEMINI_API_KEY || '')

export default async function generateDailyTasks(req: any, res: any) {
  if (req.method !== 'POST') return res.status(405).json({ error: 'Method not allowed' })
  
  const { childProfile, villageContext, behaviorLog } = req.body
  
  if (!childProfile) return res.status(400).json({ error: 'Missing child profile' })

  try {
    const model = genAI.getGenerativeModel({ model: 'gemini-pro' })
    
    let prompt = SYSTEM_PROMPTS.TASK_GENERATION
      .replace('{{age_band}}', childProfile.age_band)
      .replace('{{grit_score}}', childProfile.grit_score)
      .replace('{{discipline_score}}', childProfile.discipline_score)
      .replace('{{village_context}}', JSON.stringify(villageContext || {}))
      .replace('{{behavior_log}}', JSON.stringify(behaviorLog || []))

    const result = await model.generateContent(prompt)
    const response = result.response
    const text = response.text()
    
    // Attempt to parse JSON
    try {
      // Clean markdown code blocks if present
      const jsonStr = text.replace(/```json/g, '').replace(/```/g, '').trim()
      const tasks = JSON.parse(jsonStr)
      return res.status(200).json({ tasks })
    } catch (e) {
      console.error("Failed to parse AI JSON", text)
      return res.status(200).json({ text, error: "Raw text returned, JSON parse failed" })
    }

  } catch (e: any) {
    console.error(e)
    return res.status(500).json({ error: e.message || 'AI Generation Failed' })
  }
}
