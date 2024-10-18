import React, { useEffect, useState } from 'react';
import io from 'socket.io-client';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import 'bootstrap/dist/css/bootstrap.min.css';  // Import Bootstrap

const socket = io('http://localhost:8080/1');  // Kết nối tới server Backend

function SensorChart() {
  const [data, setData] = useState([]);
  useEffect(() => {
    // Lắng nghe sự kiện 'sensorData' từ Backend qua Socket.IO
    socket.on('sensorData', (newData) => {
      setData((prevData) => [
        ...prevData,
        {
          time: new Date().toLocaleTimeString(),
          temperature: newData.temperature,
          humidity: newData.humidity,
          brightness: newData.light , 
        },
      ].slice(-15)); // Giữ lại 15 giá trị gần nhất
    });

    // Cleanup khi component bị hủy
    return () => {
      socket.off('sensorData');
    };
  }, []);

  return (
    <div className="container mt-5">
      <h2 className="text-center">Real-time Sensor Data</h2>
      <ResponsiveContainer width="100%" height={400}>
        <LineChart data={data}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="time" />
          {/* Một trục Y cho cả nhiệt độ, độ ẩm và độ sáng đã chia cho 10 */}
          <YAxis />
          <Tooltip />
          <Legend />
          <Line type="monotone" dataKey="temperature" stroke="#8884d8" name="Temperature (°C)" />
          <Line type="monotone" dataKey="humidity" stroke="#82ca9d" name="Humidity (%)" />
          <Line type="monotone" dataKey="brightness" stroke="#ffc658" name="Brightness (lx)" />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
}

export default SensorChart;
