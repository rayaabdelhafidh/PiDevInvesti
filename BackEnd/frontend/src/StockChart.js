import React, { useState, useEffect, useRef } from "react";
import Chart from "chart.js/auto";
import './StockChart.css'; // Importing the custom CSS file

const StockChart = () => {
    const [stockData, setStockData] = useState(null);
    const [loading, setLoading] = useState(false);
    const [symbol, setSymbol] = useState("AAPL");
    const chartRef = useRef(null);

    const symbolOptions = ["AAPL", "MSFT", "GOOG", "AMZN", "TSLA", "FB"];

    const fetchData = () => {
        const apiKey = process.env.REACT_APP_ALPHA_VANTAGE_API_KEY;
        const apiUrl = `https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=${symbol}&apikey=${apiKey}`;

        setLoading(true);
        fetch(apiUrl)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                console.log("API Response Data:", data); // Log the full response
                if (data["Time Series (Daily)"]) {
                    const timeSeries = data["Time Series (Daily)"];
                    const labels = Object.keys(timeSeries).slice(0, 10);
                    const prices = labels.map(date => parseFloat(timeSeries[date]["4. close"]));

                    setStockData({ labels, prices });
                } else {
                    console.error("Invalid data structure from Alpha Vantage:", data);
                    setStockData(null); // Ensure no data is set
                }
            })
            .catch(error => {
                console.error("Error fetching stock data:", error);
                setStockData(null); // Ensure no data is set
            })
            .finally(() => setLoading(false));
    };

    useEffect(() => {
        fetchData();
    }, [symbol]);

    useEffect(() => {
        if (stockData && chartRef.current) {
            const chartInstance = new Chart(chartRef.current, {
                type: "line",
                data: {
                    labels: stockData.labels,
                    datasets: [
                        {
                            label: "Prix de l'action (" + symbol + ")",
                            data: stockData.prices,
                            borderColor: "#007bff", // Updated color
                            backgroundColor: "rgba(0, 123, 255, 0.2)", // Light blue background
                            fill: true,
                        },
                    ],
                },
                options: {
                    responsive: true,
                    scales: {
                        x: {
                            title: {
                                display: true,
                                text: 'Date',
                            },
                        },
                        y: {
                            title: {
                                display: true,
                                text: 'Prix',
                            },
                            ticks: {
                                beginAtZero: false,
                            },
                        },
                    },
                },
            });

            return () => {
                chartInstance.destroy();
            };
        }
    }, [stockData, symbol]);

    const handleSymbolChange = (event) => {
        setSymbol(event.target.value);
    };

    return (
        <div className="stock-chart-container">
            <div className="dropdown-container">
                <label htmlFor="symbol-select">Select Stock: </label>
                <select
                    id="symbol-select"
                    value={symbol}
                    onChange={handleSymbolChange}
                    className="dropdown"
                >
                    {symbolOptions.map((option) => (
                        <option key={option} value={option}>
                            {option}
                        </option>
                    ))}
                </select>
            </div>

            {loading && <p className="loading-message">Loading stock data...</p>}
            {stockData && <canvas ref={chartRef}></canvas>}
            {!loading && !stockData && <p className="error-message">Failed to load stock data.</p>}
        </div>
    );
};

export default StockChart;
