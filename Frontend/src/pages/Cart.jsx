import { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

export default function Cart() {
  const [cart, setCart] = useState(null);
  const navigate = useNavigate();
  const userId = localStorage.getItem('userId');

  useEffect(() => {
    fetchCart();
  }, []);

  const fetchCart = async () => {
    try {
      const token = localStorage.getItem('token');
      const res = await axios.get(`http://localhost:8080/cart/${userId}`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      setCart(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  const removeRow = async (itemId) => {
    try {
      const token = localStorage.getItem('token');
      await axios.delete(`http://localhost:8080/cart/remove/${itemId}`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      fetchCart();
    } catch (err) {
      console.error(err);
    }
  };

  const placeOrder = async () => {
    if (!cart || !cart.items || cart.items.length === 0) return;
    const restaurantId = cart.items[0].foodItem.restaurantId;
    try {
      const token = localStorage.getItem('token');
      await axios.post('http://localhost:8080/orders', {
        userId: parseInt(userId),
        restaurantId: restaurantId
      }, {
        headers: { Authorization: `Bearer ${token}` }
      });
      alert('Order placed successfully!');
      navigate('/orders');
    } catch (err) {
      alert(err.response?.data?.message || 'Failed to place order');
    }
  };

  let total = 0;
  if (cart && cart.items) {
    total = cart.items.reduce((acc, item) => acc + (item.foodItem.price * item.quantity), 0);
  }

  return (
    <div>
      <h2>Your Cart</h2>
      {!cart || !cart.items || cart.items.length === 0 ? <p>Cart is empty.</p> : (
        <div>
          <div className="grid">
            {cart.items.map(item => (
              <div key={item.id} className="card flex-between">
                <div>
                  <h4>{item.foodItem.name}</h4>
                  <p>{item.quantity} x ${item.foodItem.price}</p>
                </div>
                <button className="btn btn-red" style={{width: 'auto'}} onClick={() => removeRow(item.id)}>Remove</button>
              </div>
            ))}
          </div>
          <div className="card mt-4" style={{maxWidth: '400px'}}>
            <h3>Total: ${total.toFixed(2)}</h3>
            <button className="btn btn-green mt-4" onClick={placeOrder}>Place Order</button>
          </div>
        </div>
      )}
    </div>
  );
}
