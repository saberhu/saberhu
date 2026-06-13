








// ============================================================
// 约战列表页
// ============================================================
const app = getApp();

Page({
  data: {
    challenges: [],
    page: 1,
    hasMore: true,
    loading: false,
    statusFilter: null
  },

  onLoad() {
    this.loadChallenges();
  },

  loadChallenges() {
    if (this.data.loading || !this.data.hasMore) return;
    this.setData({ loading: true });

    let url = `${app.globalData.baseUrl}/challenge/page?page=${this.data.page}&size=10`;
    if (this.data.statusFilter !== null) {
      url += `&status=${this.data.statusFilter}`;
    }

    wx.request({
      url,
      success: (res) => {
        if (res.data.code === 200) {
          const records = res.data.data.records || [];
          this.setData({
            challenges: [...this.data.challenges, ...records],
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
    wx.navigateTo({ url: `/pages/challenge/detail?id=${id}` });
  },

  goCreate() {
    wx.navigateTo({ url: '/pages/challenge/create' });
  }
});








