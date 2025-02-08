import request from "@/utils/request";

export const paramsPage = (searchConfig: any) => {
  return request({
    url: "/params/page",
    method: "get",
    params: {
      ...searchConfig,
    },
  });
};

export const paramsAdd = (body: any) => {
  return request({
    url: "/params/add",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: body,
  });
};

export const paramsUpdate = (body: any) => {
  return request({
    url: "/params/update",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: body,
  });
};


export const paramsDelete = (ids: string[]) => {
  return request({
    url: "/params/delete",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: ids,
  });
};

export const paramsSelectOne = (id: string) => {
  return request({
    url: "/params/selectOne",
    method: "get",
    params: {
      id,
    },
  });
};

export const fetchValueByKey = (key: string) => {
  return request({
    url: "/params/fetch-value-by-key",
    method: "get",
    params: {
      key,
    },
  });
};
