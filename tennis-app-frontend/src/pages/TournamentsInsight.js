import React, { useEffect, useState } from 'react';
import '../style/TournamentInsight.css'; // Ensure the CSS file is correctly imported
import api from "../api/auth";


const TournamentsInsight = () => {
  const [registeredTournaments, setRegisteredTournaments] = useState([]);
  const [notRegisteredTournaments, setNotRegisteredTournaments] = useState([]);
  const [matchesByTournament, setMatchesByTournament] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const userId = localStorage.getItem('userId');

   useEffect(() => {
    (async () => {
      try {
        const all      = await api.get("/api/tournaments");
        const reg      = await api.get("/api/tournaments/registered", { params: { userId } });
        const regSet   = new Set(reg.data.map(t => t.id));
        setRegisteredTournaments(all.data.filter(t => regSet.has(t.id)));
        setNotRegisteredTournaments(all.data.filter(t => !regSet.has(t.id)));

        const promises = all.data.map(t => api.get(`/api/matches/tournament/${t.id}`));
        const results  = await Promise.allSettled(promises);
        const map = {};
        results.forEach((res, i) => { map[all.data[i].id] = res.status === "fulfilled" ? res.value.data : []; });
        setMatchesByTournament(map);
        setLoading(false);
      } catch (e) { setError(e.message); setLoading(false); }
    })();
  }, [userId]);

  if (loading) {
    return <div className="tournaments-view-container">Loading tournaments...</div>;
  }
  if (error) {
    return <div className="tournaments-view-container" style={{ color: 'red' }}>Error: {error}</div>;
  }

  // Renders matches (schedule/score) for a given tournament
  const renderMatches = (tournamentId, filterByPlayerId = null) => {
    const matchList = matchesByTournament[tournamentId] || [];
    const filteredMatches = filterByPlayerId
      ? matchList.filter(
          m =>
            m.player1?.id === filterByPlayerId ||
            m.player2?.id === filterByPlayerId
        )
      : matchList;
  
    if (filteredMatches.length === 0) {
      return <p className="info-text">No matches for this tournament.</p>;
    }
  
    return (
      <div className="match-table-wrapper">
        <table className="match-table">
          <thead>
            <tr>
              <th>Match Date</th>
              <th>Player1</th>
              <th>Player2</th>
              <th>Referee</th>
              <th>Score</th>
            </tr>
          </thead>
          <tbody>
            {filteredMatches.map((m) => (
              <tr key={m.id}>
                <td>{m.matchDate ? new Date(m.matchDate).toLocaleString() : 'Not scheduled'}</td>
                <td>{m.player1?.username || 'Unknown'}</td>
                <td>{m.player2?.username || 'Unknown'}</td>
                <td>{m.referee?.username || 'Unknown'}</td>
                <td>{m.score || 'N/A'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    );
  };

  return (
    <div className="tournaments-view-container">
      <div className="tables-wrapper">
        {/* Registered Tournaments Section */}
        <div className="tournaments-section">
          <h2>Registered Tournaments</h2>
          {registeredTournaments.length === 0 ? (
            <p className="info-text">You are not registered for any tournaments.</p>
          ) : (
            registeredTournaments.map((t) => (
              <div className="tournament-card" key={t.id}>
                <h3>
                  {t.name} <br />
                  <small>
                    {new Date(t.startDate).toLocaleDateString()} - {new Date(t.endDate).toLocaleDateString()}
                  </small>
                </h3>
                {renderMatches(t.id)}
              </div>
            ))
          )}
        </div>

        {/* Not Registered Tournaments Section */}
        <div className="tournaments-section">
          <h2>Not Registered Tournaments</h2>
          {notRegisteredTournaments.length === 0 ? (
            <p className="info-text">All tournaments are registered or no tournaments exist.</p>
          ) : (
            notRegisteredTournaments.map((t) => (
              <div className="tournament-card" key={t.id}>
    <h3>
      {t.name} <br />
      <small>
        {new Date(t.startDate).toLocaleDateString()} - {new Date(t.endDate).toLocaleDateString()}
      </small>
    </h3>
    {/* ✅ Only show matches where player is involved */}
    {renderMatches(t.id)}
  </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
};

export default TournamentsInsight;
