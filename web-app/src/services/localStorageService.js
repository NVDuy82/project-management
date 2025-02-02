export const KEY_TOKEN = "accessToken";
export const KEY_PROFILE = "myProfile";


export const setToken = (token) => {
  localStorage.setItem(KEY_TOKEN, token);
};

export const getToken = () => {
  return localStorage.getItem(KEY_TOKEN);
};

export const removeToken = () => {
  return localStorage.removeItem(KEY_TOKEN);
};

export const setMyProfile = (data) => {
  localStorage.setItem(KEY_PROFILE, JSON.stringify(data));
};

export const getMyProfile = () => {
  const data = localStorage.getItem(KEY_PROFILE)
  return data ? JSON.parse(data) : null;
};

export const removeMyProfile = () => {
  localStorage.removeItem(KEY_PROFILE);
};