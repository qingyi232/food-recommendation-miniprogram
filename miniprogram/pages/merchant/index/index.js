const { get } = require('../../../utils/request')
const app = getApp()

Page({
  data: {
    merchantInfo: null,
    stats: null,
    menuList: [
      { icon: '🏪', title: '店铺管理', url: '/pages/merchant/shop/edit/edit' },
      { icon: '🍜', title: '菜品管理', url: '/pages/merchant/dish/list/list' },
      { icon: '📋', title: '订单管理', url: '/pages/merchant/order/list/list' },
      { icon: '📊', title: '数据统计', url: '/pages/merchant/stats/stats' },
      { icon: '💬', title: '评价管理', url: '/pages/merchant/review/list/list' },
      { icon: '👤', title: '商家信息', url: '/pages/merchant/info/info' }
    ]
  },

  onShow() {
    if (!app.checkLogin() || app.globalData.role !== 'merchant') {
      wx.redirectTo({ url: '/pages/merchant/login/login' })
      return
    }
    this.setData({ merchantInfo: app.globalData.userInfo })
    this.loadStats()
  },

  async loadStats() {
    try {
      const res = await get('/api/order/merchant/stats', {}, false)
      this.setData({ stats: res.data })
    } catch (e) {}
  },

  goMenu(e) {
    wx.navigateTo({ url: e.currentTarget.dataset.url })
  },

  logout() {
    wx.showModal({
      title: '提示',
      content: '确定退出登录？',
      success: (res) => {
        if (res.confirm) {
          app.clearLoginInfo()
          wx.redirectTo({ url: '/pages/merchant/login/login' })
        }
      }
    })
  }
})
