const { get, put } = require('../../../../utils/request')

Page({
  data: {
    reviews: [],
    page: 1,
    hasMore: true,
    loading: false,
    replyingId: null,
    replyContent: ''
  },

  onLoad() { this.loadReviews(true) },
  onShow() { this.loadReviews(true) },

  async loadReviews(reset) {
    if (this.data.loading) return
    this.setData({ loading: true })
    const page = reset ? 1 : this.data.page
    const res = await get('/api/review/list', { page, size: 10 }, false)
    const newList = reset ? res.data.records : [...this.data.reviews, ...res.data.records]
    this.setData({ reviews: newList, page: page + 1, hasMore: newList.length < res.data.total, loading: false })
  },

  onReachBottom() { if (this.data.hasMore) this.loadReviews(false) },

  showReply(e) {
    this.setData({ replyingId: e.currentTarget.dataset.id, replyContent: '' })
  },

  onReplyInput(e) {
    this.setData({ replyContent: e.detail.value })
  },

  async submitReply() {
    if (!this.data.replyContent) {
      wx.showToast({ title: '请输入回复内容', icon: 'none' }); return
    }
    await put('/api/review/reply/' + this.data.replyingId, { reply: this.data.replyContent })
    wx.showToast({ title: '回复成功', icon: 'success' })
    this.setData({ replyingId: null })
    this.loadReviews(true)
  },

  cancelReply() {
    this.setData({ replyingId: null })
  }
})
