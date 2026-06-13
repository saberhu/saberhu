




// ============================================================
// 首页
// ============================================================
const app = getApp();

Page({
  data: {
    ballrooms: [],
    challenges: [],
    loading: true
  },

  onLoad() {
    this.loadData();
  },

  onShow() {
    this.loadData();
  },

  loadData() {
    this.setData({ loading: true });

    // 获取球房列表
    wx.request({
      url: `${app.globalData.baseUrl}/ballroom/page?page=1&size=3`,
      success: (res) => {
        if (res.data.code === 200) {
          this.setData({ ballrooms: res.data.data.records || [] });
        }
      }
    });

    // 获取最新约战
    wx.request({
      url: `${app.globalData.baseUrl}/challenge/page?page=1&size=5`,
      success: (res) => {
        if (res.data.code === 200) {
          this.setData({ challenges: res.data.data.records || [] });
        }
      },
      complete: () => {
        this.setData({ loading: false });
      }
    });
  },

  /** 跳转到球房详情 */
  goBallroom(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({ url: `/pages/ballroom/detail?id=${id}` });
  },

  /** 跳转到约战详情 */
  goChallenge(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({ url: `/pages/challenge/detail?id=${id}` });
  },

  /** 跳转到创建约战 */
  goCreateChallenge() {
    wx.navigateTo({ url: '/pages/challenge/create' });
  }
});




