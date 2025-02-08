import request from "@/utils/request";

export const menuButtonAdd = (data: any) => {
  return request({
    url: "/menu-button/add",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: data,
  });
};

export const menuButtonUpdate = (data: any) => {
  return request({
    url: "/menu-button/update",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: data,
  });
};

export const menuButtonDelete = (ids: string[]) => {
  return request({
    url: "/menu-button/delete",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: ids,
  });
};

export const menuButtonValid = (ids: string[]) => {
  return request({
    url: "/menu-button/valid",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: ids,
  });
};

export const menuButtonUnValid = (ids: string[]) => {
  return request({
    url: "/menu-button/unValid",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: ids,
  });
};

export const menuButtonPage = (searchConfig: any) => {
  return request({
    url: "/menu-button/page",
    method: "get",
    params: {
      ...searchConfig,
    },
  });
};

export const menuButtonSelectOne = (id: string) => {
  return request({
    url: "/menu-button/selectOne",
    method: "get",
    params: {
      id,
    },
  });
};

export const selectByIds = (ids: any[]) => {
  return request({
    url: "/menu-button/selectByIds",
    method: "post",
    data: ids,
  });
};

export const menuButtonTree = () => {
  return request({
    url: "/menu-button/menu-button-tree",
    method: "get",
  });
};