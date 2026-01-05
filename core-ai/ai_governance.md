# AI Core Governance & Rules

## 1. AI Scope
The Artificial Intelligence (Gemini) components are strictly limited to the following functions:
*   **Guidance**: Providing advice, explanations, and suggestions to the Child or Parent.
*   **Feedback**: Analyzing input (text, journaling) and offering constructive feedback.
*   **Pattern Detection**: identifying trends in mood, activity, or behavioral health for reporting.

## 2. Hard Restrictions (Blocked Areas)
The AI is **strictly prohibited** from accessing or modifying the following domains. These are handled exclusively by deterministic System Logic or Human Admin intervention:
*   **Payments**: No access to billing, subscription management, or payment processing.
*   **Permissions**: Cannot grant, revoke, or modify user permissions or app restrictions.
*   **Social Moderation Final Decisions**: The AI may flag content, but the *decision* to ban/suspend/delete is a System or Admin action.

## 3. Roles

### Gemini Role (The Model)
*   **Conversation Engine**: Natural language understanding and generation for chat interfaces.
*   **Coaching Logic**: delivering pedagogical or therapeutic-style support based on inputs.

### System Role (The Code)
*   **Enforcement**: Hard-coded logic that accepts or rejects actions.
*   **Limits**: Rate limiting, quota management, and access control.
*   **Safety**: PII scrubbing, content filtering (before sending to AI), and output validation.
