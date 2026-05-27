const { get } = require('../../../utils/request')
const { drawBarChart, drawPieChart } = require('../../../utils/wxcharts')

function getDpr() {
  try {
    return wx.getWindowInfo().pixelRatio
  } catch (e) {
    return wx.getSystemInfoSync().pixelRatio || 2
  }
}

Page({
  data: {
    stats: null,
    shops: [],
    chartLoaded: false
  },

  onLoad() {
    this.loadStats()
    this.loadShops()
    this.loadChartData()
  },

  async loadStats() {
    try {
      const res = await get('/api/order/merchant/stats')
      this.setData({ stats: res.data })
    } catch (e) {
      console.error('加载统计数据失败', e)
    }
  },

  async loadShops() {
    try {
      const res = await get('/api/shop/merchant', {}, false)
      this.setData({ shops: res.data || [] })
    } catch (e) {
      console.error('加载店铺数据失败', e)
    }
  },

  async loadChartData() {
    try {
      const res = await get('/api/order/merchant/chart-data')
      const chartData = res.data
      this.setData({ chartLoaded: true })

      setTimeout(() => {
        this.renderBarChart(chartData.dishSalesTop10 || [])
        this.renderPieChart(chartData.categorySales || [])
      }, 300)
    } catch (e) {
      console.error('加载图表数据失败', e)
    }
  },

  renderBarChart(data) {
    const query = wx.createSelectorQuery()
    query.select('#barCanvas')
      .fields({ node: true, size: true })
      .exec((res) => {
        if (!res || !res[0]) return
        const canvas = res[0].node
        const ctx = canvas.getContext('2d')
        const dpr = getDpr()
        canvas.width = res[0].width * dpr
        canvas.height = res[0].height * dpr
        ctx.scale(dpr, dpr)
        drawBarChart(canvas, ctx, data, { title: '菜品销量 TOP10' })
      })
  },

  renderPieChart(data) {
    const query = wx.createSelectorQuery()
    query.select('#pieCanvas')
      .fields({ node: true, size: true })
      .exec((res) => {
        if (!res || !res[0]) return
        const canvas = res[0].node
        const ctx = canvas.getContext('2d')
        const dpr = getDpr()
        canvas.width = res[0].width * dpr
        canvas.height = res[0].height * dpr
        ctx.scale(dpr, dpr)
        drawPieChart(canvas, ctx, data, { title: '各分类销量分布' })
      })
  }
})
