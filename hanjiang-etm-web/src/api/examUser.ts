import request from "@/utils/request";

// 分页
export const examUserList = (searchConfig: any) => {
  return request({
    url: "/exam/user/page",
    method: "get",
    params: {
      ...searchConfig,
    },
  });
};

// 参与总人数
export const examUserCounts = (examId: string) => {
  return request({
    url: "/exam/user/counts",
    method: "get",
    params: {
      examId,
    },
  });
};

// 用户完成情况
export const examUserCompletion = (examId: string) => {
  return request({
    url: "/exam/user/completion",
    method: "get",
    params: {
      examId,
    },
  });
};

// 用户合格情况
export const examUserQualified = (examId: string, score: number) => {
  return request({
    url: "/exam/user/qualified",
    method: "get",
    params: {
      examId,
      score,
    },
  });
};
