<script setup lang="ts">
import { onLaunch, onShow, onHide } from "@dcloudio/uni-app";
import useUserStore from "./store/user";
import useAuth from "./hooks/auth";
onLaunch(() => {
  console.log("App Launch");

  const userStore = useUserStore()
  const token = useAuth.getToken()
  if (token) {
    if (userStore.isLoad == false) {
      let p1 = userStore.getInfo()
      // 等待加载
      Promise.all([p1])
    }
    // 已登录，跳转到首页
    uni.reLaunch({
      url: '/pages/index/index'
    });
  } else {
    // 未登录，跳转到登录页
    uni.reLaunch({
      url: '/pages/login/login'
    });
  }

});
onShow(() => {
  console.log("App Show");
});
onHide(() => {
  console.log("App Hide");
});
</script>
<style lang="scss">
/*每个页面公共css */
@use 'uni.scss';
@use './static/scss/index.scss';


/* 全局样式重置 */
page {
  background-color: var(--background-color);
  font-family: -apple-system, BlinkMacSystemFont, 'Helvetica Neue', Helvetica, sans-serif;
  line-height: 1.6;
}

/* 统一滚动条样式 */
::-webkit-scrollbar {
  width: 0;
  height: 0;
  color: transparent;
}
</style>
