import request from "@/utils/request";

export const getUserActive = () => {
  return request({
    url: "/statistics/user-active",
    method: "get",
  });
};

export const getQuestionType = () => {
  return request({
    url: "/statistics/question-type",
    method: "get",
  });
};

export const getQuestionLevel = () => {
  return request({
    url: "/statistics/question-level",
    method: "get",
  });
};

export const getExamTrainStatus = () => {
  return request({
    url: "/statistics/exam-train-status",
    method: "get",
  });
};
