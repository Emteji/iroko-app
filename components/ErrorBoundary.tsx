import React, { Component, ErrorInfo, ReactNode } from 'react';
import { RefreshCw, AlertTriangle } from 'lucide-react';

interface Props {
  children?: ReactNode;
}

interface State {
  hasError: boolean;
  error?: Error;
}

export class ErrorBoundary extends Component<Props, State> {
  public state: State = {
    hasError: false
  };

  public static getDerivedStateFromError(error: Error): State {
    return { hasError: true, error };
  }

  public componentDidCatch(error: Error, errorInfo: ErrorInfo) {
    console.error("Uncaught error:", error, errorInfo);
  }

  public render() {
    if (this.state.hasError) {
      return (
        <div className="min-h-screen flex flex-col items-center justify-center p-8 bg-[#FAF9F6] text-center">
          <div className="w-20 h-20 bg-red-50 text-red-500 rounded-full flex items-center justify-center mb-6 animate-bounce">
            <AlertTriangle size={40} />
          </div>
          <h1 className="text-3xl font-display font-bold text-[#1A1512] mb-2">Ah! Small Wahala.</h1>
          <p className="text-gray-500 mb-8 max-w-md">
            Something went wrong. Don't worry, even giants stumble. Let's try reloading the village.
          </p>
          <button
            onClick={() => window.location.reload()}
            className="flex items-center gap-2 bg-[#8B4513] text-white px-8 py-3 rounded-xl font-bold hover:bg-[#1A1512] transition-colors"
          >
            <RefreshCw size={20} /> Reload App
          </button>
          {this.state.error && (
            <div className="mt-8 p-4 bg-gray-100 rounded-lg text-xs text-left text-gray-600 font-mono w-full max-w-lg overflow-auto">
              {this.state.error.toString()}
            </div>
          )}
        </div>
      );
    }

    return (this as any).props.children;
  }
}