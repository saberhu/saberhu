// ============================================================
// 球房地图 - 高德地图展示、列表/地图切换、点击跳转详情
// ============================================================
const { get } = require('../../utils/request');
const { getCurrentLocation, formatDistance } = require('../../utils/amap');
const app = getApp();

Page({
  data: {
    /** 球房列表 */
    ballrooms: [],
    /** 地图标记点 */
    markers: [],
    /** 当前视图模式: list / map */
    viewMode: 'map',
    /** 当前用户位置 */
    latitude: 32.89,
    longitude: 115.82,
    /** 搜索关键词 */
    keyword: '',
    /** 分页 */
    page: 1,
    hasMore: true,
    loading: false
  },

  onLoad(options) {
    if (options.keyword) {
      this.setData({ keyword: options.keyword });
    }
    this.getLocationAndLoad();
  },

  onShow() {
    if (this.data.ballrooms.length === 0) {
      this.getLocationAndLoad();
    }
  },

  /** 获取位置并加载数据 */
  async getLocationAndLoad() {
    try {
      const loc = await getCurrentLocation();
      this.setData({
        latitude: loc.latitude,
        longitude: loc.longitude
      });
    } catch (err) {
      console.log('获取位置失败，使用默认坐标');
    }
    this.loadBallrooms(true);
  },

  /** 加载球房 */
  async loadBallrooms(refresh = false) {
    if (this.data.loading) return;
    if (!refresh && !this.data.hasMore) return;

    const page = refresh ? 1 : this.data.page;
    this.setData({ loading: true });

    try {
      const params = {
        page,
        size: 20,
        latitude: this.data.latitude,
        longitude: this.data.longitude
      };
      if (this.data.keyword) {
        params.keyword = this.data.keyword;
      }
      const res = await get('/ballroom/page', params, { showLoading: false });
      const records = res.records || [];

      // 计算距离
      records.forEach(item => {
        if (item.latitude && item.longitude) {
          const d = this.calcDistance(
            this.data.latitude, this.data.longitude,
            item.latitude, item.longitude
          );
          item.distance = formatDistance(d);
        }
      });

      const markers = records.map(item => ({
        id: item.id,
        latitude: item.latitude || this.data.latitude,
        longitude: item.longitude || this.data.longitude,
        title: item.name,
        iconPath: '/images/marker.png',
        width: 30,
        height: 40,
        callout: {
          content: item.name,
          fontSize: 12,
          borderRadius: 4,
          padding: 4,
          display: 'ALWAYS'
        }
      }));

      this.setData({
        ballrooms: refresh ? records : [...this.data.ballrooms, ...records],
        markers,
        page: page + 1,
        hasMore: records.length === 20
      });
    } catch (err) {
      console.error('加载球房失败', err);
    } finally {
      this.setData({ loading: false });
    }
  },

  /** 切换视图 */
  switchView() {
    this.setData({
      viewMode: this.data.viewMode === 'map' ? 'list' : 'map'
    });
  },

  /** 标记点击 */
  onMarkerTap(e) {
    const id = e.markerId;
    wx.navigateTo({ url: `/pages/ballroom/detail?id=${id}` });
  },

  /** 跳转详情 */
  goDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({ url: `/pages/ballroom/detail?id=${id}` });
  },

  /** 计算距离（Haversine） */
  calcDistance(lat1, lng1, lat2, lng2) {
    const radLat1 = (lat1 * Math.PI) / 180;
    const radLat2 = (lat2 * Math.PI) / 180;
    const a = radLat1 - radLat2;
    const b = (lng1 * Math.PI) / 180 - (lng2 * Math.PI) / 180;
    const s = 2 * Math.asin(Math.sqrt(
      Math.pow(Math.sin(a / 2), 2) +
      Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)
    ));
    return Math.round(s * 6371000);
  },

  /** 下拉刷新 */
  onPullDownRefresh() {
    this.getLocationAndLoad().then(() => {
      wx.stopPullDownRefresh();
    });
  },

  /** 上拉加载 */
  onReachBottom() {
    if (this.data.viewMode === 'list') {
      this.loadBallrooms();
    }
  }
});
