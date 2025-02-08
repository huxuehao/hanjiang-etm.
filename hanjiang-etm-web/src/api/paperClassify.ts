import request from "@/utils/request";

export const paperClassifyList = () => {
  return request({
    url: "/paper/classify/list",
    method: "get",
  });
};
export const paperClassifyTree = () => {
  return request({
    url: "/paper/classify/tree",
    method: "get",
  });
};

export const paperClassifyAdd = (config: any) => {
  return request({
    url: "/paper/classify/add",
    method: "post",
    headers: {
      unrepeat: true,
    },
    data: config,
  });
};

export const paperClassifyEdit = (config: any) => {
  return request({
    url: "/paper/classify/edit",
    method: "post",
    headers: {
      unrepeat: true,
    },
    data: config,
  });
};

export const paperClassifyDelete = (ids: string[]) => {
  return request({
    url: "/paper/classify/delete",
    method: "post",
    headers: {
      unrepeat: true,
    },
    data: ids,
  });
};

export const paperClassifySelectById = (id: string) => {
  return request({
    url: `/paper/classify/select/${id}`,
    method: "get",
  });
};
