<template>
    <div class="navbar">
    <Hamburger id="hamburger-container" :is-active="appStore.sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />
    <BreadCrumb v-if="!settingStore.topNav" id="breadcrumb-container" class="breadcrumb-container" /> 
    <TopMenu v-if="settingStore.topNav" id="topmenu-container" class="topmenu-container"></TopMenu>

    <div class="right-menu">
      <template v-if="appStore.device !== 'mobile'">
        <HeaderSearch id="header-search" class="right-menu-item"></HeaderSearch>
       
        <el-tooltip content="源码地址" effect="dark" placement="bottom">
          <LinkGit id="ruoyi-git" class="right-menu-item hover-effect" ></LinkGit> 
        </el-tooltip>

        <el-tooltip content="文档地址" effect="dark" placement="bottom">
          <LinkDoc id="ruoyi-doc" class="right-menu-item hover-effect"></LinkDoc> 
        </el-tooltip>

        <ScreenFull id="screenfull" class="right-menu-item hover-effect"></ScreenFull>

        <el-tooltip content="主题模式" effect="dark" placement="bottom">
          <div class="right-menu-item hover-effect theme-switch-wrapper" @click="toggleTheme">
            <svg-icon v-if="settingStore.isDark" icon-class="sunny" />
            <svg-icon v-if="!settingStore.isDark" icon-class="moon" />
          </div>
        </el-tooltip>

        <el-tooltip content="布局大小" effect="dark" placement="bottom">
          <SizeSelect  id="size-select" class="right-menu-item hover-effect"></SizeSelect> 
        </el-tooltip>
      </template>

      <el-dropdown @command="handleCommand" class="avatar-container right-menu-item hover-effect" trigger="hover">
        <div class="avatar-wrapper">
          <img :src="avatar" class="user-avatar" />
          <span class="user-nickname"> {{ realName }} </span>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <router-link to="/user/profile">
              <el-dropdown-item>个人中心</el-dropdown-item>
            </router-link>
            <el-dropdown-item command="setLayout" v-if="settingStore.showSettings">
                <span>布局设置</span>
              </el-dropdown-item>
            <el-dropdown-item divided command="logout">
              <span>退出登录</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>
<script lang="ts" setup>
import useAppStore from '@/store/modules/app';
import useSettingStore from '@/store/modules/setting';
import useUserStore from '@/store/modules/user';
import { ElMessageBox } from 'element-plus';
import { computed } from 'vue';
import BreadCrumb from '../tool/BreadCrumb.vue';
import Hamburger from '../tool/Hamburger.vue';
import TopMenu from '../menu/TopMenu.vue';
import LinkDoc from '../tool/LinkDoc.vue';
import LinkGit from '../tool/LinkGit.vue';
import SizeSelect from '../tool/SizeSelect.vue';
import HeaderSearch from '../tool/HeaderSearch.vue';
import ScreenFull from '../tool/ScreenFull.vue';



const settingStore = useSettingStore()
const appStore = useAppStore()
const userStore = useUserStore()


const avatar = computed(() => userStore.userInfo.avatar)
const realName = computed(() => userStore.userInfo.realName)


function toggleSideBar(){
    appStore.toggleSideBar(true)
}

function handleCommand(command){
     switch (command) {
    case "setLayout":
      setLayout()
      break
    case "logout":
      logout()
      break
    default:
      break
  }
}

function logout() {
  ElMessageBox.confirm('确定注销并退出系统吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    userStore.logout().then(() => {
      location.href = '/index'
    })
  }).catch(() => { })
}

const emits = defineEmits(['setLayout'])
function setLayout() {
  emits('setLayout')
}

function toggleTheme() {
  settingStore.toggleTheme()
}
</script>


<style lang='scss' scoped>
$background :  var(--navbar-bg);
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: $background;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: var(--navbar-bg) 0.3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, 0.025);
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .topmenu-container {
    position: absolute;
    left: 50px;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;
    display: flex;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: $background 0.3s;

        &:hover {
          background: rgba(0, 0, 0, 0.025);
        }
      }

      &.theme-switch-wrapper {
        display: flex;
        align-items: center;

        svg {
          transition: transform 0.3s;
          
          &:hover {
            transform: scale(1.15);
          }
        }
      }
    }

    .avatar-container {
      margin-right: 0px;
      padding-right: 0px;

      .avatar-wrapper {
        margin-top: 10px;
        right: 8px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 30px;
          height: 30px;
          margin-right: 8px;
          border-radius: 50%;
        }

        .user-nickname{
          position: relative;
          left: 0px;
          bottom: 10px;
          font-size: 14px;
          font-weight: bold;
        }

        i {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
