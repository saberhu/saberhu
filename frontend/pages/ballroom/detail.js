// ============================================================
// 球房详情 - 轮播图、基本信息、导航、收藏、评价
// ============================================================
const { get, post } = require('../../utils/request');
const { openNavigation } = require('../../utils/amap');
const app = getApp();

Page({
  data: {
    ballroom: null,
    ballroomStars: '',
    reviews: [],
    isFavorite: false,
    loading: true,
    /** 评价表单 */
    showReviewForm: false,
    reviewRating: 5,
    reviewContent: ''
  },

  /** 生成星星字符串 */
  getStars(rating) {
    const count = Math.round(rating || 0);
    let s = '';
    for (let i = 0; i < count; i++) s += '⭐';
    return s;
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
      const [ballroom, reviews] = await Promise.all([
        get(`/ballroom/${this.data.id}`),
        get(`/ballroom/${this.data.id}/reviews`, { page: 1, size: 20 }, { showLoading: false })
      ]);

      // 检查收藏状态
      const userId = wx.getStorageSync('userId');
      let isFavorite = false;
      if (userId) {
        try {
          const favRes = await get(`/ballroom/${this.data.id}/favorite/check`, { userId }, { showLoading: false });
          isFavorite = favRes.favorited || false;
        } catch (e) {
          // ignore
        }
      }

      this.setData({
        ballroom,
        ballroomStars: this.getStars(ballroom.rating),
        reviews: (reviews.records || []).map(item => ({ ...item, reviewStars: this.getStars(item.rating) })),
        isFavorite
      });
    } catch (err) {
      console.error('加载球房详情失败', err);
    } finally {
      this.setData({ loading: false });
    }
  },

  /** 一键导航 */
  goNavigation() {
    if (this.data.ballroom.latitude && this.data.ballroom.longitude) {
      openNavigation(
        this.data.ballroom.latitude,
        this.data.ballroom.longitude,
        this.data.ballroom.name
      );
    } else {
      wx.showToast({ title: '暂无位置信息', icon: 'none' });
    }
  },

  /** 收藏/取消收藏 */
  async toggleFavorite() {
    const userId = wx.getStorageSync('userId');
    if (!userId) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }
    try {
      await post(`/ballroom/${this.data.id}/favorite`, { userId });
      this.setData({ isFavorite: !this.data.isFavorite });
      wx.showToast({
        title: this.data.isFavorite ? '已收藏' : '已取消收藏',
        icon: 'success'
      });
    } catch (err) {
      console.error('操作失败', err);
    }
  },

  /** 发起约战 */
  goCreateChallenge() {
    wx.navigateTo({
      url: `/pages/challenge/create?ballroomId=${this.data.id}`
    });
  },

  /** 显示评价表单 */
  showReview() {
    const userId = wx.getStorageSync('userId');
    if (!userId) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }
    this.setData({ showReviewForm: true, reviewRating: 5, reviewContent: '' });
  },

  /** 隐藏评价表单 */
  hideReview() {
    this.setData({ showReviewForm: false });
  },

  /** 选择评分 */
  onRatingChange(e) {
    this.setData({ reviewRating: e.currentTarget.dataset.rating });
  },

  /** 评价内容 */
  onReviewInput(e) {
    this.setData({ reviewContent: e.detail.value });
  },

  /** 提交评价 */
  async submitReview() {
    if (!this.data.reviewContent.trim()) {
      wx.showToast({ title: '请输入评价内容', icon: 'none' });
      return;
    }
    try {
      await post('/ballroom/review', {
        ballroomId: this.data.id,
        rating: this.data.reviewRating,
        content: this.data.reviewContent.trim()
      });
      wx.showToast({ title: '评价成功', icon: 'success' });
      this.setData({ showReviewForm: false });
      this.loadDetail();
    } catch (err) {
      console.error('评价失败', err);
    }
  }
});
