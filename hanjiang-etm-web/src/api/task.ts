import request from "@/utils/request";

export const taskList = (searchConfig: any) => {
  return request({
    url: "/task/list",
    method: "get",
    params: {
      ...searchConfig
    }
  });
};
export const taskPage = (searchConfig: any) => {
  return request({
    url: "/task/page",
    method: "get",
    params: {
      ...searchConfig
    }
  });
};
