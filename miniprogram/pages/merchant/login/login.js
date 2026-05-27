const { post } = require('../../../utils/request')
const app = getApp()

Page({
  data: { username: '', password: '' },

  onInput(e) {
    this.setData({ [e.currentTarget.dataset.field]: e.detail.value })
  },

  async doLogin() {
    const { username, password } = this.data
    if (!username || !password) {
      wx.showToast({ title: '请输入用户名和密码', icon: 'none' }); return
    }
    try {
      const res = await post('/api/merchant/login', { username, password })
      app.setLoginInfo(res.data.token, res.data.merchantInfo, 'merchant')
      wx.showToast({ title: '登录成功', icon: 'success' })
      setTimeout(() => {
        wx.redirectTo({ url: '/pages/merchant/index/index' })
      }, 1000)
    } catch (e) {}
  },

  goRegister() {
    wx.navigateTo({ url: '/pages/merchant/register/register' })
  }
})
