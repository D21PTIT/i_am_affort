import React from 'react';
import { Layout, Menu } from 'antd';
import { Route, Routes, Link } from 'react-router-dom';
import Page1 from './components/Page1';
import Page2 from './components/Page2';
import Page3 from './components/Page3';
// import Profile from './components/Profile';
import LineChart from './components/Test';
import Profile from './components/Profile';
import Contact from './components/Contact';
import SupperKien from './components/SupperKien';
import MegaKien from './components/MegaKien';
import Legendary from './components/Legendary';


const { Header, Content, Footer } = Layout;


const App = () => {
  return (
    
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
          </Menu>
        </Header>
        <Content style={{ margin: '0 16px' }}>
          <div style={{ padding: 24, background: '#fff', minHeight: 360 }}>
            <Routes>
              <Route path="/" element={<Legendary></Legendary>} />
              <Route path="/static1" element={<Page2 />} />
              <Route path="/static2" element={<MegaKien></MegaKien>} />
              <Route path="/contact" element={<SupperKien></SupperKien>} />
            </Routes>
          </div>
        </Content>
        <Footer style={{ textAlign: 'center' }}>
          Ant Design ©2024 Created by Ant UED
        </Footer>
      </Layout>
    
  );
};

export default App;
