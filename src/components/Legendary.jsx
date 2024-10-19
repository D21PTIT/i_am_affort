import { Switch, Spin } from 'antd';
import React, { useState, useEffect } from 'react';

import "../Style/lightpub.css";

function Legendary(props) {
    // Hàm để lấy trạng thái lưu trữ từ localStorage hoặc giá trị mặc định
    const getInitialSwitchStates = () => {
        const storedState = localStorage.getItem('switchStates');
        return storedState ? JSON.parse(storedState) : { switch1: 0, switch2: 0, switch3: 0 };
    };

    const [switchStates, setSwitchStates] = useState(getInitialSwitchStates);
    const [loading, setLoading] = useState({ switch1: false, switch2: false, switch3: false });

    // Mỗi khi trạng thái của switch thay đổi, lưu lại vào localStorage
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
            // Gọi API và chờ phản hồi
            const apiResponse = await callAPI(switchKey.replace('switch', ''), newValue);
            
            // Nếu API phản hồi với trạng thái mới, bạn có thể kiểm tra và sử dụng nó để cập nhật trạng thái
            // Giả sử apiResponse chứa trạng thái mới (nếu không thì sử dụng newValue như hiện tại)
            setSwitchStates((prevState) => ({
                ...prevState,
                [switchKey]: newValue  // Hoặc apiResponse.newValue nếu API phản hồi
            }));
        } catch (error) {
            console.error('API call failed');
        } finally {
            // Tắt trạng thái pending (loading)
            setLoading((prevLoading) => ({
                ...prevLoading,
                [switchKey]: false
            }));
        }
    };

    return (
        <div>
            <h1>OK123</h1>
            <div>
                <p>Switch 1 (Body: {switchStates.switch1})</p>
                <Spin spinning={loading.switch1}>
                    <Switch
                        checked={switchStates.switch1 === 1} // Kiểm tra trạng thái hiện tại
                        onChange={handleToggle('switch1')}
                    />
                </Spin>
            </div>
            <div>
                <p>Switch 2 (Body: {switchStates.switch2})</p>
                <Spin spinning={loading.switch2}>
                    <Switch
                        checked={switchStates.switch2 === 1} // Kiểm tra trạng thái hiện tại
                        onChange={handleToggle('switch2')}
                    />
                </Spin>
            </div>
            <div>
                <p>Switch 3 (Body: {switchStates.switch3})</p>
                <Spin spinning={loading.switch3}>
                    <Switch
                        checked={switchStates.switch3 === 1} // Kiểm tra trạng thái hiện tại
                        onChange={handleToggle('switch3')}
                    />
                </Spin>
            </div>
        </div>
    );
}

export default Legendary;
