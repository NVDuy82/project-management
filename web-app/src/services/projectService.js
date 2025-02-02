import httpClient from "../configurations/httpClient";
import {API} from "../configurations/configuration";
import {getToken, saveToLocalStorage, setMyProfile} from "./localStorageService";
import {logOut} from "./authenticationService";
import {useNavigate} from "react-router-dom";

export const getMyProjects = async (page) => {
  return await httpClient.get(API.MY_PROJECT, {
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
    params: {
      page: page,
      size: 10,
    }
  });
};

export const getProjectById = async (projectId) => {
  return await httpClient.get(API.PROJECT_BY_ID(projectId), {
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });
};

export const getMemberInProject = async (projectId, page) => {
  return await httpClient.get(API.GET_MEMBERS, {
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
    params: {
      projectId: projectId,
      // page: page,
      // size: 10,
    },
  });
};

export const createProject = async (data) => {
  return await httpClient.post(API.CREATE_PROJECT, data, {
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });
};

export const leaveProject = async (projectId) => {
  return await httpClient.post(API.LEAVE_PROJECT, {},{
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
    params: {
      projectId: projectId,
    },
  });
};

export const updateProject = async (projectId, data) => {
  return await httpClient.put(API.PROJECT_BY_ID(projectId), data,{
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });
};

export const addMember = async (data) => {
  return await httpClient.post(API.ADD_MEMBER, data, {
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });
};

export const updateMember = async (memberId, role) => {
  return await httpClient.post(API.MEMBER_BY_ID(memberId), {
    role: role,
  }, {
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });
};

export const removeMember = async (memberId) => {
  return await httpClient.delete(API.MEMBER_BY_ID(memberId), {
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });
};

export const searchMember = async (projectId, query) => {
  return await httpClient.post(API.MEMBER_SEARCH, {}, {
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
    params: {
      projectId: projectId,
      query: query,
    }
  });
};


export const getAllProjectRole = async () => {
  return await httpClient.get(API.PROJECT_ROLE_ALL, {
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });
};