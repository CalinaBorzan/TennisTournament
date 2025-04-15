import axios from 'axios';

// Set up the base URL to your backend API (update it with your backend URL)
const api = axios.create({
  baseURL: 'http://localhost:8080', // Assuming your backend is running on port 8080
});

// Register function
export const registerUser = async (userData) => {
  try {
    const response = await api.post('/register', userData); // Adjust the endpoint as per your backend
    return response.data;
  } catch (error) {
    console.error('Error registering user', error);
    throw error;
  }
};
export const loginUser = async (credentials) => {
    try {
      const response = await api.post('/api/users/login', credentials);
      if (response.status === 200) {
        return response.data; // Just return the data, let the component handle redirection
      }
      throw new Error('Login failed');
    } catch (error) {
      console.error('Error logging in', error);
      throw error;
    }
  };
// Default export for axios if you need it in other files
export default api;
