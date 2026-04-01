import { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams, useNavigate } from 'react-router-dom';

export default function Menu() {
  const { id } = useParams();
  const [menu, setMenu] = useState([]);
  const [quantities, setQuantities] = useState({});
  const [searchTerm, setSearchTerm] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchMenu = async () => {
      try {
        const token = localStorage.getItem('token');
        const res = await axios.get(`http://localhost:8080/restaurants/${id}/menu`, {
          headers: { Authorization: `Bearer ${token}` }
        });
        setMenu(res.data);
      } catch (err) {
        console.error(err);
      }
    };
    fetchMenu();
  }, [id]);

  const addToCart = async (foodId, quantity) => {
    try {
      const token = localStorage.getItem('token');
      const userId = localStorage.getItem('userId');
      await axios.post('http://localhost:8080/cart/add', {
        userId: parseInt(userId),
        foodItemId: foodId,
        quantity: quantity
      }, {
        headers: { Authorization: `Bearer ${token}` }
      });
      alert('Added to cart');
    } catch (err) {
      alert('Failed to add to cart');
    }
  };

  const filteredMenu = menu.filter(m => 
    m.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div>
      <div className="flex-between" style={{marginBottom: '1rem'}}>
        <h2 style={{margin: 0}}>Menu</h2>
        <input 
          type="text" 
          placeholder="Search food items..." 
          className="form-input" 
          style={{marginBottom: 0, width: '250px'}}
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
      </div>
      
      <button className="btn mb-4" style={{width: 'auto'}} onClick={() => navigate('/restaurants')}>Back to Restaurants</button>
      
      {filteredMenu.length === 0 ? <p>No food items found matching your search.</p> : (
        <div className="grid">
          {filteredMenu.map(item => (
            <div key={item.id} className="card">
              <img src={item.imageUrl || 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80'} alt={item.name} style={{width: '100%', height: '180px', objectFit: 'cover', borderRadius: '8px', marginBottom: '1rem'}} />
              <h3>{item.name}</h3>
              <p className="price-tag mt-4">${item.price}</p>
              
              <p style={{fontSize: '0.9rem', marginTop: '0.5rem', color: item.availableQuantity > 0 ? 'var(--text-muted)' : '#ef4444'}}>
                {item.availableQuantity > 0 ? `In Stock: ${item.availableQuantity}` : 'Out of Stock'}
              </p>

              {item.availableQuantity > 0 ? (
                <>
                  <div style={{display: 'flex', alignItems: 'center', gap: '0.75rem', marginTop: '1rem'}}>
                    <button className="btn btn-red" style={{width: '32px', height: '32px', padding: 0, display: 'flex', alignItems: 'center', justifyContent: 'center'}} onClick={() => setQuantities(prev => ({...prev, [item.id]: Math.max(1, (prev[item.id] || 1) - 1)}))}>-</button>
                    <span style={{fontWeight: 'bold', fontSize: '1.2rem'}}>{Math.min(quantities[item.id] || 1, item.availableQuantity)}</span>
                    <button className="btn btn-green" style={{width: '32px', height: '32px', padding: 0, display: 'flex', alignItems: 'center', justifyContent: 'center'}} onClick={() => setQuantities(prev => ({...prev, [item.id]: Math.min((prev[item.id] || 1) + 1, item.availableQuantity)}))}>+</button>
                  </div>
                  <button className="btn mt-4" onClick={() => addToCart(item.id, quantities[item.id] || 1)}>Add to Cart</button>
                </>
              ) : (
                <button className="btn mt-4" style={{background: 'var(--border)', cursor: 'not-allowed'}} disabled>Unavailable</button>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
