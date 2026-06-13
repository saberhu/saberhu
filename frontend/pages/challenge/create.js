












// ============================================================
// 创建约战页
// ============================================================
const app = getApp();

Page({
  data: {
    ballrooms: [],
    ballroomId: null,
    ballType: 1,
    formatType: 1,
    formatValue: 9,
    startTime: '',
    maxPlayers: 2,
    remark: '',
    ballTypes: [
      { value: 1, label: '中式八球' },
      { value: 2, label: '斯诺克' },
      { value: 3, label: '九球' }
    ],
    formatTypes: [
      { value: 1, label: '局数制' },
      { value: 2, label: '时间制' }
    ]
  },

  onLoad(options) {
    if (options.ballroomId) {
      this.setData({ ballroomId: parseInt(options.ballroomId) });
    }
    this.loadBallrooms();
  },

  loadBallrooms() {
    wx.request({
      url: `${app.globalData.baseUrl}/ballroom/page?page=1&size=100`,
      success: (res) => {
        if (res.data.code === 200) {
          this.setData({ ballrooms: res.data.data.records || [] });
        }
      }
    });
  },

  onBallTypeChange(e) {
    this.setData({ ballType: e.detail.value });
  },

  onFormatTypeChange(e) {
    this.setData({ formatType: e.detail.value });
  },

  onFormatValueInput(e) {
    this.setData({ formatValue: e.detail.value });
  },

  onStartTimeChange(e) {
    this.setData({ startTime: e.detail.value });
  },

  onMaxPlayersChange(e) {
    this.setData({ maxPlayers: e.detail.value });
  },

  onRemarkInput(e) {
    this.setData({ remark: e.detail.value });
  },

  submit() {
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

    wx.request({
      url: `${app.globalData.baseUrl}/challenge/create`,
      method: 'POST',
      data: {
        userId,
        ballroomId: this.data.ballroomId,
        ballType: this.data.ballType,
        formatType: this.data.formatType,
        formatValue: this.data.formatValue,
        startTime: this.data.startTime,
        maxPlayers: this.data.maxPlayers,
        remark: this.data.remark
      },
      success: (res) => {
        if (res.data.code === 200) {
          wx.showToast({ title: '发起成功', icon: 'success' });
          wx.navigateBack();
        } else {
          wx.showToast({ title: res.data.msg || '发起失败', icon: 'none' });
        }
      }
    });
  }
});












