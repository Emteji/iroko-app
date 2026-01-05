import React, { useEffect } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { useAuthStore } from './stores/authStore';
import ProtectedRoute from './components/ProtectedRoute';

// Admin Pages
import AdminLayout from './components/admin/AdminLayout';
import AdminDashboard from './pages/admin/Dashboard';
import SystemMonitoring from './pages/admin/Monitoring';
import AIOversight from './pages/admin/AIOversight';
import UserManagement from './pages/admin/Users';
import SafetyAbuse from './pages/admin/Safety';
import ContentManagement from './pages/admin/Content';

// Auth Pages
import Login from './pages/Login';
import Signup from './pages/Signup';

function App() {
  const checkAuth = useAuthStore((state) => state.checkAuth);

  useEffect(() => {
    checkAuth();
  }, [checkAuth]);

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        
        {/* Admin Routes */}
        <Route path="/admin" element={
          <ProtectedRoute>
            <AdminLayout />
          </ProtectedRoute>
        }>
          <Route index element={<Navigate to="/admin/dashboard" replace />} />
          <Route path="dashboard" element={<AdminDashboard />} />
          <Route path="monitoring" element={<SystemMonitoring />} />
          <Route path="ai" element={<AIOversight />} />
          <Route path="content" element={<ContentManagement />} />
          <Route path="users" element={<UserManagement />} />
          <Route path="safety" element={<SafetyAbuse />} />
        </Route>

        {/* Redirect root to admin for now, or landing page later */}
        <Route path="/" element={<Navigate to="/admin/dashboard" replace />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
