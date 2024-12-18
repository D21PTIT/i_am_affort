import React, { useState, useEffect } from 'react';
import { Pagination, Spin, Table, Select, Input } from 'antd';
import axios from 'axios';

function SupperKien(props) {
    const [pageSize, setPageSize] = useState(10); // Số lượng item trên mỗi trang
    const [currentPage, setCurrentPage] = useState(1); // Trang hiện tại
    const [data, setData] = useState([]); // Dữ liệu từ API (cụ thể là devices)
    const [totalRecords, setTotalRecords] = useState(0); // Tổng số bản ghi từ API
    const [loading, setLoading] = useState(false); // Trạng thái loading
    const [selectedTag, setSelectedTag] = useState("all"); // State cho tag
    const [timesort, setTimesort] = useState(false); // Mặc định sắp xếp tăng dần (cũ nhất trước)
    const [exactTime, setExactTime] = useState(''); // State cho exactTime

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
                    exactTime: exactTime || null // Truyền exactTime nếu có giá trị
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

    // Gọi lại API mỗi khi pageSize, currentPage, selectedTag, timesort, hoặc exactTime thay đổi
    useEffect(() => {
        fetchDevices();
    }, [pageSize, currentPage, selectedTag, timesort, exactTime]);

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

    // Xử lý khi thay đổi exactTime
    const handleExactTimeChange = (e) => {
        setExactTime(e.target.value);
        setCurrentPage(1); // Đặt lại trang về 1 khi thay đổi exactTime
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
            title: 'Tên thiết bị',
            dataIndex: 'name',
            key: 'name',
            align: 'center', // Căn giữa cột
        },
        {
            title: 'Trạng thái',
            dataIndex: 'status',
            key: 'status',
            align: 'center', // Căn giữa cột
        },
        {
            title: 'Thời gian ghi nhận',
            dataIndex: 'createdAt',
            key: 'createdAt',
            sorter: true, // Bật tính năng sắp xếp cho cột
            render: (text) => {
                const date = new Date(text);
                const formattedDate = `${date.getFullYear()}/${String(date.getMonth() + 1).padStart(2, '0')}/${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}:${String(date.getSeconds()).padStart(2, '0')}`;
                return formattedDate;
            }, // Định dạng lại ngày tháng
            onHeaderCell: () => ({
                onClick: handleSortChange, // Xử lý khi người dùng nhấn vào tiêu đề cột
            }),
            align: 'center', // Căn giữa cột
        },
    ];

    return (
        <div>
            {/* Ô nhập để chọn exactTime */}
            <Input
                placeholder="Nhập exact time (YYYY/MM/DD HH:mm:ss)"
                value={exactTime}
                onChange={handleExactTimeChange} // Gọi khi người dùng nhập exactTime
                style={{ width: '100%', marginBottom: 20 }}
            />
            
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
                    { value: "4", label: "Cảnh báo gió" }
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
