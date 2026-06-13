



// ============================================================
// 阜阳台球约战小程序 - 入口文件
// ============================================================

App({
  globalData: {
    userInfo: null,
    baseUrl: 'http://localhost:8080/api',
    systemInfo: null,
    /** 段位配置 */
    levelConfig: [
      { min: 0, max: 99, name: '青铜', icon: '🥉' },
      { min: 100, max: 299, name: '白银', icon: '🥈' },
      { min: 300, max: 599, name: '黄金', icon: '🥇' },
      { min: 600, max: 999, name: '铂金', icon: '💎' },
      { min: 1000, max: 9999, name: '钻石', icon: '👑' }
    ]
  },

  onLaunch() {
    // 获取系统信息
    const sysInfo = wx.getSystemInfoSync();
    this.globalData.systemInfo = sysInfo;

    // 检查登录状态
    const token = wx.getStorageSync('token');
    const userId = wx.getStorageSync('userId');
    if (token && userId) {
      this.globalData.isLoggedIn = true;
    }
  },

  /** 根据积分获取段位名称 */
  getLevelName(score) {
    const config = this.globalData.levelConfig;
    for (let i = config.length - 1; i >= 0; i--) {
      if (score >= config[i].min) {
        return config[i];
      }
    }
    return config[0];
  }
});



