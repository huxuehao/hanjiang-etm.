import request from "@/utils/request";

export const trainTemplateList = (searchConfig: any) => {
  return request({
    url: "/training/page",
    method: "get",
    params: {
      ...searchConfig,
    },
  });
};

export const trainTemplateAdd = (config: any) => {
  return request({
    url: "/training/add",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: config,
  });
};

export const trainTemplateEdit = (config: any) => {
  return request({
    url: "/training/edit",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: config,
  });
};

export const trainTemplateDelete = (ids: string[]) => {
  return request({
    url: "/training/delete",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: ids,
  });
};

export const trainTemplateSelectById = (id: string) => {
  return request({
    url: `/training/select/${id}`,
    method: "get",
  });
};

export const trainPublish = (id: string) => {
  return request({
    url: `/training/publish/${id}`,
    method: "get",
  });
};
export const trainCancelPublish = (id: string, force: number) => {
  return request({
    url: `/training/cancelPublish/${id}`,
    method: "get",
    params: {
      force: force,
    },
  });
};

export const getUserTrainConfig = (trainId: string, userId: string) => {
  return request({
    url: `/training/getUserTrainConfig`,
    method: "get",
    params: {
      trainId: trainId,
      userId: userId,
    },
  });
};

export const paperAnswer = (config: any) => {
  return request({
    url: `/training/paperAnswer`,
    method: "post",
    headers: {
      unrepeat: true
    },
    data: config,
  });
};

export const trainProgressExport = (trainId: string) => {
  return request({
    url: "/training/trainProgressExport",
    method: "get",
    responseType: "blob",
    params: {
      trainId: trainId,
    },
  });
};
