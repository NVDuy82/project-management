import httpClient from "../configurations/httpClient";
import {API} from "../configurations/configuration";
import {getToken, saveToLocalStorage, setMyProfile} from "./localStorageService";
import {logOut} from "./authenticationService";
import {useNavigate} from "react-router-dom";

export const getMyProfile = async () => {
  return await httpClient.get(API.MY_PROFILE, {
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });
};

export const getMyInfo = async () => {
  return await httpClient.get(API.MY_INFO, {
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });
};

export const useLoadUserInfo = () => {
  const navigate = useNavigate();

  return async () => {
    try {
      const response = await getMyProfile();
      setMyProfile(response.data?.result);
    } catch (error) {
      if (error.response?.status === 401) {
        logOut();
        navigate("/login");
      } else {
        console.error(error);
      }
    }
  };
};

export const getProfile = async (userId) => {
  return await httpClient.get(API.PROFILE_BY_USERID(userId), {
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });
};

export const createUser = async (username, password, email, fullName) => {
  return await httpClient.post(API.REGISTRATION, {
    username,
    password,
    email,
    fullName,
  });
};

export const updateMyProfile = async (data) => {
  return await httpClient.put(API.MY_PROFILE, data, {
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });
};


export const searchUser = async (query) => {
  return await httpClient.post(API.USER_SEARCH, {}, {
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
    params: {
      query: query,
    }
  });
};


export const changePassword = async (data) => {
  return await httpClient.post(API.CHANGE_PASSWORD, data, {
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });
};

export const getAllUser = async () => {
  return await httpClient.get(API.GET_ALL_USER, {
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });
};

export const changeUserStatus = async (data) => {
  return await httpClient.post(API.CHANGE_USER_STATUS, data, {
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });
};

export const getAllUserStatus = async () => {
  return await httpClient.post(API.GET_ALL_USER_STATUS, {
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });
};