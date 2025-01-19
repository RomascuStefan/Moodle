import React, { useEffect, useState } from 'react';
import { getLectures } from './../client/sqlClinent';

const IndexPage = () => {
  const [lectures, setLectures] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(15);
  const [links, setLinks] = useState({});
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchLectures = async (page) => {
    setLoading(true);
    setError(null);

    try {
      const data = await getLectures(page, itemsPerPage);
      setLectures(data._embedded ? data._embedded['disciplinaDTOList'] : []);
      setLinks(data._links || {});
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchLectures(currentPage);
  }, [currentPage]);

  const handleNextPage = () => {
    if (links.next) {
      setCurrentPage((prev) => prev + 1);
    }
  };

  const handlePrevPage = () => {
    if (links.previous) {
      setCurrentPage((prev) => Math.max(prev - 1, 1));
    }
  };

  return (
    <div>
      <h1>Lista Disciplinelor</h1>
      {loading && <p>Se încarcă...</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <ul>
        {lectures.map((lecture, index) => (
          <li key={index}>
            <strong>
              <a href={`/disciplina/${lecture.cod}`}>{lecture.numeDisciplina}</a>
            </strong> - {lecture.tipDisciplina}
          </li>
        ))}
      </ul>
      <div>
        {links.previous && <button onClick={handlePrevPage}>Pagina Anterioară</button>}
        {links.next && <button onClick={handleNextPage}>Pagina Următoare</button>}
      </div>
    </div>
  );
};

export default IndexPage;
