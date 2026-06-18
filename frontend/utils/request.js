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
  const { method = 'GET', data = {}, showLoading = true, showError = true, header: extraHeader = {} } = options;

  return new Promise((resolve, reject) => {
    if (showLoading) {
      wx.showLoading({ title: '加载中...', mask: true });
    }

    const token = wx.getStorageSync('token');
    const userId = wx.getStorageSync('userId');

    const header = { 'Content-Type': 'application/json', ...extraHeader };
    if (token) {
      header['Authorization'] = token;
    }
    if (userId) {
      header['userId'] = userId;
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
          if (showError) {
            wx.showToast({ title: res.data.msg || '请求失败', icon: 'none' });
          }
          reject(res.data);
        }
      },
      fail: (err) => {
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

function get(url, data, options) {
  return request(url, { ...options, method: 'GET', data });
}

function post(url, data, options) {
  return request(url, { ...options, method: 'POST', data });
}

function put(url, data, options) {
  return request(url, { ...options, method: 'PUT', data });
}

module.exports = { request, get, post, put };
