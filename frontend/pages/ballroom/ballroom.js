// ============================================================
// 球房列表页
// ============================================================
const { get } = require('../../utils/request');
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

  onShow() {
    if (this.data.ballrooms.length === 0) {
      this.loadBallrooms();
    }
  },

  /** 触底加载更多 */
  onReachBottom() {
    this.loadBallrooms();
  },

  async loadBallrooms() {
    if (this.data.loading || !this.data.hasMore) return;
    this.setData({ loading: true });
    try {
      const res = await get('/ballroom/page', { page: this.data.page, size: 10 });
      const records = res.records || [];
      this.setData({
        ballrooms: [...this.data.ballrooms, ...records],
        page: this.data.page + 1,
        hasMore: records.length === 10
      });
    } catch (err) {
      console.error('加载球房列表失败', err);
    } finally {
      this.setData({ loading: false });
    }
  },

  goDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({ url: `/pages/ballroom/detail?id=${id}` });
  }
});
