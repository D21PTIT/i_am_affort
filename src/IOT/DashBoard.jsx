import { Box,  IconButton, Typography, useTheme } from "@mui/material";
import { tokens } from "./theme";
import LineChart from "./LineChart";
import StatBox from "./StatBox";
import React, { useEffect, useState } from "react";
import io from "socket.io-client";
import DeviceThermostatIcon from '@mui/icons-material/DeviceThermostat';
import OpacityIcon from '@mui/icons-material/Opacity';
import LightModeIcon from '@mui/icons-material/LightMode';
import Box1 from "./Box1";
const Dashboard = () => {
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);
  const [sensorData, setSensorData] = useState({
    temperature: null,
    humidity: null,
    light: null,
  });
  useEffect(() => {
    const socket = io('http://localhost:8080/1');
  
    socket.on('sensorData', (data) => {
      setSensorData({
        temperature: data.temperature,
        humidity: data.humidity,
        light: data.light,
      });
    });
  
    return () => {
      socket.disconnect();
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
          style={{ background: 'linear-gradient(120deg, #74ebd5, #acb6e5)' }}
          display="flex"
          alignItems="center"
          justifyContent="center"
        >
          <StatBox
            title={sensorData.temperature !== null ? `${sensorData.temperature}°C` : "N/A"}
            subtitleSx={{ color: colors.primary[400] }}
            subtitle="Nhiệt độ"
            
            progress={sensorData.temperature/60}
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
          style={{ background: 'linear-gradient(120deg, #74ebd5, #acb6e5)' }}
          display="flex"
          alignItems="center"
          justifyContent="center"
        >
          <StatBox
            title={sensorData.humidity !== null ? `${sensorData.humidity}%` : "N/A"}
            subtitle="Độ ẩm"
            progress={sensorData.humidity/100}
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
          style={{ background: 'linear-gradient(120deg, #74ebd5, #acb6e5)' }}
          display="flex"
          alignItems="center"
          justifyContent="center"
        >
          <StatBox
            title={sensorData.light !== null ? `${sensorData.light} lx` : "N/A"}
            subtitle="Ánh sáng"
            progress={sensorData.light/3000}
            increase={sensorData.light !== null ? getLightStatus(sensorData.light) : "N/A"}
            icon={
              <LightModeIcon
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
            <LineChart isDashboard={true} />
          </Box>
        </Box>

        
        <Box
          gridColumn="span 4"
          gridRow="span 2"
          backgroundColor={colors.primary[400]}
          overflow="auto"
        >
        <Box1></Box1>

        </Box>
        
      </Box>
    </Box>
  );
};

export default Dashboard;
