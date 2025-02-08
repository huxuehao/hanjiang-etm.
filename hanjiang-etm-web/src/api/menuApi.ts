import request from "@/utils/request";

export const menuApiAdd = (data: any) => {
  return request({
    url: "/menu-api/add",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: data,
  });
};

export const menuApiUpdate = (data: any) => {
  return request({
    url: "/menu-api/update",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: data,
  });
};

export const menuApiDelete = (ids: string[]) => {
  return request({
    url: "/menu-api/delete",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: ids,
  });
};

export const menuApiValid = (ids: string[]) => {
  return request({
    url: "/menu-api/valid",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: ids,
  });
};

export const menuApiUnValid = (ids: string[]) => {
  return request({
    url: "/menu-api/unValid",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: ids,
  });
};

export const menuApiPage = (searchConfig: any) => {
  return request({
    url: "/menu-api/page",
    method: "get",
    params: {
      ...searchConfig,
    },
  });
};

export const menuApiSelectOne = (id: string) => {
  return request({
    url: "/menu-api/selectOne",
    method: "get",
    params: {
      id,
    },
  });
};

export const selectByIds = (ids: any[]) => {
  return request({
    url: "/menu-api/selectByIds",
    method: "post",
    data: ids,
  });
};

export const apiButtonTree = () => {
  return request({
    url: "/menu-api/menu-button-tree",
    method: "get",
  });
};