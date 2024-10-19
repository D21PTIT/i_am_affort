import { Box, Typography, useTheme } from "@mui/material";
import { Spin } from "antd";
import { tokens } from "./theme";
import React, { useEffect, useState } from "react";
import LightModeIcon from '@mui/icons-material/LightMode'; // Icon cho thiết bị 1
import AcUnitIcon from '@mui/icons-material/AcUnit'; // Icon cho thiết bị 2
import LightbulbIcon from '@mui/icons-material/Lightbulb'; // Icon cho thiết bị 3
import axios from 'axios';
import './lightpub.css'; // Import file CSS chứa hiệu ứng

function Box1(props) {
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);

  const getInitialSwitchStates = () => {
    const storedState = localStorage.getItem('switchStates');
    return storedState ? JSON.parse(storedState) : { switch1: 0, switch2: 0, switch3: 0 };
  };

  const [switchStates, setSwitchStates] = useState(getInitialSwitchStates);
  const [loading, setLoading] = useState({ switch1: false, switch2: false, switch3: false });

  useEffect(() => {
    localStorage.setItem('switchStates', JSON.stringify(switchStates));
  }, [switchStates]);

  const callAPI = async (switchKey, bodyValue) => {
    try {
      const response = await axios.post('http://localhost:8080/test/send', {
        topic: "home/led",
        message: `${switchKey} ${bodyValue}` // Tạo message dạng "x y"
      });
      console.log('API response:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error calling API:', error);
      throw error;
    }
  };

  const handleToggle = (switchKey) => async () => {
    setLoading((prevLoading) => ({
      ...prevLoading,
      [switchKey]: true
    }));

    const newValue = switchStates[switchKey] === 0 ? 1 : 0;

    try {
      const apiResponse = await callAPI(switchKey.replace('switch', ''), newValue);

      setSwitchStates((prevState) => ({
        ...prevState,
        [switchKey]: newValue
      }));
    } catch (error) {
      console.error('API call failed');
    } finally {
      setLoading((prevLoading) => ({
        ...prevLoading,
        [switchKey]: false
      }));
    }
  };

  return (
    <div>
      {/* Header */}
      <Box
        display="flex"
        justifyContent="space-between"
        alignItems="center"
        borderBottom={`4px solid ${colors.primary[500]}`}
        colors={colors.grey[100]}
        p="15px"
      >
        <Typography color={colors.grey[100]} variant="h5" fontWeight="600">
          Danh sách các thiết bị
        </Typography>
      </Box>

      {/* TB1 - Quạt */}
      <Box
        display="flex"
        justifyContent="space-between"
        alignItems="center"
        borderBottom={`4px solid ${colors.primary[500]}`}
        p="15px"
      >
        <Box>
          <Typography
            color={colors.greenAccent[500]}
            variant="h5"
            fontWeight="600"
          >
            _Quạt_
          </Typography>
        </Box>
        <Spin spinning={loading.switch1}>
          <Box color={colors.grey[100]}>
            {switchStates.switch1 === 1 ? (
              <LightModeIcon
                className={switchStates.switch1 === 1 ? 'rotating' : ''}
                sx={{ color: colors.greenAccent[600], fontSize: "26px" }}
              />
            ) : (
              <LightModeIcon sx={{ color: colors.redAccent[600], fontSize: "26px" }} />
            )}
          </Box>
        </Spin>
        <Box
          backgroundColor={switchStates.switch1 === 1 ? colors.redAccent[500] : colors.greenAccent[500]}
          p="5px 10px"
          borderRadius="4px"
          onClick={handleToggle('switch1')}
          style={{ cursor: 'pointer' }}
        >
          {switchStates.switch1 === 1 ? 'Tắt' : 'Bật'}
        </Box>
      </Box>

      {/* TB2 - Điều Hòa */}
      <Box
        display="flex"
        justifyContent="space-between"
        alignItems="center"
        borderBottom={`4px solid ${colors.primary[500]}`}
        p="15px"
      >
        <Box>
          <Typography
            color={colors.greenAccent[500]}
            variant="h5"
            fontWeight="600"
          >
            Điều Hòa
          </Typography>
        </Box>
        <Spin spinning={loading.switch2}>
          <Box color={colors.grey[100]}>
            {switchStates.switch2 === 1 ? (
              <AcUnitIcon
                className={switchStates.switch2 === 1 ? 'rotating' : ''}
                sx={{ color: colors.greenAccent[600], fontSize: "26px" }}
              />
            ) : (
              <AcUnitIcon sx={{ color: colors.redAccent[600], fontSize: "26px" }} />
            )}
          </Box>
        </Spin>
        <Box
          backgroundColor={switchStates.switch2 === 1 ? colors.redAccent[500] : colors.greenAccent[500]}
          p="5px 10px"
          borderRadius="4px"
          onClick={handleToggle('switch2')}
          style={{ cursor: 'pointer' }}
        >
          {switchStates.switch2 === 1 ? 'Tắt' : 'Bật'}
        </Box>
      </Box>

      {/* TB3 - Bóng đèn */}
      <Box
        display="flex"
        justifyContent="space-between"
        alignItems="center"
        borderBottom={`4px solid ${colors.primary[500]}`}
        p="15px"
      >
        <Box>
          <Typography
            color={colors.greenAccent[500]}
            variant="h5"
            fontWeight="600"
          >
            Bóng đèn
          </Typography>
        </Box>
        <Spin spinning={loading.switch3}>
          <Box color={colors.grey[100]}>
            {switchStates.switch3 === 1 ? (
              <LightbulbIcon
                className={switchStates.switch3 === 1 ? 'bouncing' : ''}
                sx={{ color: colors.greenAccent[600], fontSize: "26px" }}
              />
            ) : (
              <LightbulbIcon sx={{ color: colors.redAccent[600], fontSize: "26px" }} />
            )}
          </Box>
        </Spin>
        <Box
          backgroundColor={switchStates.switch3 === 1 ? colors.redAccent[500] : colors.greenAccent[500]}
          p="5px 10px"
          borderRadius="4px"
          onClick={handleToggle('switch3')}
          style={{ cursor: 'pointer' }}
        >
          {switchStates.switch3 === 1 ? 'Tắt' : 'Bật'}
        </Box>
      </Box>

    </div>
  );
}

export default Box1;
