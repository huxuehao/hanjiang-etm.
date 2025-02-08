import request from "@/utils/request";

export const endpointScanAndSaveApi = () => {
  return request({
    url: "/endpoint/scan-and-save-api",
    method: "get",
    headers: {
      unrepeat: true
    }
  });
};