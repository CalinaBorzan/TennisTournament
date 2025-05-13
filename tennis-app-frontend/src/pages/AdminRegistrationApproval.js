// src/pages/AdminRegistrationApproval.jsx
import React, { useEffect, useState } from 'react';
import api from '../api/auth';
import '../style/AdminRegistrationApproval.css';

const AdminRegistrationApproval = () => {
  const [requests, setRequests] = useState([]);
  const [loading, setLoading]   = useState(true);

  // Fetch all pending registrations
  useEffect(() => {
    (async () => {
      try {
        const res = await api.get('/api/tournaments/registrations/pending');
        setRequests(res.data);
      } catch (err) {
        console.error('Failed to fetch requests:', err);
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  // Approve or deny one registration
  const handleDecision = async (userId, tournamentId, action) => {
    try {
      await api.put('/api/tournaments/registrations/approve', {
        userId,
        tournamentId
      });
      // Show a popup
      window.alert(`Registration ${action}d!`);

      // Remove it from state so it disappears from the table
      setRequests(reqs =>
        reqs.filter(r =>
          !(r.userId === userId && r.tournamentId === tournamentId)
        )
      );
    } catch (err) {
      console.error(`Failed to ${action}:`, err.response?.data || err);
      window.alert(`Could not ${action} registration.`);
    }
  };

  if (loading) return <p>Loading registration requests…</p>;

  return (
    <div className="approval-page">
      <h2>Pending Tournament Registrations</h2>
      {requests.length === 0
        ? <p>No pending requests.</p>
        : (
          <table>
            <thead>
              <tr>
                <th>User</th>
                <th>Tournament</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {requests.map(r => (
                <tr key={`${r.userId}-${r.tournamentId}`}>
                  <td>{r.username}</td>
                  <td>{r.tournamentName}</td>
                  <td>
                    <button
                      onClick={() =>
                        handleDecision(r.userId, r.tournamentId, 'approve')
                      }
                    >
                      Approve
                    </button>
                    <button
                      onClick={() =>
                        handleDecision(r.userId, r.tournamentId, 'deny')
                      }
                    >
                      Deny
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )
      }
    </div>
  );
};

export default AdminRegistrationApproval;
