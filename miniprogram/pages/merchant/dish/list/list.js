const { get } = require('../../../../utils/request')

Page({
  data: {
    shops: [],
    currentShopId: null,
    dishes: []
  },

  onLoad() {
    this.loadShops()
  },

  onShow() {
    if (this.data.currentShopId) this.loadDishes()
  },

  async loadShops() {
    const res = await get('/api/shop/merchant')
    const shops = res.data
    this.setData({ shops, currentShopId: shops.length > 0 ? shops[0].id : null })
    if (shops.length > 0) this.loadDishes()
  },

  selectShop(e) {
    this.setData({ currentShopId: e.currentTarget.dataset.id })
    this.loadDishes()
  },

  async loadDishes() {
    const res = await get('/api/dish/shop/' + this.data.currentShopId, {}, false)
    this.setData({ dishes: res.data })
  },

  goAddDish() {
    wx.navigateTo({ url: '/pages/merchant/dish/edit/edit?shopId=' + this.data.currentShopId })
  },

  goEditDish(e) {
    wx.navigateTo({ url: '/pages/merchant/dish/edit/edit?id=' + e.currentTarget.dataset.id + '&shopId=' + this.data.currentShopId })
  }
})
