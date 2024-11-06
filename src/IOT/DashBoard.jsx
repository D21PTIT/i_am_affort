import { Box, Typography, useTheme } from "@mui/material";
import { tokens } from "./theme";
import LineChart from "./LineChart";
import StatBox from "./StatBox";
import React, { useEffect, useState } from "react";
import io from "socket.io-client";
import AirIcon from '@mui/icons-material/Air'; 
// import './NewStyle.css'; // Include CSS for blinking effect if not added already

const Dashboard = () => {
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);
  const [sensorData, setSensorData] = useState({
    wind: null,
  });
  const [windWarning, setWindWarning] = useState(false); // Track warning state for wind

  useEffect(() => {
    const socket1 = io('http://localhost:8080/1');  // For sensor data
    const socket2 = io('http://localhost:8080/2');  // For warnings

    // Listen for sensor data
    socket1.on('sensorData', (data) => {
      setSensorData({
        wind: data.wind,
      });
    });

    // Listen for warnings
    socket2.on('warning', (warningData) => {
      setWindWarning(warningData.messageValue === 1);  // Start/stop blinking based on warning
    });

    // Cleanup sockets on component unmount
    return () => {
      socket1.disconnect();
      socket2.disconnect();
    };
  }, []);

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
      {/* Horizontal Layout with 3 blocks: Wind Data, Chart, and Warning */}
      <Box
        display="grid"
        gridTemplateColumns="repeat(12, 1fr)"
        gap="20px"
      >
        {/* Wind Data Block */}
        <Box
          gridColumn="span 4"
          backgroundColor={colors.primary[400]}
          display="flex"
          alignItems="center"
          justifyContent="center"
        >
          <StatBox
            title={sensorData.wind !== null ? `${sensorData.wind} m/s` : "N/A"}
            subtitle="Sức gió"
            progress={sensorData.wind / 100}  // Assume max wind speed is 100 m/s for progress
            increase={sensorData.wind !== null ? getWindStatus(sensorData.wind) : "N/A"}
            icon={
              <AirIcon
                sx={{ color: colors.greenAccent[600], fontSize: "26px" }}
              />
            }
          />
        </Box>

        {/* Chart Block */}
        <Box
          gridColumn="span 4"
          backgroundColor={colors.primary[400]}
        >
          <Box
            mt="25px"
            p="0 30px"
            display="flex"
            justifyContent="center"
            alignItems="center"
          >
            <Typography
              variant="h5"
              fontWeight="600"
              color={colors.grey[100]}
            >
              Đồ thị
            </Typography>
          </Box>
          <Box height="250px" m="-20px 0 0 0">
            <LineChart isDashboard={true} />
          </Box>
        </Box>

        {/* Warning Block */}
        <Box
          gridColumn="span 4"
          backgroundColor={windWarning ? 'transparent' : colors.primary[400]}  // Blinking effect for warning
          className={windWarning ? 'blinking' : ''}
          display="flex"
          alignItems="center"
          justifyContent="center"
          style={{ "--primary-color": colors.primary[400] }}  // Pass primary color to CSS
        >
          <Typography
            variant="h6"
            color={colors.grey[100]}
          >
            {windWarning ? "Cảnh báo: Gió mạnh" : "Gió ổn định"}
          </Typography>
        </Box>
      </Box>
    </Box>
  );
};

export default Dashboard;
