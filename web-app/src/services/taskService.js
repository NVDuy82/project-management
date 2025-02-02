import httpClient from "../configurations/httpClient";
import {API} from "../configurations/configuration";
import {getToken, saveToLocalStorage, setMyProfile} from "./localStorageService";
import {logOut} from "./authenticationService";
import {useNavigate} from "react-router-dom";

export const getMyTasks = async (page) => {
  return await httpClient.get(API.MY_TASK, {
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
    params: {
      page: page,
      size: 10,
    }
  });
};

export const getTaskInProject = async (projectId, page) => {
  return await httpClient.get(API.TASK_IN_PROJECT, {
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
    params: {
      projectId: projectId,
      // page: page,
      // size: 10,
    }
  });
};

export const getTaskById = async (taskId) => {
  return await httpClient.get(API.TASK_BY_ID(taskId), {
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });
};

export const getAllTaskStatus = async () => {
  return await httpClient.get(API.TASK_STATUS_ALL, {
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });
};

export const createTask = async (data) => {
  return await httpClient.post(API.TASK_CREATE, data,{
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });
};

export const updateTask = async (taskId, data) => {
  return await httpClient.put(API.TASK_BY_ID(taskId), data,{
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });
};

export const changeTaskStatus = async (taskId, data) => {
  return await httpClient.post(API.CHANGE_TASK_STATUS(taskId), data,{
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });
};

export const assignTask = async (taskId, data) => {
  return await httpClient.post(API.ASSIGN_TASK(taskId), data,{
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });
};

export const unassignTask = async (taskId) => {
  return await httpClient.post(API.UNASSIGN_TASK(taskId), {},{
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });
};

export const deleteTask = async (taskId) => {
  return await httpClient.delete(API.TASK_BY_ID(taskId),{
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });
};