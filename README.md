# Hệ thống IoT thu thập cảm biến và điều khiển thiết bị

Dự án này được tạo ra với [Create React App](https://github.com/facebook/create-react-app).

## Mô tả
Phát triển hệ thống IoT thu thập dữ liệu từ các cảm biến và điều khiển thiết bị từ xa, tích hợp chức năng cảnh báo khi xuất hiện sự cố hoặc các giá trị vượt ngưỡng cho phép.

- **Công nghệ sử dụng:**
  - **Frontend**: JavaScript, ReactJS, Ant Design (antd).
  - **Backend**: NodeJS, ExpressJS, MongoDB.
  - **Giao thức và giao tiếp**: HTTP, MQTT, Socket.io.

## Cài đặt

### Yêu cầu hệ thống
- **Node.js** phiên bản 14+.
- **MongoDB** để lưu trữ dữ liệu cảm biến và thông tin thiết bị.
- **MQTT Broker** (Mosquitto hoặc EMQX) để giao tiếp thời gian thực.

##Cài đặt các gói cần thiết:

bash
Copy code
npm install
##Thiết lập biến môi trường: Tạo file .env trong thư mục gốc của dự án với các thông tin sau:

MONGODB_URI=<đường-dẫn-kết-nối-mongodb>
MQTT_BROKER_URL=<địa-chỉ-mqtt-broker>
Chạy ứng dụng:

##Chức năng chính
1. Thu thập dữ liệu cảm biến
Thu thập thông tin từ các cảm biến như nhiệt độ, độ ẩm, ánh sáng và lưu trữ vào MongoDB để dễ dàng theo dõi.
2. Điều khiển thiết bị từ xa
Cho phép người dùng điều khiển thiết bị (quạt, đèn, hệ thống tưới nước, v.v.) qua giao diện ReactJS hoặc các ứng dụng di động tích hợp MQTT.
3. Cảnh báo và thông báo
Thiết lập ngưỡng an toàn cho từng loại cảm biến. Khi giá trị vượt ngưỡng, hệ thống sẽ tự động gửi cảnh báo qua giao diện web hoặc gửi thông báo qua email/SMS.
4. Giao diện người dùng
Ant Design (antd): Sử dụng các thành phần UI của Ant Design để xây dựng giao diện thân thiện và dễ sử dụng.
Hiển thị biểu đồ dữ liệu cảm biến theo thời gian thực, sử dụng Charts.js để biểu diễn trực quan.
Kiến trúc hệ thống
Frontend: Sử dụng ReactJS cho giao diện người dùng, tích hợp với Ant Design để tối ưu trải nghiệm.
Backend: Node.js và ExpressJS để xây dựng API phục vụ giao tiếp giữa frontend và cơ sở dữ liệu MongoDB.
MQTT và Socket.io: Cung cấp khả năng giao tiếp thời gian thực giữa thiết bị IoT và hệ thống.
Các lệnh hữu ích
Cài đặt các gói phụ thuộc:

bash
Copy code
npm install

Chạy ứng dụng từ build:

bash
Copy code
npm start
##Đóng góp
Nếu bạn muốn đóng góp cho dự án, vui lòng fork repository này và gửi pull request với các thay đổi của bạn. Chúng tôi khuyến khích các ý tưởng mới và đóng góp từ cộng đồng để cải thiện hệ thống IoT này.

