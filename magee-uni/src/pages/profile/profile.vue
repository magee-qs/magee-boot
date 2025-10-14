<template>
  <view class="page-container">
    <!-- 用户信息卡片 -->
    <view class="user-card">
      <view class="user-avatar-section">
        <image :src="userInfo.avatar" class="user-avatar" mode="aspectFill" />
        <view class="user-info">
          <text class="user-name">{{ userInfo.name }}</text>
          <text class="user-role">{{ userInfo.role }}</text>
          <uni-tag :text="userInfo.status" :inverted="true" size="small" />
        </view>
      </view>
      <view class="user-stats">
        <view class="stat" @click="navigateTo('user/list')">
          <text class="stat-number">{{ userStats.totalUsers }}</text>
          <text class="stat-label">管理用户</text>
        </view>
        <view class="stat" @click="navigateTo('message/message')">
          <text class="stat-number">{{ userStats.unreadMessages }}</text>
          <text class="stat-label">未读消息</text>
        </view>
        <view class="stat" @click="navigateTo('log/list')">
          <text class="stat-number">{{ userStats.loginDays }}</text>
          <text class="stat-label">登录天数</text>
        </view>
      </view>
    </view>

    <!-- 功能菜单 -->
    <uni-section title="账户设置" type="line">
      <uni-list>
        <uni-list-item title="个人信息" showArrow clickable @click="navigateTo('profile/edit')">
          <template v-slot:header>
            <text class="list-icon">👤</text>
          </template>
        </uni-list-item>
        <uni-list-item title="安全设置" showArrow clickable @click="navigateTo('profile/security')">
          <template v-slot:header>
            <text class="list-icon">🔒</text>
          </template>
        </uni-list-item>
        <uni-list-item title="通知设置" showArrow clickable @click="navigateTo('profile/notification')">
          <template v-slot:header>
            <text class="list-icon">🔔</text>
          </template>
        </uni-list-item>
      </uni-list>
    </uni-section>

    <!-- 系统设置 -->
    <uni-section title="系统设置" type="line">
      <uni-list>
        <uni-list-item title="主题设置" showArrow clickable @click="showThemePicker">
          <template v-slot:header>
            <text class="list-icon">🎨</text>
          </template>
        </uni-list-item>
        <uni-list-item title="清理缓存" :rightText="cacheSize" showArrow clickable @click="clearCache">
          <template v-slot:header>
            <text class="list-icon">🧹</text>
          </template>
        </uni-list-item>
        <uni-list-item title="关于系统" showArrow clickable @click="showAbout">
          <template v-slot:header>
            <text class="list-icon">ℹ️</text>
          </template>
        </uni-list-item>
      </uni-list>
    </uni-section>

    <!-- 操作按钮 -->
    <view class="action-buttons">
      <button class="action-btn logout-btn" @click="logout">退出登录</button>
    </view>

    <!-- 主题选择弹窗 -->
    <uni-popup ref="themePopup" type="dialog">
      <uni-popup-dialog type="info" title="选择主题" :value="currentTheme" @confirm="changeTheme" mode="input">
        <picker @change="onThemeChange" :value="themeIndex" :range="themes">
          <view class="picker">
            当前主题: {{ themes[themeIndex] }}
          </view>
        </picker>
      </uni-popup-dialog>
    </uni-popup>
  </view>
</template>

<script lang="ts" setup>
import { ref } from 'vue';


const userInfo = ref({
  name: '管理员',
  role: '系统管理员',
  avatar: '/static/images/profile.jpg',
  status: '在线'
})

const userStats = ref({
  totalUsers: 1234,
  unreadMessages: 5,
  loginDays: 156
})

const cacheSize = ref('15.2MB')
const currentTheme = ref('默认主题')
const themes = ref(['默认主题', '深色主题', '蓝色主题', '绿色主题'])
const themeIndex = ref(0)


const themePopup = ref()

function showThemePicker() {
  themePopup.value.open()
}

function onThemeChange(e) {
  themeIndex.value = e.detail.value
}

function changeTheme() {
  uni.showToast({
    title: `已切换到${themes.value[themeIndex.value]}`,
    icon: 'success'
  })
  themePopup.value.close()
}

function clearCache() {
  uni.showModal({
    title: '确认清理',
    content: '确定要清理缓存吗？',
    success: (res) => {
      if (res.confirm) {
        uni.showLoading({
          title: '清理中...'
        })
        setTimeout(() => {
          uni.hideLoading()
          cacheSize.value = '0MB'
          uni.showToast({
            title: '清理完成',
            icon: 'success'
          })
        }, 1000)
      }
    }
  })
}


function showAbout() {
  uni.navigateTo({
    url: '/pages/system/about'
  })
}

function logout() {
  uni.showModal({
    title: '退出登录',
    content: '确定要退出登录吗？',
    success: (res) => {
      if (res.confirm) {
        uni.showLoading({
          title: '退出中...'
        })
        setTimeout(() => {
          uni.hideLoading()
          uni.reLaunch({
            url: '/pages/login/login'
          })
        }, 1000)
      }
    }
  })
}

</script>

<style scoped>
.page-container {
  padding: 20rpx;
}

.user-card { 
  border-radius: 20rpx;
  padding: 40rpx;
  margin-bottom: 30rpx; 
}

.user-avatar-section {
  display: flex;
  align-items: center;
  margin-bottom: 40rpx;
}

.user-avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  border: 4rpx solid rgba(255, 255, 255, 0.3);
  margin-right: 30rpx;
}

.user-info {
  flex: 1;
}

.user-name {
  display: block;
  font-size: 36rpx;
  font-weight: bold;
  margin-bottom: 10rpx;
}

.user-role {
  display: block;
  font-size: 28rpx;
  opacity: 0.9;
  margin-bottom: 15rpx;
}

.user-stats {
  display: flex;
  justify-content: space-around;
  text-align: center;
}

.stat {
  flex: 1;
}

.stat-number {
  display: block;
  font-size: 32rpx;
  font-weight: bold;
  margin-bottom: 10rpx;
}

.stat-label {
  display: block;
  font-size: 24rpx;
  opacity: 0.9;
}

.logout-btn {
  background: var(--error-color);
  color: white;
  border: none;
}

.picker {
  padding: 20rpx;
  background: #f8f8f8;
  border-radius: 10rpx;
  text-align: center;
}
</style>