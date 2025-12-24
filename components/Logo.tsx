import React from 'react';

interface LogoProps {
  className?: string;
  variant?: 'light' | 'dark' | 'gold';
}

export const Logo: React.FC<LogoProps> = ({ className = "w-10 h-10", variant = 'dark' }) => {
  const color = variant === 'light' ? '#FFFFFF' : variant === 'gold' ? '#C69C3A' : '#1A1512';
  
  return (
    <svg viewBox="0 0 100 100" className={className} fill="none" xmlns="http://www.w3.org/2000/svg">
      {/* The Iroko Tree: Symbol of Strength & Heritage */}
      
      {/* Roots - Deep and grounding */}
      <path d="M48 85C48 85 40 92 30 92M52 85C52 85 60 92 70 92" stroke={color} strokeWidth="4" strokeLinecap="round"/>
      
      {/* Trunk - Sturdy */}
      <path d="M50 85V45" stroke={color} strokeWidth="6" strokeLinecap="round"/>
      
      {/* Canopy - Broad and protective (Abstract foliage) */}
      <path d="M50 45C30 45 20 35 20 25C20 15 35 10 50 10C65 10 80 15 80 25C80 35 70 45 50 45Z" fill={color} fillOpacity="0.2"/>
      <path d="M50 45C30 45 20 35 20 25C20 15 35 10 50 10C65 10 80 15 80 25C80 35 70 45 50 45Z" stroke={color} strokeWidth="3"/>
      
      {/* Branches */}
      <path d="M50 50L35 35" stroke={color} strokeWidth="3" strokeLinecap="round"/>
      <path d="M50 55L65 35" stroke={color} strokeWidth="3" strokeLinecap="round"/>
      
      {/* Sun/Growth Accent (Top Right) */}
      <circle cx="80" cy="20" r="4" fill={variant === 'dark' ? '#C69C3A' : '#FFFFFF'} />
    </svg>
  );
};