import { useState, useEffect } from 'react';
import axios from 'axios';

export default function Orders() {
  const [orders, setOrders] = useState([]);
  const role = localStorage.getItem('role');
  const userId = localStorage.getItem('userId');
  const restaurantId = localStorage.getItem('restaurantId');

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        const token = localStorage.getItem('token');
        const url = role === 'OWNER' 
          ? `http://localhost:8080/owner/orders/${restaurantId}`
          : `http://localhost:8080/orders/user/${userId}`;
        
        const res = await axios.get(url, {
          headers: { Authorization: `Bearer ${token}` }
        });
        setOrders(res.data);
      } catch (err) {
        console.error(err);
      }
    };
    fetchOrders();
  }, [role, userId, restaurantId]);

  const handleCancel = async (id) => {
    if (!window.confirm('Are you sure you want to cancel this order?')) return;
    try {
      const token = localStorage.getItem('token');
      await axios.put(`http://localhost:8080/orders/${id}/cancel`, {}, {
        headers: { Authorization: `Bearer ${token}` }
      });
      // Optimistically update the UI status
      setOrders(orders.map(o => o.id === id ? { ...o, status: 'CANCELLED' } : o));
    } catch (err) {
      console.error(err);
      alert('Failed to cancel order');
    }
  };

  return (
    <div>
      <h2>Your Orders</h2>
      {orders.length === 0 ? <p>You have no orders.</p> : (
        <div className="grid">
          {orders.map(order => (
            <div key={order.id} className="card">
              <div className="flex-between">
                <h4>Order #{order.id}</h4>
                <div style={{display: 'flex', flexDirection: 'column', alignItems: 'flex-end', gap: '0.5rem'}}>
                  <span className={order.status === 'CANCELLED' ? "status-badge status-pending" : "status-badge status-approved"}>{order.status}</span>
                  {role === 'USER' && order.status === 'PLACED' && (
                    <button className="btn btn-red" style={{width: 'auto', padding: '0.2rem 0.6rem', fontSize: '0.8rem'}} onClick={() => handleCancel(order.id)}>
                      Cancel
                    </button>
                  )}
                </div>
              </div>
              <p className="price-tag mt-4 mb-4">${order.totalAmount}</p>
              {role === 'USER' && <p><strong>Restaurant:</strong> {order.restaurantName || 'Unknown'}</p>}
              <div style={{marginTop: '1rem', borderTop: '1px solid var(--border)', paddingTop: '0.5rem'}}>
                <ul style={{listStyle: 'none', padding: 0, margin: 0}}>
                  {order.items?.map(item => (
                    <li key={item.id} style={{color: 'var(--text-muted)', fontSize: '0.9rem'}}>
                      • {item.foodItem?.name} <span style={{color: 'var(--primary)', fontWeight: 'bold'}}>x{item.quantity}</span>
                    </li>
                  ))}
                </ul>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
