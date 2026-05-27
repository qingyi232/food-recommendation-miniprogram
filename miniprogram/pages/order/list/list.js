const { get } = require('../../../utils/request')
const util = require('../../../utils/util')

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

  onLoad() {
    this.loadOrders(true)
  },

  onShow() {
    this.loadOrders(true)
  },

  switchStatus(e) {
    this.setData({ currentStatus: e.currentTarget.dataset.status })
    this.loadOrders(true)
  },

  async loadOrders(reset) {
    if (this.data.loading) return
    this.setData({ loading: true })
    try {
      const page = reset ? 1 : this.data.page
      const params = { page, size: 10 }
      if (this.data.currentStatus !== null) params.status = this.data.currentStatus
      const res = await get('/api/order/user/page', params, false)
      const records = (res.data.records || []).map(item => {
        item.statusText = util.getStatusText(item.status)
        return item
      })
      const newList = reset ? records : [...this.data.orders, ...records]
      this.setData({
        orders: newList,
        page: page + 1,
        hasMore: newList.length < (res.data.total || 0),
        loading: false
      })
    } catch (e) {
      console.error('加载订单失败:', e)
      this.setData({ loading: false })
    }
  },

  onReachBottom() {
    if (this.data.hasMore) this.loadOrders(false)
  },

  goOrderDetail(e) {
    // 可扩展订单详情页
  }
})
