const app = getApp()

const request = (options) => {
  return new Promise((resolve, reject) => {
    const { url, method = 'GET', data = {}, loading = true } = options

    if (loading) {
      wx.showLoading({ title: '加载中...' })
    }

    const header = {
      'Content-Type': 'application/json'
    }

    if (app.globalData.token) {
      header['Authorization'] = 'Bearer ' + app.globalData.token
    }

    wx.request({
      url: app.globalData.baseUrl + url,
      method,
      data,
      header,
      success(res) {
        if (loading) wx.hideLoading()
        if (res.data.code === 200) {
          resolve(res.data)
        } else if (res.data.code === 401) {
          app.clearLoginInfo()
          wx.showToast({ title: '请先登录', icon: 'none' })
          setTimeout(() => {
            wx.navigateTo({ url: '/pages/login/login' })
          }, 1500)
          reject(res.data)
        } else {
          wx.showToast({ title: res.data.message || '请求失败', icon: 'none' })
          reject(res.data)
        }
      },
      fail(err) {
        if (loading) wx.hideLoading()
        wx.showToast({ title: '网络请求失败', icon: 'none' })
        reject(err)
      }
    })
  })
}

const get = (url, data, loading) => request({ url, method: 'GET', data, loading })
const post = (url, data, loading) => request({ url, method: 'POST', data, loading })
const put = (url, data, loading) => request({ url, method: 'PUT', data, loading })
const del = (url, data, loading) => request({ url, method: 'DELETE', data, loading })

const uploadFile = (filePath) => {
  return new Promise((resolve, reject) => {
    wx.showLoading({ title: '上传中...' })
    wx.uploadFile({
      url: app.globalData.baseUrl + '/api/upload',
      filePath,
      name: 'file',
      header: {
        'Authorization': 'Bearer ' + (app.globalData.token || '')
      },
      success(res) {
        wx.hideLoading()
        const data = JSON.parse(res.data)
        if (data.code === 200) {
          resolve(data.data)
        } else {
          wx.showToast({ title: '上传失败', icon: 'none' })
          reject(data)
        }
      },
      fail(err) {
        wx.hideLoading()
        wx.showToast({ title: '上传失败', icon: 'none' })
        reject(err)
      }
    })
  })
}

module.exports = { request, get, post, put, del, uploadFile }
