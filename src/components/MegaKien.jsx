import { Pagination, Spin, Table, DatePicker } from 'antd';
import React, { useEffect, useState } from 'react';
import axios from 'axios'; // Thêm axios để gọi API

const { RangePicker } = DatePicker;

function MegaKien(props) {
    const [pageSize, setPageSize] = useState(10); // Số lượng item trên mỗi trang
    const [currentPage, setCurrentPage] = useState(1); // Trang hiện tại
    const [totalRecords, setTotalRecords] = useState(0); // Tổng số bản ghi
    const [loading, setLoading] = useState(false); // Trạng thái loading
    const [data, setData] = useState([]); // Dữ liệu API trả về
    const [dateRange, setDateRange] = useState([null, null]); // Lưu trữ giá trị start và end
    const [sortState, setSortState] = useState({
        timesort: null, // Sắp xếp thời gian (1 hoặc -1)
        tempsort: 0, // Sắp xếp nhiệt độ (-1, 0, 1)
        humsort: 0, // Sắp xếp độ ẩm (-1, 0, 1)
        brisort: 0, // Sắp xếp ánh sáng (-1, 0, 1)
    });

    // Hàm gọi API
    const fetchData = async (page, size, start, end, sort) => {
        setLoading(true); // Bật loading

        // daysort là true nếu cả start và end đều có giá trị, false nếu không
        const daysort = start && end ? true : false;

        try {
            const response = await axios.get('http://localhost:8080/kien/iot2', {
                params: {
                    page: page, // Gửi page
                    quanty: size, // Gửi pageSize
                    start: start, // Gửi start date
                    end: end, // Gửi end date
                    daysort: daysort, // Gửi daysort
                    ...sort // Gửi giá trị sắp xếp: timesort, tempsort, humsort, brisort
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

    // Gọi API mỗi khi currentPage, pageSize, dateRange, hoặc sortState thay đổi
    useEffect(() => {
        const [start, end] = dateRange;
        fetchData(
            currentPage,
            pageSize,
            start ? start.format('YYYY-MM-DD') : null,
            end ? end.format('YYYY-MM-DD') : null,
            sortState
        );
    }, [currentPage, pageSize, dateRange, sortState]);

    const handlePageChange = (page, size) => {
        setCurrentPage(page); // Cập nhật trang hiện tại
        setPageSize(size); // Cập nhật số lượng item trên mỗi trang
    };

    // Xử lý khi người dùng chọn khoảng thời gian
    const handleDateRangeChange = (dates) => {
        setDateRange(dates); // Cập nhật giá trị start và end
    };

    // Xử lý sự kiện sắp xếp cột
    const handleTableChange = (pagination, filters, sorter) => {
        let newSortState = { timesort: 0, tempsort: 0, humsort: 0, brisort: 0 };

        if (sorter.columnKey === 'createdAt') {
            newSortState.timesort = sorter.order === 'ascend' ? 1 : sorter.order === 'descend' ? -1 : 0;
        } else if (sorter.columnKey === 'temperature') {
            newSortState.tempsort = sorter.order === 'ascend' ? 1 : sorter.order === 'descend' ? -1 : 0;
        } else if (sorter.columnKey === 'humidity') {
            newSortState.humsort = sorter.order === 'ascend' ? 1 : sorter.order === 'descend' ? -1 : 0;
        } else if (sorter.columnKey === 'light') {
            newSortState.brisort = sorter.order === 'ascend' ? 1 : sorter.order === 'descend' ? -1 : 0;
        }

        setSortState(newSortState); // Cập nhật trạng thái sắp xếp
    };

    // Định nghĩa cột cho bảng và căn giữa nội dung
    const columns = [
        {
            title: 'ID',
            dataIndex: '_id',
            key: '_id',
            align: 'center', // Căn giữa
        },
        {
            title: 'Humidity',
            dataIndex: 'humidity',
            key: 'humidity',
            align: 'center', // Căn giữa
            sorter: true, // Cho phép sắp xếp
        },
        {
            title: 'Light',
            dataIndex: 'light',
            key: 'light',
            align: 'center', // Căn giữa
            sorter: true, // Cho phép sắp xếp
        },
        {
            title: 'Temperature',
            dataIndex: 'temperature',
            key: 'temperature',
            align: 'center', // Căn giữa
            sorter: true, // Cho phép sắp xếp
        },
        {
            title: 'Created At',
            dataIndex: 'createdAt',
            key: 'createdAt',
            align: 'center', // Căn giữa
            sorter: true, // Cho phép sắp xếp
            render: (text) => new Date(text).toLocaleString(), // Định dạng thời gian
        },
    ];

    return (
        <div>
            {/* Thêm RangePicker để chọn khoảng thời gian */}
            <RangePicker onChange={handleDateRangeChange} />

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

export default MegaKien;
