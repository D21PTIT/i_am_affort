import React, { useEffect, useState } from "react";
import "./Page1.css";
import { Switch } from "antd";
import axios from "axios";

function Page1() {
  const [switches, setSwitches] = useState({
    1: JSON.parse(localStorage.getItem("device-1")) || false,
    2: JSON.parse(localStorage.getItem("device-2")) || false,
    3: JSON.parse(localStorage.getItem("device-3")) || false,
  });

  const onChange = (checked, switchNumber) => {
    let device = "";
    let status = checked ? "on" : "off";

    if (switchNumber === 1) {
      device = "fan";
    }
    if (switchNumber === 2) {
      device = "airc";
    }
    if (switchNumber === 3) {
      device = "lightb";
    }

    // Lưu trạng thái vào Local Storage
    localStorage.setItem(`device-${switchNumber}`, JSON.stringify(checked));

    // Cập nhật trạng thái của switch trong state
    setSwitches((prevSwitches) => ({
      ...prevSwitches,
      [switchNumber]: checked,
    }));

    const fetchDevices = async () => {
      try {
        const response = await axios.post(
          "http://localhost:8080/iot/createDevice",
          {
            tag: switchNumber,
            name: device,
            status: status,
          }
        );
        console.log("Device status updated:", response.data);
      } catch (err) {
        console.error("Error updating device:", err);
      }
    };

    fetchDevices();
  };

  return (
    <div className="page1-container">
      <div className="zone zone1">Zone 1</div>
      <div className="zone zone2">Zone 2</div>
      <div className="zone zone3">
        <div>
          <div>
            <p>Quạt</p>
            <Switch
              checked={switches[1]}
              onChange={(checked) => onChange(checked, 1)}
              style={{ marginRight: 8 }}
            />
          </div>

          <div>
            <p>Điều hòa</p>
            <Switch
              checked={switches[2]}
              onChange={(checked) => onChange(checked, 2)}
              style={{ marginRight: 8 }}
            />
          </div>

          <div>
            <p>Bóng điện</p>
            <Switch
              checked={switches[3]}
              onChange={(checked) => onChange(checked, 3)}
              style={{ marginRight: 8 }}
            />
          </div>
        </div>
      </div>
    </div>
  );
}

export default Page1;
