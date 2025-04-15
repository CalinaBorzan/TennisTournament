import React, { useEffect, useState } from 'react';
import '../style/TournamentInsight.css'; // Ensure the CSS file is correctly imported

const TournamentsInsight = () => {
  const [registeredTournaments, setRegisteredTournaments] = useState([]);
  const [notRegisteredTournaments, setNotRegisteredTournaments] = useState([]);
  const [matchesByTournament, setMatchesByTournament] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const userId = localStorage.getItem('userId');

  useEffect(() => {
    const fetchData = async () => {
      try {
        // 1) Fetch all tournaments
        const allRes = await fetch('http://localhost:8080/api/tournaments', { credentials: 'include' });
        if (!allRes.ok) {
          throw new Error('Failed to fetch all tournaments');
        }
        const allData = await allRes.json();

        // 2) Fetch tournaments the user is registered for
        const regRes = await fetch(`http://localhost:8080/api/tournaments/registered?userId=${userId}`, { credentials: 'include' });
        if (!regRes.ok) {
          throw new Error('Failed to fetch registered tournaments');
        }
        const regData = await regRes.json();

        // Separate tournaments into registered and not registered
        const registeredSet = new Set(regData.map((t) => t.id));
        const userRegistered = [];
        const userNotRegistered = [];
        allData.forEach((t) => {
          if (registeredSet.has(t.id)) {
            userRegistered.push(t);
          } else {
            userNotRegistered.push(t);
          }
        });

        // 3) For every tournament, fetch match details (schedule/score)
        const uniqueTournaments = [...userRegistered, ...userNotRegistered];
        const matchPromises = uniqueTournaments.map(async (t) => {
          const matchRes = await fetch(`http://localhost:8080/api/matches/tournament/${t.id}`, { credentials: 'include' });
          if (!matchRes.ok) {
            return { tournamentId: t.id, matches: [] }; // fallback if error
          }
          const matchData = await matchRes.json();
          return { tournamentId: t.id, matches: matchData };
        });

        const matchResults = await Promise.all(matchPromises);
        const matchesMap = {};
        matchResults.forEach((item) => {
          matchesMap[item.tournamentId] = item.matches;
        });

        // Update state
        setRegisteredTournaments(userRegistered);
        setNotRegisteredTournaments(userNotRegistered);
        setMatchesByTournament(matchesMap);
        setLoading(false);
      } catch (err) {
        console.error(err);
        setError(err.message);
        setLoading(false);
      }
    };

    fetchData();
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
