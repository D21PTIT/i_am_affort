import { useState, useEffect, useRef } from "react";
import { io } from "socket.io-client";
import { tokens } from "../IOT/theme"; // Ensure the tokens.js file is available for color

const socket = io("http://localhost:8080/1");

const useSocketData = () => {
  const [data, setData] = useState([
    {
      id: "Sức gió",
      color: tokens("dark").redAccent[500],
      data: [],
    },
  ]);

  const dataRef = useRef(data);

  useEffect(() => {
    const fetchInitialData = async () => {
      try {
        const response = await fetch("http://localhost:8080/get20value");
        const initialData = await response.json();
        initialData.reverse();

        setData((prevData) => {
          const updatedData = [...prevData];

          initialData.forEach((item) => {
            const currentTime = new Date(item.createdAt).toLocaleTimeString();
            updatedData[0].data.push({ x: currentTime, y: item.wind });
          });

          dataRef.current = updatedData; // Update ref directly
          return updatedData;
        });
      } catch (error) {
        console.error("Error fetching initial data:", error);
      }
    };

    fetchInitialData();

    // Connect to socket to receive new data
    socket.on("sensorData", (newData) => {
      if (newData && newData.wind) {
        const currentTime = new Date().toLocaleTimeString();

        // Use ref to keep data up to date immediately
        const updatedData = [...dataRef.current];

        const windData = updatedData.find((item) => item.id === "Sức gió");
        if (windData) {
          windData.data.push({ x: currentTime, y: newData.wind });
          if (windData.data.length > 10) windData.data.shift();
        }

        // Set both state and ref
        dataRef.current = updatedData;
        setData(updatedData);  // Trigger re-render
      } else {
        console.error("Received data is not in the correct format:", newData);
      }
    });

    return () => {
      socket.off("sensorData");
    };
  }, []);

  return data;
};

export default useSocketData;
