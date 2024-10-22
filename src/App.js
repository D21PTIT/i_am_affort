import React from 'react';
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

const { Header, Content, Footer } = Layout;

const App = () => {
  const [theme, colorMode] = useMode();

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
              <Link to="/static1">Data table 1</Link>
            </Menu.Item>
            <Menu.Item key="3">
              <Link to="/static2">Data table 2</Link>
            </Menu.Item>
            <Menu.Item key="4">
              <Link to="/contact">Contact</Link>
            </Menu.Item>
            {/* <Menu.Item key="5">
              <Link to="/Test">Test</Link>
            </Menu.Item>
            <Menu.Item key="6">
              <Link to="/dp">DP</Link>
            </Menu.Item> */}
          </Menu>
        </Header>

        <Content style={{ margin: '16px' }}>
          <div style={{ padding: 24, background: '#fff', minHeight: 360 }}>
            <Routes>
              <Route path="/" element={<RareKien />} />
              <Route path="/static1" element={<MegaKien />} />
              <Route path="/static2" element={<SupperKien />} />
              <Route path="/contact" element={<Contact/>} />
              {/* <Route path="/Test" element={<RealTime />} />
              <Route path="/dp" element={<Dashboard />} /> */}
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
