import { createClient } from '@supabase/supabase-js';
import { GoogleGenerativeAI } from '@google/generative-ai';
import dotenv from 'dotenv';

// Load env vars
dotenv.config();

async function testConnections() {
  console.log("🔍 Starting Connectivity Test...");
  let hasError = false;

  // 1. Check Supabase
  const sbUrl = process.env.VITE_SUPABASE_URL || process.env.SUPABASE_URL;
  const sbKey = process.env.SUPABASE_SERVICE_ROLE_KEY || process.env.VITE_SUPABASE_ANON_KEY;

  if (!sbUrl || !sbKey) {
    console.error("❌ Supabase Env Vars Missing (SUPABASE_URL, SUPABASE_SERVICE_ROLE_KEY)");
    hasError = true;
  } else {
    try {
      console.log("   Connecting to Supabase...");
      const supabase = createClient(sbUrl, sbKey);
      // Try a simple query
      const { data, error } = await supabase.from('children').select('count', { count: 'exact', head: true });
      if (error) {
        console.error("❌ Supabase Connection Failed:", error.message);
        hasError = true;
      } else {
        console.log("✅ Supabase Connection: OK");
      }
    } catch (e: any) {
      console.error("❌ Supabase Exception:", e.message);
      hasError = true;
    }
  }

  // 2. Check Gemini
  const geminiKey = process.env.GEMINI_API_KEY;
  if (!geminiKey) {
    console.error("❌ Gemini Env Var Missing (GEMINI_API_KEY)");
    hasError = true;
  } else {
    try {
      console.log("   Connecting to Gemini...");
      const genAI = new GoogleGenerativeAI(geminiKey);
      const model = genAI.getGenerativeModel({ model: "gemini-pro" });
      const result = await model.generateContent("Say 'Hello' if you can hear me.");
      const response = await result.response;
      const text = response.text();
      if (text) {
        console.log("✅ Gemini Connection: OK");
        console.log("   Response:", text);
      } else {
        console.error("❌ Gemini returned empty response");
        hasError = true;
      }
    } catch (e: any) {
      console.error("❌ Gemini Exception:", e.message);
      hasError = true;
    }
  }

  if (hasError) {
    console.error("\n💥 Some checks failed. Please review your .env file or Vercel Environment Variables.");
    process.exit(1);
  } else {
    console.log("\n✨ All Systems Operational. Ready for Launch.");
    process.exit(0);
  }
}

testConnections();
