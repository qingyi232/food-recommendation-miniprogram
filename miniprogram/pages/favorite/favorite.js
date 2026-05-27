const { get } = require('../../utils/request')

Page({
  data: {
    currentTab: 0,
    favoriteShops: [],
    favoriteFoods: []
  },

  onLoad(options) {
    if (options.tab) this.setData({ currentTab: parseInt(options.tab) })
  },

  onShow() {
    this.loadFavoriteShops()
    this.loadFavoriteFoods()
  },

  switchTab(e) {
    this.setData({ currentTab: e.currentTarget.dataset.tab })
  },

  async loadFavoriteShops() {
    try {
      const res = await get('/api/favorite/shop/list', {}, false)
      this.setData({ favoriteShops: res.data })
    } catch (e) {}
  },

  async loadFavoriteFoods() {
    try {
      const res = await get('/api/favorite/food/list', {}, false)
      this.setData({ favoriteFoods: res.data })
    } catch (e) {}
  },

  goShopDetail(e) {
    wx.navigateTo({ url: '/pages/shop/detail/detail?id=' + e.currentTarget.dataset.id })
  },

  goDishDetail(e) {
    wx.navigateTo({ url: '/pages/dish/detail/detail?id=' + e.currentTarget.dataset.id })
  }
})
