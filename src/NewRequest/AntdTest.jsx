import { Button, Col, Divider, Row, Space } from 'antd';
import React from 'react';


function AntdTest(props) {
    const style = {
        background: '#0092ff',
        height: '450px'
    };
    const style1 = {
        background: '#0092ff',
        height: '150px'
    };
    return (
        <div>
            <Row >
                <Col span={5}>
                    <div style={style}>col-6</div>
                </Col>
                <Col span={18} push={1} >
                    <div style={style}>col-6</div>
                </Col>
            </Row>

            <Row gutter={10} style={{paddingTop: 20}} justify={'space-between'}>
                <Col span={3}>
                    <div style={style1}>TV1</div>
                </Col>
                <Col span={3}>
                    <div style={style1}>TV1</div>
                </Col>
                <Col span={3}>
                    <div style={style1}>TV1</div>
                </Col>
                <Col span={3}>
                    <div style={style1}>TV1</div>
                </Col>
                <Col span={3}>
                    <div style={style1}>TV1</div>
                </Col>
                <Col span={3}>
                    <div style={style1}>TV1</div>
                </Col>
                <Col span={3}>
                    <div style={style1}>TV1</div>
                </Col>
            </Row>



        </div>
    );
}

export default AntdTest;