import React, { useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { uploadFile } from './../client/mongoClient';

const UploadFilePage = () => {
  const { cod } = useParams(); // Get `cod` from the URL
  const navigate = useNavigate();

  const [locatie, setLocatie] = useState('laborator'); // Default value
  const [selectedFile, setSelectedFile] = useState(null);
  const [error, setError] = useState(null);

  const handleFileChange = (event) => {
    setSelectedFile(event.target.files[0]);
  };

  const handleUpload = async () => {
    if (!selectedFile) {
      setError('Please select a file to upload.');
      return;
    }

    try {
      await uploadFile(cod, locatie, selectedFile);
      navigate(`/disciplina/${cod}`); // Redirect back to the discipline page
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div>
      <h1>Upload File</h1>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <div>
        <label htmlFor="locatie">Select Location:</label>
        <select
          id="locatie"
          value={locatie}
          onChange={(e) => setLocatie(e.target.value)}
        >
          <option value="laborator">Laborator</option>
          <option value="curs">Curs</option>
        </select>
      </div>
      <div>
        <label htmlFor="file">Select File:</label>
        <input
          id="file"
          type="file"
          onChange={handleFileChange}
        />
      </div>
      <button onClick={handleUpload}>Upload</button>
    </div>
  );
};

export default UploadFilePage;
