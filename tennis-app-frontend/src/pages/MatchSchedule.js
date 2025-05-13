import React, { useState, useEffect } from 'react';
import '../style/MatchSchedule.css';
import api from "../api/auth";


const MatchSchedule = () => {
  const [matches, setMatches] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

   useEffect(() => {
    api.get("/api/matches")
       .then(r => { setMatches(r.data); setLoading(false); })
       .catch(e => { setError(e.message); setLoading(false); });
  }, []);

  if (loading) {
    return <div className="match-schedule-loading">Loading matches...</div>;
  }

  if (error) {
    return <div className="match-schedule-error">{error}</div>;
  }

  if (matches.length === 0) {
    return <div className="no-matches">No matches found.</div>;
  }

  return (
    <div className="match-schedule-container">
      <h1>Tennis Match Schedule</h1>
      <table className="match-table">
        <thead>
          <tr>
            <th>Match ID</th>
            <th>Tournament</th>
            <th>Player 1</th>
            <th>Player 2</th>
            <th>Match Date</th>
            <th>Score</th>
          </tr>
        </thead>
        <tbody>
          {matches.map((match) => (
            <tr key={match.id}>
              {/* match.id */}
              <td>{match.id}</td>

              {/* Show the tournament name if available */}
              <td>{match.tournament ? match.tournament.name : 'Unknown'}</td>

              {/* Show player1.username if available */}
              <td>{match.player1 ? match.player1.username : 'N/A'}</td>

              {/* Show player2.username if available */}
              <td>{match.player2 ? match.player2.username : 'N/A'}</td>

              {/* Convert matchDate to a localized string if present */}
              <td>
                {match.matchDate
                  ? new Date(match.matchDate).toLocaleString()
                  : 'Not scheduled'}
              </td>

              {/* Show the score if it exists, or "Not set yet" */}
              <td>{match.score ? match.score : 'Not set yet'}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default MatchSchedule;
