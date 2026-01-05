import { GoogleGenerativeAI } from "@google/generative-ai";
import { LocationContext } from '../types';

const genAI = new GoogleGenerativeAI(process.env.API_KEY || '');
const model = genAI.getGenerativeModel({ model: "gemini-1.5-flash" });

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

// Helper to sanitize JSON string from AI (Removes markdown code blocks)
const cleanJsonString = (str: string): string => {
  if (!str) return "{}";
  // Remove ```json and ``` wrapping if present
  let cleaned = str.replace(/```json/g, "").replace(/```/g, "");
  return cleaned.trim();
};

export const generateLessonPlan = async (
  topic: string,
  childAge: number,
  context: string,
  persona: PersonaType = 'elder',
  locationContext: LocationContext = 'home'
): Promise<any> => {
  if (!process.env.API_KEY) {
    console.warn("No API Key provided. Returning mock data.");
    return mockLessonResponse(topic, persona, locationContext);
  }

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
  `;

  try {
    const result = await model.generateContent(prompt);
    const response = await result.response;
    const text = response.text();

    if (!text) throw new Error("No response from AI");
    
    const cleanedText = cleanJsonString(text);
    return JSON.parse(cleanedText);

  } catch (error) {
    console.error("Gemini API Error:", error);
    // Fallback to mock data on error ensures the app doesn't crash for the user
    return mockLessonResponse(topic, persona, locationContext);
  }
};

export const generateScenario = async (
  category: string,
  age: number,
  persona: PersonaType,
  locationContext: LocationContext = 'home'
): Promise<any> => {
   if (!process.env.API_KEY) return mockScenarioResponse(category, persona, locationContext);

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
   `;

   try {
    const result = await model.generateContent(prompt);
    const response = await result.response;
    const text = response.text();

    if (!text) throw new Error("No response from AI");
    
    const cleanedText = cleanJsonString(text);
    return JSON.parse(cleanedText);

  } catch (error) {
    console.error("Gemini Scenario Error:", error);
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