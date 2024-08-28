import React, { useEffect, useState } from "react";
import axios from "axios";
import { Line } from "react-chartjs-2";
import { Switch } from "antd";
import "./Page1.css";
import ReactSpeedometer from "react-d3-speedometer/slim";
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

function Page1() {
  const [data, setData] = useState([]);
  const [switches, setSwitches] = useState({
    1: JSON.parse(localStorage.getItem("device-1")) || false,
    2: JSON.parse(localStorage.getItem("device-2")) || false,
    3: JSON.parse(localStorage.getItem("device-3")) || false,
  });

  const [realtime, setRealtime] = useState([]);
  const [date, setDate] = useState([]);
  const [tem, setTem] = useState([]);
  const [hum, setHum] = useState([]);
  const [light, setLight] = useState([]);

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

  useEffect(() => {
    fetch10Data();
  }, []);

  const fetchDevices = async () => {
    try {
      const response = await axios.get("http://localhost:8080/iot/getAllData");
      const data = response.data;
      setData(data.data);
    } catch (err) {
      console.error("Error fetching users:", err);
    }
  };

  useEffect(() => {
    fetchDevices();
  }, []);

  const onChange = (checked, switchNumber) => {
    let device = "";
    let status = checked ? "on" : "off";

    if (switchNumber === 1) {
      device = "fan";
    }
    if (switchNumber === 2) {
      device = "airc";
    }
    if (switchNumber === 3) {
      device = "lightb";
    }

    // Lưu trạng thái vào Local Storage
    localStorage.setItem(`device-${switchNumber}`, JSON.stringify(checked));

    // Cập nhật trạng thái của switch trong state
    setSwitches((prevSwitches) => ({
      ...prevSwitches,
      [switchNumber]: checked,
    }));

    const updateDeviceStatus = async () => {
      try {
        const response = await axios.post(
          "http://localhost:8080/iot/createDevice",
          {
            tag: switchNumber,
            name: device,
            status: status,
          }
        );
        console.log("Device status updated:", response.data);
      } catch (err) {
        console.error("Error updating device:", err);
      }
    };

    updateDeviceStatus();
  };

  const chartData = {
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

  const chartOptions = {
    scales: {
      y: {
        beginAtZero: true,
      },
    },
  };

  return (
    <div className="page1-container">
      <div className="zone zone1">
        <div>
          <p>Độ ẩm</p>
          <div className="react-speedometer-container">
            <ReactSpeedometer
              value={data[0]?.humidity}
              segments={5}
              maxValue={100}
              width={150}
              height={150}
              segmentColors={[
                "#bf616a",
                "#d08770",
                "#ebcb8b",
                "#a3be8c",
                "#b48ead",
              ]}
              className="react-speedometer"
            />
          </div>

          <p>{data[0]?.humidity}</p>
        </div>
        <div>
          <p>Ánh sáng</p>
          <p>{data[0]?.light}</p>
        </div>

        <div>
          <p>Nhiệt độ</p>
          <p>{data[0]?.temperature}</p>
        </div>
      </div>
      <div className="zone zone2">
        <Line
          data={chartData}
          options={chartOptions}
          style={{ width: "100%", height: "500px" }}
        />
      </div>

      <div className="zone zone3">
        <div>
          <div>
            <p>Quạt</p>
            <Switch
              checked={switches[1]}
              onChange={(checked) => onChange(checked, 1)}
              style={{ marginRight: 8 }}
            />
          </div>

          <div>
            <p>Điều hòa</p>
            <Switch
              checked={switches[2]}
              onChange={(checked) => onChange(checked, 2)}
              style={{ marginRight: 8 }}
            />
          </div>

          <div>
            <p>Bóng điện</p>
            <Switch
              checked={switches[3]}
              onChange={(checked) => onChange(checked, 3)}
              style={{ marginRight: 8 }}
            />
          </div>
        </div>
      </div>
    </div>
  );
}

export default Page1;
