import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const CreateSong = () => {
  const nav = useNavigate()
  const [formData, setFormData] = useState({
    TenBaiHat: '',
    CaSi: '',
    NamPhatHanh: '',
    LuotXem: ''
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch('http://localhost:8080/song', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
      });

      if (response.ok) {
        console.log('Song created successfully');
        // Reset form
        setFormData({
          TenBaiHat: '',
          CaSi: '',
          NamPhatHanh: '',
          LuotXem: ''
        });
      } else {
        console.error('Failed to create song');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  };
  const handleCancel =()=>{
    nav('/');
  }

  return (
    <div>
      <h2>Create New Song</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Song Title</label>
          <input type="text" name="TenBaiHat" value={formData.TenBaiHat} onChange={handleChange} />
        </div>
        <div>
          <label>Artist</label>
          <input type="text" name="CaSi" value={formData.CaSi} onChange={handleChange} />
        </div>
        <div>
          <label>Release Year</label>
          <input type="text" name="NamPhatHanh" value={formData.NamPhatHanh} onChange={handleChange} />
        </div>
        <div>
          <label>Views</label>
          <input type="text" name="LuotXem" value={formData.LuotXem} onChange={handleChange} />
        </div>
        <button type="submit">Submit</button>
        <button onClick={() => handleCancel()}>Cancel</button>
      </form>
    </div>
  );
};

export default CreateSong;
