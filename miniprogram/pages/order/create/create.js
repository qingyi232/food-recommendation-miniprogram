const { post } = require('../../../utils/request')

Page({
  data: {
    shopId: null,
    shopName: '',
    merchantId: null,
    items: [],
    totalAmount: 0,
    remark: ''
  },

  onLoad(options) {
    if (options.shopId) this.setData({ shopId: parseInt(options.shopId) })
    if (options.shopName) this.setData({ shopName: decodeURIComponent(options.shopName) })
    if (options.merchantId) this.setData({ merchantId: parseInt(options.merchantId) })
    const items = wx.getStorageSync('orderItems')
    if (items) {
      this.setData({ items })
      this.calcTotal()
      wx.removeStorageSync('orderItems')
    }
  },

  calcTotal() {
    let total = 0
    this.data.items.forEach(item => {
      total += item.price * item.quantity
    })
    this.setData({ totalAmount: total.toFixed(2) })
  },

  onRemarkInput(e) {
    this.setData({ remark: e.detail.value })
  },

  async submitOrder() {
    if (this.data.items.length === 0) {
      wx.showToast({ title: '请添加菜品', icon: 'none' }); return
    }
    const orderItems = this.data.items.map(item => ({
      dishId: item.id,
      dishName: item.name,
      dishImage: item.image,
      price: item.price,
      quantity: item.quantity,
      amount: (item.price * item.quantity).toFixed(2)
    }))
    try {
      await post('/api/order/create', {
        shopId: this.data.shopId,
        merchantId: this.data.merchantId,
        totalAmount: parseFloat(this.data.totalAmount),
        remark: this.data.remark,
        items: orderItems
      })
      wx.showToast({ title: '下单成功', icon: 'success' })
      post('/api/recommend/behavior', {
        behaviorType: 'order', targetType: 'shop', targetId: String(this.data.shopId)
      }, false).catch(() => {})
      setTimeout(() => { wx.navigateBack() }, 1000)
    } catch (e) {}
  }
})
