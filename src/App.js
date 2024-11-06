import React, { useEffect, useState } from 'react';
import { Layout, Menu } from 'antd';
import { Route, Routes, Link } from 'react-router-dom';

import SupperKien from './components/SupperKien';
import MegaKien from './components/MegaKien';
import Legendary from './components/Legendary';
import RealTime from './components/RealTime';
import Dashboard from './IOT/DashBoard';
import { ThemeProvider, CssBaseline } from '@mui/material';
import {  useMode } from './IOT/theme';
import EpicKien from './NewRequest/EpicKien';
import RareKien from './NewRequest/RareKien';
import Contact from './components/Contact';
import io from 'socket.io-client';
import AntdTest from './NewRequest/AntdTest';
const socket = io('http://localhost:8080/3');

const { Header, Content, Footer } = Layout;

const App = () => {
  const [theme, colorMode] = useMode();
  const [warningCount, setWarningCount] = useState(0);
  useEffect(() => {
    // Kết nối socket và lắng nghe sự kiện `countWarning`
    socket.on('countWarning', (data) => {
        setWarningCount(data.count1);
        console.log(data);
    });

    // Cleanup khi component unmount để tránh lắng nghe sự kiện nhiều lần
    return () => {
        socket.off('countWarning');
    };
}, []);
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Layout style={{ minHeight: '100vh' }}>
        <Header style={{ background: '#fff', padding: 0 }}>
          <Menu theme="light" mode="horizontal" defaultSelectedKeys={['1']}>
            <Menu.Item key="1">
              <Link to="/">Trang Chủ</Link>
            </Menu.Item>
            <Menu.Item key="2">
              <Link to="/static1">Dữ liệu cảm biến</Link>
            </Menu.Item>
            <Menu.Item key="3">
              <Link to="/static2">Lịch sử bật tắt</Link>
            </Menu.Item>
            <Menu.Item key="4">
              <Link to="/contact">Liên Hệ</Link>
            </Menu.Item>
            <Menu.Item key="5">
              <Link to="/cc">Số lượng cảnh báo: {warningCount}</Link>
            </Menu.Item>
            {/* <Menu.Item key="5">
              <Link to="/Test">Test</Link>
            </Menu.Item> */}
            <Menu.Item key="6">
              <Link to="/dp">DP</Link>
            </Menu.Item> 
          </Menu>
        </Header>
        <Content style={{ margin: '16px' }}>
          <div style={{ padding: 24, background: '#fff', minHeight: 360 }}>
            <Routes>
              <Route path="/" element={<RareKien />} />
              <Route path="/static1" element={<EpicKien></EpicKien>} />
              <Route path="/static2" element={<SupperKien />} />
              <Route path="/contact" element={<Contact/>} />
              {/* {/* <Route path="/Test" element={<RealTime />} /> */}
              <Route path="/dp" element={<AntdTest />} /> 
              {/* <RareKien /> */}
            </Routes>
          </div>
        </Content>
        
        {/* <EpicKien/> */}
        
        

        <Footer style={{ textAlign: 'center' }}>Ant Design ©2024 Created by Ant UED</Footer>
      </Layout>
    </ThemeProvider>
  );
};

export default App;
