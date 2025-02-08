import request from "@/utils/request";

export const orgAdd = (data: any) => {
    return request({
      url: "/org/add",
      method: "post",
      headers: {
        unrepeat: true
      },
      data: data,
    });
  };

  export const orgUpdate = (data: any) => {
    return request({
      url: "/org/update",
      method: "post",
      headers: {
        unrepeat: true
      },
      data: data,
    });
  };


  export const orgDelete = (ids: string[]) => {
    return request({
      url: "/org/delete",
      method: "post",
      headers: {
        unrepeat: true
      },
      data: ids,
    });
  };

export const orgTree = (searchConfig: any) => {
  return request({
    url: "/org/tree",
    method: "get",
    params: {
      ...searchConfig,
    },
  });
};

export const orgSelectOne = (id: string) => {
    return request({
      url: "/org/selectOne",
      method: "get",
      params: {
        id
      },
    });
  };