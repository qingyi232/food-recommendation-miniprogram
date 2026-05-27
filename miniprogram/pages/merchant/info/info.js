const { get, put } = require('../../../utils/request')
const app = getApp()

Page({
  data: {
    form: { name: '', contactName: '', phone: '', email: '' }
  },

  onLoad() { this.loadInfo() },

  async loadInfo() {
    const res = await get('/api/merchant/info')
    const info = res.data
    this.setData({
      form: { name: info.name || '', contactName: info.contactName || '', phone: info.phone || '', email: info.email || '' }
    })
  },

  onInput(e) {
    this.setData({ ['form.' + e.currentTarget.dataset.field]: e.detail.value })
  },

  async saveInfo() {
    try {
      await put('/api/merchant/update', this.data.form)
      wx.showToast({ title: '保存成功', icon: 'success' })
    } catch (e) {}
  }
})
