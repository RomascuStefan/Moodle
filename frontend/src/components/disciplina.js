import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getLectureByCod } from './../client/sqlClinent';
import { getFilesByCod, downloadFile } from './../client/mongoClient';

const DisciplinaPage = () => {
  const { cod } = useParams();
  const [disciplina, setDisciplina] = useState(null);
  const [files, setFiles] = useState({ fisiereLaborator: [], fisiereCurs: [] });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      setError(null);

      try {
        const lectureData = await getLectureByCod(cod);
        setDisciplina(lectureData);

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
      // Pass the correct lowercase values for "locatie"
      await downloadFile(cod, locatie.toLowerCase(), numeFisier);
    } catch (err) {
      alert(`Error downloading file: ${err.message}`);
    }
  };

  return (
    <div>
      {loading && <p>Se încarcă...</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}
      {disciplina && (
        <div>
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
