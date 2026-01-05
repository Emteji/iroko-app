/** @type {import('tailwindcss').Config} */

export default {
  darkMode: "class",
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    container: {
      center: true,
    },
    extend: {
      colors: {
        iroko: {
          brown: "#52361E",
          clay: "#3E2816",
          gold: "#D9A53D",
          burntGold: "#C68E17",
          forest: "#2E7D32",
          darkSoil: "#2D1B0E",
          cream: "#FDFBF7",
          white: "#FAFAFA",
        }
      },
      fontFamily: {
        serif: ['"Playfair Display"', 'serif'],
        sans: ['"Inter"', 'sans-serif'],
      }
    },
  },
  plugins: [],
};
