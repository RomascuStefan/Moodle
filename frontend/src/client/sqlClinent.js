import { createHeaders, buildUrlWithParams } from './authHelper';

const BASE_URL = 'http://localhost:8080/api/academia';

export const getLectures = async (page = 1, itemsPerPage = 15, type = null, category = null) => {
    const params = {
        page: page.toString(),
        items_per_page: itemsPerPage.toString(),
        ...(type && { type }),
        ...(category && { category }),
    };
    
    const url = buildUrlWithParams(`${BASE_URL}/student/lectures`, params);
    
    const headers = createHeaders();
    
    const response = await fetch(url, { headers });
    
    if (!response.ok) {
        throw new Error(`Failed to fetch lectures: ${response.statusText}`);
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
