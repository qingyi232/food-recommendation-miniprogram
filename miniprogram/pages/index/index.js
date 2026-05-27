const { get, post } = require('../../utils/request')
const app = getApp()

Page({
  data: {
    bannerList: [
      { id: 1, image: 'https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=750&h=300&fit=crop' },
      { id: 2, image: 'https://images.unsplash.com/photo-1414235077428-338989a2e8c0?w=750&h=300&fit=crop' },
      { id: 3, image: 'https://images.unsplash.com/photo-1567620905732-2d1ec7ab7445?w=750&h=300&fit=crop' }
    ],
    categoryList: [],
    popularShops: [],
    popularDishes: [],
    recommendShops: [],
    searchKeyword: ''
  },

  onLoad() {
    this.loadCategories()
    this.loadPopularShops()
    this.loadPopularDishes()
  },

  onShow() {
    if (app.checkLogin()) {
      this.loadRecommendShops()
    }
  },

  onPullDownRefresh() {
    this.loadCategories()
    this.loadPopularShops()
    this.loadPopularDishes()
    if (app.checkLogin()) {
      this.loadRecommendShops()
    }
    wx.stopPullDownRefresh()
  },

  async loadCategories() {
    const res = await get('/api/category/list', {}, false)
    this.setData({ categoryList: res.data.slice(0, 8) })
  },

  async loadPopularShops() {
    const res = await get('/api/recommend/popular/shops', { limit: 6 }, false)
    this.setData({ popularShops: res.data })
  },

  async loadPopularDishes() {
    const res = await get('/api/recommend/popular/dishes', { limit: 6 }, false)
    this.setData({ popularDishes: res.data })
  },

  async loadRecommendShops() {
    try {
      const res = await get('/api/recommend/shops', { limit: 6 }, false)
      this.setData({ recommendShops: res.data })
    } catch (e) {}
  },

  onSearchInput(e) {
    this.setData({ searchKeyword: e.detail.value })
  },

  goSearch() {
    wx.navigateTo({
      url: '/pages/search/search?keyword=' + this.data.searchKeyword
    })
  },

  goCategory(e) {
    const id = e.currentTarget.dataset.id
    wx.switchTab({ url: '/pages/category/category' })
  },

  goShopDetail(e) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({ url: '/pages/shop/detail/detail?id=' + id })
    if (app.checkLogin()) {
      post('/api/recommend/behavior', {
        behaviorType: 'click', targetType: 'shop', targetId: String(id)
      }, false).catch(() => {})
    }
  },

  goDishDetail(e) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({ url: '/pages/dish/detail/detail?id=' + id })
    if (app.checkLogin()) {
      post('/api/recommend/behavior', {
        behaviorType: 'click', targetType: 'dish', targetId: String(id)
      }, false).catch(() => {})
    }
  },

  goShopList() {
    wx.navigateTo({ url: '/pages/shop/list/list' })
  },

  goAllCategories() {
    wx.switchTab({ url: '/pages/category/category' })
  }
})
