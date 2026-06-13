

// ============================================================
// 约战广场 - 球种筛选、卡片列表、悬浮发起按钮
// ============================================================
const { get } = require('../../utils/request');
const app = getApp();

Page({
  data: {
    /** 球种筛选tab */
    ballTypeTabs: [
      { value: 0, label: '全部' },
      { value: 1, label: '中式八球' },
      { value: 2, label: '斯诺克' },
      { value: 3, label: '九球' }
    ],
    /** 当前选中球种 */
    currentBallType: 0,
    /** 约战列表 */
    challenges: [],
    /** 分页 */
    page: 1,
    hasMore: true,
    loading: false
  },

  onLoad() {
    this.loadChallenges();
  },

  onShow() {
    // 返回时刷新
    if (this.data.challenges.length > 0) {
      this.loadChallenges(true);
    }
  },

  /** 下拉刷新 */
  onPullDownRefresh() {
    this.loadChallenges(true).then(() => {
      wx.stopPullDownRefresh();
    });
  },

  /** 上拉加载 */
  onReachBottom() {
    this.loadChallenges();
  },

  /** 加载约战列表 */
  async loadChallenges(refresh = false) {
    if (this.data.loading) return;
    if (!refresh && !this.data.hasMore) return;

    const page = refresh ? 1 : this.data.page;
    this.setData({ loading: true });

    try {
      const params = { page, size: 10 };
      if (this.data.currentBallType > 0) {
        params.ballType = this.data.currentBallType;
      }
      const res = await get('/challenge/page', params);
      const records = res.records || [];
      this.setData({
        challenges: refresh ? records : [...this.data.challenges, ...records],
        page: page + 1,
        hasMore: records.length === 10
      });
    } catch (err) {
      console.error('加载约战失败', err);
    } finally {
      this.setData({ loading: false });
    }
  },

  /** 切换球种筛选 */
  onBallTypeChange(e) {
    const value = e.currentTarget.dataset.value;
    this.setData({ currentBallType: value, page: 1, hasMore: true });
    this.loadChallenges(true);
  },

  /** 跳转约战详情 */
  goDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({ url: `/pages/challenge/detail?id=${id}` });
  },

  /** 发起约战 */
  goCreate() {
    wx.navigateTo({ url: '/pages/challenge/create' });
  },

  /** 格式化时间 */
  formatTime(time) {
    if (!time) return '';
    return time.replace('T', ' ').substring(0, 16);
  },

  /** 获取段位 */
  getLevelName(score) {
    return app.getLevelName(score || 0).name;
  }
});


