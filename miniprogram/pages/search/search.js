const { get } = require('../../utils/request')

Page({
  data: {
    keyword: '',
    currentTab: 0,
    shopList: [],
    dishList: [],
    searched: false
  },

  onLoad(options) {
    if (options.keyword) {
      this.setData({ keyword: options.keyword })
      this.doSearch()
    }
  },

  onKeywordInput(e) {
    this.setData({ keyword: e.detail.value })
  },

  switchTab(e) {
    this.setData({ currentTab: e.currentTarget.dataset.tab })
  },

  async doSearch() {
    const keyword = this.data.keyword.trim()
    if (!keyword) {
      wx.showToast({ title: '请输入搜索关键词', icon: 'none' }); return
    }
    this.setData({ searched: true })
    const [shopRes, dishRes] = await Promise.all([
      get('/api/shop/list', { page: 1, size: 20, keyword }, false),
      get('/api/dish/list', { page: 1, size: 20, keyword }, false)
    ])
    this.setData({
      shopList: shopRes.data.records,
      dishList: dishRes.data.records
    })
  },

  goShopDetail(e) {
    wx.navigateTo({ url: '/pages/shop/detail/detail?id=' + e.currentTarget.dataset.id })
  },

  goDishDetail(e) {
    wx.navigateTo({ url: '/pages/dish/detail/detail?id=' + e.currentTarget.dataset.id })
  }
})
