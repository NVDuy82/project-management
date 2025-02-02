import {getToken, removeToken, setToken} from "./localStorageService";
import httpClient from "../configurations/httpClient";
import {API} from "../configurations/configuration";

export const logIn = async (username, password) => {
  const response = await httpClient.post(API.LOGIN, {
    username: username,
    password: password,
  });

  setToken(response.data?.result?.token);

  return response;
};

export const introspect = async () => {
  const response = await httpClient.post(API.INTROSPECT, {
    token: getToken(),
  })
    .catch((error) => {
      return false;
    });

  return response.data?.result?.valid;
};

export const logOut = async () => {
  await httpClient.post(API.LOGOUT, {
    token: getToken(),
  })
    .catch(error => {});

  removeToken();
};

export const refreshToken = async () => {
  const response = await httpClient.post(API.REFRESH_TOKEN, {
    token: getToken(),
  })
    .catch(error => {});

  if (response?.status === 200) {
    setToken(response.data?.result?.token);
  }

  return response;
};

export const isAuthenticated = async () => {
  if (!getToken()) return false;

  if ((await introspect()) === true) {
    return true;
  } else {
    await refreshToken();
    const result = await introspect();
    console.log("Ket qua: ", result);
    return result;
  }
};
