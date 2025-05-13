import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../style/Profile.css';
import api from "../api/auth";


const Profile = () => {
  const userId = localStorage.getItem('userId');
  const navigate = useNavigate();

  const [user, setUser] = useState(null);
  const [formData, setFormData] = useState(null);
  const [editMode, setEditMode] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!userId) { navigate("/login"); return; }

    api.get(`/api/users/${userId}`)
       .then(r => {
         setUser(r.data);
         setFormData({ ...r.data, password: "" });
       })
       .catch(() => setError("You are not logged in."));
  }, [userId, navigate]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSave = async () => {
    try {
    const { data } = await api.put(
   `/api/users/update-user/${userId}`,
   {
     firstName: formData.firstName,
     lastName : formData.lastName,
     username : formData.username,
     email    : formData.email,
     password : formData.password || ""   // empty string means “unchanged”
   }
 );      setUser(data);
      setEditMode(false);
      alert("Profile updated");
    } catch (e) { alert(e.message); }
  };

  const handleDelete = async () => {
    if (!window.confirm("Delete account?")) return;
    try {
      await api.delete(`/api/users/delete/${userId}`);
      localStorage.clear();
      navigate("/login");
    } catch (e) { alert(e.message); }
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
