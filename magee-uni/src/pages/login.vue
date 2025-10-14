<template>
  <view class="login-container">
    <!-- 背景装饰 -->
    <view class="background-decoration">
      <view class="decoration-circle circle-1"></view>
      <view class="decoration-circle circle-2"></view>
      <view class="decoration-circle circle-3"></view>
    </view>

    <!-- 登录表单区域 -->
    <view class="login-form">
      <!-- 头部 -->
      <view class="login-header">
        <text class="welcome-text">欢迎回来</text>
        <text class="subtitle">用户管理系统</text>
      </view>

      <!-- 登录表单 -->
      <uni-forms ref="loginForm" :modelValue="formData" :rules="rules" class="form-content">
        <!-- 用户名/手机号 -->
        <uni-forms-item name="username" class="form-item">
          <uni-easyinput v-model="formData.username" placeholder="请输入用户名或手机号" :inputBorder="false" prefixIcon="person"
            class="custom-input" />
        </uni-forms-item>

        <!-- 密码 -->
        <uni-forms-item name="password" class="form-item">
          <uni-easyinput v-model="formData.password" type="password" placeholder="请输入密码" :inputBorder="false"
            prefixIcon="locked" class="custom-input" />
        </uni-forms-item>

        <!-- 记住密码和忘记密码 -->
        <view class="form-options">
          <view class="remember-me" @click="toggleRemember">
            <view class="checkbox" :class="{ checked: formData.remember }">
              <text class="checkmark" v-if="formData.remember">✓</text>
            </view>
            <text class="option-text">记住密码</text>
          </view>
          <text class="forgot-password" @click="forgotPassword">忘记密码？</text>
        </view>

        <!-- 登录按钮 -->
        <button class="login-btn" :class="{ loading: loading }" :disabled="loading" @click="handleLogin">
          <text v-if="!loading">登录</text>
          <text v-else>登录中...</text>
        </button>

        <!-- 其他登录方式 -->
        <view class="other-login" v-if="showOtherLogin">
          <view class="divider">
            <view class="divider-line"></view>
            <text class="divider-text">其他登录方式</text>
            <view class="divider-line"></view>
          </view>
          <view class="login-methods">
            <view class="method-item" @click="wechatLogin">
              <image src="/static/login/wechat.png" class="method-icon" mode="aspectFit" />
              <text class="method-text">微信</text>
            </view>
            <view class="method-item" @click="smsLogin">
              <image src="/static/login/sms.png" class="method-icon" mode="aspectFit" />
              <text class="method-text">短信</text>
            </view>
          </view>
        </view>
      </uni-forms>
    </view>

    <!-- 底部信息 -->
    <view class="login-footer">
      <text class="footer-text">还没有账号？</text>
      <text class="register-link" @click="goToRegister">立即注册</text>
    </view>

    <!-- 忘记密码弹窗 -->
    <uni-popup ref="forgotPopup" type="dialog">
      <uni-popup-dialog type="info" title="忘记密码" content="请联系系统管理员重置密码" confirmText="联系管理员"
        @confirm="contactAdmin"></uni-popup-dialog>
    </uni-popup>
  </view>
</template>

<script lang="ts" setup>
import cache from '@/hooks/cache';
import { onLoad } from '@dcloudio/uni-app';
import { ref } from 'vue';

const loading = ref(false)
const showOtherLogin = ref(false)
const formData = ref({
  userName: 'admin',
  password: '123456',
  remember: false
})
const rules = ref({
  username: {
    rules: [
      { required: true, errorMessage: '请输入用户名或手机号' },
      { minLength: 2, maxLength: 20, errorMessage: '用户名长度在2-20个字符之间' }
    ]
  },
  password: {
    rules: [
      { required: true, errorMessage: '请输入密码' },
      { minLength: 6, errorMessage: '密码至少6位' }
    ]
  }
})

onLoad(() => {

})

function loadRememberedAccount() {
  // 加载缓存账号和密码
}

const loginForm = ref()

// 处理登录
async function handleLogin() {
  try {
    // 验证表单
    await loginForm.value.validate()
    loading.value = true

    // 模拟登录请求
    const loginResult: any = await mockLogin()
    if (loginResult.success) {
      // 保存登录状态
      saveLoginState(loginResult)

      // 如果记住密码，保存账号信息
      if (formData.value.remember) {
        cache.set('rememberedAccount', formData.value)
      } else {
        cache.remove('rememberedAccount')
      }

      // 显示登录成功提示
      uni.showToast({
        title: '登录成功',
        icon: 'success',
        duration: 1500
      })

      // 跳转到首页
      setTimeout(() => {
        uni.switchTab({
          url: '/pages/index/index'
        })
      }, 1500)
    } else {
      uni.showToast({
        title: loginResult.message || '登录失败',
        icon: 'none'
      })
    }

  } catch (error) {
    console.log('登录失败:', error)
  } finally {
    loading.value = false
  }
}
// 模拟登录
function mockLogin() {
  return new Promise((resolve) => {
    setTimeout(() => {
      // 模拟登录验证
      if (formData.value.userName && formData.value.password) {
        resolve({
          success: true,
          userInfo: {
            id: 1,
            username: formData.value.userName,
            name: '管理员',
            role: 'admin',
            token: 'mock-token-' + Date.now()
          }
        })
      } else {
        resolve({
          success: false,
          message: '用户名或密码错误'
        })
      }
    }, 1500)
  })
}

