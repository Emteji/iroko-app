import 'dotenv/config';
import { supabase } from '../lib/supabaseClient';
import aiHandler from '../api/ai/generate';

async function runTests() {
  console.log("🚀 Starting Backend Verification...");
  console.log("-----------------------------------");

  // 1. Database Connection Test
  console.log("\n🔍 Testing Database (Supabase)...");
  try {
    // Check for seed data in mission_templates
    const { data, error } = await supabase
      .from('mission_templates')
      .select('title')
      .limit(3);

    if (error) throw error;

    if (data && data.length > 0) {
      console.log("✅ Database Connected!");
      console.log(`   Found ${data.length} mission templates (e.g., "${data[0].title}")`);
    } else {
      console.warn("⚠️ Database connected but 'mission_templates' is empty.");
      console.warn("   Did you run the MASTER_SCHEMA.sql script in Supabase?");
    }
  } catch (err: any) {
    console.error("❌ Database Connection Failed:");
    console.error("   ", err.message);
  }

  // 2. AI Service Test
  console.log("\n🧠 Testing AI Service (Gemini via Serverless Function)...");
  
  // Mock Request/Response for the API handler
  const req = {
    method: 'POST',
    body: {
      action: 'generate_lesson',
      prompt: 'Create a lesson about "Respect for Elders" for a 7-year-old in Nigeria.'
    }
  };

  const res = {
    statusCode: 200,
    jsonData: null,
    status: function(code: number) { 
      this.statusCode = code; 
      return this; 
    },
    json: function(data: any) {
      this.jsonData = data;
      return this;
    }
  };

  try {
    await aiHandler(req, res);

    if (res.statusCode === 200 && res.jsonData) {
      console.log("✅ AI Generation Successful!");
      const lesson = res.jsonData;
      console.log(`   Title: ${lesson.title}`);
      console.log(`   Objective: ${lesson.objective}`);
    } else {
      console.error("❌ AI Generation Failed:");
      console.error("   Status:", res.statusCode);
      console.error("   Response:", JSON.stringify(res.jsonData, null, 2));
    }
  } catch (err: any) {
    console.error("❌ AI Service Exception:");
    console.error("   ", err.message);
  }

  console.log("\n-----------------------------------");
  console.log("🏁 Verification Complete");
}

runTests();
