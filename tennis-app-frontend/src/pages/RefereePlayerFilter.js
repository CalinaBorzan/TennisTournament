// src/pages/RefereePlayerFilter.jsx
import React, { useState } from 'react';
import api from '../api/auth';

const RefereePlayerFilter = () => {
  const [filters, setFilters] = useState({
    firstName: '',
    lastName: '',
    tournamentName: ''
  });
  const [players, setPlayers] = useState([]);

  const handleChange = e => {
    setFilters(f => ({ ...f, [e.target.name]: e.target.value }));
  };

  const handleFilter = async () => {
    try {
      const res = await api.post("/api/users/filter-players", filters);
      setPlayers(res.data);
    } catch (err) {
      console.error("Failed to filter players", err);
    }
  };

  return (
    <div className="filter-page">
      <h2>Filter Players</h2>
      <div className="filter-inputs">
        <input
          type="text"
          name="firstName"
          placeholder="First Name"
          value={filters.firstName}
          onChange={handleChange}
        />
        <input
          type="text"
          name="lastName"
          placeholder="Last Name"
          value={filters.lastName}
          onChange={handleChange}
        />
        <input
          type="text"
          name="tournamentName"
          placeholder="Tournament Name"
          value={filters.tournamentName}
          onChange={handleChange}
        />
        <button onClick={handleFilter}>Filter</button>
      </div>

      <table>
        <thead>
          <tr>
            <th>Username</th><th>Email</th><th>Name</th>
          </tr>
        </thead>
        <tbody>
          {players.map(p => (
            <tr key={p.id}>
              <td>{p.username}</td>
              <td>{p.email}</td>
              <td>{p.firstName} {p.lastName}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default RefereePlayerFilter;
