import { useNavigate } from 'react-router-dom';

export default function Home() {
  const navigate = useNavigate();
  const token = localStorage.getItem('token');

  return (
    <div className="home-container fade-in">
      {/* Hero Section */}
      <section style={{ textAlign: 'center', padding: '5rem 1rem', background: 'linear-gradient(180deg, rgba(99,102,241,0.06) 0%, transparent 100%)', borderRadius: '32px', marginBottom: '4rem', border: '1px solid rgba(99,102,241,0.1)' }}>
        <h1 style={{ fontSize: '3.8rem', fontWeight: '800', marginBottom: '1.2rem', background: 'linear-gradient(135deg, var(--primary), var(--secondary))', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent', lineHeight: 1.1 }}>
          Delicious Food,<br/>Delivered Fast.
        </h1>
        <p style={{ fontSize: '1.25rem', color: 'var(--text-muted)', maxWidth: '650px', margin: '0 auto 2.5rem', lineHeight: 1.6 }}>
          Discover the best local restaurants, browse mouth-watering menus, and have your favorite meals delivered straight to your door in minutes.
        </p>
        <div style={{ display: 'flex', gap: '1rem', justifyContent: 'center', flexWrap: 'wrap' }}>
          <button 
            className="btn" 
            style={{ width: 'auto', padding: '1.1rem 2.8rem', fontSize: '1.15rem', borderRadius: '50px' }}
            onClick={() => navigate(token ? '/restaurants' : '/login')}
          >
            Start Ordering
          </button>
          {!token && (
            <button 
              className="btn" 
              style={{ width: 'auto', padding: '1.1rem 2.8rem', fontSize: '1.15rem', borderRadius: '50px', background: 'white', color: 'var(--primary)', border: '2px solid var(--primary)', boxShadow: 'none' }}
              onClick={() => navigate('/register')}
            >
              Partner With Us
            </button>
          )}
        </div>
      </section>

      {/* How it Works Section */}
      <section style={{ marginBottom: '5rem' }}>
        <h2 style={{ textAlign: 'center', fontSize: '2.2rem', marginBottom: '3rem', fontWeight: '700' }}>How It Works</h2>
        <div className="grid">
          <div className="card" style={{ textAlign: 'center', padding: '3rem 2rem', alignItems: 'center' }}>
            <div style={{ fontSize: '3.5rem', marginBottom: '1.5rem', background: 'rgba(99,102,241,0.1)', width: '90px', height: '90px', display: 'flex', alignItems: 'center', justifyContent: 'center', borderRadius: '50%' }}>🍔</div>
            <h3 style={{fontSize: '1.5rem'}}>1. Browse Menus</h3>
            <p style={{fontSize: '1.05rem'}}>Explore top-rated local restaurants and discover a wide variety of cuisines.</p>
          </div>
          <div className="card" style={{ textAlign: 'center', padding: '3rem 2rem', alignItems: 'center' }}>
            <div style={{ fontSize: '3.5rem', marginBottom: '1.5rem', background: 'rgba(236,72,153,0.1)', width: '90px', height: '90px', display: 'flex', alignItems: 'center', justifyContent: 'center', borderRadius: '50%' }}>🛒</div>
            <h3 style={{fontSize: '1.5rem'}}>2. Place Order</h3>
            <p style={{fontSize: '1.05rem'}}>Add your favorite items to your cart and seamlessly proceed to checkout.</p>
          </div>
          <div className="card" style={{ textAlign: 'center', padding: '3rem 2rem', alignItems: 'center' }}>
            <div style={{ fontSize: '3.5rem', marginBottom: '1.5rem', background: 'rgba(16,185,129,0.1)', width: '90px', height: '90px', display: 'flex', alignItems: 'center', justifyContent: 'center', borderRadius: '50%' }}>🚀</div>
            <h3 style={{fontSize: '1.5rem'}}>3. Fast Delivery</h3>
            <p style={{fontSize: '1.05rem'}}>Sit back and relax while your fresh food is prepared and delivered instantly.</p>
          </div>
        </div>
      </section>

      {/* Visual Banner */}
      <section style={{ 
        background: 'url(https://images.unsplash.com/photo-1504674900247-0877df9cc836?q=80&w=2070) center/cover', 
        borderRadius: '32px', 
        padding: '6rem 2rem', 
        textAlign: 'center',
        position: 'relative',
        overflow: 'hidden',
        boxShadow: 'var(--shadow-lg)'
      }}>
        <div style={{ position: 'absolute', top: 0, left: 0, width: '100%', height: '100%', background: 'linear-gradient(rgba(0,0,0,0.4), rgba(0,0,0,0.8))' }}></div>
        <div style={{ position: 'relative', zIndex: 1, color: 'white' }}>
          <h2 style={{ fontSize: '3rem', marginBottom: '1.2rem', color: 'white', fontWeight: '800' }}>Craving Something Special?</h2>
          <p style={{ fontSize: '1.25rem', marginBottom: '2.5rem', opacity: 0.9, maxWidth: '500px', margin: '0 auto 2.5rem' }}>Join thousands of happy customers today and satisfy your hunger.</p>
          <button 
            className="btn btn-green" 
            style={{ width: 'auto', margin: '0 auto', padding: '1.2rem 3.5rem', fontSize: '1.2rem', borderRadius: '50px', boxShadow: '0 10px 25px rgba(16,185,129,0.4)' }}
            onClick={() => navigate(token ? '/restaurants' : '/register')}
          >
            {token ? 'Browse Restaurants' : 'Create an Account'}
          </button>
        </div>
      </section>
    </div>
  );
}
