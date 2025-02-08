import request from "@/utils/request";

export const questionClassifyList = () => {
  return request({
    url: "/question/classify/list",
    method: "get",
  });
};
export const questionClassifyTree = () => {
  return request({
    url: "/question/classify/tree",
    method: "get",
  });
};

export const questionClassifyAdd = (config: any) => {
  return request({
    url: "/question/classify/add",
    method: "post",
    headers: {
      unrepeat: true,
    },
    data: config,
  });
};

export const questionClassifyEdit = (config: any) => {
  return request({
    url: "/question/classify/edit",
    method: "post",
    headers: {
      unrepeat: true,
    },
    data: config,
  });
};

export const questionClassifyDelete = (ids: string[]) => {
  return request({
    url: "/question/classify/delete",
    method: "post",
    headers: {
      unrepeat: true,
    },
    data: ids,
  });
};

export const questionClassifySelectById = (id: string) => {
  return request({
    url: `/question/classify/select/${id}`,
    method: "get",
  });
};
