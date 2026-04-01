import { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

export default function Restaurants() {
  const [restaurants, setRestaurants] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchRestaurants = async () => {
      try {
        const token = localStorage.getItem('token');
        const res = await axios.get('http://localhost:8080/restaurants', {
          headers: { Authorization: `Bearer ${token}` }
        });
        setRestaurants(res.data);
      } catch (err) {
        console.error(err);
      }
    };
    fetchRestaurants();
  }, []);

  const filteredRestaurants = restaurants.filter(r => 
    r.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div>
      <div className="flex-between" style={{marginBottom: '1.5rem'}}>
        <h2 style={{margin: 0}}>Restaurants</h2>
        <input 
          type="text" 
          placeholder="Search restaurants..." 
          className="form-input" 
          style={{marginBottom: 0, width: '250px'}}
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
      </div>
      
      {filteredRestaurants.length === 0 ? <p>No restaurants found.</p> : (
        <div className="grid">
          {filteredRestaurants.map(r => (
            <div key={r.id} className="card">
              <h3>{r.name}</h3>
              <p>{r.location}</p>
              <button className="btn mt-4" onClick={() => navigate(`/restaurants/${r.id}/menu`)}>
                View Menu
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
