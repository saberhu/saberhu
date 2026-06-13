










// ============================================================
// 发起约战 - 表单页
// ============================================================
const { get, post } = require('../../utils/request');
const app = getApp();

Page({
  data: {
    /** 球房列表 */
    ballrooms: [],
    ballroomIndex: -1,
    ballroomId: null,
    ballroomName: '',
    /** 球种 */
    ballType: 1,
    ballTypes: [
      { value: 1, label: '中式八球' },
      { value: 2, label: '斯诺克' },
      { value: 3, label: '九球' }
    ],
    /** 赛制 */
    formatType: 1,
    formatTypes: [
      { value: 1, label: '局数制' },
      { value: 2, label: '时间制' }
    ],
    formatValue: 9,
    /** 开始时间 */
    startTime: '',
    startTimeDisplay: '',
    /** 最大人数 */
    maxPlayers: 2,
    /** 备注 */
    remark: '',
    /** 提交中 */
    submitting: false
  },

  onLoad(options) {
    if (options.ballroomId) {
      this.setData({ ballroomId: parseInt(options.ballroomId) });
    }
    this.loadBallrooms();
    // 设置默认时间
    const now = new Date();
    const dateStr = now.toISOString().split('T')[0];
    this.setData({ startTime: dateStr, startTimeDisplay: dateStr });
  },

  async loadBallrooms() {
    try {
      const res = await get('/ballroom/page', { page: 1, size: 100 }, { showLoading: false });
      const list = res.records || [];
      this.setData({ ballrooms: list });
      // 如果传入了球房ID，自动选中
      if (this.data.ballroomId) {
        const idx = list.findIndex(b => b.id === this.data.ballroomId);
        if (idx > -1) {
          this.setData({ ballroomIndex: idx, ballroomName: list[idx].name });
        }
      }
    } catch (err) {
      console.error('加载球房失败', err);
    }
  },

  /** 选择球房 */
  onBallroomChange(e) {
    const idx = e.detail.value;
    const ballroom = this.data.ballrooms[idx];
    this.setData({
      ballroomIndex: idx,
      ballroomId: ballroom.id,
      ballroomName: ballroom.name
    });
  },

  /** 选择球种 */
  onBallTypeChange(e) {
    this.setData({ ballType: parseInt(e.detail.value) + 1 });
  },

  /** 选择赛制 */
  onFormatTypeChange(e) {
    this.setData({ formatType: parseInt(e.detail.value) + 1 });
  },

  /** 赛制值输入 */
  onFormatValueInput(e) {
    this.setData({ formatValue: parseInt(e.detail.value) || 0 });
  },

  /** 选择日期 */
  onStartTimeChange(e) {
    this.setData({ startTime: e.detail.value, startTimeDisplay: e.detail.value });
  },

  /** 最大人数 */
  onMaxPlayersInput(e) {
    this.setData({ maxPlayers: parseInt(e.detail.value) || 2 });
  },

  /** 备注 */
  onRemarkInput(e) {
    this.setData({ remark: e.detail.value });
  },

  /** 提交 */
  async submit() {
    const userId = wx.getStorageSync('userId');
    if (!userId) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }
    if (!this.data.ballroomId) {
      wx.showToast({ title: '请选择球房', icon: 'none' });
      return;
    }
    if (!this.data.startTime) {
      wx.showToast({ title: '请选择开始时间', icon: 'none' });
      return;
    }

    this.setData({ submitting: true });
    try {
      const res = await post('/challenge/create', {
        userId,
        ballroomId: this.data.ballroomId,
        ballType: this.data.ballType,
        formatType: this.data.formatType,
        formatValue: this.data.formatValue,
        startTime: this.data.startTime,
        maxPlayers: this.data.maxPlayers,
        remark: this.data.remark
      });
      wx.showToast({ title: '发起成功', icon: 'success' });
      // 跳转到约战详情
      wx.redirectTo({ url: `/pages/challenge/detail?id=${res.id || res}` });
    } catch (err) {
      console.error('发起约战失败', err);
    } finally {
      this.setData({ submitting: false });
    }
  }
});








