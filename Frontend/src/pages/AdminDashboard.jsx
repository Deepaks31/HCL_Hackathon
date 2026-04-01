import { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

export default function AdminDashboard() {
  const [pendingOwners, setPendingOwners] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const role = localStorage.getItem('role');
    if (role !== 'ADMIN') {
      navigate('/');
      return;
    }
    fetchPending();
  }, [navigate]);

  const fetchPending = async () => {
    try {
      const token = localStorage.getItem('token');
      const res = await axios.get('http://localhost:8080/admin/pending-owners', {
        headers: { Authorization: `Bearer ${token}` }
      });
      setPendingOwners(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  const handleAction = async (id, action) => {
    try {
      const token = localStorage.getItem('token');
      await axios.put(`http://localhost:8080/admin/${action}-owner/${id}`, {}, {
        headers: { Authorization: `Bearer ${token}` }
      });
      fetchPending();
    } catch (err) {
      alert('Action failed');
    }
  };

  return (
    <div>
      <h2>Admin Dashboard: Pending Approvals</h2>
      {pendingOwners.length === 0 ? <p>No pending approvals.</p> : (
        <div className="grid">
          {pendingOwners.map(owner => (
            <div key={owner.id} className="card">
              <div className="flex-between">
                <h3>{owner.name}</h3>
                <span className="status-badge status-pending">Pending</span>
              </div>
              <p>Email: {owner.email}</p>
              <p><strong>Restaurant:</strong> {owner.restaurant?.name} ({owner.restaurant?.location})</p>
              <div className="flex-between mt-4">
                <button className="btn btn-green" style={{width: '48%'}} onClick={() => handleAction(owner.id, 'approve')}>Approve</button>
                <button className="btn btn-red" style={{width: '48%'}} onClick={() => handleAction(owner.id, 'reject')}>Reject</button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
