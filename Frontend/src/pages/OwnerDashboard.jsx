import { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

export default function OwnerDashboard() {
  const [foodName, setFoodName] = useState('');
  const [price, setPrice] = useState('');
  const [quantity, setQuantity] = useState('');
  const [imageUrl, setImageUrl] = useState('');
  const [menuItems, setMenuItems] = useState([]);
  const [editingId, setEditingId] = useState(null);
  const navigate = useNavigate();
  
  const restaurantId = localStorage.getItem('restaurantId'); 

  useEffect(() => {
    const role = localStorage.getItem('role');
    if (role !== 'OWNER') {
        navigate('/');
    } else {
        fetchMenu();
    }
  }, [navigate]);

  const fetchMenu = async () => {
    try {
      const token = localStorage.getItem('token');
      const res = await axios.get(`http://localhost:8080/restaurants/${restaurantId}/menu`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      setMenuItems(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  const handleAddOrUpdateFood = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem('token');
      const payload = {
        name: foodName,
        price: parseFloat(price),
        availableQuantity: parseInt(quantity),
        imageUrl: imageUrl,
        restaurantId
      };
      
      if (editingId) {
        await axios.put(`http://localhost:8080/owner/foods/${editingId}`, payload, {
          headers: { Authorization: `Bearer ${token}` }
        });
        alert('Food updated successfully');
      } else {
        await axios.post('http://localhost:8080/owner/foods', payload, {
          headers: { Authorization: `Bearer ${token}` }
        });
        alert('Food added successfully');
      }
      
      setFoodName('');
      setPrice('');
      setQuantity('');
      setImageUrl('');
      setEditingId(null);
      fetchMenu();
    } catch (err) {
      alert(editingId ? 'Failed to update food' : 'Failed to add food');
    }
  };

  const handleEdit = (item) => {
    setEditingId(item.id);
    setFoodName(item.name);
    setPrice(item.price);
    setQuantity(item.availableQuantity);
    setImageUrl(item.imageUrl || '');
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Are you sure you want to delete this item?')) return;
    try {
      const token = localStorage.getItem('token');
      await axios.delete(`http://localhost:8080/owner/foods/${id}`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      alert('Food deleted successfully');
      fetchMenu();
    } catch (err) {
      alert('Cannot delete food item. It may be linked to existing orders. Try setting quantity to 0 instead.');
    }
  };

  return (
    <div>
      <h2>Owner Dashboard</h2>
      
      <div className="form-card" style={{margin: '2rem 0', maxWidth: '100%'}}>
        <h3>{editingId ? 'Update Menu Item' : 'Add Menu Item'}</h3>
        <form onSubmit={handleAddOrUpdateFood} style={{display: 'flex', gap: '1rem', alignItems: 'center'}}>
          <input type="text" placeholder="Food Name" className="form-input" style={{marginBottom: 0}} value={foodName} onChange={e => setFoodName(e.target.value)} required />
          <input type="number" placeholder="Price" className="form-input" style={{marginBottom: 0}} value={price} onChange={e => setPrice(e.target.value)} required min="0" step="0.01" />
          <input type="number" placeholder="Quantity" className="form-input" style={{marginBottom: 0}} value={quantity} onChange={e => setQuantity(e.target.value)} required min="0" />
          <input type="url" placeholder="Image URL (Optional)" className="form-input" style={{marginBottom: 0}} value={imageUrl} onChange={e => setImageUrl(e.target.value)} />
          
          <button type="submit" className="btn" style={{width: 'auto'}}>
            {editingId ? 'Update Food' : 'Add Food'}
          </button>
          
          {editingId && (
            <button type="button" className="btn btn-red" style={{width: 'auto'}} onClick={() => {
              setEditingId(null); setFoodName(''); setPrice(''); setQuantity(''); setImageUrl('');
            }}>Cancel</button>
          )}
        </form>
      </div>

      <h3>Your Active Menu</h3>
      {menuItems.length === 0 ? <p>You have not added any menu items yet.</p> : (
        <div className="grid">
          {menuItems.map(item => (
            <div key={item.id} className="card">
              <img src={item.imageUrl || 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80'} alt={item.name} style={{width: '100%', height: '150px', objectFit: 'cover', borderRadius: '8px', marginBottom: '1rem'}} />
              <h4>{item.name}</h4>
              <p className="price-tag mt-2">${item.price}</p>
              <p className="mt-2" style={{color: 'var(--text-muted)'}}>
                Available Stock: <strong style={{color: item.availableQuantity > 0 ? 'var(--primary)' : 'var(--text-danger)'}}>
                  {item.availableQuantity}
                </strong>
              </p>
              
              <div style={{display: 'flex', gap: '0.75rem', marginTop: '1.5rem'}}>
                <button className="btn" style={{width: '100px', padding: '0.5rem'}} onClick={() => handleEdit(item)}>Edit</button>
                <button className="btn btn-red" style={{width: '100px', padding: '0.5rem'}} onClick={() => handleDelete(item.id)}>Delete</button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
