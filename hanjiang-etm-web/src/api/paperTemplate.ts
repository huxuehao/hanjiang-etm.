import request from "@/utils/request";

export const paperTemplateList = (searchConfig: any) => {
  return request({
    url: "/paper/template/page",
    method: "get",
    params: {
      ...searchConfig,
    },
  });
};

export const paperTemplateAdd = (config: any) => {
  return request({
    url: "/paper/template/add",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: config,
  });
};

export const paperTemplateEdit = (config: any) => {
  return request({
    url: "/paper/template/edit",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: config,
  });
};

export const paperTemplateDelete = (ids: string[]) => {
  return request({
    url: "/paper/template/delete",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: ids,
  });
};

export const paperTemplateSelectById = (id: string | null) => {
  return request({
    url: `/paper/template/select/${id}`,
    method: "get",
  });
};
