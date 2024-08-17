import React from 'react';
import { Layout, Menu } from 'antd';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import Page1 from './components/Page1';
import Page2 from './components/Page2';
import Page3 from './components/Page3';
import Profile from './components/Profile';

const { Header, Content, Footer } = Layout;

// Sample components for each route
const Home = () => <div>Home Content</div>;
const Static1 = () => <div>Static 1 Content</div>;
const Static2 = () => <div>Static 2 Content</div>;
const Contact = () => <div>Contact Content</div>;

const App = () => {
  return (
    
      <Layout style={{ minHeight: '100vh' }}>
        <Header style={{ background: '#fff', padding: 0 }}>
          <Menu theme="light" mode="horizontal" defaultSelectedKeys={['1']}>
            <Menu.Item key="1">
              <Link to="/">Trang Chủ</Link>
            </Menu.Item>
            <Menu.Item key="2">
              <Link to="/static1">Static 1</Link>
            </Menu.Item>
            <Menu.Item key="3">
              <Link to="/static2">Static 2</Link>
            </Menu.Item>
            <Menu.Item key="4">
              <Link to="/contact">Contact</Link>
            </Menu.Item>
          </Menu>
        </Header>
        <Content style={{ margin: '0 16px' }}>
          <div style={{ padding: 24, background: '#fff', minHeight: 360 }}>
            <Routes>
              <Route path="/" element={<Page1></Page1>} />
              <Route path="/static1" element={<Page2 />} />
              <Route path="/static2" element={<Page3 />} />
              <Route path="/contact" element={<Profile />} />
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
