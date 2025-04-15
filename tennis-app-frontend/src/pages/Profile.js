import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../style/Profile.css';

const Profile = () => {
  const userId = localStorage.getItem('userId');
  const navigate = useNavigate();

  const [user, setUser] = useState(null);
  const [formData, setFormData] = useState(null);
  const [editMode, setEditMode] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!userId) {
      navigate('/login');
      return;
    }

    fetch(`http://localhost:8080/api/users/${userId}`, {
      credentials: 'include',
    })
      .then((res) => {
        if (!res.ok) throw new Error('Not logged in');
        return res.json();
      })
      .then((data) => {
        setUser(data);
        setFormData({
          firstName: data.firstName,
          lastName: data.lastName,
          username: data.username,
          email: data.email,
          password: '',
        });
      })
      .catch((err) => {
        console.error(err);
        setError('You are not logged in.');
      });
  }, [userId, navigate]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSave = async () => {
    try {
      const payload = { ...formData, role: user.role };

      const res = await fetch(`http://localhost:8080/api/users/update-user/${userId}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(payload),
      });

      if (!res.ok) throw new Error(await res.text());

      const updatedUser = await res.json();
      setUser(updatedUser);
      setEditMode(false);
      alert('Profile updated!');
    } catch (err) {
      alert(`Failed to update: ${err.message}`);
    }
  };

  const handleDelete = async () => {
    if (!window.confirm('Are you sure you want to delete your account?')) return;

    try {
      const res = await fetch(`http://localhost:8080/api/users/delete/${userId}`, {
        method: 'DELETE',
        credentials: 'include',
      });

      if (!res.ok) throw new Error('Failed to delete account');

      localStorage.clear();
      navigate('/login');
    } catch (err) {
      alert(err.message);
    }
  };

  if (error) return <div className="profile-message">{error}</div>;
  if (!user || !formData) return <div className="profile-message">Loading...</div>;

  return (
    <div className="profile-wrapper">
      <div className="profile-card">
        <h2>Your Profile</h2>

        {editMode ? (
          <div className="profile-form">
            <input name="firstName" value={formData.firstName} onChange={handleChange} placeholder="First Name" />
            <input name="lastName" value={formData.lastName} onChange={handleChange} placeholder="Last Name" />
            <input name="username" value={formData.username} onChange={handleChange} placeholder="Username" />
            <input name="email" value={formData.email} onChange={handleChange} placeholder="Email" />
            <input name="password" value={formData.password} onChange={handleChange} placeholder="New Password" type="password" />
            <div className="profile-buttons">
              <button className="save-btn" onClick={handleSave}>Save</button>
              <button className="cancel-btn" onClick={() => setEditMode(false)}>Cancel</button>
            </div>
          </div>
        ) : (
          <div className="profile-info">
            <p><strong>First Name:</strong> {user.firstName}</p>
            <p><strong>Last Name:</strong> {user.lastName}</p>
            <p><strong>Username:</strong> {user.username}</p>
            <p><strong>Email:</strong> {user.email}</p>
            <p><strong>Role:</strong> {user.role}</p>
            <div className="profile-buttons">
              <button className="edit-btn" onClick={() => setEditMode(true)}>Edit Profile</button>
              <button className="delete-btn" onClick={handleDelete}>Delete Account</button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default Profile;
