import React from "react";
import "./Page1.css";
import { Switch } from "antd";

function Page1() {
  const onChange = (checked, switchNumber) => {
    console.log(`Switch ${switchNumber}: ${checked ? 1 : 0}`);
    if (switchNumber === 1) {
    }
  };

  return (
    <div className="page1-container">
      <div className="zone zone1">Zone 1</div>
      <div className="zone zone2">Zone 2</div>
      <div className="zone zone3">
        <div>
          <Switch
            defaultChecked
            onChange={(checked) => onChange(checked, 1)}
            style={{ marginRight: 8 }}
          />
          <Switch
            defaultChecked
            onChange={(checked) => onChange(checked, 2)}
            style={{ marginRight: 8 }}
          />
          <Switch
            defaultChecked
            onChange={(checked) => onChange(checked, 3)}
            style={{ marginRight: 8 }}
          />
        </div>
      </div>
      {/* <div className="zone zone4">Zone 4</div> */}
    </div>
  );
}

export default Page1;
