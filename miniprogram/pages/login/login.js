const { post } = require('../../utils/request')
const app = getApp()

Page({
  data: {
    username: '',
    password: ''
  },

  onUsernameInput(e) {
    this.setData({ username: e.detail.value })
  },

  onPasswordInput(e) {
    this.setData({ password: e.detail.value })
  },

  async doLogin() {
    const { username, password } = this.data
    if (!username || !password) {
      wx.showToast({ title: '请输入用户名和密码', icon: 'none' })
      return
    }
    try {
      const res = await post('/api/user/login', { username, password })
      app.setLoginInfo(res.data.token, res.data.userInfo, 'user')
      wx.showToast({ title: '登录成功', icon: 'success' })
      setTimeout(() => { wx.navigateBack() }, 1000)
    } catch (e) {
      console.error('登录失败:', e)
    }
  },

  wxLogin() {
    wx.login({
      success: async (res) => {
        if (res.code) {
          try {
            const result = await post('/api/user/wxLogin', { code: res.code })
            app.setLoginInfo(result.data.token, result.data.userInfo, 'user')
            wx.showToast({ title: '登录成功', icon: 'success' })
            setTimeout(() => { wx.navigateBack() }, 1000)
          } catch (e) {
            console.error('微信登录失败:', e)
          }
        }
      }
    })
  },

  goRegister() {
    wx.navigateTo({ url: '/pages/register/register' })
  }
})
