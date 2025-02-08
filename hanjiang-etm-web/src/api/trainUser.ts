import request from "@/utils/request";

// 分页
export const trainUserList = (searchConfig: any) => {
  return request({
    url: "/training/user/page",
    method: "get",
    params: {
      ...searchConfig,
    },
  });
};

// 参与总人数
export const trainUserCounts = (trainId: string) => {
  return request({
    url: "/training/user/counts",
    method: "get",
    params: {
      trainId,
    },
  });
};

// 用户完成情况
export const trainUserCompletion = (trainId: string) => {
  return request({
    url: "/training/user/completion-info",
    method: "get",
    params: {
      trainId,
    },
  });
};

// 用户合格情况
export const trainUserQualified = (trainId: string, score: number) => {
  return request({
    url: "/training/user/qualified-info",
    method: "get",
    params: {
      trainId,
      score,
    },
  });
};
