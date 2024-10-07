
import React from 'react';
import { Switch } from 'antd';

function Mythic(props) {

    const [isChecked, setIsChecked] = useState(false);

    const handleToggle = (checked) => {
        setIsChecked(checked);
        console.log('Switch state:', checked);
    };
    return (
        <div>

            <Switch checked={isChecked} onChange={handleToggle} />
        </div>
    );
}

export default Mythic;