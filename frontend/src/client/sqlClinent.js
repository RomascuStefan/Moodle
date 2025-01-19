import { createHeaders } from './authHelper';

const BASE_URL = 'http://localhost:8080/api/academia';

export const getLectures = async (page = 0) => {
  const headers = createHeaders();

  const response = await fetch(`${BASE_URL}/lectures?page=${page}`, { headers });

  if (!response.ok) {
    throw new Error(`Failed to fetch lectures: ${response.statusText}`);
  }

  return response.json();
};

export const getDisciplinesByRole = async (link) => {
  const headers = createHeaders();

  const cleanLink = link.split('{')[0]
  const response = await fetch(cleanLink, { headers });

  if (!response.ok) {
    throw new Error(`Failed to fetch disciplines by role: ${response.statusText}`);
  }

  return response.json();
};

export const getLectureByCod = async (cod) => {
  const url = `${BASE_URL}/lectures/${cod}`;
  
  const headers = createHeaders();

  const response = await fetch(url, { headers });

  if (!response.ok) {
    throw new Error(`Failed to fetch lecture with cod ${cod}: ${response.statusText}`);
  }

  return response.json();
};

export const createDisciplina = async (disciplinaData) => {
    const response = await fetch(`${BASE_URL}/lectures`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${localStorage.getItem('jwt')}`,
      },
      body: JSON.stringify(disciplinaData),
    });
  
    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Failed to create disciplina');
    }
    return response.json();
  };
