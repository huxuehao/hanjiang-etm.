import request from "@/utils/request";

export const examTemplateList = (searchConfig: any) => {
  return request({
    url: "/exam/template/page",
    method: "get",
    params: {
      ...searchConfig,
    },
  });
};

export const paperQuestionList = (serchConfig: any) => {
  return request({
    url: "/paper/question/page",
    method: "get",
    params: {
      ...serchConfig,
    },
  });
};

export const examTemplateAdd = (config: any) => {
  return request({
    url: "/exam/template/add",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: config,
  });
};

export const examTemplateSelectById = (id: string[]) => {
  return request({
    url: `/exam/template/select/${id}`,
    method: "get",
  });
};

export const examTemplateEdit = (config: any) => {
  return request({
    url: "/exam/template/edit",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: config,
  });
};

export const examTemplateDelete = (ids: string) => {
  return request({
    url: "/exam/template/delete",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: ids,
  });
};

/**
 * 发布考试
 * @param id 考试ID
 */
export const examTemplatePublish = (id: string) => {
  return request({
    url: `/exam/template/publish/${id}`,
    method: "get",
  });
};
export const examTemplatePublishV2 = (id: string) => {
  return request({
    url: `/exam/template/publishV2/${id}`,
    method: "get",
  });
};
/**
 * 撤销发布
 * @param id 考试ID
 * @param force 0：普通撤销，1：强制撤销
 */
export const examTemplateCancelPublish = (id: string, force: number) => {
  return request({
    url: `/exam/template/cancelPublish/${id}`,
    method: "get",
    params: {
      force: force,
    },
  });
};
export const examTemplateCancelPublishV2 = (id: string, force: number) => {
  return request({
    url: `/exam/template/cancelPublishV2/${id}`,
    method: "get",
    params: {
      force: force,
    },
  });
};

export const examUserConfig = (examId: string, userId: string) => {
  return request({
    url: "/exam/template/examConfig",
    method: "get",
    params: {
      examId: examId,
      userId: userId,
    },
  });
};

export const examConfigByCorrect = (examId: string, userId: string) => {
  return request({
    url: "/exam/template/examConfigByCorrect",
    method: "get",
    params: {
      examId: examId,
      userId: userId,
    },
  });
};

export const examAnswer = (config: any) => {
  return request({
    url: "/exam/template/examAnswer",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: config,
  });
};
export const examAnswerV2 = (config: any) => {
  return request({
    url: "/exam/template/examAnswerV2",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: config,
  });
};

export const examCorrect = (config: any) => {
  return request({
    url: "/exam/template/correct",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: config,
  });
};

export const examProgressExport = (examId: string) => {
  return request({
    url: "/exam/template/examProgressExport",
    method: "get",
    responseType: "blob",
    params: {
      examId: examId,
    },
  });
};

/**
 * 检测随机option是否合法
 */
export const checkRandomOption = (data: any) => {
  return request({
    url: "/exam/template/checkRandomOption",
    method: "post",
    headers: {
      unrepeat: true
    },
    data: data,
  });
};
