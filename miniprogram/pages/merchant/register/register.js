const { post } = require('../../../utils/request')

Page({
  data: { username: '', password: '', confirmPassword: '', name: '', contactName: '', phone: '' },

  onInput(e) {
    this.setData({ [e.currentTarget.dataset.field]: e.detail.value })
  },

  async doRegister() {
    const { username, password, confirmPassword, name, contactName, phone } = this.data
    if (!username || !password || !name) {
      wx.showToast({ title: '请填写必填项', icon: 'none' }); return
    }
    if (password !== confirmPassword) {
      wx.showToast({ title: '两次密码不一致', icon: 'none' }); return
    }
    try {
      await post('/api/merchant/register', { username, password, name, contactName, phone })
      wx.showToast({ title: '注册成功，待审核', icon: 'success' })
      setTimeout(() => { wx.navigateBack() }, 1500)
    } catch (e) {}
  }
})
