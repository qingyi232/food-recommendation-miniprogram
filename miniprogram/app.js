App({
  globalData: {
    baseUrl: 'http://localhost:8080',
    userInfo: null,
    token: null,
    role: null
  },

  onLaunch() {
    const token = wx.getStorageSync('token');
    const userInfo = wx.getStorageSync('userInfo');
    const role = wx.getStorageSync('role');
    if (token) {
      this.globalData.token = token;
      this.globalData.userInfo = userInfo;
      this.globalData.role = role;
    }
  },

  setLoginInfo(token, userInfo, role) {
    this.globalData.token = token;
    this.globalData.userInfo = userInfo;
    this.globalData.role = role;
    wx.setStorageSync('token', token);
    wx.setStorageSync('userInfo', userInfo);
    wx.setStorageSync('role', role);
  },

  clearLoginInfo() {
    this.globalData.token = null;
    this.globalData.userInfo = null;
    this.globalData.role = null;
    wx.removeStorageSync('token');
    wx.removeStorageSync('userInfo');
    wx.removeStorageSync('role');
  },

  checkLogin() {
    return !!this.globalData.token;
  }
})
