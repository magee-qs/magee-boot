<template>
  <view class="page-container">
    <!-- 顶部统计卡片 -->
    <view class="stats-section">
      <uni-grid :column="3" :highlight="false" :show-border="false">
        <uni-grid-item>
          <view class="stat-item">
            <text class="stat-number">11,234</text>
            <text class="stat-label">总用户数</text>
          </view>
        </uni-grid-item>
        <uni-grid-item>
          <view class="stat-item">
            <text class="stat-number">561</text>
            <text class="stat-label">今日新增</text>
          </view>
        </uni-grid-item>
        <uni-grid-item>
          <view class="stat-item">
            <text class="stat-number">89%</text>
            <text class="stat-label">活跃度</text>
          </view>
        </uni-grid-item>
      </uni-grid>
    </view>

    <!-- 功能模块 -->
    <uni-section title="系统功能" type="line">
      <uni-grid :column="3" :highlight="true" :show-border="false">
        <uni-grid-item v-for="item in functionList" :key="item.id" @click="useTab.navigateTo(item.url)">
          <view class="function-item">
            <text class="function-icon" :style="{ color: item.color }">{{ item.icon }}</text>
            <text class="function-text">{{ item.name }}</text>
          </view>
        </uni-grid-item>
      </uni-grid>
    </uni-section>

    <!-- 快捷操作 -->
    <uni-section title="快捷操作" type="line">
      <view class="quick-actions">
        <uni-tag v-for="action in quickActions" :key="action.id" :text="action.name" :type="action.type"
          :inverted="true" @click="handleQuickAction(action)" class="action-tag" />
      </view>
    </uni-section>

    <!-- 最近活动 -->
    <uni-section title="最近活动" type="line">
      <uni-list>
        <uni-list-item v-for="activity in recentActivities" :key="activity.id" :title="activity.title"
          :note="activity.time" :show-arrow="false" :show-badge="activity.type === 'important'" badge-text="重要" />
      </uni-list>
    </uni-section>
  </view>
</template>

<script setup lang="ts">
import useTab from '@/hooks/tab'
import { onPullDownRefresh } from '@dcloudio/uni-app'
import { ref } from 'vue'

const functionList = ref([
  { id: 1, name: '用户管理', icon: '👥', color: '#007AFF', url: '/pages/user/list' },
  { id: 2, name: '角色管理', icon: '🔑', color: '#34C759', url: '/pages/role/list' },
  { id: 3, name: '部门管理', icon: '🏢', color: '#FF9500', url: '/pages/department/list' },
  { id: 4, name: '权限管理', icon: '🔒', color: '#FF3B30', url: '/pages/permission/list' },
  { id: 5, name: '操作日志', icon: '📊', color: '#5856D6', url: '/pages/log/list' },
  { id: 6, name: '系统设置', icon: '⚙️', color: '#8E8E93', url: '/pages/system/settings' }
])
const quickActions = ref([
  { id: 1, name: '新增用户', type: 'primary' },
  { id: 2, name: '数据导出', type: 'success' },
  { id: 3, name: '批量操作', type: 'warning' }
])

const recentActivities = ref([
  { id: 1, title: '管理员新增了用户张三', time: '2分钟前', type: 'normal' },
  { id: 2, title: '系统权限配置已更新', time: '1小时前', type: 'important' },
  { id: 3, title: '用户李四修改了个人信息', time: '3小时前', type: 'normal' }
])


function handleQuickAction(action){
   switch(action.id) {
        case 1:
          uni.navigateTo({
            url: '/pages/user/form'
          })
          break
        case 2:
          uni.showToast({
            title: '开始导出数据',
            icon: 'success'
          })
          break
        case 3:
          uni.showActionSheet({
            itemList: ['批量删除', '批量启用', '批量禁用'],
            success: (res) => {
              console.log('选择了：' + res.tapIndex)
            }
          })
          break
      }
}

onPullDownRefresh(() => {
    setTimeout(() => {
      uni.stopPullDownRefresh()
      uni.showToast({
        title: '刷新成功',
        icon: 'success'
      })
    }, 1000)
})

</script>

<style scoped>
 .page-container {
  padding: 20rpx;
}
</style>
