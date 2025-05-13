import React, { useEffect, useState } from 'react';
import '../style/Referee.css';
import api from "../api/auth";


const RefereeSchedule = () => {
  const [matches, setMatches] = useState([]);
  const [error, setError] = useState(null);
  const userId = localStorage.getItem('userId');

 useEffect(() => {
    api.get(`/api/matches/referee/${userId}`)
       .then(r => setMatches(r.data))
       .catch(e => setError(e.message));
  }, [userId]);

  if (error) return <div>Error: {error}</div>;

  return (
    <div className="referee-schedule">
      <h2>Your Referee Program</h2>
      {matches.length === 0 ? (
        <p>No assigned matches.</p>
      ) : (
        <table className="match-table">
          <thead>
            <tr>
              <th>Date</th>
              <th>Player 1</th>
              <th>Player 2</th>
              <th>Score</th>
            </tr>
          </thead>
          <tbody>
            {matches.map(match => (
              <tr key={match.id}>
                <td>{new Date(match.matchDate).toLocaleString()}</td>
                <td>{match.player1?.username || 'N/A'}</td>
                <td>{match.player2?.username || 'N/A'}</td>
                <td>{match.score || 'N/A'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default RefereeSchedule;