// 保存登录状态
function saveLoginState(loginResult) {
  const { userInfo, token } = loginResult
}


const forgotPopup = ref()
// 忘记密码
function forgotPassword() {
  forgotPopup.value.open()
}

// 联系管理员
function contactAdmin() {
  uni.makePhoneCall({
    phoneNumber: '400-123-4567'
  })
}

// 微信登录
function wechatLogin() {
  uni.showToast({
    title: '微信登录功能开发中',
    icon: 'none'
  })
}

// 短信登录
function smsLogin() {
  uni.showToast({
    title: '短信登录功能开发中',
    icon: 'none'
  })
}

// 跳转到注册页面
function goToRegister() {
  uni.showToast({
    title: '注册功能开发中',
    icon: 'none'
  })
  // 实际项目中可以跳转到注册页面
  // uni.navigateTo({
  //   url: '/pages/register/register'
  // })
}


</script>

<style scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  position: relative;
  overflow: hidden;
}

/* 背景装饰 */
.background-decoration {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
}

.decoration-circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
}

.circle-1 {
  width: 200rpx;
  height: 200rpx;
  top: -100rpx;
  right: -100rpx;
}

.circle-2 {
  width: 150rpx;
  height: 150rpx;
  bottom: 200rpx;
  left: -75rpx;
}

.circle-3 {
  width: 100rpx;
  height: 100rpx;
  bottom: 100rpx;
  right: 50rpx;
}

/* 登录表单 */
.login-form {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 0 60rpx;
  z-index: 1;
}

.login-header {
  text-align: center;
  margin-bottom: 80rpx;
}

.welcome-text {
  display: block;
  font-size: 48rpx;
  font-weight: bold;
  color: white;
  margin-bottom: 20rpx;
}

.subtitle {
  display: block;
  font-size: 32rpx;
  color: rgba(255, 255, 255, 0.8);
}

/* 表单内容 */
.form-content {
  background: white;
  border-radius: 24rpx;
  padding: 60rpx 40rpx;
  box-shadow: 0 20rpx 60rpx rgba(0, 0, 0, 0.1);
}

.form-item {
  margin-bottom: 40rpx;
}

.custom-input {
  background: #F8F9FA;
  border-radius: 16rpx;
  padding: 0 20rpx;
  height: 100rpx;
}

/* 表单选项 */
.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 60rpx;
}

.remember-me {
  display: flex;
  align-items: center;
}

.checkbox {
  width: 36rpx;
  height: 36rpx;
  border: 2rpx solid #DDD;
  border-radius: 8rpx;
  margin-right: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;
}

.checkbox.checked {
  background: #007AFF;
  border-color: #007AFF;
}

.checkmark {
  color: white;
  font-size: 24rpx;
  font-weight: bold;
}

.option-text {
  font-size: 28rpx;
  color: #666;
}

.forgot-password {
  font-size: 28rpx;
  color: #007AFF;
}

/* 登录按钮 */
.login-btn {
  background: linear-gradient(135deg, #007AFF 0%, #5856D6 100%);
  color: white;
  border: none;
  border-radius: 16rpx;
  height: 100rpx;
  font-size: 32rpx;
  font-weight: 500;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;
}

.login-btn:active {
  transform: scale(0.98);
}

.login-btn.loading {
  opacity: 0.7;
}

/* 其他登录方式 */
.other-login {
  margin-top: 60rpx;
}

.divider {
  display: flex;
  align-items: center;
  margin-bottom: 40rpx;
}

.divider-line {
  flex: 1;
  height: 1rpx;
  background: #EEE;
}

.divider-text {
  padding: 0 30rpx;
  font-size: 26rpx;
  color: #999;
}

.login-methods {
  display: flex;
  justify-content: center;
  gap: 80rpx;
}

.method-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.method-icon {
  width: 80rpx;
  height: 80rpx;
  margin-bottom: 16rpx;
}

.method-text {
  font-size: 24rpx;
  color: #666;
}

/* 底部信息 */
.login-footer {
  padding: 40rpx 0;
  text-align: center;
  z-index: 1;
}

.footer-text {
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.8);
  margin-right: 16rpx;
}

.register-link {
  font-size: 28rpx;
  color: white;
  font-weight: 500;
}
</style>