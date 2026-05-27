const { get, post } = require('../../../utils/request')
const app = getApp()

Page({
  data: {
    dish: null,
    isFavorite: false
  },

  onLoad(options) {
    this.dishId = options.id
    this.loadDishDetail()
    if (app.checkLogin()) {
      this.checkFavorite()
      post('/api/recommend/behavior', {
        behaviorType: 'view', targetType: 'dish', targetId: String(this.dishId)
      }, false).catch(() => {})
    }
  },

  async loadDishDetail() {
    const res = await get('/api/dish/detail/' + this.dishId)
    this.setData({ dish: res.data })
    wx.setNavigationBarTitle({ title: res.data.name })
  },

  async checkFavorite() {
    try {
      const res = await get('/api/favorite/food/check/' + this.dishId, {}, false)
      this.setData({ isFavorite: res.data })
    } catch (e) {}
  },

  async toggleFavorite() {
    if (!app.checkLogin()) {
      wx.navigateTo({ url: '/pages/login/login' }); return
    }
    if (this.data.isFavorite) {
      await require('../../../utils/request').del('/api/favorite/food/' + this.dishId)
      this.setData({ isFavorite: false })
      wx.showToast({ title: '已取消收藏', icon: 'success' })
    } else {
      await post('/api/favorite/food', { dishId: parseInt(this.dishId) })
      this.setData({ isFavorite: true })
      wx.showToast({ title: '收藏成功', icon: 'success' })
      post('/api/recommend/behavior', {
        behaviorType: 'favorite', targetType: 'dish', targetId: String(this.dishId)
      }, false).catch(() => {})
    }
  },

  onShareAppMessage() {
    if (app.checkLogin()) {
      post('/api/favorite/share', {
        targetId: String(this.dishId), targetType: 'dish', shareType: 'wechat'
      }, false).catch(() => {})
    }
    return {
      title: this.data.dish ? this.data.dish.name : '推荐菜品',
      path: '/pages/dish/detail/detail?id=' + this.dishId
    }
  },

  goShop() {
    if (this.data.dish) {
      wx.navigateTo({ url: '/pages/shop/detail/detail?id=' + this.data.dish.shopId })
    }
  }
})
