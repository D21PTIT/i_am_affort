import { Box, Typography, useTheme } from "@mui/material";
import { tokens } from "../IOT/theme";
import LineChart from "../IOT/LineChart";
import StatBox from "../IOT/StatBox";
import React, { useEffect, useState } from "react";
import io from "socket.io-client";
import DeviceThermostatIcon from '@mui/icons-material/DeviceThermostat';
import OpacityIcon from '@mui/icons-material/Opacity';
import LightModeIcon from '@mui/icons-material/LightMode';
import AirIcon from '@mui/icons-material/Air'; // New icon for wind data
import Box1 from "../IOT/Box1";
import './NewStyle.css';
import FinalChart from "./FinalChart";

const RareKien = () => {
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);
  const [sensorData, setSensorData] = useState({
    temperature: null,
    humidity: null,
    light: null,
    wind: null,
  });

  const [windWarning, setWindWarning] = useState(false);  // Track warning state for wind

  useEffect(() => {
    const socket1 = io('http://localhost:8080/1');  // Data for sensor readings
    const socket2 = io('http://localhost:8080/2');  // Data for warnings

    // Listen for sensor data on socket1
    socket1.on('sensorData', (data) => {
      setSensorData({
        temperature: data.temperature,
        humidity: data.humidity,
        light: data.light,
        wind: data.wind,
      });
    });

    // Listen for warnings on socket2
    socket2.on('warning', (warningData) => {
      if (warningData.messageValue === 1) {
        setWindWarning(true);  // Start blinking
      } else if (warningData.messageValue === 0) {
        setWindWarning(false);  // Stop blinking
      }
    });

    // Cleanup: disconnect both sockets when the component unmounts
    return () => {
      socket1.disconnect();
      socket2.disconnect();
    };
  }, []);

  const getTemperatureStatus = (temperature) => {
    if (temperature <= 10) {
      return "Rất lạnh";
    } else if (temperature > 10 && temperature <= 20) {
      return "Lạnh";
    } else if (temperature > 20 && temperature <= 30) {
      return "Mát";
    } else if (temperature > 30 && temperature <= 35) {
      return "Ấm";
    } else {
      return "Nóng";
    }
  };

  const getHumidityStatus = (humidity) => {
    if (humidity <= 30) {
      return "Khô";
    } else if (humidity > 30 && humidity <= 50) {
      return "Thoải mái";
    } else if (humidity > 50 && humidity <= 70) {
      return "Ẩm";
    } else {
      return "Rất ẩm";
    }
  };

  const getLightStatus = (light) => {
    if (light <= 500) {
      return "Mờ";
    } else if (light > 500 && light <= 1000) {
      return "Bình thường";
    } else if (light > 1000 && light <= 2000) {
      return "Sáng";
    } else {
      return "Rất sáng";
    }
  };

  const getWindStatus = (wind) => {
    if (wind <= 5) {
      return "Yếu";
    } else if (wind > 5 && wind <= 15) {
      return "Trung bình";
    } else {
      return "Mạnh";
    }
  };

  return (
    <Box m="20px">
      {/* GRID & CHARTS */}
      <Box
        display="grid"
        gridTemplateColumns="repeat(12, 1fr)"
        gridAutoRows="140px"
        gap="20px"
      >
        {/* ROW 1 */}
        <Box
          gridColumn="span 3"
          backgroundColor={colors.primary[400]}
          display="flex"
          alignItems="center"
          justifyContent="center"
        >
          <StatBox
            title={sensorData.temperature !== null ? `${sensorData.temperature}°C` : "N/A"}
            subtitle="Nhiệt độ"
            progress={sensorData.temperature / 60}
            increase={sensorData.temperature !== null ? getTemperatureStatus(sensorData.temperature) : "N/A"}
            icon={
              <DeviceThermostatIcon
                sx={{ color: colors.greenAccent[600], fontSize: "26px" }}
              />
            }
          />
        </Box>

        <Box
          gridColumn="span 3"
          backgroundColor={colors.primary[400]}
          display="flex"
          alignItems="center"
          justifyContent="center"
        >
          <StatBox
            title={sensorData.humidity !== null ? `${sensorData.humidity}%` : "N/A"}
            subtitle="Độ ẩm"
            progress={sensorData.humidity / 100}
            increase={sensorData.humidity !== null ? getHumidityStatus(sensorData.humidity) : "N/A"}
            icon={
              <OpacityIcon
                sx={{ color: colors.greenAccent[600], fontSize: "26px" }}
              />
            }
          />
        </Box>

        <Box
          gridColumn="span 3"
          backgroundColor={colors.primary[400]}
          display="flex"
          alignItems="center"
          justifyContent="center"
        >
          <StatBox
            title={sensorData.light !== null ? `${sensorData.light} lx` : "N/A"}
            subtitle="Ánh sáng"
            progress={sensorData.light / 3000}
            increase={sensorData.light !== null ? getLightStatus(sensorData.light) : "N/A"}
            icon={
              <LightModeIcon
                sx={{ color: colors.greenAccent[600], fontSize: "26px" }}
              />
            }
          />
        </Box>

        {/* New Wind Data Box */}
        <Box
          gridColumn="span 3"
          backgroundColor={windWarning ? 'transparent' : colors.primary[400]}  // Hiệu ứng nhấp nháy với màu đỏ khi cảnh báo kích hoạt
          className={windWarning ? 'blinking' : ''}
          display="flex"
          alignItems="center"
          justifyContent="center"
          style={{ "--primary-color": colors.primary[400] }}  // Truyền màu primary vào CSS
        >
          <StatBox
            title={sensorData.wind !== null ? `${sensorData.wind} m/s` : "N/A"}
            subtitle="Sức gió"
            progress={sensorData.wind / 100}  // Giả sử tốc độ gió tối đa là 100 m/s cho progress
            increase={sensorData.wind !== null ? getWindStatus(sensorData.wind) : "N/A"}
            icon={
              <AirIcon
                sx={{ color: colors.greenAccent[600], fontSize: "26px" }}
              />
            }
          />
        </Box>


        {/* ROW 2 */}
        <Box
          gridColumn="span 8"
          gridRow="span 2"
          backgroundColor={colors.primary[400]}
        >
          <Box
            mt="25px"
            p="0 30px"
            display="flex "
            justifyContent="space-between"
            alignItems="center"
          >
            <Box>
              <Typography
                variant="h5"
                fontWeight="600"
                color={colors.grey[100]}
              >
                Đồ thị
              </Typography>
            </Box>
          </Box>
          <Box height="250px" m="-20px 0 0 0">
            <FinalChart isDashboard={true} />
          </Box>
        </Box>

        <Box
          gridColumn="span 4"
          gridRow="span 2"
          backgroundColor={colors.primary[400]}
          overflow="auto"
        >
          <Box1 />
        </Box>
      </Box>
    </Box>
  );
};

export default RareKien;
