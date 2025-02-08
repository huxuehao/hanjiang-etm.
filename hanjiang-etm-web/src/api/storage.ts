import request from "@/utils/request";

export const storageAdd = (data: any) => {
  return request({
    url: "/storage/add",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: data,
  });
};

export const storageUpdate = (data: any) => {
  return request({
    url: "/storage/update",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: data,
  });
};

export const storageDelete = (ids: string[]) => {
  return request({
    url: "/storage/delete",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: ids,
  });
};

export const storagePage = (searchConfig: any) => {
  return request({
    url: "/storage/page",
    method: "get",
    params: {
      ...searchConfig,
    },
  });
};

export const storageSelectOne = (id: string) => {
  return request({
    url: "/storage/selectOne",
    method: "get",
    params: {
      id,
    },
  });
};

export const storageUpdateProtocol = (data: any) => {
  return request({
    url: "/storage/updateProtocol",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: data,
  });
};

export const storageValidSuccess = (id: any) => {
  return request({
    url: "/storage/validSuccess",
    method: "get",
    headers: {
      unrepeat: true
    },
    params: {
      id
    }
  });
};
