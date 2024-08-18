import React, { useEffect, useState } from "react";
import axios from "axios";
import { Select, Table } from "antd";
function Page3() {
  const [device, setDevice] = useState([]);
  const [id, setId] = useState("0");
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
      render: (text) => new Date(text).toLocaleString(),
    },
  ];
  useEffect(() => {
    // Hàm lấy dữ liệu người dùng từ API
    const fetchDevices = async () => {
      let response = "";
      try {
        if (id === "0") {
          response = await axios.get(`http://localhost:8080/iot/getAllDevice`);
        } else {
          response = await axios.get(
            `http://localhost:8080/iot/getDeviceBySearch?tmp=${id}`
          );
        }
        const data = response.data;
        setDevice(data.device);
      } catch (err) {
        console.error("Error fetching users:", err);
      }
    };
    fetchDevices();
  }, [id]);
  const handleChange = (value) => {
    setId(value);
  };
  return (
    <>
      <h2>Lich su bat tat thiet bi</h2>
      <Select
        defaultValue="Tất cả"
        style={{ width: 120 }}
        onChange={handleChange}
        options={[
          { value: "0", label: "Tất cả" },
          { value: "1", label: "Quạt" },
          { value: "2", label: "Điều hòa" },
          { value: "3", label: "Bóng đèn" },
        ]}
      />
      <Table columns={columns} dataSource={device} />
    </>
  );
}

export default Page3;
