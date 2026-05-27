const { get } = require('../../utils/request')
const app = getApp()

Page({
  data: {
    isLogin: false,
    userInfo: null,
    menuList: [
      { icon: '📋', title: '我的订单', url: '/pages/order/list/list' },
      { icon: '⭐', title: '收藏店铺', url: '/pages/favorite/favorite?tab=0' },
      { icon: '❤️', title: '收藏美食', url: '/pages/favorite/favorite?tab=1' },
      { icon: '💬', title: '我的评价', url: '/pages/review/list/list?type=mine' },
      { icon: '🍴', title: '口味偏好', url: '/pages/taste/taste' },
      { icon: '🏪', title: '商家入口', url: '/pages/merchant/login/login' }
    ]
  },

  onShow() {
    const isLogin = app.checkLogin()
    this.setData({ isLogin })
    if (isLogin) {
      this.loadUserInfo()
    }
  },

  async loadUserInfo() {
    try {
      const res = await get('/api/user/info', {}, false)
      this.setData({ userInfo: res.data })
    } catch (e) {
      console.error('\u83b7\u53d6\u7528\u6237\u4fe1\u606f\u5931\u8d25:', e)
      this.setData({ isLogin: false })
    }
  },

  goLogin() {
    wx.navigateTo({ url: '/pages/login/login' })
  },

  goMenu(e) {
    const url = e.currentTarget.dataset.url
    if (!app.checkLogin() && url !== '/pages/merchant/login/login') {
      wx.navigateTo({ url: '/pages/login/login' })
      return
    }
    wx.navigateTo({ url })
  },

  logout() {
    wx.showModal({
      title: '提示',
      content: '确定退出登录？',
      success: (res) => {
        if (res.confirm) {
          app.clearLoginInfo()
          this.setData({ isLogin: false, userInfo: null })
          wx.showToast({ title: '已退出登录', icon: 'success' })
        }
      }
    })
  }
})
