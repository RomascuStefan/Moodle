import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getLectureByCod } from './../client/sqlClinent';
import { getFilesByCod, downloadFile } from './../client/mongoClient';

const DisciplinaPage = () => {
  const { cod } = useParams();
  const navigate = useNavigate();
  const [disciplina, setDisciplina] = useState(null);
  const [files, setFiles] = useState({ fisiereLaborator: [], fisiereCurs: [] });
  const [uploadLink, setUploadLink] = useState(null); // Store the upload-files link
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      setError(null);

      try {
        const lectureData = await getLectureByCod(cod);
        setDisciplina(lectureData);

        // Check for the upload-files link in the HATEOAS response
        if (lectureData._links?.['upload-files']) {
          setUploadLink(lectureData._links['upload-files'].href);
        }

        const fileData = await getFilesByCod(cod);
        setFiles(fileData);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [cod]);

  const handleFileClick = async (locatie, numeFisier) => {
    try {
      await downloadFile(cod, locatie.toLowerCase(), numeFisier);
    } catch (err) {
      alert(`Error downloading file: ${err.message}`);
    }
  };

  const handleUploadFiles = () => {
    navigate(`/disciplina/${cod}/upload`); // Redirect to the upload file page
  };

  return (
    <div>
      {loading && <p>Se încarcă...</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}
      {disciplina && (
        <div>
          {uploadLink && (
            <button onClick={handleUploadFiles} style={{ marginBottom: '1rem' }}>
              Upload Files
            </button>
          )}
          <h1>
            {disciplina.numeDisciplina}: {disciplina.numeTitular}
          </h1>
        </div>
      )}
      <div>
        <div>
          <h3>Laborator:</h3>
          <ul>
            {files.fisiereLaborator.map((file, index) => (
              <li key={index}>
                <button onClick={() => handleFileClick('laborator', file)}>
                  {file}
                </button>
              </li>
            ))}
          </ul>
        </div>
        <div>
          <h3>Curs:</h3>
          <ul>
            {files.fisiereCurs.map((file, index) => (
              <li key={index}>
                <button onClick={() => handleFileClick('curs', file)}>
                  {file}
                </button>
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
};

export default DisciplinaPage;
