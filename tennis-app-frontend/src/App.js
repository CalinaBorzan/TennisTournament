import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LoginPage from './pages/Login';
import RegisterPage from './pages/Register';
import Home from './pages/Home'; // Changed from HomePage to Home
import Dashboard from './pages/Dashboard';
import ViewUsers from './pages/ViewUsers';
import Profile from './pages/Profile';
import TournamentRegistration from './pages/TournamentRegistration';
import MatchSchedule from './pages/MatchSchedule';
import TournamentsView from './pages/TournamentsInsight';
import RefereeSchedule from './pages/RefereeSchedule';
import ManageScores from './pages/ManageScore';
import ExportMatches from './pages/ExportMatches';
import TournamentsInsight from './pages/TournamentsInsight';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} /> {/* Updated to use Home component */}
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/user-management" element={<ViewUsers />} />
        <Route path="/profile" element={<Profile />} />
        <Route path="/tournament-registration" element={<TournamentRegistration />} />
        <Route path="/schedule" element={<MatchSchedule/>} />
        <Route path="/tournaments-view" element={<TournamentsInsight/>} />
        <Route path="/referee-schedule" element={<RefereeSchedule/>} />
        <Route path="/manage-scores" element={<ManageScores/>} />
        <Route path="/export-matches" element={<ExportMatches/>} />








      </Routes>
    </Router>
  );
}

export default App;
