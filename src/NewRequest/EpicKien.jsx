import { Pagination, Spin, Table, DatePicker, Input, Select, Row, Col } from 'antd';
import React, { useEffect, useState } from 'react';
import axios from 'axios';

const { Option } = Select;

function EpicKien(props) {
    const [pageSize, setPageSize] = useState(10);
    const [currentPage, setCurrentPage] = useState(1);
    const [totalRecords, setTotalRecords] = useState(0);
    const [loading, setLoading] = useState(false);
    const [data, setData] = useState([]);
    const [exactTime, setExactTime] = useState('');
    const [type, setType] = useState('ALL');
    const [exactValue, setExactValue] = useState('');
    const [sortState, setSortState] = useState({
        timesort: null, // Sắp xếp theo thời gian (1 hoặc -1)
        tag: null, // Trường sắp xếp (1: Humidity, 2: Light, 3: Temperature, 4: Wind)
        order: null, // Thứ tự sắp xếp (-1 hoặc 1)
    });

    // Hàm gọi API
    const fetchData = async (page, size, exactTime, sort, type, exactValue) => {
        setLoading(true);

        try {
            const response = await axios.get('http://localhost:8080/kien/iot3', {
                params: {
                    page,
                    quanty: size,
                    exactTime: exactTime || null,
                    type: type === 'ALL' ? null : type,
                    exactValue: exactValue || null,
                    timesort: sort.timesort || null,
                    tag: sort.tag || null,
                    order: sort.order || null
                }
            });

            setTotalRecords(response.data.totalRecords);
            setData(response.data.data);
        } catch (error) {
            console.error('Error fetching data:', error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchData(currentPage, pageSize, exactTime, sortState, type, exactValue);
    }, [currentPage, pageSize, exactTime, sortState, type, exactValue]);

    const handlePageChange = (page, size) => {
        setCurrentPage(page);
        setPageSize(size);
    };

    const handleExactTimeChange = (e) => {
        setExactTime(e.target.value);
    };

    const handleTypeChange = (value) => {
        setType(value);
        if (value === 'ALL') {
            setExactValue('');
        }
    };

    const handleExactValueChange = (e) => {
        setExactValue(e.target.value);
    };

    const handleTableChange = (pagination, filters, sorter) => {
        let newSortState = { timesort: null, tag: null, order: null };

        if (sorter.columnKey === 'createdAt') {
            newSortState.timesort = sorter.order === 'ascend' ? 1 : -1;
        } else {
            newSortState.timesort = null;
            if (sorter.columnKey === 'humidity') {
                newSortState.tag = 1;
            } else if (sorter.columnKey === 'light') {
                newSortState.tag = 2;
            } else if (sorter.columnKey === 'temperature') {
                newSortState.tag = 3;
            } else if (sorter.columnKey === 'wind') {
                newSortState.tag = 4;
            }
            newSortState.order = sorter.order === 'ascend' ? 1 : -1;
        }

        setSortState(newSortState);
    };

    const columns = [
        {
            title: 'ID',
            dataIndex: 'stt',
            key: 'stt',
            align: 'center',
            width: 80,
        },
        {
            title: 'Độ ẩm',
            dataIndex: 'humidity',
            key: 'humidity',
            align: 'center',
            sorter: true,
        },
        {
            title: 'Ánh sáng',
            dataIndex: 'light',
            key: 'light',
            align: 'center',
            sorter: true,
        },
        {
            title: 'Nhiệt độ',
            dataIndex: 'temperature',
            key: 'temperature',
            align: 'center',
            sorter: true,
        },
        {
            title: 'Tốc độ gió',
            dataIndex: 'wind',
            key: 'wind',
            align: 'center',
            sorter: true,
        },
        {
            title: 'Thời gian ghi nhận',
            dataIndex: 'createdAt',
            key: 'createdAt',
            align: 'center',
            sorter: true,
            render: (text) => {
                const date = new Date(text);
                const formattedDate = `${date.getFullYear()}/${String(date.getMonth() + 1).padStart(2, '0')}/${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}:${String(date.getSeconds()).padStart(2, '0')}`;
                return formattedDate;
            },
        },
    ];

    return (
        <div>
            <Input
                placeholder="Enter exact time (YYYY/MM/DD HH:mm:ss)"
                value={exactTime}
                onChange={handleExactTimeChange}
                style={{ width: '100%', margin: '10px 0' }}
            />
            <Row gutter={16} style={{ marginBottom: '10px' }}>
                <Col span={12}>
                    <Select
                        defaultValue="ALL"
                        style={{ width: '100%' }}
                        onChange={handleTypeChange}
                    >
                        <Option value="ALL">Tất cả</Option>
                        <Option value="Humidity">Độ ẩm</Option>
                        <Option value="Light">Ánh sáng</Option>
                        <Option value="Temperature">Nhiệt độ</Option>
                        <Option value="WindSpeed">Sức gió</Option>
                    </Select>
                </Col>
                <Col span={12}>
                    <Input
                        placeholder="Enter exact value"
                        value={exactValue}
                        onChange={handleExactValueChange}
                        style={{ width: '100%' }}
                        disabled={type === 'ALL'}
                    />
                </Col>
            </Row>

            <Spin spinning={loading}>
                <Table
                    columns={columns}
                    dataSource={data}
                    rowKey="_id"
                    pagination={false}
                    onChange={handleTableChange}
                />
                <Pagination
                    current={currentPage}
                    total={totalRecords}
                    showSizeChanger
                    onShowSizeChange={handlePageChange}
                    onChange={handlePageChange}
                    pageSize={pageSize}
                    pageSizeOptions={['10', '20', '50']}
                />
            </Spin>
        </div>
    );
}

export default EpicKien;
