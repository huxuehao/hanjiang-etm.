import request from "@/utils/request";

export const attachPage = (searchConfig: any) => {
  return request({
    url: "/attach/page",
    method: "get",
    params: {
      ...searchConfig,
    },
  });
};

export const attachList = (ids: string[]) => {
  return request({
    url: "/attach/list",
    method: "get",
    headers: {
      unrepeat: true
    },
    data: ids,
  });
};

export const attachDelete = (ids: string[]) => {
  return request({
    url: "/attach/delete",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: ids,
  });
};

export const attachSelectOne = (id: string) => {
  return request({
    url: "/attach/selectOne",
    method: "get",
    params: {
      id,
    },
  });
};

// 普通上传
export const attachUpload = (file: any) => {
  const formData = new FormData();
  formData.append('file', file);
  return request({
    url: "/attach/upload",
    method: "post",
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data: formData,
  });
};

// 分片上传
export const attachChunkUpload = (formData: FormData) => {
  return request({
    url: "/attach/chunk-upload",
    method: "post",
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data: formData,
  });
};

export const attachDownload = (id: string) => {
  return request({
    url: `/attach/download/${id}`,
    method: "get",
  });
};

export const attachBatchDownload = (ids: string[]) => {
  return request({
    url: "/attach/batchDownload",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: ids,
  });
};

export const attachLogPage = (searchConfig: any) => {
  return request({
    url: "/attach/log/page",
    method: "get",
    params: {
      ...searchConfig,
    },
  });
};
