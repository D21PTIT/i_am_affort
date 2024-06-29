import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';

function SongEdit({ songs, setOk }) {
    const { id } = useParams();
    const navigate = useNavigate();
    const song = songs.find(song => song._id === id);

    const [formData, setFormData] = useState({
        TenBaiHat: '',
        CaSi: '',
        NamPhatHanh: '',
        LuotXem: 0,
    });

    useEffect(() => {
        if (song) {
            setFormData({
                TenBaiHat: song.TenBaiHat,
                CaSi: song.CaSi,
                NamPhatHanh: song.NamPhatHanh,
                LuotXem: song.LuotXem,
            });
        }
    }, [song]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch(`http://localhost:8080/song/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(formData),
            });
            if (response.ok) {
                const updatedSong = await response.json();
                setOk(2);
                navigate('/');
            } else {
                console.error('Failed to update the song');
            }
        } catch (error) {
            console.error('Error:', error);
        }

    };

    if (!song) {
        return <div>Song not found</div>;
    }

    return (
        <div>
            <h1>Edit Song</h1>
            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="title">Title</label>
                    <input
                        type="text"
                        id="title"
                        name="TenBaiHat"
                        value={formData.TenBaiHat}
                        onChange={handleChange}
                    />
                </div>
                <div>
                    <label htmlFor="artist">Artist</label>
                    <input
                        type="text"
                        id="artist"
                        name="CaSi"
                        value={formData.CaSi}
                        onChange={handleChange}
                    />
                </div>
                <div>
                    <label htmlFor="album">Album</label>
                    <input
                        type="text"
                        id="album"
                        name="NamPhatHanh"
                        value={formData.NamPhatHanh}
                        onChange={handleChange}
                    />
                </div>
                <div>
                    <label htmlFor="year">Year</label>
                    <input
                        type="number"
                        id="year"
                        name="LuotXem"
                        value={formData.LuotXem}
                        onChange={handleChange}
                    />
                </div>
                <button type="submit">Save</button>
            </form>
        </div>
    );
}

export default SongEdit;
