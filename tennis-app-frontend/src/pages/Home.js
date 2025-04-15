import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../style/Home.css'; // Import the CSS file for styling

function Home() {
  const navigate = useNavigate();

  const handleRegister = () => {
    navigate('/register'); // Navigate to register page
  };

  const handleLogin = () => {
    navigate('/login'); // Navigate to login page
  };

  return (
    <div className="home-container">
      <div className="content">
        <h1>Welcome to the Tennis Tournament App</h1>
        <p>Your journey to a great tennis experience begins here!</p>
        <div className="buttons">
          <button onClick={handleRegister} className="btn register-btn">
            Register
          </button>
          <button onClick={handleLogin} className="btn login-btn">
            Login
          </button>
        </div>
      </div>
    </div>
  );
}

export default Home;
