import express from 'express'
import generateDailyTasks from '../ai/generate.js'
import getGuidance from '../ai/guidance.js'
import { GoogleGenerativeAI } from '@google/generative-ai'

const router = express.Router()
const genAI = new GoogleGenerativeAI(process.env.GEMINI_API_KEY || '')

router.post('/tasks/generate', generateDailyTasks)
router.post('/guidance', getGuidance)

router.post('/chat', async (req, res) => {
  try {
      const { message, childName, context } = req.body
      const model = genAI.getGenerativeModel({ model: 'gemini-pro' })
      const prompt = `
        You are the Voice of the Village, a wise, calm, and encouraging AI guardian for a child named ${childName}.
        Context: ${context || 'General conversation'}
        Child says: "${message}"
        
        Respond in 1-2 sentences. Be kind, authoritative but warm. Use metaphors from nature (trees, rivers, sun).
      `
      const result = await model.generateContent(prompt)
      const text = result.response.text()
      res.json({ text })
  } catch (e: any) {
      console.error("Chat Error", e)
      res.status(500).json({ error: 'failed to generate response', text: "The wind is silent right now. Try again later." })
  }
})

export default router
