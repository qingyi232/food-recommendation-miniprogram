const { get } = require('../../utils/request')
const app = getApp()

Page({
  data: {
    currentTab: 0,
    recommendShops: [],
    recommendDishes: [],
    isLogin: false
  },

  onLoad() {
    this.checkLoginAndLoad()
  },

  onShow() {
    this.checkLoginAndLoad()
  },

  onPullDownRefresh() {
    this.checkLoginAndLoad()
    wx.stopPullDownRefresh()
  },

  checkLoginAndLoad() {
    const isLogin = app.checkLogin()
    this.setData({ isLogin })
    if (isLogin) {
      this.loadRecommendShops()
      this.loadRecommendDishes()
    }
  },

  switchTab(e) {
    this.setData({ currentTab: e.currentTarget.dataset.tab })
  },

  async loadRecommendShops() {
    try {
      const res = await get('/api/recommend/shops', { limit: 20 }, false)
      this.setData({ recommendShops: res.data })
    } catch (e) {}
  },

  async loadRecommendDishes() {
    try {
      const res = await get('/api/recommend/dishes', { limit: 20 }, false)
      this.setData({ recommendDishes: res.data })
    } catch (e) {}
  },

  goLogin() {
    wx.navigateTo({ url: '/pages/login/login' })
  },

  goShopDetail(e) {
    wx.navigateTo({ url: '/pages/shop/detail/detail?id=' + e.currentTarget.dataset.id })
  },

  goDishDetail(e) {
    wx.navigateTo({ url: '/pages/dish/detail/detail?id=' + e.currentTarget.dataset.id })
  }
})
