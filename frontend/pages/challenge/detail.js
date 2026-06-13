













// ============================================================
// 约战详情 - 信息展示、报名/取消、确认/拒绝、记分入口
// ============================================================
const { get, post } = require('../../utils/request');
const app = getApp();

Page({
  data: {
    challenge: null,
    signups: [],
    /** 当前用户是否为发起人 */
    isInitiator: false,
    /** 当前用户是否已报名 */
    hasSigned: false,
    loading: true
  },

  onLoad(options) {
    this.setData({ id: options.id });
    this.loadDetail();
  },

  onShow() {
    if (this.data.id) {
      this.loadDetail();
    }
  },

  async loadDetail() {
    this.setData({ loading: true });
    try {
      const challenge = await get(`/challenge/${this.data.id}`);
      const userId = wx.getStorageSync('userId');
      const isInitiator = userId && challenge.initiatorId === userId;
      const hasSigned = challenge.signups && challenge.signups.some(s => s.userId === userId);

      this.setData({
        challenge,
        signups: challenge.signups || [],
        isInitiator,
        hasSigned
      });
    } catch (err) {
      console.error('加载详情失败', err);
    } finally {
      this.setData({ loading: false });
    }
  },

  /** 报名 */
  async signup() {
    const userId = wx.getStorageSync('userId');
    if (!userId) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }
    try {
      await post(`/challenge/${this.data.id}/signup`, { userId });
      wx.showToast({ title: '报名成功', icon: 'success' });
      this.loadDetail();
    } catch (err) {
      console.error('报名失败', err);
    }
  },

  /** 取消报名 */
  async cancelSignup() {
    const userId = wx.getStorageSync('userId');
    try {
      await post(`/challenge/${this.data.id}/cancelSignup`, { userId });
      wx.showToast({ title: '已取消报名', icon: 'success' });
      this.loadDetail();
    } catch (err) {
      console.error('取消报名失败', err);
    }
  },

  /** 取消约战（发起人） */
  async cancelChallenge() {
    wx.showModal({
      title: '确认取消',
      content: '确定要取消这场约战吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await post(`/challenge/${this.data.id}/cancel`, { userId: wx.getStorageSync('userId') });
            wx.showToast({ title: '已取消', icon: 'success' });
            this.loadDetail();
          } catch (err) {
            console.error('取消失败', err);
          }
        }
      }
    });
  },

  /** 确认报名（发起人） */
  async confirmSignup(e) {
    const signupId = e.currentTarget.dataset.id;
    try {
      await post(`/challenge/signup/${signupId}/confirm`);
      wx.showToast({ title: '已确认', icon: 'success' });
      this.loadDetail();
    } catch (err) {
      console.error('确认失败', err);
    }
  },

  /** 拒绝报名（发起人） */
  async rejectSignup(e) {
    const signupId = e.currentTarget.dataset.id;
    try {
      await post(`/challenge/signup/${signupId}/reject`);
      wx.showToast({ title: '已拒绝', icon: 'success' });
      this.loadDetail();
    } catch (err) {
      console.error('拒绝失败', err);
    }
  },

  /** 进入记分页 */
  goScore() {
    wx.navigateTo({ url: `/pages/challenge/score?id=${this.data.id}` });
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













