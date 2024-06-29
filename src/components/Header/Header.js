import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function Header(props) {
  const [searchTerm, setSearchTerm] = useState('');
  const navigate = useNavigate();
  const [songs, setSongs] = useState([]);
  const handleChange = (event) => {
    setSearchTerm(event.target.value);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const response = await fetch(`http://localhost:8080/search/song?song=${searchTerm}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });
      const data = await response.json();
      setSongs(data);
    } catch (error) {
      console.error('Error:', error);
    }
    
  };

  const handleExit = ()=>{
    //navigate('/');
    setSongs([])
    console.log(songs)
  }

  return (
    <div>
      <h1>Ứng dụng nghe nhạc chất lượng cao</h1>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Search..."
          value={searchTerm}
          onChange={handleChange}
        />
        <button type="submit">Search</button>
        {
            songs&&songs.length>0&&songs.map((item, index)=>{
                return (
                    <div key= {index}>
                        <h3>{item?.TenBaiHat}</h3>
                        <h3>{item?.CaSi}</h3>
                        <h3>{item?.NamPhatHanh}</h3>
                        <h3>{item?.LuotXem}</h3>

                    </div>
                )
            })
        }
      </form>
      <button onClick={handleExit}>Cancel</button>
    </div>
  );
}

export default Header;
