// src/App.js
import React from "react";
import { Routes, Route } from "react-router-dom";

import LoginPage           from "./pages/Login";
import RegisterPage        from "./pages/Register";
import Home                from "./pages/Home";
import Dashboard           from "./pages/Dashboard";
import ViewUsers           from "./pages/ViewUsers";
import Profile             from "./pages/Profile";
import TournamentRegistration from "./pages/TournamentRegistration";
import MatchSchedule       from "./pages/MatchSchedule";
import TournamentsInsight  from "./pages/TournamentsInsight";
import RefereeSchedule     from "./pages/RefereeSchedule";
import ManageScores        from "./pages/ManageScore";
import ExportMatches       from "./pages/ExportMatches";
import AdminRegistrationApproval from './pages/AdminRegistrationApproval';
import RefereePlayerFilter from './pages/RefereePlayerFilter';



function App() {
  return (
    <Routes>
      <Route path="/"                    element={<Home />} />
      <Route path="/login"               element={<LoginPage />} />
      <Route path="/register"            element={<RegisterPage />} />
      <Route path="/dashboard"           element={<Dashboard />} />
      <Route path="/user-management"     element={<ViewUsers />} />
      <Route path="/profile"             element={<Profile />} />
      <Route path="/tournament-registration" element={<TournamentRegistration />} />
      <Route path="/schedule"            element={<MatchSchedule />} />
      <Route path="/tournaments-view"    element={<TournamentsInsight />} />
      <Route path="/referee-schedule"    element={<RefereeSchedule />} />
      <Route path="/manage-scores"       element={<ManageScores />} />
      <Route path="/export-matches"      element={<ExportMatches />} />
      <Route path="/admin/registrations" element={<AdminRegistrationApproval />} />
      <Route path="/referee/filter-players" element={<RefereePlayerFilter />} />


    </Routes>
  );
}

export default App;
