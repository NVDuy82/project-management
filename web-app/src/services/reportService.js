import httpClient from "../configurations/httpClient";
import {API} from "../configurations/configuration";
import {getToken, saveToLocalStorage, setMyProfile} from "./localStorageService";
import {logOut} from "./authenticationService";
import {useNavigate} from "react-router-dom";

export const getReport = async (projectId) => {
  return await httpClient.get(API.REPORT, {
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
    params: {
      projectId: projectId,
    }
  });
};
