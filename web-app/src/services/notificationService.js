import httpClient from "../configurations/httpClient";
import {API} from "../configurations/configuration";
import {getToken, saveToLocalStorage, setMyProfile} from "./localStorageService";
import {logOut} from "./authenticationService";
import {useNavigate} from "react-router-dom";

export const getMyNotifications = async (page) => {
  return await httpClient.get(API.MY_NOTIFICATION, {
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
    params: {
      page: page,
      size: 10,
    }
  });
};

