import { Switch } from 'antd';
import React, { useState, useEffect } from 'react';

function Legendary(props) {
    // Hàm để lấy trạng thái lưu trữ từ localStorage hoặc giá trị mặc định
    const getInitialSwitchStates = () => {
        const storedState = localStorage.getItem('switchStates');
        return storedState ? JSON.parse(storedState) : { switch1: 0, switch2: 0, switch3: 0 };
    };

    const [switchStates, setSwitchStates] = useState(getInitialSwitchStates);

    // Mỗi khi trạng thái của switch thay đổi, lưu lại vào localStorage
    useEffect(() => {
        localStorage.setItem('switchStates', JSON.stringify(switchStates));
    }, [switchStates]);

    const callAPI = async (switchKey, bodyValue) => {
        try {
            const response = await fetch('http://localhost:8080/test/send', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ 
                    topic: "home/led",
                    message: `${switchKey} ${bodyValue}` // Tạo message dạng "x y"
                }),
            });
            const result = await response.json();
            console.log('API response:', result);
        } catch (error) {
            console.error('Error calling API:', error);
        }
    };

    const handleToggle = (switchKey) => () => {
        setSwitchStates((prevState) => {
            const newValue = prevState[switchKey] === 0 ? 1 : 0;
            callAPI(switchKey.replace('switch', ''), newValue); // Truyền số thứ tự nút và giá trị mới
            return {
                ...prevState,
                [switchKey]: newValue
            };
        });
    };

    return (
        <div>
            <h1>OK</h1>
            <div>
                <p>Switch 1 (Body: {switchStates.switch1})</p>
                <Switch 
                    checked={switchStates.switch1 === 1} // Kiểm tra trạng thái hiện tại
                    onChange={handleToggle('switch1')} 
                />
            </div>
            <div>
                <p>Switch 2 (Body: {switchStates.switch2})</p>
                <Switch 
                    checked={switchStates.switch2 === 1} // Kiểm tra trạng thái hiện tại
                    onChange={handleToggle('switch2')} 
                />
            </div>
            <div>
                <p>Switch 3 (Body: {switchStates.switch3})</p>
                <Switch 
                    checked={switchStates.switch3 === 1} // Kiểm tra trạng thái hiện tại
                    onChange={handleToggle('switch3')} 
                />
            </div>
        </div>
    );
}

export default Legendary;
