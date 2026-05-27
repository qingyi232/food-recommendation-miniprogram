const { get, post, put, uploadFile } = require('../../../../utils/request')

Page({
  data: {
    dishId: null,
    shopId: null,
    form: { name: '', description: '', price: '', originalPrice: '', tags: '', spicyLevel: 0, image: '' },
    spicyOptions: ['不辣', '微辣', '中辣', '特辣']
  },

  onLoad(options) {
    if (options.shopId) this.setData({ shopId: options.shopId })
    if (options.id) {
      this.setData({ dishId: options.id })
      this.loadDish()
    }
  },

  async loadDish() {
    const res = await get('/api/dish/detail/' + this.data.dishId)
    const dish = res.data
    this.setData({
      form: {
        name: dish.name || '', description: dish.description || '',
        price: dish.price ? String(dish.price) : '', originalPrice: dish.originalPrice ? String(dish.originalPrice) : '',
        tags: dish.tags || '', spicyLevel: dish.spicyLevel || 0, image: dish.image || ''
      }
    })
  },

  onFormInput(e) {
    this.setData({ ['form.' + e.currentTarget.dataset.field]: e.detail.value })
  },

  onSpicyChange(e) {
    this.setData({ 'form.spicyLevel': parseInt(e.detail.value) })
  },

  chooseImage() {
    wx.chooseImage({
      count: 1,
      sizeType: ['compressed'],
      sourceType: ['album', 'camera'],
      success: async (res) => {
        try {
          const url = await uploadFile(res.tempFilePaths[0])
          this.setData({ 'form.image': url })
        } catch (e) {
          console.error('\u4e0a\u4f20\u5931\u8d25:', e)
        }
      }
    })
  },

  removeImage() {
    this.setData({ 'form.image': '' })
  },

  async saveDish() {
    const { form, dishId, shopId } = this.data
    if (!form.name || !form.price) {
      wx.showToast({ title: '请填写名称和价格', icon: 'none' }); return
    }
    const data = {
      shopId: parseInt(shopId), name: form.name, description: form.description,
      price: parseFloat(form.price), originalPrice: form.originalPrice ? parseFloat(form.originalPrice) : null,
      tags: form.tags, spicyLevel: form.spicyLevel, image: form.image || null, status: 1
    }
    try {
      if (dishId) {
        data.id = parseInt(dishId)
        await put('/api/dish/update', data)
        wx.showToast({ title: '修改成功', icon: 'success' })
      } else {
        await post('/api/dish/add', data)
        wx.showToast({ title: '添加成功', icon: 'success' })
      }
      setTimeout(() => { wx.navigateBack() }, 1000)
    } catch (e) {}
  }
})
