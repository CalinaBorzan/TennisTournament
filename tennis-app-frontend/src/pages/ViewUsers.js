import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../style/ViewUsers.css';

const ViewUsers = () => {
  const [users, setUsers] = useState([]);
  const [editingUser, setEditingUser] = useState(null);
  const [editForm, setEditForm] = useState({
    firstName: '',
    lastName: '',
    username: '',
    email: '',
    role: 'player'
  });
  const navigate = useNavigate();

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = () => {
    fetch('http://localhost:8080/api/users/all', {
      credentials: 'include',
    })
      .then(res => {
        console.log('RAW RESPONSE:', res);  // Check response object
        if (res.redirected) {
          console.warn('Unexpected redirect to:', res.url);
          window.location.href = '/login';
          return;
        }
        return res.json();
      })
      .then(data => {
        console.log('PARSED DATA:', data);  // Verify parsed data
        setUsers(data.filter(user => user.role !== 'admin'));
      })
      .catch(err => console.error('Fetch error:', err));
  };
  
  const handleDelete = async (id) => {
    if (!window.confirm('Are you sure you want to delete this user?')) return;
    
    try {
      const response = await fetch(`http://localhost:8080/api/users/delete/${id}`, {
        method: 'DELETE',
        credentials: 'include', // If using cookies/session
      });
      
      if (response.ok) {
        fetchUsers(); // Refresh the list
        navigate('/user-management'); // Redirect back (optional)
      } else {
        const error = await response.text();
        alert(`Delete failed: ${error}`);
      }
    } catch (err) {
      console.error('Delete failed:', err);
      alert('Delete failed. Please try again.');
    }
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

  const handleUpdate = async (e) => {
    e.preventDefault();
    
    try {
      const response = await fetch(`http://localhost:8080/api/users/update-user/${editingUser.id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(editForm),
      });

      if (response.ok) {
        fetchUsers(); // Refresh the list
        setEditingUser(null);
      } else {
        const error = await response.text();
        alert(`Update failed: ${error}`);
      }
    } catch (err) {
      console.error('Update failed:', err);
      alert('Update failed. Please try again.');
    }
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