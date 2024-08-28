import React from "react";
import "./Contact.css";

const Contact = () => {
  return (
    <div className="contact-card">
      <div className="header">
        <img
          src="https://encrypted-tbn0.gstatic.com/licensed-image?q=tbn:ANd9GcS0s_muqdU3W0AYaP8oQd_kJSOhiACWyKibnda85v9_U36mAfgIZ9fERZegmUxDCyh1qBCp8F-ldtmJDjo"
          alt="Profile"
          className="profile-image"
        />
        <h2>Trần Trung Kiên</h2>
        <p>B21DCCN467</p>
      </div>
      <div className="about-me">
        <h3>About me</h3>
        <p>
          Những ai có sự biết ơn (cho bản thân họ) sẽ được cho thêm, và anh hay
          cô ta sẽ trở nên giàu có. Những ai không có sự biết ơn (cho bản thân
          họ), thậm chí những gì anh hay cô ta có sẽ bị lấy đi.
        </p>
      </div>
      <div className="social-links">
        <h3>Connect with me</h3>
        <a
          href="https://github.com/D21PTIT"
          target="_blank"
          rel="noopener noreferrer"
        >
          GitHub: D21PTIT
        </a>
      </div>
      <h2>API doc in here</h2>
    </div>
  );
};

export default Contact;
