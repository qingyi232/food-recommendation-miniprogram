const { post } = require('../../../utils/request')

Page({
  data: {
    shopId: null,
    dishId: null,
    rating: 5,
    tasteRating: 5,
    serviceRating: 5,
    environmentRating: 5,
    content: ''
  },

  onLoad(options) {
    if (options.shopId) this.setData({ shopId: options.shopId })
    if (options.dishId) this.setData({ dishId: options.dishId })
  },

  setRating(e) {
    this.setData({ rating: e.currentTarget.dataset.value })
  },

  setTasteRating(e) {
    this.setData({ tasteRating: e.currentTarget.dataset.value })
  },

  setServiceRating(e) {
    this.setData({ serviceRating: e.currentTarget.dataset.value })
  },

  setEnvironmentRating(e) {
    this.setData({ environmentRating: e.currentTarget.dataset.value })
  },

  onContentInput(e) {
    this.setData({ content: e.detail.value })
  },

  async submitReview() {
    if (!this.data.content) {
      wx.showToast({ title: '请输入评价内容', icon: 'none' }); return
    }
    const data = {
      shopId: parseInt(this.data.shopId),
      rating: this.data.rating,
      tasteRating: this.data.tasteRating,
      serviceRating: this.data.serviceRating,
      environmentRating: this.data.environmentRating,
      content: this.data.content
    }
    if (this.data.dishId) data.dishId = parseInt(this.data.dishId)

    try {
      await post('/api/review/add', data)
      wx.showToast({ title: '评价成功', icon: 'success' })
      post('/api/recommend/behavior', {
        behaviorType: 'review', targetType: 'shop', targetId: String(this.data.shopId)
      }, false).catch(() => {})
      setTimeout(() => { wx.navigateBack() }, 1000)
    } catch (e) {}
  }
})
