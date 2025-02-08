import request from "@/utils/request";

export const questionTemplateList = (searchConfig: any) => {
  return request({
    url: "/question/template/page",
    method: "get",
    params: {
      ...searchConfig,
    },
  });
};

export const questionTemplateAdd = (config: any) => {
  return request({
    url: "/question/template/add",
    method: "post",
    headers: {
      unrepeat: true,
    },
    data: config,
  });
};

export const questionTemplateEdit = (config: any) => {
  return request({
    url: "/question/template/edit",
    method: "post",
    headers: {
      unrepeat: true,
    },
    data: config,
  });
};

export const questionTemplateDelete = (ids: string[]) => {
  return request({
    url: "/question/template/delete",
    method: "post",
    headers: {
      unrepeat: true,
    },
    data: ids,
  });
};

export const questionTemplateSelectById = (id: string) => {
  return request({
    url: `/question/template/select/${id}`,
    method: "get",
  });
};

export const questionFileParser = (file: File) => {
  const formData = new FormData();
  formData.append("file", file);
  return request({
    url: `/question/template/file-parser`,
    method: "post",
    headers: {
      unrepeat: true,
      "Content-Type": "multipart/form-data",
    },
    data: formData,
  });
};
export const downloadExcelTemplate = () => {
  return request({
    url: `/question/template/downloadTemplate`,
    method: "get",
    responseType: "blob",
  });
};

export const submitImport = (data: any) => {
  return request({
    url: "/question/template/submitImport",
    method: "post",
    headers: {
      unrepeat: true,
    },
    data: data,
  });
};
export const cancelImport = (data: any) => {
  return request({
    url: "/question/template/cancelImport",
    method: "post",
    headers: {
      unrepeat: true,
    },
    data: data,
  });
};
