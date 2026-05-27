const { get } = require('../../../utils/request')

Page({
  data: {
    shopList: [],
    page: 1,
    hasMore: true,
    loading: false,
    keyword: ''
  },

  onLoad(options) {
    if (options.keyword) {
      this.setData({ keyword: options.keyword })
    }
    this.loadShops(true)
  },

  async loadShops(reset) {
    if (this.data.loading) return
    this.setData({ loading: true })
    const page = reset ? 1 : this.data.page
    const res = await get('/api/shop/list', {
      page, size: 10, keyword: this.data.keyword
    }, false)
    const newList = reset ? res.data.records : [...this.data.shopList, ...res.data.records]
    this.setData({
      shopList: newList,
      page: page + 1,
      hasMore: newList.length < res.data.total,
      loading: false
    })
  },

  onReachBottom() {
    if (this.data.hasMore) this.loadShops(false)
  },

  goShopDetail(e) {
    wx.navigateTo({ url: '/pages/shop/detail/detail?id=' + e.currentTarget.dataset.id })
  }
})
