import React, { useState, useEffect } from 'react';
import '../style/TournamentRegistration.css';

const TournamentRegistration = () => {
  const [tournaments, setTournaments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await fetch('http://localhost:8080/api/tournaments/available', { credentials: 'include' });
        const data = await res.json();
        const today = new Date();
        setTournaments(data.filter(t => new Date(t.endDate) >= today));
        setLoading(false);
      } catch (err) {
        setError('Failed to fetch tournaments.');
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  const handleRegister = async (id) => {
    try {
      const userId = localStorage.getItem('userId');
      const res = await fetch(`http://localhost:8080/api/tournaments/register?tournamentId=${id}&userId=${userId}`, {
        method: 'POST',
        credentials: 'include',
        headers: { 'Content-Type': 'application/json' }
      });
      if (!res.ok) throw new Error('Registration failed');
      alert('Registered!');
    } catch (err) {
      alert(err.message);
    }
  };

  if (loading) return <div className="register-loading">Loading tournaments...</div>;
  if (error) return <div className="register-error">{error}</div>;

  return (
    <div className="register-tournaments-container">
      <h1>Register for Tournaments</h1>
      <div className="register-grid">
        {tournaments.map(t => (
          <div key={t.id} className="register-card">
            <h3>{t.name}</h3>
            <p className="date-range">
              {new Date(t.startDate).toLocaleDateString()} - {new Date(t.endDate).toLocaleDateString()}
            </p>
            {new Date(t.registrationDeadline) < new Date() ? (
              <button disabled className="register-disabled-btn">Registration Closed</button>
            ) : (
              <button onClick={() => handleRegister(t.id)} className="register-btn">Register Now</button>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

export default TournamentRegistration;
