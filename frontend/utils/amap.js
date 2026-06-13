
// ============================================================
// 高德地图工具方法
// ============================================================

/**
 * 计算两点之间的距离（米）
 */
function calcDistance(lat1, lng1, lat2, lng2) {
  const radLat1 = (lat1 * Math.PI) / 180;
  const radLat2 = (lat2 * Math.PI) / 180;
  const a = radLat1 - radLat2;
  const b = (lng1 * Math.PI) / 180 - (lng2 * Math.PI) / 180;
  const s = 2 * Math.asin(Math.sqrt(
    Math.pow(Math.sin(a / 2), 2) +
    Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)
  ));
  return Math.round(s * 6371000);
}

/**
 * 格式化距离显示
 */
function formatDistance(meters) {
  if (meters < 1000) {
    return `${meters}m`;
  }
  return `${(meters / 1000).toFixed(1)}km`;
}

/**
 * 打开地图导航
 * @param {number} latitude  目的地纬度
 * @param {number} longitude 目的地经度
 * @param {string} name      目的地名称
 */
function openNavigation(latitude, longitude, name) {
  wx.openLocation({
    latitude,
    longitude,
    name,
    scale: 18
  });
}

/**
 * 获取当前地理位置
 * @returns {Promise<{latitude, longitude}>}
 */
function getCurrentLocation() {
  return new Promise((resolve, reject) => {
    wx.getLocation({
      type: 'gcj02',
      success: (res) => {
        resolve({ latitude: res.latitude, longitude: res.longitude });
      },
      fail: (err) => {
        // 用户拒绝授权时提示
        wx.showModal({
          title: '提示',
          content: '需要获取您的位置信息，请点击确定前往设置',
          success: (res) => {
            if (res.confirm) {
              wx.openSetting();
            }
          }
        });
        reject(err);
      }
    });
  });
}

module.exports = { calcDistance, formatDistance, openNavigation, getCurrentLocation };

