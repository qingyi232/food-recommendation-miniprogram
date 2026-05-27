const { get } = require('../../../utils/request')
const app = getApp()

Page({
  data: {
    reviews: [],
    page: 1,
    hasMore: true,
    loading: false,
    shopId: null,
    isMine: false
  },

  onLoad(options) {
    if (options.shopId) this.setData({ shopId: options.shopId })
    if (options.type === 'mine') this.setData({ isMine: true })
    this.loadReviews(true)
  },

  async loadReviews(reset) {
    if (this.data.loading) return
    this.setData({ loading: true })
    const page = reset ? 1 : this.data.page
    const params = { page, size: 10 }
    if (this.data.shopId) params.shopId = this.data.shopId

    let url = '/api/review/list'
    if (this.data.isMine) url = '/api/review/user'

    const res = await get(url, params, false)
    const newList = reset ? res.data.records : [...this.data.reviews, ...res.data.records]
    this.setData({
      reviews: newList,
      page: page + 1,
      hasMore: newList.length < res.data.total,
      loading: false
    })
  },

  onReachBottom() {
    if (this.data.hasMore) this.loadReviews(false)
  }
})
