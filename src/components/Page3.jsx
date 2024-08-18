import React, { useEffect, useState } from "react";
import axios from "axios";
import { Table } from "antd";
function Page3() {
  const [device, setDevice] = useState([]);
  const columns = [
    {
      title: "ID",
      dataIndex: "tag",
      key: "tag",
      render: (text) => <a>{text}</a>,
    },
    {
      title: "Name",
      dataIndex: "name",
      key: "name",
      render: (text) => <a>{text}</a>,
    },
    {
      title: "Trang thai",
      dataIndex: "status",
      key: "status",
      render: (text) => <a>{text}</a>,
    },
    {
      title: "Thoi diem",
      dataIndex: "createdAt",
      key: "createdAt",
      render: (text) => <a>{text}</a>,
    },
  ];
  useEffect(() => {
    // Hàm lấy dữ liệu người dùng từ API
    const fetchDevices = async () => {
      try {
        const response = await axios.get(
          "http://localhost:8080/iot/getAllDevice"
        );
        const data = response.data;
        setDevice(data.device);
      } catch (err) {
        console.error("Error fetching users:", err);
      }
    };

    fetchDevices();
  }, []);
  return (
    <>
      <h2>Lich su bat tat thiet bi</h2>
      <Table columns={columns} dataSource={device} />
    </>
  );
}

export default Page3;
