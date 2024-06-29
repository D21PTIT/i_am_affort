import React, { useState, useEffect } from 'react';
import { Routes, Route, useNavigate } from 'react-router-dom';
import SongDetail from './components/Content/SongDetail';
import SongList from './components/Content/SongList';
import CreateSong from './components/Content/CreateSong';
import './App.css';
import SongEdit from './components/Content/SongEdit';

function App() {
  const [songs, setSongs] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetch('http://localhost:8080/song')
      .then(response => response.json())
      .then(data => setSongs(data))
      .catch(error => console.error('Error fetching songs:', error));
  }, []);

  const handleCreateClick = () => {
    navigate('/create');
  };

  const handleDelete = (id) => {
    setSongs(prevSongs => prevSongs.filter(song => song._id !== id));
  };

  return (
    <div className="App">
      <h1>Song List</h1>
      <button onClick={handleCreateClick}>Create</button>
      <Routes>
        <Route path="/" element={<SongList songs={songs} onDelete={handleDelete} />} />
        <Route path="/song/:id" element={<SongDetail songs={songs} />} />
        <Route path="/create" element={<CreateSong />} />
        <Route path="/songedit/:id" element={<SongEdit songs={songs}/>} />
      </Routes>
    </div>
  );
}

export default App;
