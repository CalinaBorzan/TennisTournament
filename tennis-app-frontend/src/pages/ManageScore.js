import React, { useEffect, useState } from 'react';
import '../style/Referee.css';
import api from "../api/auth";


const ManageScores = () => {
  const [matches, setMatches] = useState([]);
  const [error, setError] = useState(null);
  const [scores, setScores] = useState({});
  const userId = localStorage.getItem('userId');

  useEffect(() => {
    api.get(`/api/matches/referee/${userId}`)
       .then(r => {
         setMatches(r.data);
         const init = {};
         r.data.forEach(m => { init[m.id] = m.score || ""; });
         setScores(init);
       })
       .catch(e => setError(e.message));
  }, [userId]);

  const handleScoreChange = (matchId, value) => {
    setScores(prev => ({ ...prev, [matchId]: value }));
  };

   const handleScoreSubmit = async id => {
    try {
      const { data } = await api.put(`/api/matches/${id}/update-score`, { score: scores[id] });
      setMatches(prev => prev.map(m => (m.id === id ? { ...m, score: data.score } : m)));
      alert("Score updated!");
    } catch (e) {
      alert(e.message);
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
