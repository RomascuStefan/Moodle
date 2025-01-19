import React from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

const YourDisciplinesPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const disciplines = location.state?.disciplines || [];

  const handleDisciplineClick = (cod) => {
    navigate(`/disciplina/${cod}`);
  };

  return (
    <div>
      <h1>Your Disciplines</h1>
      {disciplines.length === 0 ? (
        <p>No disciplines available</p>
      ) : (
        <ul>
          {disciplines.map((discipline, index) => (
            <li
              key={index}
              onClick={() => handleDisciplineClick(discipline.cod)}
              style={{ cursor: 'pointer', color: 'blue', textDecoration: 'underline' }}
            >
              <strong>
                {discipline.numeDisciplina}: {discipline.numeTitular}
              </strong>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default YourDisciplinesPage;
