const { post } = require('../../utils/request')

Page({
  data: {
    username: '',
    password: '',
    confirmPassword: '',
    nickname: '',
    phone: ''
  },

  onInput(e) {
    const field = e.currentTarget.dataset.field
    this.setData({ [field]: e.detail.value })
  },

  async doRegister() {
    const { username, password, confirmPassword, nickname, phone } = this.data
    if (!username || !password) {
      wx.showToast({ title: '请输入用户名和密码', icon: 'none' }); return
    }
    if (password !== confirmPassword) {
      wx.showToast({ title: '两次密码不一致', icon: 'none' }); return
    }
    try {
      await post('/api/user/register', { username, password, nickname, phone })
      wx.showToast({ title: '注册成功', icon: 'success' })
      setTimeout(() => { wx.navigateBack() }, 1000)
    } catch (e) {}
  }
})
