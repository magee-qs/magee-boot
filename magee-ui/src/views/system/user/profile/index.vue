<template>
   <div class="app-container">
      <el-row :gutter="20">
         <el-col :span="6" :xs="24">
            <el-card class="box-card">
               <template v-slot:header>
                  <div class="clearfix">
                     <span>个人信息</span>
                  </div>
               </template>
               <div>
                  <div class="text-center">
                     <UserAvatar></UserAvatar>
                  </div>
                  <ul class="list-group list-group-striped">
                     <li class="list-group-item">
                        <svg-icon icon-class="user" />用户名称
                        <div class="pull-right">{{ state.user.userName }}</div>
                     </li>
                     <li class="list-group-item">
                        <svg-icon icon-class="phone" />手机号码
                        <div class="pull-right  ">{{ state.user.phone }}</div>
                     </li>
                     <li class="list-group-item">
                        <svg-icon icon-class="email" />用户邮箱
                        <div class="pull-right">{{ state.user.email }}</div>
                     </li>
                     <li class="list-group-item">
                        <svg-icon icon-class="tree" />所属部门
                        <div class="pull-right" v-if="state.user.dept">{{ state.user.dept.deptName }} / {{
                           state.postGroup }}</div>
                     </li>
                     <li class="list-group-item">
                        <svg-icon icon-class="peoples" />所属角色
                        <div class="pull-right">{{ state.roleGroup }}</div>
                     </li>
                     <li class="list-group-item">
                        <svg-icon icon-class="date" />创建日期
                        <div class="pull-right">{{ state.user.createTime }}</div>
                     </li>
                  </ul>
               </div>
            </el-card>
         </el-col>
         <el-col :span="18" :xs="24">
            <el-card>
               <template v-slot:header>
                  <div class="clearfix">
                     <span>基本资料</span>
                  </div>
               </template>
               <el-tabs v-model="selectedTab">
                  <el-tab-pane label="基本资料" name="userinfo">
                     <UserForm :user="state.user" ></UserForm>
                  </el-tab-pane>
                  <el-tab-pane label="修改密码" name="resetPwd">
                     <ResetPwd></ResetPwd>
                  </el-tab-pane>
               </el-tabs>
            </el-card>
         </el-col>
      </el-row>
   </div>
</template>
<script lang="ts" setup>

import { getProfile } from "@/api/system/user";
import { onMounted, reactive, ref } from "vue";
import { useRoute } from "vue-router";
import UserForm from "./UserForm.vue";
import ResetPwd from "./ResetPwd.vue";
import UserAvatar from "./UserAvatar.vue";


const route = useRoute()
const selectedTab: any = ref('userinfo')
const state = reactive({
   user: {} as any,
   roleGroup: {},
   postGroup: {}
})
 
function getUserProfile() {
   //读取用户
   getProfile().then((res: any) => {
      state.user = res.data
      state.roleGroup = res.roleGroup || {}
      state.postGroup = res.postGroup || {}
   })
}

onMounted(() => {
   const activeTab = route.params && route.params?.activeTab
   if (activeTab) {
      selectedTab.value = activeTab
   }

   getUserProfile()
})

</script>