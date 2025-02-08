import request from "@/utils/request";

export const menuAdd = (data: any) => {
  return request({
    url: "/menu/add",
    method: "post",
    headers: {
      unrepeat: true,
    },
    data: data,
  });
};

export const menuUpdate = (data: any) => {
  return request({
    url: "/menu/update",
    method: "post",
    headers: {
      unrepeat: true,
    },
    data: data,
  });
};

export const menuDelete = (ids: string[]) => {
  return request({
    url: "/menu/delete",
    method: "post",
    headers: {
      unrepeat: true,
    },
    data: ids,
  });
};

export const menuTree = (searchConfig: any) => {
  return request({
    url: "/menu/tree",
    method: "get",
    params: {
      ...searchConfig,
    },
  });
};

export const menuTreePermission = () => {
  return request({
    url: "/menu/permission-tree",
    method: "get",
  });
};

export const menuList = () => {
  return request({
    url: "/menu/list",
    method: "get",
  });
};

export const menuSelectOne = (id: string) => {
  return request({
    url: "/menu/selectOne",
    method: "get",
    params: {
      id,
    },
  });
};

export const menuSetVliad = (ids: string[], valid: number) => {
  return request({
    url: "/menu/set-valid",
    method: "post",
    headers: {
      unrepeat: true,
    },
    params: {
      valid
    },
    data: ids,
  });
};