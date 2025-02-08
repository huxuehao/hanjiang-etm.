import setting from "@/config/setting";
import cache from "@/utils/cache";

const TokenKey = setting.accessToken;
const refreshTokenKey = setting.refreshToken;
const permissionsKey = setting.permissions;


/**
 * 获取token
 * @returns token
 */
export function getToken() {
  return cache.local.get(TokenKey);
}

/**
 * 设置token
 * @param token token
 */
export function setToken(token: string) {
  cache.local.set(TokenKey, token);
}

/**
 * 清除token
 */
export function removeToken() {
  cache.local.remove(TokenKey);
}

/**
 * 获取refreshToken
 * @returns token
 */
export function getRefreshToken() {
  return cache.local.get(refreshTokenKey);
}

/**
 * 设置refreshToken
 * @param token token
 */
export function setRefreshToken(refreshToken: string) {
  cache.local.set(refreshTokenKey, refreshToken);
}

/**
 * 清除refreshToken
 */
export function removeRefreshToken() {
  cache.local.remove(refreshTokenKey);
}
/**
 * 清除refreshToken
 */
export function removePermissions() {
  cache.local.remove(permissionsKey);
}
