import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createDisciplina } from './../client/sqlClinent';

const CreateDisciplinaPage = () => {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    titularId: '',
    numeDisciplina: '',
    anStudiu: '',
    tipDisciplina: 'impusa',
    categorieDisciplina: 'domeniu',
    tipExaminare: 'examen',
  });
  const [error, setError] = useState(null);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);

    try {
      await createDisciplina(formData);
      navigate('/'); // Redirect to the main page
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div>
      <h1>Create Disciplina</h1>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <form onSubmit={handleSubmit}>
        <div>
          <label>Titular ID:</label>
          <input
            type="text"
            name="titularId"
            value={formData.titularId}
            onChange={handleChange}
          />
        </div>
        <div>
          <label>Nume Disciplina:</label>
          <input
            type="text"
            name="numeDisciplina"
            value={formData.numeDisciplina}
            onChange={handleChange}
          />
        </div>
        <div>
          <label>An Studiu:</label>
          <input
            type="number"
            name="anStudiu"
            min="1"
            max="4"
            value={formData.anStudiu}
            onChange={handleChange}
          />
        </div>
        <div>
          <label>Tip Disciplina:</label>
          <select
            name="tipDisciplina"
            value={formData.tipDisciplina}
            onChange={handleChange}
          >
            <option value="impusa">Impusa</option>
            <option value="optionala">Optionala</option>
            <option value="liber_aleasa">Liber Aleasa</option>
          </select>
        </div>
        <div>
          <label>Categorie Disciplina:</label>
          <select
            name="categorieDisciplina"
            value={formData.categorieDisciplina}
            onChange={handleChange}
          >
            <option value="domeniu">Domeniu</option>
            <option value="specialitate">Specialitate</option>
            <option value="adiacenta">Adiacenta</option>
          </select>
        </div>
        <div>
          <label>Tip Examinare:</label>
          <select
            name="tipExaminare"
            value={formData.tipExaminare}
            onChange={handleChange}
          >
            <option value="examen">Examen</option>
            <option value="colocviu">Colocviu</option>
          </select>
        </div>
        <button type="submit">Create</button>
      </form>
    </div>
  );
};

export default CreateDisciplinaPage;
