import React, { useEffect, useState } from 'react';
import io from 'socket.io-client';

const socket = io('http://localhost:8080'); // Địa chỉ của BE

function RealTime() {
  const [sensorData, setSensorData] = useState({
    temperature: null,
    humidity: null,
    light: null,
    timestamp: null
  });

  useEffect(() => {
    // Lắng nghe sự kiện 'sensorData' từ BE
    socket.on('sensorData', (data) => {
      setSensorData(data);
    });

    // Cleanup khi component bị hủy
    return () => {
      socket.off('sensorData');
    };
  }, []);

  return (
    <div>
      <h1>Real-Time Sensor Dashboard</h1>
      <div>
        <h2>Temperature: {sensorData.temperature ? `${sensorData.temperature} °C` : 'Loading...'}</h2>
        <h2>Humidity: {sensorData.humidity ? `${sensorData.humidity} %` : 'Loading...'}</h2>
        <h2>Brightness: {sensorData.light ? `${sensorData.light}` : 'Loading...'}</h2>
        <h3>Last Updated: {sensorData.timestamp ? new Date(sensorData.timestamp).toLocaleTimeString() : 'Loading...'}</h3>
      </div>
    </div>
  );
}

export default RealTime;
