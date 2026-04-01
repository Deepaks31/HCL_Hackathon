import { Link, useNavigate } from 'react-router-dom';

export default function Navbar() {
  const navigate = useNavigate();
  const token = localStorage.getItem('token');
  const role = localStorage.getItem('role');

  const handleLogout = () => {
    localStorage.clear();
    navigate('/');
  };

  return (
    <div className="nav-bar">
      <h2><Link to="/">FoodApp</Link></h2>
      <div className="nav-links">
        {!token ? (
          <>
            <Link to="/">Login</Link>
            <Link to="/register">Sign Up</Link>
          </>
        ) : (
          <>
            {role === 'USER' && (
              <>
                <Link to="/restaurants">Restaurants</Link>
                <Link to="/cart">Cart</Link>
                <Link to="/orders">Orders</Link>
              </>
            )}
            {role === 'OWNER' && (
              <>
                <Link to="/owner/dashboard">Dashboard</Link>
                <Link to="/orders">Order History</Link>
              </>
            )}
            {role === 'ADMIN' && (
              <>
                <Link to="/admin/dashboard">Approvals</Link>
              </>
            )}
            <a href="#" onClick={handleLogout}>Logout</a>
          </>
        )}
      </div>
    </div>
  );
}
