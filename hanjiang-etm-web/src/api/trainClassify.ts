import request from "@/utils/request";

export const trainClassifyList = () => {
  return request({
    url: "/training/classify/list",
    method: "get",
  });
};
export const trainClassifyTree = () => {
  return request({
    url: "/training/classify/tree",
    method: "get",
  });
};

export const trainClassifyAdd = (config: any) => {
  return request({
    url: "/training/classify/add",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: config,
  });
};

export const trainClassifyEdit = (config: any) => {
  return request({
    url: "/training/classify/edit",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: config,
  });
};

export const trainClassifyDelete = (ids: string[]) => {
  return request({
    url: "/training/classify/delete",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: ids,
  });
};

export const trainClassifySelectById = (id: string) => {
  return request({
    url: `/training/classify/select/${id}`,
    method: "get",
  });
};
