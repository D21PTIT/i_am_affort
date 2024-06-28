import React from 'react';
import { useParams } from 'react-router-dom';

const SongDetail = ({ songs }) => {
  const { id } = useParams();
  const song = songs.find(song => song._id === id);

  if (!song) {
    return <div>Song not found</div>;
  }

  return (
    <div>
      <h2>{song.TenBaiHat}</h2>
      <p><strong>Artist:</strong> {song.CaSi}</p>
      <p><strong>Release Year:</strong> {song.NamPhatHanh}</p>
      <p><strong>Views:</strong> {song.LuotXem}</p>
    </div>
  );
};

export default SongDetail;
