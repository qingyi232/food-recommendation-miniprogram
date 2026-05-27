const { get, put } = require('../../../../utils/request')
const util = require('../../../../utils/util')

Page({
  data: {
    orders: [],
    page: 1,
    hasMore: true,
    loading: false,
    currentStatus: null,
    statusList: [
      { label: '全部', value: null },
      { label: '待确认', value: 0 },
      { label: '已确认', value: 1 },
      { label: '已完成', value: 2 },
      { label: '已取消', value: 3 }
    ]
  },

  onLoad() { this.loadOrders(true) },
  onShow() { this.loadOrders(true) },

  switchStatus(e) {
    this.setData({ currentStatus: e.currentTarget.dataset.status })
    this.loadOrders(true)
  },

  async loadOrders(reset) {
    if (this.data.loading) return
    this.setData({ loading: true })
    const page = reset ? 1 : this.data.page
    const params = { page, size: 10 }
    if (this.data.currentStatus !== null) params.status = this.data.currentStatus
    const res = await get('/api/order/merchant/page', params, false)
    const records = res.data.records.map(item => {
      item.statusText = util.getStatusText(item.status)
      return item
    })
    const newList = reset ? records : [...this.data.orders, ...records]
    this.setData({ orders: newList, page: page + 1, hasMore: newList.length < res.data.total, loading: false })
  },

  onReachBottom() { if (this.data.hasMore) this.loadOrders(false) },

  confirmOrder(e) {
    this.updateStatus(e.currentTarget.dataset.id, 1, '确认订单')
  },

  completeOrder(e) {
    this.updateStatus(e.currentTarget.dataset.id, 2, '完成订单')
  },

  cancelOrder(e) {
    this.updateStatus(e.currentTarget.dataset.id, 3, '取消订单')
  },

  updateStatus(id, status, action) {
    wx.showModal({
      title: '提示',
      content: '确定' + action + '？',
      success: async (res) => {
        if (res.confirm) {
          await put('/api/order/status/' + id, { status })
          wx.showToast({ title: action + '成功', icon: 'success' })
          this.loadOrders(true)
        }
      }
    })
  }
})
