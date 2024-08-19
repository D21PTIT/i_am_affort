import React, { useEffect, useState } from "react";
import axios from "axios";
import { Line } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";

// Đăng ký các thành phần của Chart.js
ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

const LineChart = () => {
  const [realtime, setRealtime] = useState([]);
  const [date, setDate] = useState([]);
  const [tem, setTem] = useState([]);
  const [hum, setHum] = useState([]);
  const [light, setLight] = useState([]);

  useEffect(() => {
    const fetch10Data = async () => {
      try {
        const response = await axios.get("http://localhost:8080/iot/get10Data");
        const data = response.data.data;
        setRealtime(data);

        // Lấy ra các giá trị cần thiết từ dữ liệu
        const dates = data.map((item) =>
          new Date(item.createdAt).toLocaleTimeString()
        );
        const temperatures = data.map((item) => item.temperature);
        const humidity = data.map((item) => item.humidity);
        const lights = data.map((item) => item.light);

        setDate(dates);
        setTem(temperatures);
        setHum(humidity);
        setLight(lights);
      } catch (err) {
        console.error("Error fetching data:", err);
      }
    };

    fetch10Data();
  }, []);

  const data = {
    labels: date,
    datasets: [
      {
        label: "Nhiệt độ",
        data: tem,
        fill: false,
        backgroundColor: "rgba(75,192,192,1)",
        borderColor: "rgba(75,192,192,1)",
      },
      {
        label: "Độ ẩm",
        data: hum,
        fill: false,
        backgroundColor: "rgba(153,102,255,1)",
        borderColor: "rgba(153,102,255,1)",
      },
      {
        label: "Ánh sáng",
        data: light,
        fill: false,
        backgroundColor: "rgba(255,159,64,1)",
        borderColor: "rgba(255,159,64,1)",
      },
    ],
  };

  const options = {
    scales: {
      y: {
        beginAtZero: true,
      },
    },
  };

  return (
    <div>
      <Line data={data} options={options} />
    </div>
  );
};

export default LineChart;
