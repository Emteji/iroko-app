import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { Login } from './pages/Login'
import { DashboardLayout, DashboardOverview } from './pages/Dashboard'
import { Users } from './pages/Users'
import { Payments } from './pages/Payments'
import { Settings } from './pages/Settings'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/dashboard" element={<DashboardLayout />}>
          <Route index element={<DashboardOverview />} />
          <Route path="users" element={<Users />} />
          <Route path="payments" element={<Payments />} />
          <Route path="settings" element={<Settings />} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}

export default App
