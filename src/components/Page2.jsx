import { Table } from "antd";
import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate, useSearchParams } from "react-router-dom";
function Page2(props) {
  const [data, setData] = useState([]);
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();
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
      render: (text) => new Date(text).toLocaleString(),
    },
  ];

  useEffect(() => {
    // Hàm lấy dữ liệu người dùng từ API
    const fetchDevices = async () => {
      try {
        const response = await axios.get(
          "http://localhost:8080/iot/getAllData"
        );
        const data = response.data;
        setData(data.data);
      } catch (err) {
        console.error("Error fetching users:", err);
      }
    };

    fetchDevices();
  }, []);
  return (
    <div>
      <h2>Du lieu thuc</h2>
      <Table columns={columns} dataSource={data} />
    </div>
  );
}

export default Page2;
