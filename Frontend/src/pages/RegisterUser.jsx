import { useState } from 'react';
import axios from 'axios';
import { useNavigate, Link } from 'react-router-dom';

export default function RegisterUser() {
  const [role, setRole] = useState('USER');
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [restaurantName, setRestaurantName] = useState('');
  const [location, setLocation] = useState('');
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleRegister = async (e) => {
    e.preventDefault();
    try {
      if (role === 'USER') {
        const res = await axios.post('http://localhost:8080/auth/register', { name, email, password });
        setMessage(res.data.message || 'Registration successful. You can now login.');
      } else {
        const res = await axios.post('http://localhost:8080/auth/register-owner', { name, email, password, restaurantName, location });
        setMessage(res.data.message || 'Registration successful. Admin approval required to login.');
      }
      setError('');
      setTimeout(() => navigate('/'), 2500);
    } catch (err) {
      setError(err.response?.data?.message || 'Registration failed');
      setMessage('');
    }
  };

  return (
    <div className="form-card">
      <h2 className="text-center mb-4">Create Account</h2>
      
      <div style={{ display: 'flex', gap: '1rem', marginBottom: '2rem', justifyContent: 'center' }}>
        <button 
          type="button"
          onClick={() => setRole('USER')}
          className="btn"
          style={{
            background: role === 'USER' ? 'var(--primary)' : 'var(--text-muted)',
            boxShadow: role === 'USER' ? 'var(--shadow-md)' : 'none'
          }}
        >
          Customer
        </button>
        <button 
          type="button"
          onClick={() => setRole('OWNER')}
          className="btn"
          style={{
            background: role === 'OWNER' ? 'var(--primary)' : 'var(--text-muted)',
            boxShadow: role === 'OWNER' ? 'var(--shadow-md)' : 'none'
          }}
        >
          Partner with Us
        </button>
      </div>

      {error && <p className="mb-4 text-center" style={{color: '#ef4444', fontWeight: '500'}}>{error}</p>}
      {message && <p className="mb-4 text-center" style={{color: '#10b981', fontWeight: '500'}}>{message}</p>}

      <form onSubmit={handleRegister}>
        <input type="text" placeholder="Full Name" className="form-input" value={name} onChange={e => setName(e.target.value)} required />
        <input type="email" placeholder="Email" className="form-input" value={email} onChange={e => setEmail(e.target.value)} required />
        <input type="password" placeholder="Password" className="form-input" value={password} onChange={e => setPassword(e.target.value)} required />
        
        {role === 'OWNER' && (
          <div className="fade-in">
            <h4 style={{marginTop: '1rem', marginBottom: '1rem', color: 'var(--text-main)'}}>Restaurant Details</h4>
            <input type="text" placeholder="Restaurant Name" className="form-input" value={restaurantName} onChange={e => setRestaurantName(e.target.value)} required />
            <input type="text" placeholder="Location" className="form-input" value={location} onChange={e => setLocation(e.target.value)} required />
          </div>
        )}

        <button type="submit" className="btn mt-4">Sign Up</button>
      </form>
      <p className="text-center mt-4">
        Already have an account? <Link to="/" className="link-styled">Login here</Link>
      </p>
    </div>
  );
}
