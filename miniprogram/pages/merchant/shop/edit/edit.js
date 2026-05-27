const { get, post, put } = require('../../../../utils/request')

Page({
  data: {
    shops: [],
    showForm: false,
    form: { name: '', description: '', address: '', phone: '', businessHours: '', avgPrice: '', categoryId: '', tags: '' },
    categories: [],
    editId: null
  },

  onLoad() {
    this.loadShops()
    this.loadCategories()
  },

  async loadShops() {
    const res = await get('/api/shop/merchant', {}, false)
    this.setData({ shops: res.data })
  },

  async loadCategories() {
    const res = await get('/api/category/list', {}, false)
    this.setData({ categories: res.data })
  },

  showAddForm() {
    this.setData({ showForm: true, editId: null, form: { name: '', description: '', address: '', phone: '', businessHours: '', avgPrice: '', categoryId: '', tags: '' } })
  },

  editShop(e) {
    const shop = e.currentTarget.dataset.shop
    this.setData({
      showForm: true,
      editId: shop.id,
      form: {
        name: shop.name || '', description: shop.description || '', address: shop.address || '',
        phone: shop.phone || '', businessHours: shop.businessHours || '',
        avgPrice: shop.avgPrice ? String(shop.avgPrice) : '', categoryId: shop.categoryId ? String(shop.categoryId) : '', tags: shop.tags || ''
      }
    })
  },

  hideForm() { this.setData({ showForm: false }) },

  onFormInput(e) {
    const field = e.currentTarget.dataset.field
    this.setData({ ['form.' + field]: e.detail.value })
  },

  onCategoryChange(e) {
    this.setData({ 'form.categoryId': this.data.categories[e.detail.value].id })
  },

  async saveShop() {
    const { form, editId } = this.data
    if (!form.name) { wx.showToast({ title: '请输入店铺名称', icon: 'none' }); return }
    const data = { ...form, avgPrice: parseFloat(form.avgPrice) || 0, categoryId: parseInt(form.categoryId) || null }
    try {
      if (editId) {
        data.id = editId
        await put('/api/shop/update', data)
        wx.showToast({ title: '修改成功', icon: 'success' })
      } else {
        await post('/api/shop/add', data)
        wx.showToast({ title: '添加成功', icon: 'success' })
      }
      this.setData({ showForm: false })
      this.loadShops()
    } catch (e) {}
  }
})
