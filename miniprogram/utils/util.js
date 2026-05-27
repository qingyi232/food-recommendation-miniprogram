const formatTime = (date) => {
  const d = new Date(date)
  const year = d.getFullYear()
  const month = (d.getMonth() + 1).toString().padStart(2, '0')
  const day = d.getDate().toString().padStart(2, '0')
  const hour = d.getHours().toString().padStart(2, '0')
  const minute = d.getMinutes().toString().padStart(2, '0')
  return `${year}-${month}-${day} ${hour}:${minute}`
}

const renderStars = (rating) => {
  const stars = []
  const full = Math.floor(rating)
  const half = rating - full >= 0.5 ? 1 : 0
  const empty = 5 - full - half
  for (let i = 0; i < full; i++) stars.push('full')
  if (half) stars.push('half')
  for (let i = 0; i < empty; i++) stars.push('empty')
  return stars
}

const getStatusText = (status) => {
  const map = { 0: '待确认', 1: '已确认', 2: '已完成', 3: '已取消' }
  return map[status] || '未知'
}

const getMerchantStatusText = (status) => {
  const map = { 0: '待审核', 1: '正常', 2: '已禁用' }
  return map[status] || '未知'
}

module.exports = { formatTime, renderStars, getStatusText, getMerchantStatusText }
