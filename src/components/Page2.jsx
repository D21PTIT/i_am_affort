import { Table } from "antd";
import React, { useState } from "react";

function Page2(props) {
  const [data, setData] = useState([]);
  const columns = [
    {
      title: "Độ ẩm",
      dataIndex: "humidity",
      key: "humidity",
      render: (text) => <a>{text}</a>,
    },
    {
      title: "Ánh sáng",
      dataIndex: "light",
      key: "light",
      render: (text) => <a>{text}</a>,
    },
    {
      title: "Nhiệt độ",
      dataIndex: "temperature",
      key: "temperature",
      render: (text) => <a>{text}</a>,
    },
    {
      title: "Thoi diem",
      dataIndex: "createdAt",
      key: "createdAt",
      render: (text) => <a>{text}</a>,
    },
  ];
  return (
    <div>
      <h2>Du lieu thuc</h2>
      <Table columns={columns} dataSource={data} />
    </div>
  );
}

export default Page2;
