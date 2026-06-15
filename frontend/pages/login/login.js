

// ============================================================
// 模拟登录页 - 本地开发环境使用
// ============================================================
const app = getApp();

Page({
  data: {
    users: [
      { id: 1, nickname: '测试球友', levelScore: 1200, wins: 10, losses: 3, avatarChar: '测', avatarClass: 'c1' },
      { id: 2, nickname: '张教练', levelScore: 1500, wins: 25, losses: 5, avatarChar: '张', avatarClass: 'c2' },
      { id: 3, nickname: '李球王', levelScore: 1350, wins: 18, losses: 8, avatarChar: '李', avatarClass: 'c3' },
      { id: 4, nickname: '王台球', levelScore: 1100, wins: 8, losses: 6, avatarChar: '王', avatarClass: 'c4' },
      { id: 5, nickname: '赵一杆', levelScore: 900, wins: 5, losses: 12, avatarChar: '赵', avatarClass: 'c5' }
    ]
  },

  login(e) {
    const { id, nickname } = e.currentTarget.dataset;
    wx.setStorageSync('userId', id);
    wx.showToast({ title: `已登录为 ${nickname}`, icon: 'success', duration: 1500 });
    setTimeout(() => {
      wx.switchTab({ url: '/pages/challenge/challenge' });
    }, 1500);
  }
});

