import { useNavigate } from 'react-router-dom'
import { Button } from '@/components/ui/Button'
import { Card } from '@/components/ui/Card'
import { Users, Calendar, Award, Shield, Smartphone, Heart } from 'lucide-react'

export default function Home() {
  const navigate = useNavigate()

  const features = [
    {
      icon: <Users className="w-6 h-6 text-purple-600" />,
      title: "Multiple Children",
      description: "Manage multiple children with individual profiles and task assignments"
    },
    {
      icon: <Calendar className="w-6 h-6 text-purple-600" />,
      title: "Task Scheduling",
      description: "Create recurring tasks with specific times and reward systems"
    },
    {
      icon: <Award className="w-6 h-6 text-purple-600" />,
      title: "Reward System",
      description: "Motivate your children with points, badges, and privilege unlocks"
    },
    {
      icon: <Shield className="w-6 h-6 text-purple-600" />,
      title: "Session Control",
      description: "Monitor and control device sessions with real-time status updates"
    },
    {
      icon: <Smartphone className="w-6 h-6 text-purple-600" />,
      title: "Offline First",
      description: "Works seamlessly offline with automatic sync when connected"
    },
    {
      icon: <Heart className="w-6 h-6 text-purple-600" />,
      title: "Behavior Tracking",
      description: "Monitor progress and track behavioral improvements over time"
    }
  ]

  return (
    <div className="min-h-screen bg-white">
      {/* Hero Section */}
      <div className="bg-gradient-to-br from-purple-600 to-purple-800 text-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20">
          <div className="text-center">
            <h1 className="text-4xl md:text-6xl font-bold mb-6">
              Iroko Parent App
            </h1>
            <p className="text-xl md:text-2xl mb-8 text-purple-100 max-w-3xl mx-auto">
              Empower your parenting journey with intelligent task management, behavioral tracking, 
              and digital discipline tools designed for modern families.
            </p>
            <div className="flex flex-col sm:flex-row gap-4 justify-center">
              <Button 
                onClick={() => navigate('/signup')}
                size="lg"
                className="bg-white text-purple-600 hover:bg-gray-100 font-semibold"
              >
                Get Started Free
              </Button>
              <Button 
                onClick={() => navigate('/login')}
                size="lg"
                variant="outline"
                className="border-white text-white hover:bg-white hover:text-purple-600 font-semibold"
              >
                Sign In
              </Button>
            </div>
          </div>
        </div>
      </div>

      {/* Features Section */}
      <div className="py-20 bg-gray-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-16">
            <h2 className="text-3xl md:text-4xl font-bold text-gray-900 mb-4">
              Everything You Need for Digital Parenting
            </h2>
            <p className="text-xl text-gray-600 max-w-2xl mx-auto">
              Comprehensive tools to help you manage your children's digital activities and daily responsibilities
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            {features.map((feature, index) => (
              <Card key={index} className="p-6 hover:shadow-lg transition-shadow">
                <div className="flex items-start space-x-4">
                  <div className="flex-shrink-0">
                    <div className="w-12 h-12 bg-purple-100 rounded-lg flex items-center justify-center">
                      {feature.icon}
                    </div>
                  </div>
                  <div className="flex-1">
                    <h3 className="text-lg font-semibold text-gray-900 mb-2">
                      {feature.title}
                    </h3>
                    <p className="text-gray-600">
                      {feature.description}
                    </p>
                  </div>
                </div>
              </Card>
            ))}
          </div>
        </div>
      </div>

      {/* CTA Section */}
      <div className="bg-purple-600 py-16">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <h2 className="text-3xl font-bold text-white mb-4">
            Ready to Transform Your Parenting?
          </h2>
          <p className="text-xl text-purple-100 mb-8 max-w-2xl mx-auto">
            Join thousands of parents who are already using Iroko to create better digital habits for their children.
          </p>
          <Button 
            onClick={() => navigate('/signup')}
            size="lg"
            className="bg-white text-purple-600 hover:bg-gray-100 font-semibold px-8"
          >
            Start Your Journey Today
          </Button>
        </div>
      </div>

      {/* Footer */}
      <footer className="bg-gray-900 text-white py-12">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center">
            <h3 className="text-2xl font-bold mb-4">Iroko Parent App</h3>
            <p className="text-gray-400 mb-8">
              Empowering parents, inspiring children, building better digital futures.
            </p>
            <div className="flex justify-center space-x-6">
              <Button 
                onClick={() => navigate('/login')}
                variant="ghost"
                className="text-gray-300 hover:text-white"
              >
                Sign In
              </Button>
              <Button 
                onClick={() => navigate('/signup')}
                variant="ghost"
                className="text-gray-300 hover:text-white"
              >
                Get Started
              </Button>
            </div>
          </div>
        </div>
      </footer>
    </div>
  )
}