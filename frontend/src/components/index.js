import React, { useEffect, useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { getLectures, getDisciplinesByRole } from './../client/sqlClinent';

const IndexPage = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const queryParams = new URLSearchParams(location.search);
  const initialPage = parseInt(queryParams.get('page')) || 0;

  const [lectures, setLectures] = useState([]);
  const [links, setLinks] = useState({});
  const [currentPage, setCurrentPage] = useState(initialPage);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const updateURL = (page) => {
    const params = new URLSearchParams();
    params.set('page', page);
    navigate({ search: params.toString() });
  };

  const fetchLecturesByPage = async (page) => {
    setLoading(true);
    setError(null);

    try {
      const data = await getLectures(page);
      setLectures(data._embedded?.disciplinaDTOList || []);
      setLinks(data._links || {});
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchLecturesByPage(currentPage);
  }, [currentPage]);

  useEffect(() => {
    updateURL(currentPage);
  }, [currentPage]);

  const handleViewYourDisciplines = async () => {
    const link = links['view-your-discipline']?.href;

    if (!link) {
      return;
    }

    try {
      const data = await getDisciplinesByRole(link);
      navigate('/your-disciplines', { state: { disciplines: data._embedded?.disciplinaDTOList || [] } });
    } catch (err) {
      setError(err.message);
    }
  };

  const handleNextPage = () => {
    if (links['next_page']) {
      setCurrentPage((prevPage) => prevPage + 1);
    }
  };

  const handlePreviousPage = () => {
    if (links['previous_page']) {
      setCurrentPage((prevPage) => Math.max(prevPage - 1, 0));
    }
  };

  const handleCreateDisciplina = () => {
    navigate('/create-disciplina');
  };

  return (
    <div>
      <h1>Lista Discipline</h1>
      {links['add-disciplina'] && (
        <button onClick={handleCreateDisciplina} style={{ marginBottom: '20px' }}>
          Create Disciplina
        </button>
      )}
      {loading && <p>Se încarcă...</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <ul>
        {lectures.map((lecture) => (
          <li key={lecture.cod}>
            <strong>
              {lecture.numeDisciplina}: {lecture.numeTitular}
            </strong>
          </li>
        ))}
      </ul>
      <div>
        {links['previous_page'] && (
          <button onClick={handlePreviousPage}>PREVIOUS</button>
        )}
        {links['next_page'] && (
          <button onClick={handleNextPage}>NEXT</button>
        )}
      </div>
      <div>
        {links['view-your-discipline'] && (
          <button onClick={handleViewYourDisciplines}>
            View Your Disciplines
          </button>
        )}
      </div>
    </div>
  );
};

export default IndexPage;
