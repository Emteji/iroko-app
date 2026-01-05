import React from 'react';

export const PatternBackground = () => (
  <svg width="100%" height="100%" xmlns="http://www.w3.org/2000/svg">
    <defs>
      <pattern id="mudcloth" x="0" y="0" width="100" height="100" patternUnits="userSpaceOnUse">
        {/* Simplified Mudcloth / Adire inspired geometric patterns */}
        <path d="M10,50 L40,20 L70,50 L40,80 Z" fill="none" stroke="currentColor" strokeWidth="2" />
        <circle cx="90" cy="10" r="5" fill="currentColor" />
        <circle cx="90" cy="90" r="5" fill="currentColor" />
        <path d="M10,10 L30,10 M10,15 L30,15" stroke="currentColor" strokeWidth="2" />
        <path d="M70,85 L90,85 M70,90 L90,90" stroke="currentColor" strokeWidth="2" />
      </pattern>
    </defs>
    <rect width="100%" height="100%" fill="url(#mudcloth)" />
  </svg>
);