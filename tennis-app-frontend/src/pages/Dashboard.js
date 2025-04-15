import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../style/Dashboard.css';

const Dashboard = () => {
  const [user, setUser] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const userId = localStorage.getItem('userId');
    if (!userId) {
      navigate('/login');
      return;
    }

    fetch(`http://localhost:8080/api/users/${userId}`)
      .then(res => res.json())
      .then(fetchedUser => setUser(fetchedUser))
      .catch(err => navigate('/login'));
  }, [navigate]);

  if (!user) {
    return <div className="dashboard-loading">Loading...</div>;
  }

  const { firstName, role } = user;

  return (
    <div className="dashboard-container">
      <div className="sidebar">
        <h2>{role.charAt(0).toUpperCase() + role.slice(1)} Panel</h2>
        <button className="sidebar-btn" onClick={() => navigate('/profile')}>View Profile</button>
      </div>

      <div className="dashboard-main">
        <h1>Welcome, {firstName}!</h1>
        <p className="dashboard-subtitle">Choose an action to get started:</p>

        <div className="dashboard-cards">
          {role === 'player' && (
            <>
              <div className="dashboard-card" onClick={() => navigate('/tournament-registration')}>
                <p>Register for Tournament</p>
              </div>
              <div className="dashboard-card" onClick={() => navigate('/tournaments-view')}>
                <p>View Tournaments</p>
              </div>
            </>
          )}
          {role === 'referee' && (
            <>
              <div className="dashboard-card" onClick={() => navigate('/referee-schedule')}>
                <p>View Your Program</p>
              </div>
              <div className="dashboard-card" onClick={() => navigate('/manage-scores')}>
                <p>Manage Match Scores</p>
              </div>
            </>
          )}
          {role === 'admin' && (
            <>
              <div className="dashboard-card" onClick={() => navigate('/user-management')}>
                <p>Manage Users</p>
              </div>
              <div className="dashboard-card" onClick={() => navigate('/export-matches')}>
                <p>Export Matches</p>
              </div>
            </>
          )}
        </div>

        <img
  src="/dashboard.png"
  alt="Dashboard illustration"
  className="dashboard-illustration"
/>
      </div>
    </div>
  );
};

export default Dashboard;
