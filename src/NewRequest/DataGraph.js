import { useState, useEffect } from "react";
import { io } from "socket.io-client";
import { tokens } from "../IOT/theme"; // Đảm bảo bạn đã có file tokens.js để lấy màu sắc

const socket = io("http://localhost:8080/1");

const DataGraph = () => {
  const [data, setData] = useState([
    {
      id: "Nhiệt độ",
      color: tokens("dark").greenAccent[500],
      data: [{ x: "0", y: 0 }],
    },
    {
      id: "Độ ẩm",
      color: tokens("dark").blueAccent[300],
      data: [{ x: "0", y: 0 }],
    },
    {
      id: "Sức gió",
      color: tokens("dark").grey[500],
      data: [{ x: "0", y: 0 }],
    },
    {
      id: "Ánh sáng",
      color: tokens("dark").redAccent[200],
      data: [{ x: "0", y: 0 }],
    }
    
  ]);

  useEffect(() => {
    socket.on("sensorData", (newData) => {
      if (newData && newData.temperature && newData.humidity && newData.light && newData.wind) {
        const currentTime = new Date().toLocaleTimeString();

        setData((prevData) => {
          const updatedData = [...prevData];

          // Cập nhật nhiệt độ
          const temperatureData = updatedData.find((item) => item.id === "Nhiệt độ");
          if (temperatureData) {
            temperatureData.data.push({ x: currentTime, y: newData.temperature });
            if (temperatureData.data.length > 10) temperatureData.data.shift();
          }

          // Cập nhật độ ẩm
          const humidityData = updatedData.find((item) => item.id === "Độ ẩm");
          if (humidityData) {
            humidityData.data.push({ x: currentTime, y: newData.humidity });
            if (humidityData.data.length > 10) humidityData.data.shift();
          }

          // Cập nhật ánh sáng
          const lightData = updatedData.find((item) => item.id === "Ánh sáng");
          if (lightData) {
            lightData.data.push({ x: currentTime, y: (newData.light) });
            if (lightData.data.length > 10) lightData.data.shift();
          }

          const windData = updatedData.find((item) => item.id === "Sức gió");
          if (windData) {
            windData.data.push({ x: currentTime, y: (newData.wind) });
            if (windData.data.length > 10) windData.data.shift();
          }

          return updatedData;
        });
      } else {
        console.error("Dữ liệu nhận được không đúng định dạng:", newData);
      }
    });

    return () => {
      socket.off("sensorData");
    };
  }, []);

  return data;
};

export default DataGraph;
