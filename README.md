<<<<<<< HEAD
# IROKO Platform

The **IROKO Platform** is a multi-modal digital village that combines AI, Voice, and traditional African storytelling to guide children through their daily routines and emotional development.

## 🏗️ Project Structure

The repository is organized as a monorepo containing:

*   **`app-child/`**: Native Android App for Children (Voice-first, Gamified).
*   **`app-parent/`**: Native Android App for Parents (Dashboard, Oversight).
*   **`backend-core/`**: Node.js/Express Backend (AI Engine, Webhooks, API).
*   **`src/`** (Root): React Web Admin Dashboard (Oversight, Content Management).
*   **`core-*/`**: Shared Android Libraries (`core-data`, `core-auth`, `core-ui`).
*   **`api/`**: Vercel Serverless Functions (Production Backend).

## 🚀 Quick Start (Local Development)

### Prerequisites
*   Node.js v20+
*   Java JDK 17+
*   Android Studio

### 1. Web Admin & Backend (Vercel Style)
You can run the full web stack using `vercel dev` or standard npm scripts:

```bash
# Install dependencies
npm install

# Run Frontend & Backend (Proxy)
npm run client:dev
```
*   **Admin Dashboard:** [http://localhost:5173](http://localhost:5173)
*   **Backend API:** [http://localhost:5173/api/health](http://localhost:5173/api/health)

### 2. Android Apps
Open the project in Android Studio.
*   **Child App:** Select `app-child` run configuration.
*   **Parent App:** Select `app-parent` run configuration.

Ensure you have a `local.properties` file with your SDK path.

## ☁️ Deployment (Vercel)

This project is configured for one-click deployment on Vercel.

1.  Push code to GitHub.
2.  Import project into Vercel.
3.  Set the following **Environment Variables** in Vercel Project Settings:
    *   `GEMINI_API_KEY`
    *   `SUPABASE_URL`
    *   `SUPABASE_SERVICE_ROLE_KEY`
    *   `VITE_SUPABASE_URL`
    *   `VITE_SUPABASE_ANON_KEY`
4.  Deploy!

## 🧠 AI Features
*   **Voice of the Village:** Powered by Gemini Flash 1.5. Responds to children with metaphors and kindness.
*   **Daily Guidance:** Generates daily missions based on Child's "Village Context" (Rural/Urban, Age, Grit Score).

## 🛡️ Security
*   **RLS (Row Level Security):** Supabase policies ensure Parents only see their own children.
*   **Admin Access:** Protected routes in the Web Dashboard.
=======
<div align="center">
<img width="1200" height="475" alt="GHBanner" src="https://github.com/user-attachments/assets/0aa67016-6eaf-458a-adb2-6e31a0763ed6" />
</div>

# Run and deploy your AI Studio app

This contains everything you need to run your app locally.

View your app in AI Studio: https://ai.studio/apps/drive/13Xd39uECZzQA78ji21pIhYTNYTPF0uTK

## Run Locally

**Prerequisites:**  Node.js


1. Install dependencies:
   `npm install`
2. Set the `GEMINI_API_KEY` in [.env.local](.env.local) to your Gemini API key
3. Run the app:
   `npm run dev`
>>>>>>> 007fed1d121d31c4df56af9c55161247f929fd49
