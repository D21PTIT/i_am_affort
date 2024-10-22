import React, { useState, useEffect } from 'react';
import { Pagination, Spin, Table, Select } from 'antd';
import axios from 'axios';

function SupperKien(props) {
    const [pageSize, setPageSize] = useState(10); // Số lượng item trên mỗi trang
    const [currentPage, setCurrentPage] = useState(1); // Trang hiện tại
    const [data, setData] = useState([]); // Dữ liệu từ API (cụ thể là devices)
    const [totalRecords, setTotalRecords] = useState(0); // Tổng số bản ghi từ API
    const [loading, setLoading] = useState(false); // Trạng thái loading
    const [selectedTag, setSelectedTag] = useState("all"); // State cho tag
    const [timesort, setTimesort] = useState(false); // Mặc định sắp xếp tăng dần (cũ nhất trước)

    // Hàm gọi API
    const fetchDevices = async () => {
        setLoading(true); // Hiển thị hiệu ứng quay quay trước khi gọi API
        try {
            const response = await axios.get("http://localhost:8080/kien/iot1", {
                params: {
                    page: currentPage,
                    type: selectedTag, // Lọc theo tag được chọn
                    quanty: pageSize,    // Số lượng bản ghi mỗi trang
                    timesort: timesort, // Sắp xếp theo thời gian (true: mới nhất, false: cũ nhất)
                },
            });

            const responseData = response.data;
            setData(responseData.devices); // Lưu danh sách thiết bị vào state
            setTotalRecords(responseData.totalRecords); // Lưu tổng số bản ghi vào state
        } catch (err) {
            console.error("Error fetching devices:", err);
        } finally {
            setLoading(false); // Ẩn hiệu ứng quay quay sau khi hoàn tất API call
        }
    };

    // Gọi lại API mỗi khi pageSize, currentPage, selectedTag, hoặc timesort thay đổi
    useEffect(() => {
        fetchDevices();
    }, [pageSize, currentPage, selectedTag, timesort]);

    // Xử lý khi thay đổi số lượng item trên mỗi trang hoặc trang hiện tại
    const handlePageChange = (page, size) => {
        setCurrentPage(page);
        setPageSize(size);
    };

    // Xử lý khi thay đổi tag
    const handleTagChange = (value) => {
        setSelectedTag(value);
        setCurrentPage(1); // Đặt lại trang về 1 mỗi khi thay đổi tag
    };

    // Xử lý khi sắp xếp cột "Created At"
    const handleSortChange = () => {
        // Đảo ngược trạng thái của timesort khi nhấn vào tiêu đề cột
        setTimesort((prevSort) => !prevSort);
    };

    // Định nghĩa cột cho bảng dữ liệu
    const columns = [
        {
            title: 'ID',
            dataIndex: 'stt',
            key: 'stt',
            align: 'center', // Căn giữa cột
        },
        {
            title: 'Tag',
            dataIndex: 'tag',
            key: 'tag',
            align: 'center', // Căn giữa cột
        },
        {
            title: 'Name',
            dataIndex: 'name',
            key: 'name',
            align: 'center', // Căn giữa cột
        },
        {
            title: 'Status',
            dataIndex: 'status',
            key: 'status',
            align: 'center', // Căn giữa cột
        },
        {
            title: 'Created At',
            dataIndex: 'createdAt',
            key: 'createdAt',
            sorter: true, // Bật tính năng sắp xếp cho cột
            render: (text) => new Date(text).toLocaleString(), // Định dạng lại ngày tháng
            onHeaderCell: () => ({
                onClick: handleSortChange, // Xử lý khi người dùng nhấn vào tiêu đề cột
            }),
            align: 'center', // Căn giữa cột
        },
    ];

    return (
        <div>
            {/* Select để chọn tag */}
            <Select
                defaultValue="Tất cả"
                style={{ width: 120, marginBottom: 20 }}
                onChange={handleTagChange}
                options={[
                    { value: "all", label: "Tất cả" },
                    { value: "1", label: "Quạt" },
                    { value: "2", label: "Điều hòa" },
                    { value: "3", label: "Bóng đèn" },
                ]}
            />
            
            {/* Hiển thị hiệu ứng Spin khi đang tải dữ liệu */}
            <Spin spinning={loading}>
                <Table 
                    dataSource={data} // Dữ liệu từ API
                    columns={columns} // Cấu hình các cột của bảng
                    pagination={false} // Tắt phân trang mặc định của bảng (vì bạn dùng phân trang bên ngoài)
                    rowKey="_id" // Sử dụng _id làm key cho mỗi hàng
                />
                <Pagination
                    Pagination align="end"
                    current={currentPage}
                    total={totalRecords} // Tổng số bản ghi từ state
                    showSizeChanger // Hiển thị bộ chọn số lượng mục hiển thị trên mỗi trang
                    onShowSizeChange={handlePageChange} // Xử lý khi số lượng mục/page thay đổi
                    onChange={handlePageChange} // Xử lý khi thay đổi trang
                    pageSize={pageSize} // Số lượng item hiển thị trên mỗi trang
                    pageSizeOptions={['10', '20', '50']} // Các tùy chọn cho số lượng item mỗi trang
                />
            </Spin>
        </div>
    );
}

export default SupperKien;
