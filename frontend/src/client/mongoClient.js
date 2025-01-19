import { createHeaders } from './authHelper';

const BASE_URL = 'http://localhost:8081/api/academia_mongo';

export const getFilesByCod = async (cod) => {
  const url = `${BASE_URL}/${cod}?action=get_files`;

  const headers = createHeaders();

  const response = await fetch(url, { headers });

  if (!response.ok) {
    throw new Error(`Failed to fetch files for cod ${cod}: ${response.statusText}`);
  }

  return response.json();
};

export const downloadFile = async (cod, locatie, numeFisier) => {
  const url = `${BASE_URL}/${cod}?action=download&locatie=${encodeURIComponent(locatie)}&numeFisier=${encodeURIComponent(numeFisier)}`;

  const headers = createHeaders();

  const response = await fetch(url, { headers });

  if (!response.ok) {
    throw new Error(`Failed to download file ${numeFisier}: ${response.statusText}`);
  }

  // Download file as a blob
  const blob = await response.blob();
  const downloadUrl = window.URL.createObjectURL(blob);

  const a = document.createElement('a');
  a.href = downloadUrl;
  a.download = numeFisier;
  document.body.appendChild(a);
  a.click();
  document.body.removeChild(a);
};
