import { useState } from 'react';
import axios from 'axios';
import { useNavigate, Link } from 'react-router-dom';

export default function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const res = await axios.post('http://localhost:8080/auth/login', { email, password });
      const { token, role, id, restaurantId } = res.data;
      
      if (!token && !role) {
          setError(res.data.message);
          return;
      }

      localStorage.setItem('token', token);
      localStorage.setItem('role', role);
      if (id) localStorage.setItem('userId', id);
      if (restaurantId) localStorage.setItem('restaurantId', restaurantId);

      if (role === 'ADMIN') navigate('/admin/dashboard');
      else if (role === 'OWNER') navigate('/owner/dashboard');
      else navigate('/restaurants');
      
    } catch (err) {
      setError(err.response?.data?.message || 'Login failed');
    }
  };

  return (
    <div className="form-card">
      <h2 className="text-center mb-4">Login to FoodApp</h2>
      {error && <p className="mb-4 text-center" style={{color: '#ef4444'}}>{error}</p>}
      <form onSubmit={handleLogin}>
        <input type="email" placeholder="Email" className="form-input" value={email} onChange={e => setEmail(e.target.value)} required />
        <input type="password" placeholder="Password" className="form-input" value={password} onChange={e => setPassword(e.target.value)} required />
        <button type="submit" className="btn">Login</button>
      </form>
      <p className="text-center mt-4">
        Don't have an account? <Link to="/register" className="link-styled">Sign up</Link>
      </p>
    </div>
  );
}
