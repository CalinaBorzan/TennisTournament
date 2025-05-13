import React, { useEffect, useState } from 'react';
import '../style/ViewUsers.css';
import api from "../api/auth";


const ViewUsers = () => {
  const [users, setUsers] = useState([]);
  const [editingUser, setEditingUser] = useState(null);

  const [editForm, setEditForm] = useState({
    firstName: '',
    lastName: '',
    username: '',
    email: '',
    role: 'player',
    password : ""            // <-- add this so it exists in state

  });



  const fetchUsers = () => {
    api.get("/api/users/all")
       .then(r => setUsers(r.data.filter(u => u.role !== "admin")))
       .catch(console.error);
  };
  useEffect(fetchUsers, []);
  
  const handleDelete = async id => {
    if (!window.confirm("Delete user?")) return;
    await api.delete(`/api/users/delete/${id}`);
    fetchUsers();
  };


  const handleEdit = (user) => {
    setEditingUser(user);
    setEditForm({
      firstName: user.firstName,
      lastName: user.lastName,
      username: user.username,
      email: user.email,
      role: user.role
    });
  };

 const handleUpdate = async e => {
  e.preventDefault();      // stop form submit reload

  const payload = {
    firstName: editForm.firstName,
    lastName : editForm.lastName,
    username : editForm.username,
    email    : editForm.email,
    password : editForm.password || "",   // blank = keep current
    role     : editForm.role
  };

  await api.put(`/api/users/update-user/${editingUser.id}`, payload);
  setEditingUser(null);
  fetchUsers();
};

  const handleChange = (e) => {
    const { name, value } = e.target;
    setEditForm(prev => ({ ...prev, [name]: value }));
  };

  return (
    <div className="view-users">
      <h2>Manage Users</h2>
      
      {editingUser && (
        <div className="edit-modal">
          <div className="edit-form">
            <h3>Edit User</h3>
            <form onSubmit={handleUpdate}>
              <label>
                First Name:
                <input
                  type="text"
                  name="firstName"
                  value={editForm.firstName}
                  onChange={handleChange}
                  required
                />
              </label>
              
              <label>
                Last Name:
                <input
                  type="text"
                  name="lastName"
                  value={editForm.lastName}
                  onChange={handleChange}
                  required
                />
              </label>
              
              <label>
                Username:
                <input
                  type="text"
                  name="username"
                  value={editForm.username}
                  onChange={handleChange}
                  required
                />
              </label>
              
              <label>
                Email:
                <input
                  type="email"
                  name="email"
                  value={editForm.email}
                  onChange={handleChange}
                  required
                />
              </label>
              
              <label>
                Role:
                <select
                  name="role"
                  value={editForm.role}
                  onChange={handleChange}
                  disabled={editingUser.role === 'admin'} // Prevent changing admin role
                >
                  <option value="player">Player</option>
                  <option value="referee">Referee</option>
                </select>
              </label>
              
              <div className="form-actions">
                <button type="submit">Save</button>
                <button type="button" onClick={() => setEditingUser(null)}>Cancel</button>
              </div>
            </form>
          </div>
        </div>
      )}

<table>
  <thead>
    <tr>
      <th>First</th>
      <th>Last</th>
      <th>Username</th>
      <th>Email</th>
      <th>Role</th>
      <th>Actions</th>
    </tr>
  </thead>
  <tbody>
    {users.map(user => (
      <tr key={user.id}>
        <td>{user.firstName}</td>
        <td>{user.lastName}</td>
        <td>{user.username}</td>
        <td>{user.email}</td>
        <td>{user.role}</td>
        <td>
        <button className="edit-btn" onClick={() => handleEdit(user)}>Edit</button>
        <button className="delete-btn" onClick={() => handleDelete(user.id)}>Delete</button>
        </td>
      </tr>
    ))}
  </tbody>
</table>
    </div>
  );
};

export default ViewUsers;