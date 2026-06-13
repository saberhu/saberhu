








// ============================================================
// 球房详情页
// ============================================================
const app = getApp();

Page({
  data: {
    ballroom: null,
    reviews: [],
    isFavorite: false,
    loading: true
  },

  onLoad(options) {
    this.setData({ id: options.id });
    this.loadDetail();
  },

  loadDetail() {
    const id = this.data.id;

    wx.request({
      url: `${app.globalData.baseUrl}/ballroom/${id}`,
      success: (res) => {
        if (res.data.code === 200) {
          this.setData({ ballroom: res.data.data });
        }
      }
    });

    wx.request({
      url: `${app.globalData.baseUrl}/ballroom/${id}/reviews?page=1&size=10`,
      success: (res) => {
        if (res.data.code === 200) {
          this.setData({ reviews: res.data.data.records || [] });
        }
      },
      complete: () => {
        this.setData({ loading: false });
      }
    });
  },

  toggleFavorite() {
    const userId = wx.getStorageSync('userId');
    if (!userId) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }

    wx.request({
      url: `${app.globalData.baseUrl}/ballroom/favorite`,
      method: 'POST',
      data: { userId, ballroomId: this.data.id },
      success: () => {
        this.setData({ isFavorite: !this.data.isFavorite });
        wx.showToast({ title: this.data.isFavorite ? '已收藏' : '已取消收藏', icon: 'success' });
      }
    });
  },

  goCreateChallenge() {
    wx.navigateTo({
      url: `/pages/challenge/create?ballroomId=${this.data.id}`
    });
  }
});








