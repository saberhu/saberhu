



// ============================================================
// 阜阳台球约战小程序 - 入口文件
// ============================================================

App({
  globalData: {
    userInfo: null,
    baseUrl: 'http://localhost:8080/api'
  },

  onLaunch() {
    // 获取系统信息
    const sysInfo = wx.getSystemInfoSync();
    this.globalData.systemInfo = sysInfo;
  }
});



