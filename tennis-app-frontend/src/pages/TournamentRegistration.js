import React, { useState, useEffect } from 'react';
import '../style/TournamentRegistration.css';
import api from "../api/auth";

const TournamentRegistration = () => {
  const [tournaments, setTournaments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    api.get("/api/tournaments/available")
       .then(r => {
         const today = new Date();
         setTournaments(r.data.filter(t => new Date(t.endDate) >= today));
         setLoading(false);
       })
       .catch(() => { setError("Failed to fetch"); setLoading(false); });
  }, []);

   const handleRegister = async id => {
    try {
      await api.post("/api/tournaments/request", null, { params: { tournamentId: id } });
      alert("Request sent. Waiting for approval");
    } catch (e) { alert(e.message); }
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
