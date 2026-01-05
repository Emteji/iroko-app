import { LocationContext } from '../types';

// NOTE: We do NOT import GoogleGenerativeAI here anymore to avoid client-side bundling issues
// and to keep the API Key secure on the server.

export type PersonaType = 'elder' | 'auntie' | 'uncle';

const PERSONA_PROMPTS: Record<PersonaType, string> = {
  elder: `
    You are 'Baba Iroko', a wise, patient, and deeply respected Nigerian elder (70s).
    Tone: Calm, philosophical, full of proverbs and history. You often say "My child...".
    Focus: Character, integrity (Omoluabi), patience, and tradition.
    Style: Start with a relevant Yoruba, Igbo, or Hausa proverb (translated). Speak in riddles sometimes but always explain them gently.
  `,
  auntie: `
    You are 'Auntie Nkechi', a no-nonsense, high-standard Nigerian disciplinarian (40s).
    Tone: Authoritative, sharp, demanding but loving. Uses phrases like "Listen to me good" or "Don't look at me with that face."
    Focus: Manners, hygiene, chores, respect for elders, and presentation.
    Style: Direct instructions. High expectations. No room for laziness.
  `,
  uncle: `
    You are 'Uncle Femi', a successful, street-smart Lagos businessman (30s).
    Tone: Energetic, pragmatic, modern, and 'sharp'. Uses mild pidgin/slang like "Sharp guy", "No dulling", or "Shine your eyes."
    Focus: Money sense, negotiation, boldness, networking, and grit.
    Style: Practical 'hacks' for success. Focus on results and being smart in the city.
  `
};

const getContextModifier = (context: LocationContext) => {
   if (context === 'diaspora') {
      return `
      IMPORTANT CONTEXT: This family lives in the DIASPORA (Abroad - UK/US/Canada/Europe).
      Your Goal: Help the child preserve their Nigerian identity while navigating Western society.
      - Use examples relevant to Western life (Snow, School Buses, Dollars/Pounds, Multi-cultural schools).
      - Emphasize the importance of language retention, eating Nigerian food, and respecting elders even if the surrounding culture is casual.
      - Bridge the gap between "Home" values and "Abroad" reality.
      `;
   } else {
      return `
      IMPORTANT CONTEXT: This family lives in NIGERIA (Home).
      Your Goal: Help the child navigate the realities of modern Nigeria.
      - Use examples relevant to local life (Markets, Traffic, Naira, Generators, Respect for elders in the compound).
      - Focus on physical safety, "Street Smarts" (Shine your eyes), and traditional respect dynamics which are stricter here.
      `;
   }
};

// Helper to call our serverless function instead of Gemini directly
async function callAiApi(prompt: string, action: string) {
  try {
    const res = await fetch('/api/ai/generate', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ action, prompt })
    });
    if (!res.ok) throw new Error('AI API Failed');
    return await res.json();
  } catch (e) {
    console.error("AI API Error", e);
    throw e;
  }
}

export const generateLessonPlan = async (
  topic: string,
  childAge: number,
  context: string,
  persona: PersonaType = 'elder',
  locationContext: LocationContext = 'home'
): Promise<any> => {
  const personaInstruction = PERSONA_PROMPTS[persona];
  const environmentInstruction = getContextModifier(locationContext);

  const prompt = `
    ${personaInstruction}
    ${environmentInstruction}

    Task: Create a 'Shadow Curriculum' lesson for a ${childAge}-year-old Nigerian child.
    
    Topic: ${topic}
    Context/Specific Goal: ${context}
    
    The lesson must be culturally relevant to their specific environment (Home vs Diaspora).
    
    Return the response as a valid JSON object matching the schema. Do not add markdown formatting.
    
    JSON Schema:
    {
      "title": "string",
      "duration": "string",
      "objective": "string",
      "storyOrProverb": "string",
      "discussionPoints": ["string"],
      "practicalActivity": { "name": "string", "instructions": ["string"] },
      "streetSmartTip": "string"
    }
  `;

  try {
    return await callAiApi(prompt, 'generate_lesson');
  } catch (error) {
    console.warn("Falling back to mock lesson due to API error");
    return mockLessonResponse(topic, persona, locationContext);
  }
};

export const generateScenario = async (
  category: string,
  age: number,
  persona: PersonaType,
  locationContext: LocationContext = 'home'
): Promise<any> => {
   const personaInstruction = PERSONA_PROMPTS[persona];
   const environmentInstruction = getContextModifier(locationContext);

   const prompt = `
     ${personaInstruction}
     ${environmentInstruction}

     Task: Generate a multiple-choice roleplay scenario for a ${age}-year-old child to test their ${category}.
     
     Scenario Context: Everyday life suitable for their location (${locationContext}).
     
     Structure:
     1. The Situation (A short paragraph setting the scene).
     2. Three Options (A, B, C). One is wise/integrity-based, one is reckless, one is average.
     3. The Evaluation (What you, the persona, think of each choice).
     
     Return JSON. Do not add markdown formatting.

     JSON Schema:
     {
       "situation": "string",
       "options": [
         { "id": "A", "text": "string", "outcome": "string", "score": 0 },
         { "id": "B", "text": "string", "outcome": "string", "score": 100 },
         { "id": "C", "text": "string", "outcome": "string", "score": 50 }
       ]
     }
   `;

   try {
    return await callAiApi(prompt, 'generate_scenario');
  } catch (error) {
    console.warn("Falling back to mock scenario due to API error");
    return mockScenarioResponse(category, persona, locationContext);
  }
};

const mockLessonResponse = (topic: string, persona: PersonaType, location: LocationContext) => ({
  title: `The Way of ${topic}`,
  duration: "15 mins",
  objective: "To understand the value of patience.",
  storyOrProverb: persona === 'uncle' ? "Lagos no be for sleeping bike. But rush and crash is not the way." : "The patient dog eats the fattest bone.",
  discussionPoints: ["Why do we wait?", "What happens when you rush?"],
  practicalActivity: {
    name: location === 'diaspora' ? "The 'No' Challenge (Supermarket)" : "The Market Bargain",
    instructions: ["Wait before asking.", "Count to ten."]
  },
  streetSmartTip: persona === 'auntie' ? "Fix your collar before you step out." : "Shine your eyes."
});

const mockScenarioResponse = (category: string, persona: PersonaType, location: LocationContext) => ({
  situation: location === 'diaspora' 
    ? "You are at school and your friends are laughing at your Nigerian lunch."
    : "You are at the market and the seller gives you N200 extra change by mistake.",
  options: [
    { id: "A", text: location === 'diaspora' ? "Throw the food away." : "Keep the money.", outcome: "Ah! Never deny your heritage.", score: 0 },
    { id: "B", text: location === 'diaspora' ? "Share it and explain it's delicious." : "Return the money.", outcome: "You have done well. Pride in who you are.", score: 100 },
    { id: "C", text: location === 'diaspora' ? "Eat silently." : "Tell your mummy.", outcome: "Okay, but stand tall.", score: 50 }
  ]
});
