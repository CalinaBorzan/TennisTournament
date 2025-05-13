import axios from 'axios';
import { jwtDecode } from 'jwt-decode';

const api = axios.create({
  baseURL: 'http://localhost:8080',            // <- backend root
  withCredentials: false                       // JWT is in header, not cookie
});

/* ------------------------------------------------------------------ */
/* 1)  Intercept every request and attach the token if we have one     */
/* ------------------------------------------------------------------ */
api.interceptors.request.use(config => {
  const token = localStorage.getItem('jwt');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

/* ------------------------------------------------------------------ */
/* 2)  Auth helpers                                                   */
/* ------------------------------------------------------------------ */
export const registerUser = data =>
  api.post('/api/users/register', data).then(r => r.data);

export const loginUser = creds =>
  api.post('/api/users/login', creds).then(r => {
    const { token, user } = r.data;
    localStorage.setItem('jwt', token);        
    api.defaults.headers.common.Authorization = `Bearer ${token}`; // safety net
    return { token, user };
  });

export const logout = () => localStorage.removeItem('jwt');

export const getCurrentUser = () => {
  const token = localStorage.getItem('jwt');
  if (!token) return null;

  try {
    const payload = jwtDecode(token);
    // Token expired?
    if (Date.now() >= payload.exp * 1000) {
      logout();
      return null;
    }
    return { ...payload };                     // username + role in your token
  } catch {
    logout();
    return null;
  }
};

export default api;   // keep the raw Axios instance for the rest of the app
