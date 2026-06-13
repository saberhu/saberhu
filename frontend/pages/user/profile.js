// ============================================================
// 个人中心 - 头像、段位、胜率、战绩、收藏、设置
// ============================================================
const { get, post } = require('../../utils/request');
const app = getApp();

Page({
  data: {
    user: null,
    /** 统计数据 */
    stats: {
      totalGames: 0,
      wins: 0,
      losses: 0,
      winRate: 0
    },
    /** 我的约战列表 */
    myChallenges: [],
    /** 收藏球房 */
    favorites: [],
    /** 当前tab */
    activeTab: 'challenges',
    loading: true
  },

  onLoad() {
    this.checkLogin();
  },

  onShow() {
    if (wx.getStorageSync('userId')) {
      this.loadData();
    }
  },

  checkLogin() {
    const userId = wx.getStorageSync('userId');
    if (!userId) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      setTimeout(() => {
        // 跳转到登录页（实际项目中可跳转登录）
        wx.switchTab({ url: '/pages/index/index' });
      }, 1500);
      return;
    }
    this.loadData();
  },

  async loadData() {
    this.setData({ loading: true });
    const userId = wx.getStorageSync('userId');
    try {
      const [user, challenges, favorites] = await Promise.all([
        get(`/user/${userId}`),
        get('/challenge/my', { userId, page: 1, size: 20 }, { showLoading: false }),
        get('/ballroom/favorite/list', { userId }, { showLoading: false })
      ]);

      const stats = {
        totalGames: (user.wins || 0) + (user.losses || 0),
        wins: user.wins || 0,
        losses: user.losses || 0,
        winRate: user.wins && (user.wins + user.losses) > 0
          ? Math.round((user.wins / (user.wins + user.losses)) * 100)
          : 0
      };

      this.setData({
        user,
        stats,
        myChallenges: challenges.records || [],
        favorites: favorites.records || []
      });
    } catch (err) {
      console.error('加载个人数据失败', err);
    } finally {
      this.setData({ loading: false });
    }
  },

  /** 切换tab */
  switchTab(e) {
    const tab = e.currentTarget.dataset.tab;
    this.setData({ activeTab: tab });
  },

  /** 跳转约战详情 */
  goChallenge(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({ url: `/pages/challenge/detail?id=${id}` });
  },

  /** 跳转球房详情 */
  goBallroom(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({ url: `/pages/ballroom/detail?id=${id}` });
  },

  /** 获取段位 */
  getLevelName(score) {
    return app.getLevelName(score || 0).name;
  },

  /** 获取段位图标 */
  getLevelIcon(score) {
    return app.getLevelName(score || 0).icon;
  },

  /** 格式化时间 */
  formatTime(time) {
    if (!time) return '';
    return time.replace('T', ' ').substring(0, 16);
  }
});
