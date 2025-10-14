<template>
  <view class="page-container">
    <!-- 消息类型筛选 -->
    <uni-segmented-control :current="currentTab" :values="tabs" @clickItem="onTabClick"  style="margin-left: 20rpx; margin-right: 20rpx;"/>

    <!-- 消息列表 -->
    <scroll-view scroll-y class="message-list" @refresherrefresh="onRefresh" refresher-enabled>
      <uni-list>
        <uni-list-item v-for="message in filteredMessages" :key="message.id" :title="message.title"
          :note="message.content" :rightText="message.time" :show-badge="!message.read" badge-text="新" clickable
          @click="readMessage(message)" :class="['message-item', message.type + '-message']">
          <template v-slot:header>
            <view class="message-avatar">
              <text class="avatar-icon">{{ message.sender.charAt(0) }}</text>
            </view>
          </template>
        </uni-list-item>
      </uni-list>

      <uni-load-more :status="loadStatus" />
    </scroll-view>

    <!-- 发送消息悬浮按钮 -->
    <view class="fab-container">
      <uni-fab :pattern="fabPattern" :horizontal="'right'" :vertical="'bottom'" @fabClick="sendMessage" />
    </view>

    <!-- 发送消息弹窗 -->
    <uni-popup ref="messagePopup" type="bottom">
      <view class="popup-content">
        <uni-section title="发送消息" type="line">
          <uni-forms :modelValue="messageForm" :rules="messageRules" ref="form">
            <uni-forms-item label="接收人" name="receiver">
              <uni-data-select v-model="messageForm.receiver" :localdata="receivers" />
            </uni-forms-item>
            <uni-forms-item label="消息内容" name="content">
              <uni-easyinput type="textarea" v-model="messageForm.content" placeholder="请输入消息内容" />
            </uni-forms-item>
            <button type="primary" @click="submitMessage" class="submit-btn">发送</button>
          </uni-forms>
        </uni-section>
      </view>
    </uni-popup>
  </view>
</template>

<script lang="ts" setup>
import { onPullDownRefresh } from '@dcloudio/uni-app';
import { computed, ref } from 'vue';

const currentTab = ref(0)
const tabs = ref(['全部', '未读', '系统', '用户'])
const loadStatus = ref('more')
const fabPattern = ref({
  color: '#007AFF',
  backgroundColor: '#FFFFFF',
  selectedColor: '#007AFF'
})
const messageForm = ref({
  receiver: '',
  content: ''
})

const messageRules = ref({
  receiver: {
    rules: [{ required: true, errorMessage: '请选择接收人' }]
  },
  content: {
    rules: [{ required: true, errorMessage: '请输入消息内容' }]
  }
})

const receivers = ref([
  { value: 'all', text: '所有用户' },
  { value: 'admin', text: '管理员' },
  { value: 'user', text: '普通用户' }
])

const messages = ref([
  {
    id: 1,
    title: '系统通知',
    content: '系统将于今晚进行维护升级',
    time: '10:30',
    type: 'system',
    read: false,
    sender: '系统管理员'
  },
  {
    id: 2,
    title: '新用户注册',
    content: '用户张三已完成注册',
    time: '09:15',
    type: 'user',
    read: true,
    sender: '注册系统'
  },
  {
    id: 3,
    title: '重要提醒',
    content: '请及时处理待办事项',
    time: '昨天',
    type: 'important',
    read: false,
    sender: '系统提醒'
  }
])

const filteredMessages = computed(() => {
  if (currentTab.value === 0) return messages.value
  if (currentTab.value === 1) return messages.value.filter(msg => !msg.read)
  if (currentTab.value === 2) return messages.value.filter(msg => msg.type === 'system')
  if (currentTab.value === 3) return messages.value.filter(msg => msg.type === 'user')
  return messages
})


const messagePopup  = ref ()
const form = ref()

function onTabClick(e) {
  currentTab.value = e.currentIndex
}

function onRefresh() {
  setTimeout(() => {
    uni.stopPullDownRefresh()
    loadStatus.value = 'noMore'
    // 模拟新消息
    messages.value.unshift({
      id: Date.now(),
      title: '新消息',
      content: '这是一条新消息',
      time: '刚刚',
      type: 'system',
      read: false,
      sender: '系统'
    })
  }, 1000)
}

function readMessage(message) {
  message.read = true
  uni.showToast({
    title: '标记为已读',
    icon: 'success'
  })
}

function sendMessage(){
  messagePopup.value.open()
}

function submitMessage(){
  form.value.validate().then(() => {
    uni.showLoading({
        title: '发送中...'
    })
     setTimeout(() => {
          uni.hideLoading()
          messagePopup.value.close()
          uni.showToast({
            title: '发送成功',
            icon: 'success'
          })
          messageForm.value = {
            receiver: '',
            content: ''
          }
        }, 1500)
  })
}

onPullDownRefresh(() => {
  onRefresh()
})

</script>

<style scoped>
.message-list {
  height: calc(100vh - 200rpx);
  padding: 20rpx;
}

.message-avatar {
  margin-right: 20rpx;
}

.avatar-icon {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: bold;
  background: var(--primary-color);
}

.fab-container {
  position: fixed;
  bottom: 100rpx;
  right: 30rpx;
  z-index: 999;
}

/* 消息类型特定样式 */
.system-message .avatar-icon {
  background: var(--primary-color);
}

.user-message .avatar-icon {
  background: var(--success-color);
}

.important-message .avatar-icon {
  background: var(--error-color);
}
</style>