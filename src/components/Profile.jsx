import * as React from "react";
import ReactSpeedometer from "react-d3-speedometer/slim";

export default function Profile() {
  return (
    <div>
      <ReactSpeedometer
        value={33}
        segments={5}
        width={200}
        height={500}
        maxValue={100}
        segmentColors={["#bf616a", "#d08770", "#ebcb8b", "#a3be8c", "#b48ead"]}
        // startColor will be ignored
        // endColor will be ignored
      />
    </div>
  );
}
// https://www.npmjs.com/package/react-d3-speedometer
