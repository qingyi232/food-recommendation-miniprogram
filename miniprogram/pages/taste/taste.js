const { get, put } = require('../../utils/request')
const app = getApp()

Page({
  data: {
    spicy: 0,
    sweet: 0,
    sour: 0,
    salty: 0,
    categories: [],
    selectedCategories: [],
    levelLabels: ['不喜欢', '一般', '比较喜欢', '喜欢', '非常喜欢']
  },

  onLoad() {
    this.loadCategories()
    this.loadPreference()
  },

  async loadCategories() {
    const res = await get('/api/category/list', {}, false)
    this.setData({ categories: res.data })
  },

  async loadPreference() {
    try {
      const res = await get('/api/user/info', {}, false)
      if (res.data && res.data.tastePreference) {
        const pref = JSON.parse(res.data.tastePreference)
        this.setData({
          spicy: pref.spicy || 0,
          sweet: pref.sweet || 0,
          sour: pref.sour || 0,
          salty: pref.salty || 0,
          selectedCategories: pref.categories || []
        })
      }
    } catch (e) {}
  },

  onSliderChange(e) {
    const field = e.currentTarget.dataset.field
    this.setData({ [field]: e.detail.value })
  },

  toggleCategory(e) {
    const id = e.currentTarget.dataset.id
    let selected = this.data.selectedCategories
    const idx = selected.indexOf(id)
    if (idx > -1) {
      selected.splice(idx, 1)
    } else {
      selected.push(id)
    }
    this.setData({ selectedCategories: selected })
  },

  async savePreference() {
    const preference = JSON.stringify({
      spicy: this.data.spicy,
      sweet: this.data.sweet,
      sour: this.data.sour,
      salty: this.data.salty,
      categories: this.data.selectedCategories
    })
    try {
      await put('/api/user/taste', { tastePreference: preference })
      wx.showToast({ title: '保存成功', icon: 'success' })
      setTimeout(() => { wx.navigateBack() }, 1000)
    } catch (e) {}
  }
})
