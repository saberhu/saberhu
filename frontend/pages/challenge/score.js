// ============================================================
// 对战记分 - 实时比分、得分按钮、结束比赛
// ============================================================
const { get, post } = require('../../utils/request');
const app = getApp();

Page({
  data: {
    challenge: null,
    /** 当前用户角色 */
    isInitiator: false,
    /** 比分 */
    scoreInitiator: 0,
    scoreOpponent: 0,
    loading: true,
    submitting: false
  },

  onLoad(options) {
    this.setData({ id: options.id });
    this.loadDetail();
  },

  async loadDetail() {
    this.setData({ loading: true });
    try {
      const challenge = await get(`/challenge/${this.data.id}`);
      const userId = wx.getStorageSync('userId');
      const isInitiator = userId && challenge.initiatorId === userId;

      this.setData({
        challenge,
        isInitiator,
        scoreInitiator: challenge.scoreInitiator || 0,
        scoreOpponent: challenge.scoreOpponent || 0
      });
    } catch (err) {
      console.error('加载失败', err);
    } finally {
      this.setData({ loading: false });
    }
  },

  /** 发起人得分 */
  scoreForInitiator() {
    if (!this.data.isInitiator) return;
    this.setData({ scoreInitiator: this.data.scoreInitiator + 1 });
  },

  /** 对手得分 */
  scoreForOpponent() {
    if (!this.data.isInitiator) return;
    this.setData({ scoreOpponent: this.data.scoreOpponent + 1 });
  },

  /** 本局结束（发起人减分） */
  undoInitiatorScore() {
    if (!this.data.isInitiator || this.data.scoreInitiator <= 0) return;
    this.setData({ scoreInitiator: this.data.scoreInitiator - 1 });
  },

  /** 本局结束（对手减分） */
  undoOpponentScore() {
    if (!this.data.isInitiator || this.data.scoreOpponent <= 0) return;
    this.setData({ scoreOpponent: this.data.scoreOpponent - 1 });
  },

  /** 结束比赛并提交比分 */
  async finishMatch() {
    wx.showModal({
      title: '确认结束',
      content: `确定以 ${this.data.scoreInitiator} : ${this.data.scoreOpponent} 结束比赛吗？`,
      success: async (res) => {
        if (res.confirm) {
          this.setData({ submitting: true });
          try {
            await post(`/challenge/${this.data.id}/finish`, {
              userId: wx.getStorageSync('userId'),
              scoreInitiator: this.data.scoreInitiator,
              scoreOpponent: this.data.scoreOpponent
            });
            wx.showToast({ title: '比赛结束', icon: 'success' });
            wx.navigateBack();
          } catch (err) {
            console.error('提交失败', err);
          } finally {
            this.setData({ submitting: false });
          }
        }
      }
    });
  },

  /** 获取段位 */
  getLevelName(score) {
    return app.getLevelName(score || 0).name;
  }
});
