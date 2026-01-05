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

import ChildDashboard from './pages/ChildDashboard';
import ParentDashboard from './pages/Dashboard';

// Auth Pages
import Login from './pages/Login';
import Signup from './pages/Signup';
import Home from './pages/Home';

function App() {
  const checkAuth = useAuthStore((state) => state.checkAuth);

  useEffect(() => {
    checkAuth();
  }, [checkAuth]);

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        
        {/* Main Application Routes */}
        <Route path="/dashboard" element={<ParentDashboard />} />
        <Route path="/child" element={<ChildDashboard />} />
        
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
      </Routes>
    </BrowserRouter>
  );
}

export default App;
