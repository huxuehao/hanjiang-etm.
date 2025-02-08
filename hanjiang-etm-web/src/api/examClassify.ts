import request from "@/utils/request";

export const examClassifyTree = () => {
  return request({
    url: "/exam/classify/tree",
    method: "get",
  });
};

//分类新增
export const examClassifyAdd = (config: any) => {
  return request({
    url: "/exam/classify/add",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: config,
  });
};

//分类编辑
export const examClassifyEdit = (config: any) => {
  return request({
    url: "/exam/classify/edit",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: config,
  });
};

//分类删除
export const examClassifyDelete = (ids: string[]) => {
  return request({
    url: "/exam/classify/delete",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: ids,
  });
};

//分类列表
export const examClassifyList = () => {
  return request({
    url: "/exam/classify/list",
    method: "get",
  });
};
