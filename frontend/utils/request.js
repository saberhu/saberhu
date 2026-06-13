// ============================================================
// 通用请求工具 - 统一处理 baseUrl、token、错误提示
// ============================================================

const app = getApp();

/**
 * 发起 HTTP 请求
 * @param {string} url    接口路径（相对路径，自动拼接 baseUrl）
 * @param {object} options 请求选项
 * @returns {Promise}
 */
function request(url, options = {}) {
  const { method = 'GET', data = {}, showLoading = true, showError = true } = options;

  return new Promise((resolve, reject) => {
    // 加载提示
    if (showLoading) {
      wx.showLoading({ title: '加载中...', mask: true });
    }

    // 获取 token
    const token = wx.getStorageSync('token');

    // 请求头
    const header = { 'Content-Type': 'application/json' };
    if (token) {
      header['Authorization'] = token;
    }

    wx.request({
      url: `${app.globalData.baseUrl}${url}`,
      method,
      data,
      header,
      success: (res) => {
        if (res.data.code === 200) {
          resolve(res.data.data);
        } else {
          // 业务错误
          if (showError) {
            wx.showToast({ title: res.data.msg || '请求失败', icon: 'none' });
          }
          reject(res.data);
        }
      },
      fail: (err) => {
        // 网络错误
        if (showError) {
          wx.showToast({ title: '网络异常，请稍后重试', icon: 'none' });
        }
        reject(err);
      },
      complete: () => {
        if (showLoading) {
          wx.hideLoading();
        }
      }
    });
  });
}

/**
 * GET 请求
 */
function get(url, data, options) {
  return request(url, { ...options, method: 'GET', data });
}

/**
 * POST 请求
 */
function post(url, data, options) {
  return request(url, { ...options, method: 'POST', data });
}

/**
 * PUT 请求
 */
function put(url, data, options) {
  return request(url, { ...options, method: 'PUT', data });
}

module.exports = { request, get, post, put };
