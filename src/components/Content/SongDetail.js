import React from 'react';
import { useNavigate, useParams } from 'react-router-dom';

const SongDetail = ({ songs }) => {
  const { id } = useParams();
  const song = songs.find(song => song._id === id);
  const nav = useNavigate();

  if (!song) {
    return <div>Song not found</div>;
  }
  const handleCancel = ()=>{
    nav('/');
  }

  return (
    <div>
      <h2>{song.TenBaiHat}</h2>
      <p><strong>Artist:</strong> {song.CaSi}</p>
      <p><strong>Release Year:</strong> {song.NamPhatHanh}</p>
      <p><strong>Views:</strong> {song.LuotXem}</p>
      <button onClick={() => handleCancel()}>Cancel</button>
    </div>
  );
};

export default SongDetail;
