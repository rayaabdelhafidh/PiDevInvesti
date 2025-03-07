import React from 'react';
import { BrowserRouter as Router, Routes, Route, useParams, Link } from 'react-router-dom';
import ProjectAnalytics from './ProjectAnalytics';
import StockChart from './StockChart';
import './App.css'; // Import custom styles

const App = () => {
    const ProjectAnalyticsWrapper = () => {
        const { id } = useParams(); // Access the 'id' parameter from the URL
        return <ProjectAnalytics projectId={id} />;
    };

    return (
        <Router>
            <div className="app-container">
                <header className="app-header">
                    <h1>Welcome to the Project Analytics App</h1>
                </header>
                <nav className="app-nav">
                    <Link to="/" className="nav-link">Stock Chart</Link>
                    <Link to="/projectanalytics/" className="nav-link">Project Analytics</Link>
                </nav>
                <main className="app-main">
                    <Routes>
                        <Route path="/" element={<StockChart symbol="AAPL" />} />
                        <Route path="/projectanalytics/:id" element={<ProjectAnalyticsWrapper />} />
                    </Routes>
                </main>
            </div>
        </Router>
    );
};

export default App;
