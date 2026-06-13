













// ============================================================
// 约战详情页
// ============================================================
const app = getApp();

Page({
  data: {
    challenge: null,
    loading: true
  },

  onLoad(options) {
    this.setData({ id: options.id });
    this.loadDetail();
  },

  loadDetail() {
    wx.request({
      url: `${app.globalData.baseUrl}/challenge/${this.data.id}`,
      success: (res) => {
        if (res.data.code === 200) {
          this.setData({ challenge: res.data.data });
        }
      },
      complete: () => {
        this.setData({ loading: false });
      }
    });
  },

  signup() {
    const userId = wx.getStorageSync('userId');
    if (!userId) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }

    wx.request({
      url: `${app.globalData.baseUrl}/challenge/${this.data.id}/signup`,
      method: 'POST',
      data: { userId },
      success: (res) => {
        if (res.data.code === 200) {
          wx.showToast({ title: '报名成功', icon: 'success' });
          this.loadDetail();
        } else {
          wx.showToast({ title: res.data.msg || '报名失败', icon: 'none' });
        }
      }
    });
  }
});













