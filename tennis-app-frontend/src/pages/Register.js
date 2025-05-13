// src/pages/Register.jsx
import React, { useState } from 'react';
import { useNavigate }        from 'react-router-dom';
import '../style/Register.css';
import { registerUser }       from '../api/auth';

const Register = () => {
  const [firstName, setFirstName] = useState('');
  const [lastName,  setLastName]  = useState('');
  const [username,  setUsername]  = useState('');
  const [password,  setPassword]  = useState('');
  const [email,     setEmail]     = useState('');
  const [role,      setRole]      = useState('player');
  const [error,     setError]     = useState('');

  const navigate = useNavigate();

  const handleSubmit = async e => {
    e.preventDefault();
    setError(''); // clear old error

    // (Optional) simple client-side email format check
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      setError('Please enter a valid email address.');
      return;
    }

    try {
      const user = await registerUser({ firstName, lastName, username, password, email, role });
      localStorage.setItem('userId', user.id);
      navigate('/dashboard');
    } catch (e) {
      // if the server returned a message, show that; otherwise a generic fallback
      const msg = e.response?.data || 'Registration failed – please try again.';
      setError(msg);
    }
  };

  return (
    <div className="register-container">
      <div className="form-container">
        <h2>Create an Account</h2>
        {error && <p className="error-message">{error}</p>}
        <form onSubmit={handleSubmit}>
          <label>First Name</label>
          <input value={firstName} onChange={e => setFirstName(e.target.value)} />

          <label>Last Name</label>
          <input value={lastName} onChange={e => setLastName(e.target.value)} />

          <label>Username</label>
          <input value={username} onChange={e => setUsername(e.target.value)} />

          <label>Password</label>
          <input type="password" value={password} onChange={e => setPassword(e.target.value)} />

          <label>Email</label>
          <input type="email" value={email} onChange={e => setEmail(e.target.value)} />

          <label>Role</label>
          <select value={role} onChange={e => setRole(e.target.value)}>
            <option value="player">Tennis Player</option>
            <option value="referee">Referee</option>
            <option value="admin">Admin</option>
          </select>

          <button type="submit" className="register-btn">Register</button>
        </form>
      </div>
    </div>
  );
};

export default Register;
