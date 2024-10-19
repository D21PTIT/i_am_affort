import React, { useEffect, useState } from 'react';
import io from 'socket.io-client';
import useSocketData from '../IOT/useSocketData';

// Initialize the sockets
const socket = io('http://localhost:8080/1'); // Socket for sensor data
const socket2 = io('http://localhost:8080/2'); // Socket for warning data

function RealTime() {
  const [sensorData, setSensorData] = useState({
    temperature: null,
    humidity: null,
    light: null,
    timestamp: null
  });


  const [warning, setWarning] = useState(false); // State to manage warning

  useEffect(() => {
    // Listen to sensorData events from socket 1
    socket.on('sensorData', (data) => {
      setSensorData(data);
      console.log(data);
    });

    // Listen to events from socket 2
    socket2.on('warning', (data) => {
      console.log(data.messageValue);
      if (data.messageValue === 1) {
        setWarning(true); // Set warning to true if data is 1
      } else {
        setWarning(false); // Reset if data is not 1
      }
    });
    
    

    // Cleanup when component is unmounted
    return () => {
      socket.off('sensorData');
      socket2.off('alert');
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

      {/* Flashing warning if warning is true */}
      {warning && (
        <div style={{
          backgroundColor: 'red',
          color: 'white',
          padding: '10px',
          marginTop: '20px',
          textAlign: 'center',
          animation: 'flash 1s infinite'
        }}>
          Warning: Sensor Alert!
        </div>
      )}

      {/* Add CSS for flashing effect */}
      <style>
        {`
          @keyframes flash {
            0% { opacity: 1; }
            50% { opacity: 0; }
            100% { opacity: 1; }
          }
        `}
      </style>
    </div>
  );
}

export default RealTime;
