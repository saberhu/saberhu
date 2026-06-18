// ============================================================
// 首页 - 搜索栏、轮播图、快捷入口、约战列表、球房推荐
// ============================================================
const { get } = require('../../utils/request');
const app = getApp();

Page({
  data: {
    /** 搜索关键词 */
    keyword: '',
    /** 轮播图列表 */
    swiperList: [
      { id: 1, image: '/images/banner1.svg' },
      { id: 2, image: '/images/banner2.svg' },
      { id: 3, image: '/images/banner3.svg' }
    ],
    /** 附近约战列表 */
    challenges: [],
    /** 热门球房推荐 */
    ballrooms: [],
    /** 加载状态 */
    loading: true,
    /** 当前用户位置 */
    location: null
  },

  onLoad() {
    this.loadData();
  },

  onShow() {
    if (this.data.challenges.length === 0) {
      this.loadData();
    }
  },

  /** 下拉刷新 */
  onPullDownRefresh() {
    this.loadData().then(() => {
      wx.stopPullDownRefresh();
    });
  },

  async loadData() {
    this.setData({ loading: true });
    try {
      const [challenges, ballrooms] = await Promise.all([
        get('/challenge/page', { page: 1, size: 5, status: 0 }),
        get('/ballroom/page', { page: 1, size: 3, sortBy: 'rating' })
      ]);
      this.setData({
        challenges: challenges.records || [],
        ballrooms: ballrooms.records || []
      });
    } catch (err) {
      console.error('首页加载失败', err);
    } finally {
      this.setData({ loading: false });
    }
  },

  /** 搜索提交 */
  onSearch(e) {
    const keyword = e.detail.value.trim();
    if (keyword) {
      wx.navigateTo({ url: `/pages/ballroom/map?keyword=${keyword}` });
    }
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

  /** 快速约战 */
  goCreateChallenge() {
    wx.navigateTo({ url: '/pages/challenge/create' });
  },

  /** 附近球房 */
  goBallroomMap() {
    wx.navigateTo({ url: '/pages/ballroom/map' });
  },

  /** 我的战绩 */
  goMyStats() {
    wx.switchTab({ url: '/pages/user/profile' });
  },

  formatTime(time) {
    if (!time) return '';
    return time.replace('T', ' ').substring(0, 16);
  },

  /** 获取段位名称 */
  getLevelName(score) {
    return app.getLevelName(score).name;
  }
});
