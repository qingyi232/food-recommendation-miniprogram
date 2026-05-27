/**
 * 轻量级微信小程序图表工具
 * 支持柱状图 (bar) 和饼图 (pie)
 */

const COLORS = ['#ff6b35', '#36a2eb', '#ffce56', '#4bc0c0', '#9966ff',
  '#ff9f40', '#ff6384', '#c9cbcf', '#7bc043', '#f37735']

function getDpr() {
  try {
    return wx.getWindowInfo().pixelRatio
  } catch (e) {
    return wx.getSystemInfoSync().pixelRatio || 2
  }
}

function drawBarChart(canvas, ctx, data, options) {
  options = options || {}
  var dpr = getDpr()
  var width = canvas.width / dpr
  var height = canvas.height / dpr

  var padding = { top: 30, right: 20, bottom: 60, left: 50 }
  var chartW = width - padding.left - padding.right
  var chartH = height - padding.top - padding.bottom

  ctx.clearRect(0, 0, width, height)

  if (!data || data.length === 0) {
    ctx.fillStyle = '#999'
    ctx.font = '14px sans-serif'
    ctx.textAlign = 'center'
    ctx.fillText('暂无数据', width / 2, height / 2)
    return
  }

  var title = options.title || ''
  if (title) {
    ctx.fillStyle = '#333'
    ctx.font = 'bold 14px sans-serif'
    ctx.textAlign = 'center'
    ctx.fillText(title, width / 2, 20)
  }

  var values = data.map(function(d) { return d.value || d.sales || 0 })
  var maxVal = Math.max.apply(null, values) || 1
  var barCount = data.length
  var gap = 8
  var barW = Math.min((chartW - gap * (barCount + 1)) / barCount, 40)
  var totalBarArea = barW * barCount + gap * (barCount + 1)
  var offsetX = padding.left + (chartW - totalBarArea) / 2

  ctx.strokeStyle = '#e8e8e8'
  ctx.lineWidth = 0.5
  ctx.fillStyle = '#999'
  ctx.font = '10px sans-serif'
  ctx.textAlign = 'right'
  var ySteps = 5
  for (var i = 0; i <= ySteps; i++) {
    var y = padding.top + chartH - (chartH * i / ySteps)
    var val = Math.round(maxVal * i / ySteps)
    ctx.beginPath()
    ctx.moveTo(padding.left, y)
    ctx.lineTo(width - padding.right, y)
    ctx.stroke()
    ctx.fillText(String(val), padding.left - 5, y + 4)
  }

  for (var j = 0; j < data.length; j++) {
    var item = data[j]
    var v = item.value || item.sales || 0
    var barH = (v / maxVal) * chartH
    var x = offsetX + gap + j * (barW + gap)
    var by = padding.top + chartH - barH
    var color = COLORS[j % COLORS.length]

    var gradient = ctx.createLinearGradient(x, by, x, padding.top + chartH)
    gradient.addColorStop(0, color)
    gradient.addColorStop(1, color + '88')
    ctx.fillStyle = gradient
    roundRect(ctx, x, by, barW, barH, 3)

    ctx.fillStyle = '#333'
    ctx.font = '10px sans-serif'
    ctx.textAlign = 'center'
    ctx.fillText(String(v), x + barW / 2, by - 5)

    ctx.fillStyle = '#666'
    ctx.font = '9px sans-serif'
    ctx.save()
    ctx.translate(x + barW / 2, padding.top + chartH + 8)
    ctx.rotate(Math.PI / 6)
    var label = item.name.length > 5 ? item.name.substring(0, 5) + '..' : item.name
    ctx.textAlign = 'left'
    ctx.fillText(label, 0, 0)
    ctx.restore()
  }
}

function drawPieChart(canvas, ctx, data, options) {
  options = options || {}
  var dpr = getDpr()
  var width = canvas.width / dpr
  var height = canvas.height / dpr

  ctx.clearRect(0, 0, width, height)

  if (!data || data.length === 0) {
    ctx.fillStyle = '#999'
    ctx.font = '14px sans-serif'
    ctx.textAlign = 'center'
    ctx.fillText('暂无数据', width / 2, height / 2)
    return
  }

  var title = options.title || ''
  if (title) {
    ctx.fillStyle = '#333'
    ctx.font = 'bold 14px sans-serif'
    ctx.textAlign = 'center'
    ctx.fillText(title, width / 2, 20)
  }

  var total = 0
  for (var k = 0; k < data.length; k++) {
    total += (data[k].value || 0)
  }
  if (total === 0) {
    ctx.fillStyle = '#999'
    ctx.font = '14px sans-serif'
    ctx.textAlign = 'center'
    ctx.fillText('暂无数据', width / 2, height / 2)
    return
  }

  var cx = width * 0.38
  var cy = height / 2 + 10
  var radius = Math.min(cx - 20, cy - 35)
  var startAngle = -Math.PI / 2

  for (var i = 0; i < data.length; i++) {
    var ratio = (data[i].value || 0) / total
    var angle = ratio * Math.PI * 2
    var color = COLORS[i % COLORS.length]

    ctx.beginPath()
    ctx.moveTo(cx, cy)
    ctx.arc(cx, cy, radius, startAngle, startAngle + angle)
    ctx.closePath()
    ctx.fillStyle = color
    ctx.fill()

    startAngle += angle
  }

  ctx.beginPath()
  ctx.arc(cx, cy, radius * 0.45, 0, Math.PI * 2)
  ctx.fillStyle = '#fff'
  ctx.fill()

  var legendX = width * 0.68
  var legendY = 40
  var legendSize = 10
  var lineH = 22

  for (var m = 0; m < data.length; m++) {
    if (legendY > height - 10) break
    var c = COLORS[m % COLORS.length]
    var r = ((data[m].value || 0) / total * 100).toFixed(1)

    ctx.fillStyle = c
    ctx.fillRect(legendX, legendY, legendSize, legendSize)

    ctx.fillStyle = '#333'
    ctx.font = '10px sans-serif'
    ctx.textAlign = 'left'
    var lb = data[m].name.length > 6 ? data[m].name.substring(0, 6) + '..' : data[m].name
    ctx.fillText(lb + ' ' + r + '%', legendX + legendSize + 6, legendY + legendSize - 1)

    legendY += lineH
  }
}

function roundRect(ctx, x, y, w, h, r) {
  if (h <= 0) return
  r = Math.min(r, w / 2, h / 2)
  ctx.beginPath()
  ctx.moveTo(x + r, y)
  ctx.lineTo(x + w - r, y)
  ctx.arcTo(x + w, y, x + w, y + r, r)
  ctx.lineTo(x + w, y + h)
  ctx.lineTo(x, y + h)
  ctx.lineTo(x, y + r)
  ctx.arcTo(x, y, x + r, y, r)
  ctx.closePath()
  ctx.fill()
}

module.exports = { drawBarChart: drawBarChart, drawPieChart: drawPieChart }
