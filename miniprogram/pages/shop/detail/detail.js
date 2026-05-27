const { get, post } = require('../../../utils/request')
const app = getApp()

Page({
  data: {
    shop: null,
    dishes: [],
    reviews: [],
    isFavorite: false,
    currentTab: 0,
    cart: {},
    cartTotal: 0,
    cartCount: 0
  },

  onLoad(options) {
    this.shopId = options.id
    this.loadShopDetail()
    this.loadDishes()
    this.loadReviews()
    if (app.checkLogin()) {
      this.checkFavorite()
      post('/api/recommend/behavior', {
        behaviorType: 'view', targetType: 'shop', targetId: String(this.shopId)
      }, false).catch(() => {})
    }
  },

  switchTab(e) {
    this.setData({ currentTab: e.currentTarget.dataset.tab })
  },

  async loadShopDetail() {
    const res = await get('/api/shop/detail/' + this.shopId)
    this.setData({ shop: res.data })
    wx.setNavigationBarTitle({ title: res.data.name })
  },

  async loadDishes() {
    const res = await get('/api/dish/shop/' + this.shopId, {}, false)
    this.setData({ dishes: res.data })
  },

  async loadReviews() {
    const res = await get('/api/review/list', { shopId: this.shopId, page: 1, size: 10 }, false)
    this.setData({ reviews: res.data.records })
  },

  async checkFavorite() {
    try {
      const res = await get('/api/favorite/shop/check/' + this.shopId, {}, false)
      this.setData({ isFavorite: res.data })
    } catch (e) {}
  },

  async toggleFavorite() {
    if (!app.checkLogin()) {
      wx.navigateTo({ url: '/pages/login/login' }); return
    }
    if (this.data.isFavorite) {
      await require('../../../utils/request').del('/api/favorite/shop/' + this.shopId)
      this.setData({ isFavorite: false })
      wx.showToast({ title: '已取消收藏', icon: 'success' })
    } else {
      await post('/api/favorite/shop', { shopId: parseInt(this.shopId) })
      this.setData({ isFavorite: true })
      wx.showToast({ title: '收藏成功', icon: 'success' })
      post('/api/recommend/behavior', {
        behaviorType: 'favorite', targetType: 'shop', targetId: String(this.shopId)
      }, false).catch(() => {})
    }
  },

  onShareAppMessage() {
    if (app.checkLogin()) {
      post('/api/favorite/share', {
        targetId: String(this.shopId), targetType: 'shop', shareType: 'wechat'
      }, false).catch(() => {})
    }
    return {
      title: this.data.shop ? this.data.shop.name : '推荐店铺',
      path: '/pages/shop/detail/detail?id=' + this.shopId
    }
  },

  goDishDetail(e) {
    wx.navigateTo({ url: '/pages/dish/detail/detail?id=' + e.currentTarget.dataset.id })
  },

  addToCart(e) {
    const dish = e.currentTarget.dataset.dish
    const cart = this.data.cart
    if (!cart[dish.id]) {
      cart[dish.id] = { ...dish, quantity: 0 }
    }
    cart[dish.id].quantity++
    this.updateCartInfo(cart)
  },

  removeFromCart(e) {
    const id = e.currentTarget.dataset.id
    const cart = this.data.cart
    if (cart[id] && cart[id].quantity > 0) {
      cart[id].quantity--
      if (cart[id].quantity === 0) delete cart[id]
    }
    this.updateCartInfo(cart)
  },

  updateCartInfo(cart) {
    let cartTotal = 0, cartCount = 0
    Object.values(cart).forEach(item => {
      cartTotal += item.price * item.quantity
      cartCount += item.quantity
    })
    this.setData({ cart, cartTotal: cartTotal.toFixed(2), cartCount })
  },

  goOrder() {
    if (!app.checkLogin()) {
      wx.navigateTo({ url: '/pages/login/login' }); return
    }
    if (this.data.cartCount === 0) {
      wx.showToast({ title: '请先选择菜品', icon: 'none' }); return
    }
    const items = Object.values(this.data.cart)
    wx.setStorageSync('orderItems', items)
    wx.navigateTo({
      url: '/pages/order/create/create?shopId=' + this.shopId +
           '&shopName=' + encodeURIComponent(this.data.shop.name) +
           '&merchantId=' + this.data.shop.merchantId
    })
  },

  goAddReview() {
    if (!app.checkLogin()) {
      wx.navigateTo({ url: '/pages/login/login' }); return
    }
    wx.navigateTo({ url: '/pages/review/add/add?shopId=' + this.shopId })
  },

  goAllReviews() {
    wx.navigateTo({ url: '/pages/review/list/list?shopId=' + this.shopId })
  },

  callPhone() {
    if (this.data.shop && this.data.shop.phone) {
      wx.makePhoneCall({ phoneNumber: this.data.shop.phone })
    }
  }
})
