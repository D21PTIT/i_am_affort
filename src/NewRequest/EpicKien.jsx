import { Pagination, Spin, Table, DatePicker, Input, Select, Row, Col } from 'antd';
import React, { useEffect, useState } from 'react';
import axios from 'axios'; // Thêm axios để gọi API

const { RangePicker } = DatePicker;
const { Option } = Select; // Thêm Option cho Select

function EpicKien(props) {  // Changed from MegaKien to EpicKien
    const [pageSize, setPageSize] = useState(10); // Số lượng item trên mỗi trang
    const [currentPage, setCurrentPage] = useState(1); // Trang hiện tại
    const [totalRecords, setTotalRecords] = useState(0); // Tổng số bản ghi
    const [loading, setLoading] = useState(false); // Trạng thái loading
    const [data, setData] = useState([]); // Dữ liệu API trả về
    const [exactTime, setExactTime] = useState(''); // Lưu giá trị exactTime nhập vào
    const [type, setType] = useState('ALL'); // Loại cảm biến
    const [exactValue, setExactValue] = useState(''); // Giá trị chính xác
    const [sortState, setSortState] = useState({
        timesort: null, // Sắp xếp thời gian (1 hoặc -1)
        tempsort: 0, // Sắp xếp nhiệt độ (-1, 0, 1)
        humsort: 0, // Sắp xếp độ ẩm (-1, 0, 1)
        brisort: 0, // Sắp xếp ánh sáng (-1, 0, 1)
        windsort: 0, // Sắp xếp sức gió (-1, 0, 1) - Added for Wind Speed sorting
    });

    // Hàm gọi API
    const fetchData = async (page, size, exactTime, sort, type, exactValue) => {
        setLoading(true); // Bật loading

        try {
            const response = await axios.get('http://localhost:8080/kien/iot3', {
                params: {
                    page: page, // Gửi page
                    quanty: size, // Gửi pageSize
                    exactTime: exactTime || null, // Gửi exactTime (nếu có giá trị)
                    type: type === 'ALL' ? null : type, // Gửi loại cảm biến, bỏ qua nếu chọn ALL
                    exactValue: exactValue || null, // Gửi giá trị chính xác
                    ...sort // Gửi giá trị sắp xếp: timesort, tempsort, humsort, brisort, windsort - Included windsort
                }
            });
            // Giả sử API trả về dạng: { totalRecords, data }
            setTotalRecords(response.data.totalRecords); // Cập nhật tổng số bản ghi
            setData(response.data.data); // Cập nhật dữ liệu
        } catch (error) {
            console.error('Error fetching data:', error);
        } finally {
            setLoading(false); // Tắt loading
        }
    };

    // Gọi API mỗi khi currentPage, pageSize, exactTime, sortState, type, hoặc exactValue thay đổi
    useEffect(() => {
        fetchData(currentPage, pageSize, exactTime, sortState, type, exactValue);
    }, [currentPage, pageSize, exactTime, sortState, type, exactValue]);

    const handlePageChange = (page, size) => {
        setCurrentPage(page); // Cập nhật trang hiện tại
        setPageSize(size); // Cập nhật số lượng item trên mỗi trang
    };

    // Xử lý khi người dùng nhập exactTime
    const handleExactTimeChange = (e) => {
        setExactTime(e.target.value); // Cập nhật giá trị exactTime nhập vào
    };

    // Xử lý khi chọn loại cảm biến
    const handleTypeChange = (value) => {
        setType(value); // Cập nhật loại cảm biến
        if (value === 'ALL') {
            setExactValue(''); // Xóa giá trị exactValue nếu chọn ALL
        }
    };

    // Xử lý khi nhập giá trị chính xác
    const handleExactValueChange = (e) => {
        setExactValue(e.target.value); // Cập nhật giá trị chính xác
    };

    // Xử lý sự kiện sắp xếp cột
    const handleTableChange = (pagination, filters, sorter) => {
        let newSortState = { timesort: 0, tempsort: 0, humsort: 0, brisort: 0, windsort: 0 };

        if (sorter.columnKey === 'createdAt') {
            newSortState.timesort = sorter.order === 'ascend' ? 1 : sorter.order === 'descend' ? -1 : 0;
        } else if (sorter.columnKey === 'temperature') {
            newSortState.tempsort = sorter.order === 'ascend' ? 1 : sorter.order === 'descend' ? -1 : 0;
        } else if (sorter.columnKey === 'humidity') {
            newSortState.humsort = sorter.order === 'ascend' ? 1 : sorter.order === 'descend' ? -1 : 0;
        } else if (sorter.columnKey === 'light') {
            newSortState.brisort = sorter.order === 'ascend' ? 1 : sorter.order === 'descend' ? -1 : 0;
        } else if (sorter.columnKey === 'windSpeed') {
            newSortState.windsort = sorter.order === 'ascend' ? 1 : sorter.order === 'descend' ? -1 : 0; // Added windSpeed sorting
        }

        setSortState(newSortState); // Cập nhật trạng thái sắp xếp
    };

    // Định nghĩa cột cho bảng và căn giữa nội dung
    const columns = [
        {
            title: 'ID',
            dataIndex: 'stt',
            key: 'stt',
            align: 'center', // Căn giữa
        },
        {
            title: 'Độ ẩm',
            dataIndex: 'humidity',
            key: 'humidity',
            align: 'center', // Căn giữa
            sorter: true, // Cho phép sắp xếp
        },
        {
            title: 'Ánh sáng',
            dataIndex: 'light',
            key: 'light',
            align: 'center', // Căn giữa
            sorter: true, // Cho phép sắp xếp
        },
        {
            title: 'Nhiệt độ',
            dataIndex: 'temperature',
            key: 'temperature',
            align: 'center', // Căn giữa
            sorter: true, // Cho phép sắp xếp
        },
        {
            title: 'Tốc độ gió', // New column for wind speed
            dataIndex: 'wind',
            key: 'wind',
            align: 'center', // Căn giữa
            sorter: true, // Cho phép sắp xếp
        },
        {
            title: 'Thời gian ghi nhận',
            dataIndex: 'createdAt',
            key: 'createdAt',
            align: 'center', // Căn giữa
            sorter: true, // Cho phép sắp xếp
            render: (text) => new Date(text).toLocaleString(), // Định dạng thời gian
        },
    ];

    return (
        <div>

            {/* Ô nhập cho exactTime */}
            <Input
                placeholder="Enter exact time (YYYY/MM/DD HH:mm:ss)"
                value={exactTime}
                onChange={handleExactTimeChange} // Xử lý khi người dùng nhập exactTime
                style={{ width: '100%', margin: '10px 0' }}
            />

            {/* Row và Col để hiển thị 2 ô song song */}
            <Row gutter={16} style={{ marginBottom: '10px' }}>
                <Col span={12}>
                    {/* Ô chọn loại cảm biến */}
                    <Select
                        defaultValue="ALL"
                        style={{ width: '100%' }}
                        onChange={handleTypeChange} // Xử lý khi chọn loại cảm biến
                    >
                        <Option value="ALL">Tất cả</Option>
                        <Option value="Humidity">Độ ẩm</Option>
                        <Option value="Light">Ánh sáng</Option>
                        <Option value="Temperature">Nhiệt độ</Option>
                        <Option value="WindSpeed">Sức gió</Option> {/* Added Wind Speed as option */}
                    </Select>
                </Col>
                <Col span={12}>
                    {/* Ô nhập giá trị chính xác */}
                    <Input
                        placeholder="Enter exact value"
                        value={exactValue}
                        onChange={handleExactValueChange} // Xử lý khi người dùng nhập giá trị chính xác
                        style={{ width: '100%' }}
                        disabled={type === 'ALL'} // Vô hiệu hóa nếu chọn ALL
                    />
                </Col>
            </Row>

            <Spin spinning={loading}>
                {/* Hiển thị bảng dữ liệu từ API */}
                <Table
                    columns={columns} // Định nghĩa cột cho bảng
                    dataSource={data} // Dữ liệu từ API
                    rowKey="_id" // Định danh từng bản ghi với khóa _id
                    pagination={false} // Sử dụng phân trang custom phía dưới
                    onChange={handleTableChange} // Gọi khi người dùng thay đổi thứ tự sắp xếp
                />

                {/* Phân trang */}
                <Pagination
                    current={currentPage}
                    total={totalRecords} // Tổng số bản ghi từ API
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

export default EpicKien; // Changed component name to EpicKien
