





// ============================================================
// 球房列表页
// ============================================================
const app = getApp();

Page({
  data: {
    ballrooms: [],
    page: 1,
    hasMore: true,
    loading: false
  },

  onLoad() {
    this.loadBallrooms();
  },

  loadBallrooms() {
    if (this.data.loading || !this.data.hasMore) return;
    this.setData({ loading: true });

    wx.request({
      url: `${app.globalData.baseUrl}/ballroom/page?page=${this.data.page}&size=10`,
      success: (res) => {
        if (res.data.code === 200) {
          const records = res.data.data.records || [];
          this.setData({
            ballrooms: [...this.data.ballrooms, ...records],
            page: this.data.page + 1,
            hasMore: records.length === 10
          });
        }
      },
      complete: () => {
        this.setData({ loading: false });
      }
    });
  },

  goDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({ url: `/pages/ballroom/detail?id=${id}` });
  }
});





