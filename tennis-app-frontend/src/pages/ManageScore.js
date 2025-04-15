import React, { useEffect, useState } from 'react';
import '../style/Referee.css';

const ManageScores = () => {
  const [matches, setMatches] = useState([]);
  const [error, setError] = useState(null);
  const [scores, setScores] = useState({});
  const userId = localStorage.getItem('userId');

  useEffect(() => {
    const fetchMatches = async () => {
      try {
        const res = await fetch(`http://localhost:8080/api/matches/referee/${userId}`);
        if (!res.ok) throw new Error('Failed to fetch matches');
        const data = await res.json();
        setMatches(data);
        const initialScores = {};
        data.forEach(match => {
          initialScores[match.id] = match.score || '';
        });
        setScores(initialScores);
      } catch (err) {
        setError(err.message);
      }
    };

    fetchMatches();
  }, [userId]);

  const handleScoreChange = (matchId, value) => {
    setScores(prev => ({ ...prev, [matchId]: value }));
  };

  const handleScoreSubmit = async (matchId) => {
    try {
      const res = await fetch(`http://localhost:8080/api/matches/${matchId}/update-score`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ score: scores[matchId] }),
      });

      if (!res.ok) throw new Error('Failed to update score');

      const updatedMatch = await res.json();
      setMatches(prev =>
        prev.map(m => (m.id === matchId ? { ...m, score: updatedMatch.score } : m))
      );
      alert('Score updated successfully!');
    } catch (err) {
      alert(err.message);
    }
  };

  if (error) return <div className="manage-scores">Error: {error}</div>;

  return (
    <div className="manage-scores">
      <h2>Manage Your Match Scores</h2>
      {matches.length === 0 ? (
        <p className="info-text">No matches assigned to you yet.</p>
      ) : (
        matches.map(match => (
          <div key={match.id} className="score-card">
            <p>
              <strong>{match.player1?.username || 'Player1'}</strong> vs{' '}
              <strong>{match.player2?.username || 'Player2'}</strong>
            </p>
            <p><strong>Date:</strong> {match.matchDate ? new Date(match.matchDate).toLocaleString() : 'TBD'}</p>
            <input
              type="text"
              value={scores[match.id]}
              onChange={(e) => handleScoreChange(match.id, e.target.value)}
              placeholder="Enter score"
            />
            <button onClick={() => handleScoreSubmit(match.id)}>Submit Score</button>
          </div>
        ))
      )}
    </div>
  );
};

export default ManageScores;
