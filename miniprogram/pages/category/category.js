const { get } = require('../../utils/request')

Page({
  data: {
    categoryList: [],
    currentCategoryId: null,
    shopList: [],
    page: 1,
    hasMore: true,
    loading: false
  },

  onLoad() {
    this.loadCategories()
  },

  async loadCategories() {
    const res = await get('/api/category/list')
    const list = res.data
    this.setData({
      categoryList: list,
      currentCategoryId: list.length > 0 ? list[0].id : null
    })
    if (list.length > 0) {
      this.loadShops(true)
    }
  },

  selectCategory(e) {
    const id = e.currentTarget.dataset.id
    this.setData({ currentCategoryId: id, page: 1, hasMore: true, shopList: [] })
    this.loadShops(true)
  },

  async loadShops(reset) {
    if (this.data.loading) return
    this.setData({ loading: true })
    const page = reset ? 1 : this.data.page
    const res = await get('/api/shop/list', {
      page,
      size: 10,
      categoryId: this.data.currentCategoryId
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
    if (this.data.hasMore) {
      this.loadShops(false)
    }
  },

  goShopDetail(e) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({ url: '/pages/shop/detail/detail?id=' + id })
  }
})
