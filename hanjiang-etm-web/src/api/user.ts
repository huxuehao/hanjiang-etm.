import request from "@/utils/request";

export const userAdd = (data: any) => {
  return request({
    url: "/user/add",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: data,
  });
};

export const userUpdate = (data: any) => {
  return request({
    url: "/user/update",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: data,
  });
};

export const useRePwd = (data: any) => {
  return request({
    url: "/user/re-pwd",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: data,
  });
};

export const userDelete = (ids: string[]) => {
  return request({
    url: "/user/delete",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: ids,
  });
};

export const userValid = (ids: string[]) => {
  return request({
    url: "/user/valid",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: ids,
  });
};

export const userResetPwd = (ids: string[]) => {
  return request({
    url: "/user/resetPwd",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: ids,
  });
};

export const userPage = (searchConfig: any) => {
  return request({
    url: "/user/page",
    method: "get",
    params: {
      ...searchConfig,
    },
  });
};

export const userSelectOne = (id: string) => {
  return request({
    url: "/user/selectOne",
    method: "get",
    params: {
      id,
    },
  });
};

export const selectByIds = (ids: any[]) => {
  return request({
    url: "/user/selectByIds",
    method: "post",
    data: ids,
  });
};

export const selectCurrentUserInfo = () => {
  return request({
    url: "/user/current-user-info",
    method: "get",
  });
};

export const login = (body: any) => {
  return request({
    url: "/user/login",
    method: "post",
    headers: {
      token: false,
      unrepeat: true
    },
    data: body,
  });
};

export const logout = (id: string) => {
  return request({
    url: "/user/logout",
    method: "get",
    params: {
      id,
    },
  });
};

export const refreshToken = (refreshToken: string) => {
  return request({
    url: "/user/refresh-token",
    method: "get",
    headers: {
      _retry: true,
    },
    params: {
      refreshToken,
    },
  });
};
