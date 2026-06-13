













// ============================================================
// 个人中心页
// ============================================================
const app = getApp();

Page({
  data: {
    userInfo: null,
    isLoggedIn: false
  },

  onShow() {
    const userId = wx.getStorageSync('userId');
    if (userId) {
      this.setData({ isLoggedIn: true });
      this.loadUserInfo(userId);
    }
  },

  loadUserInfo(userId) {
    wx.request({
      url: `${app.globalData.baseUrl}/user/${userId}`,
      success: (res) => {
        if (res.data.code === 200) {
          this.setData({ userInfo: res.data.data });
        }
      }
    });
  },

  login() {
    wx.login({
      success: (res) => {
        if (res.code) {
          wx.request({
            url: `${app.globalData.baseUrl}/user/login`,
            method: 'POST',
            data: { code: res.code },
            success: (res) => {
              if (res.data.code === 200) {
                const user = res.data.data;
                wx.setStorageSync('userId', user.id);
                this.setData({ userInfo: user, isLoggedIn: true });
                wx.showToast({ title: '登录成功', icon: 'success' });
              }
            }
          });
        }
      }
    });
  },

  goMyChallenges() {
    wx.navigateTo({ url: '/pages/challenge/challenge' });
  },

  goMyFavorites() {
    wx.navigateTo({ url: '/pages/ballroom/ballroom' });
  }
});













