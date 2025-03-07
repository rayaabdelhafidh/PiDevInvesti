import React, { useEffect, useState, useRef } from "react";
import axios from "axios";
import { Line, Bar } from "react-chartjs-2";
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    BarElement,
    Title,
    Tooltip,
    Legend,
} from "chart.js";

ChartJS.register(
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    BarElement,
    Title,
    Tooltip,
    Legend
);

const ProjectAnalytics = ({ projectId }) => {
    const [analytics, setAnalytics] = useState(null);
    const apiUrl = process.env.REACT_APP_API_BASE_URL;

    useEffect(() => {
        if (!projectId) return;

        axios
            .get(`${apiUrl}/project/analytics/projects/${projectId}`)
            .then((response) => setAnalytics(response.data))
            .catch((error) => {
                console.error("Error fetching analytics:", error);
                setAnalytics(null);
            });
    }, [projectId, apiUrl]);

    if (!analytics) return <p>Loading...</p>;

    const investmentData = {
        labels: Object.keys(analytics.investmentTrends),
        datasets: [
            {
                label: "Investments Over Time",
                data: Object.values(analytics.investmentTrends),
                backgroundColor: "rgba(54, 162, 235, 0.5)",
                borderColor: "rgba(54, 162, 235, 1)",
                borderWidth: 2,
                fill: true,
            },
        ],
    };

    const returnData = {
        labels: Object.keys(analytics.returnTrends),
        datasets: [
            {
                label: "Returns Over Time",
                data: Object.values(analytics.returnTrends),
                backgroundColor: "rgba(75, 192, 192, 0.5)",
                borderColor: "rgba(75, 192, 192, 1)",
                borderWidth: 2,
                fill: true,
            },
        ],
    };

    return (
        <div style={{ maxWidth: "1000px", margin: "auto", textAlign: "center", fontFamily: "Arial, sans-serif" }}>
            <h1 style={{ fontSize: "28px", marginBottom: "10px" }}>📊 Project Analytics</h1>

            {/* Key Metrics */}
            <div style={{
                display: "flex",
                justifyContent: "space-around",
                background: "#f8f9fa",
                padding: "15px",
                borderRadius: "10px",
                boxShadow: "0 4px 6px rgba(0, 0, 0, 0.1)",
                marginBottom: "20px"
            }}>
                <p><strong>Total Invested:</strong> ${analytics.totalInvested}</p>
                <p><strong>Total Return:</strong> ${analytics.totalReturn}</p>
                <p><strong>Average ROI:</strong> {analytics.avgROI}%</p>
            </div>

            {/* Charts in a row */}
            <div style={{
                display: "flex",
                justifyContent: "center",
                gap: "20px",
                flexWrap: "wrap"
            }}>
                <div style={{
                    width: "400px",
                    height: "300px",
                    background: "white",
                    padding: "15px",
                    borderRadius: "10px",
                    boxShadow: "0 4px 6px rgba(0, 0, 0, 0.1)"
                }}>
                    <h3 style={{ fontSize: "18px" }}>📈 Investments</h3>
                    <Line data={investmentData} options={{ maintainAspectRatio: false }} />
                </div>

                <div style={{
                    width: "400px",
                    height: "300px",
                    background: "white",
                    padding: "15px",
                    borderRadius: "10px",
                    boxShadow: "0 4px 6px rgba(0, 0, 0, 0.1)"
                }}>
                    <h3 style={{ fontSize: "18px" }}>📊 Returns</h3>
                    <Bar data={returnData} options={{ maintainAspectRatio: false }} />
                </div>
            </div>
        </div>
    );
};

export default ProjectAnalytics;
