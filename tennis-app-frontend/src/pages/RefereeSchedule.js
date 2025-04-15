import React, { useEffect, useState } from 'react';
import '../style/Referee.css';


const RefereeSchedule = () => {
  const [matches, setMatches] = useState([]);
  const [error, setError] = useState(null);
  const userId = localStorage.getItem('userId');

  useEffect(() => {
    fetch(`http://localhost:8080/api/matches/referee/${userId}`)
      .then(res => {
        if (!res.ok) throw new Error('Failed to fetch referee matches');
        return res.json();
      })
      .then(data => setMatches(data))
      .catch(err => setError(err.message));
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
