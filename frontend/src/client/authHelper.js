const TOKEN_KEY = 'jwt';

export const getToken = () => {
    const token = localStorage.getItem(TOKEN_KEY);
    if (!token) {
        throw new Error('Missing token. Please log in.');
    }
    return token;
};

export const createHeaders = () => {
    const token = getToken();
    return {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
    };
};

export const buildUrlWithParams = (baseUrl, params) => {
    const urlParams = new URLSearchParams(params);
    return `${baseUrl}?${urlParams.toString()}`;
};

export const setToken = (token) => {
    localStorage.setItem(TOKEN_KEY, token);
};