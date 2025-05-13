import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { loginUser } from "../api/auth";
import { useAuth } from "../context/AuthContext";        // ①  add
import "../style/Login.css";

function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage]   = useState("");
  const navigate = useNavigate();
  const { setUser } = useAuth();                         // ②  grab setter

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const { user } = await loginUser({ username, password });   // ③
      localStorage.setItem("userId", user.id);  // optional convenience
      setUser(user);                            // ④  tell context
      setMessage("Login successful!");
      navigate("/dashboard");
    } catch (err) {
      console.error("Login error:", err);
      setMessage("Login failed. Please check your credentials.");
    }
  };

  return (
    <div className="login-container">
      <div className="login-card">
        <h2>Login to TennisApp</h2>
        <p className="subtitle">Enter your credentials below</p>

        <form onSubmit={handleLogin} className="login-form">
          <input
            type="text"
            placeholder="Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <button type="submit">Log In</button>
        </form>

        {message && <p className="login-message">{message}</p>}
      </div>
    </div>
  );
}

export default Login;
