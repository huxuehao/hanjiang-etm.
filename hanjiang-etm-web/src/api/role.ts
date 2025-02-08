import request from "@/utils/request";

export const roleAdd = (data: any) => {
  return request({
    url: "/role/add",
    method: "post",
    headers: {
      unrepeat: true,
    },
    data: data,
  });
};

export const roleUpdate = (data: any) => {
  return request({
    url: "/role/update",
    method: "post",
    headers: {
      unrepeat: true,
    },
    data: data,
  });
};

export const roleDelete = (ids: string[]) => {
  return request({
    url: "/role/delete",
    method: "post",
    headers: {
      unrepeat: true,
    },
    data: ids,
  });
};

export const roleTree = (searchConfig: any) => {
  return request({
    url: "/role/tree",
    method: "get",
    params: {
      ...searchConfig,
    },
  });
};

export const roleSelectOne = (id: string) => {
  return request({
    url: "/role/selectOne",
    method: "get",
    params: {
      id,
    },
  });
};

export const saveAuthConfig = (config: any) => {
  return request({
    url: "/role/save-auth-config",
    method: "post",
    headers: {
      unrepeat: true,
    },
    data: config
  });
};

export const getAuthConfig = (roleId: string) => {
  return request({
    url: "/role/get-auth-config",
    method: "get",
    params: {
      roleId,
    },
  });
};

export const saveUserRole = (config: any) => {
  return request({
    url: "/role/save-user-role",
    method: "post",
    headers: {
      unrepeat: true,
    },
    data: config
  });
};

export const getUserRole = (userId: string) => {
  return request({
    url: "/role/get-user-role",
    method: "get",
    params: {
      userId,
    },
  });
};

export const getButtonPermissions = () => {
  return request({
    url: "/role/get-button-permissions",
    method: "get",
  });
};

