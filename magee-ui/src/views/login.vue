<template>
    <div class="login">
        <el-form ref="loginRef" :model="loginForm" :rules="loginRules" class="login-form">
            <h3 class="title">{{ title }}</h3>
            <el-form-item prop="userName">
                <el-input v-model="loginForm.userName" type="text" size="large" auto-complete="off" placeholder="账号">
                    <template #prefix><svg-icon icon-class="user" class="el-input__icon input-icon" /></template>
                </el-input>
            </el-form-item>
            <el-form-item prop="password">
                <el-input v-model="loginForm.password" type="password" size="large" auto-complete="off" placeholder="密码"
                    @keyup.enter="handleLogin">
                    <template #prefix><svg-icon icon-class="password" class="el-input__icon input-icon" /></template>
                </el-input>
            </el-form-item>
            <el-form-item prop="checkCode">
                <el-input v-model="loginForm.checkCode" size="large" auto-complete="off" placeholder="验证码"
                    style="width: 63%" @keyup.enter="handleLogin">
                    <template #prefix><svg-icon icon-class="validCode" class="el-input__icon input-icon" /></template>
                </el-input>
                <div class="login-code">
                    <img :src="codeUrl" @click="getCode" class="login-code-img" />
                </div>
            </el-form-item>
            <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>
            <el-form-item style="width:100%;">
                <el-button :loading="loading" size="large" type="primary" style="width:100%;"
                    @click.prevent="handleLogin">
                    <span v-if="!loading">登 录</span>
                    <span v-else>登 录 中...</span>
                </el-button>
            </el-form-item>
        </el-form>
        <!--  底部  -->
        <div class="el-login-footer">
            <span>Copyright © 2018-2025 ruoyi.vip All Rights Reserved.</span>
        </div>
    </div>
</template>
<script lang="ts" setup>
import { getCodeImg } from '@/api/login';
import { onMounted, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import Cookies from 'js-cookie'
import { decrypt, encrypt } from '@/utils/jsencrypt';
import useUserStore from '@/store/modules/user';
 

const title = import.meta.env.VITE_APP_TITLE

const loginForm = ref({
    userName: 'admin',
    password: '123456',
    checkCode: '1111',
    checkKey: '1111',
    rememberMe: true,
})
const loginRules = {
    userName: [{ required: true, trigger: "blur", message: "请输入您的账号" }],
    password: [{ required: true, trigger: "blur", message: "请输入您的密码" }],
    checkCode: [{ required: true, trigger: "change", message: "请输入验证码" }]
}
const codeUrl = ref("")
const loading = ref(false)
const redirect = ref(undefined)
const loginRef: any = ref(null)

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

watch(route, (newRoute) => {
    redirect.value = newRoute.query && newRoute.query.redirect
}, { immediate: true })


function getCode() {
    getCodeImg().then((res: any) => {
        codeUrl.value = "data:image/gif;base64," + res.img
        loginForm.value.checkKey = res.uuid 
    })
}

function getCookie() {
    const username = Cookies.get("username")
    const password = Cookies.get("password") 

    loginForm.value.userName === undefined ? loginForm.value.userName : username
    loginForm.value.password === password === undefined ? loginForm.value.password : decrypt(password) 
}

function handleLogin() {
    loginRef.value.validate(valid => {
        if (valid) {
            loading.value = true
            // 勾选了需要记住密码设置在 cookie 中设置记住用户名和密码
            if (loginForm.value.rememberMe) {
                Cookies.set("username", loginForm.value.userName, { expires: 30 })
                Cookies.set("password", encrypt(loginForm.value.password), { expires: 30 }) 
            } else {
                // 否则移除
                Cookies.remove("username")
                Cookies.remove("password") 
            }

            let param = loginForm.value
            delete param['rememberMe']

            userStore.login(param).then(() => {
                const query = route.query
                const otherQueryParams = Object.keys(query).reduce((acc, cur) => {
                    if (cur !== "redirect") {
                        acc[cur] = query[cur]
                    }
                    return acc
                }, {})
                router.push({ path: redirect.value || "/", query: otherQueryParams })
            }).catch(() => {
                loading.value = false
                getCode()
            })
        }
    })
}

onMounted(() => {
    getCode()
    getCookie()
})

</script>

<style lang='scss' scoped>
.login {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    background-image: url("../assets/images/background.svg");
    background-size: cover;
}

.title {
    margin: 0px auto 30px auto;
    text-align: center;
    color: #707070;
}

.login-form {
    border-radius: 6px;
    background: #ffffff;
    width: 400px;
    padding: 25px 25px 5px 25px;
    z-index: 1;

    .el-input {
        height: 40px;

        input {
            height: 40px;
        }
    }

    .input-icon {
        height: 39px;
        width: 14px;
        margin-left: 0px;
    }
}

.login-tip {
    font-size: 13px;
    text-align: center;
    color: #bfbfbf;
}

.login-code {
    width: 33%;
    height: 40px;
    float: right;

    img {
        cursor: pointer;
        vertical-align: middle;
    }
}

.el-login-footer {
    height: 40px;
    line-height: 40px;
    position: fixed;
    bottom: 0;
    width: 100%;
    text-align: center;
    color: #fff;
    font-family: Arial;
    font-size: 12px;
    letter-spacing: 1px;
}

.login-code-img {
    height: 40px;
    padding-left: 12px;
}
</style>