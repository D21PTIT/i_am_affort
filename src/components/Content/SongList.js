import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';

const SongList = ({ songs, onDelete }) => {
  // const [songs, setSongs] = useState([]);
  // useEffect(() => {
  //   fetch('http://localhost:8080/song')
  //     .then(response => response.json())
  //     .then(data => setSongs(data))
  //     .catch(error => console.error('Error fetching songs:', error));
  // }, []);

  const navigate = useNavigate();
  const handleDelete = async (id) => {
    try {
      const response = await fetch(`http://localhost:8080/song/${id}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ id }),
      });
      if (response.ok) {
        onDelete(id);
      } else {
        console.error('Failed to delete the song.');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  };
  const handleEdit= (id)=>{
    navigate(`/songedit/${id}`)
  }
  return (
    <ul>
      {songs.map((song) => (
        <li key={song._id}>
          <Link to={`/song/${song._id}`}>{song.TenBaiHat}</Link>
          <button onClick={() => handleDelete(song._id)}>Delete</button>
          <button onClick={() => handleEdit(song._id)}>Edit</button>
          
          
        </li>
      ))}
    </ul>
  );
};

export default SongList;
