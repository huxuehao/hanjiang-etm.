import { getButtonPermissions } from "@/api/role";
import cache from "./cache";
import setting from "@/config/setting";

// 获取用户权限
const permissionsKey = setting.permissions
export const getUserPermissions = () => {
  return new Promise(async (resolve, reject) => {
    const permissions = cache.local.getJSON(permissionsKey)
    if(!isEmpty(permissions)) {
      resolve(permissions);
      return
    }
    await getButtonPermissions().then((res) => {
      cache.local.setJSON(permissionsKey, res.data.data)
      resolve(res.data.data);
    }).catch((error) => {
      reject(error)
    })
  })
  
};

/**
 * 转换请求参数，将对象转成字符串链，主要是为了除了可能存在的问题
 * @param params 请求参数
 */
export function tansRequestParams(params: any) {
  let result = "";
  for (const propName of Object.keys(params)) {
    const value = params[propName];
    var part = encodeURIComponent(propName) + "=";
    if (value !== null && value !== "" && typeof value !== "undefined") {
      if (typeof value === "object") {
        for (const key of Object.keys(value)) {
          if (
            value[key] !== null &&
            value[key] !== "" &&
            typeof value[key] !== "undefined"
          ) {
            let params = propName;
            var subPart = encodeURIComponent(params) + "=";
            result += subPart + encodeURIComponent(value[key]) + "&";
          }
        }
      } else {
        result += part + encodeURIComponent(value) + "&";
      }
    }
  }
  return result;
}

/**
 * 检测请求是否重复
 * @param url 请求路径
 * @param body 请求体
 */
export function checkRequestRepeat(url: string, body: object) {
  const currnetReqSession = {
    url: url,
    data: typeof body === "object" ? JSON.stringify(body) : body,
    time: new Date().getTime(),
  };

  const lastReqSession = cache.session.getJSON("lastReqSession");
  if (
    lastReqSession === null ||
    lastReqSession === undefined ||
    lastReqSession === ""
  ) {
    cache.session.setJSON("lastReqSession", currnetReqSession);
    return false;
  } else {
    // 上次的请求地址
    const l_url = lastReqSession.url;
    // 上次的请求数据
    const l_data = lastReqSession.data;
    // 上次的请求时间
    const l_time = lastReqSession.time;
    // 间隔时间(ms)，小于此时间视为重复提交
    const interval = setting.repeatReqInterval;
    if (
      l_url === currnetReqSession.url &&
      l_data === currnetReqSession.data &&
      currnetReqSession.time - l_time < interval
    ) {
      cache.session.setJSON("lastReqSession", currnetReqSession);
      return true;
    } else {
      cache.session.setJSON("lastReqSession", currnetReqSession);
      return false;
    }
  }
}

/**
 * 函数防抖
 * @param func 函数
 * @param delay 防抖间隔
 * @param immediate 是否立即执行
 */
export const debounce = (func: Function, delay: number, immediate: boolean) => {
  let timer: any;
  return function (this: any, ...args: any[]) {
    if (timer) clearTimeout(timer);
    if (immediate) {
      const firstRun = !timer;
      timer = setTimeout(() => {
        timer = null;
      }, delay);
      if (firstRun) {
        func.apply(this, args);
      }
    } else {
      timer = setTimeout(() => {
        func.apply(this, args);
      }, delay);
    }
  };
};

/**
 * 格式化日期时间
 * @param time 日期，支持多种格式（number | string | Date）
 * @param pattern 格式化字符串（如：yyyy-MM-dd HH:mm:ss）
 * @returns 格式化后字符串
 */
export function formatDate(time: number | string | Date, pattern: string = "yyyy-MM-dd HH:mm:ss") {
  let date: Date;

  // 将 time 转成 Date
  if (typeof time === "string") {
    date = new Date(time);
  } else if (typeof time === "number") {
    date = new Date(time);
  } else if (time instanceof Date) {
    date = time;
  } else {
    throw new Error("Invalid time argument");
  }

  // 补充 0
  const pad = (n: number | string) => {
    if (typeof n === "string") {
      return Number(n) < 10 ? "0" + n : n.toString();
    } else {
      return n < 10 ? "0" + n : n.toString();
    }
  };

  // 根据 pattern 格式化日期
  return pattern.replace(/(yyyy|MM|dd|HH|mm|ss|SSS)/g, (match) => {
    switch (match) {
      case "yyyy":
        return date.getFullYear().toString();
      case "MM":
        return pad(date.getMonth() + 1); // 月份从 0 开始，所以需要加 1
      case "dd":
        return pad(date.getDate());
      case "HH":
        return pad(date.getHours());
      case "mm":
        return pad(date.getMinutes());
      case "ss":
        return pad(date.getSeconds());
      case "SSS":
        return pad(date.getMilliseconds());
      default:
        return match;
    }
  });
}

/**
 * 将数据转成树
 * @param data 数据
 * @param id id字段名
 * @param parentId parentId字段名
 * @param children children字段名
 * @returns
 */
export function handleTree(
  data: [any],
  id: string,
  parentId: string,
  children: string
) {
  let config = {
    id: id || "id",
    parentId: parentId || "parentId",
    childrenList: children || "children",
  };

  const childrenListMap: any = {};
  const nodeIds: any = {};
  const tree = [];

  for (let d of data) {
    let parentId = d[config.parentId];
    if (childrenListMap[parentId] == null) {
      childrenListMap[parentId] = [];
    }
    nodeIds[d[config.id]] = d;
    childrenListMap[parentId].push(d);
  }

  for (let d of data) {
    let parentId = d[config.parentId];
    if (nodeIds[parentId] == null) {
      tree.push(d);
    }
  }

  for (let t of tree) {
    adaptToChildrenList(t);
  }

  function adaptToChildrenList(o: any) {
    if (childrenListMap[o[config.id]] !== null) {
      o[config.childrenList] = childrenListMap[o[config.id]];
    }

    if (o[config.childrenList]) {
      for (let c of o[config.childrenList]) {
        adaptToChildrenList(c);
      }
    }
  }
  return tree;
}

/**
 * 是否为url
 * @param path 路径
 * @returns 是否
 */
export function isUrl(path: string) {
  return /^(https?:|mailto:|tel:)/.test(path);
}

/**
 * 是否为邮箱
 * @param email 邮箱
 * @returns 是否
 */
export function isEmail(email: string) {
  const reg =
    /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  return reg.test(email);
}

/**
 * 是否为空
 * @param obj 对象
 * @returns 是否
 */
export function isEmpty(obj: any) {
  if (obj === null || obj === "" || obj === undefined) {
    return true;
  }
  if (obj instanceof Array && obj.length === 0) {
    return true;
  }
  if (obj instanceof Object && Object.keys(obj).length == 0) {
    return true;
  }
  return false;
}

    // 格式化文件大小
export function formatFileSize(sizeInBytes: number) {
  if (sizeInBytes < 1024) {
    return `${sizeInBytes} B`;
  } else if (sizeInBytes < 1024 * 1024) {
    return `${(sizeInBytes / 1024).toFixed(2)} KB`;
  } else if (sizeInBytes < 1024 * 1024 * 1024) {
    return `${(sizeInBytes / (1024 * 1024)).toFixed(2)} MB`;
  } else {
    return `${(sizeInBytes / (1024 * 1024 * 1024)).toFixed(2)} GB`;
  }
}
