<template>
    <el-form ref="pwdRef" :model="user" :rules="rules" label-width="80px">
        <el-form-item label="旧密码" prop="oldPassword">
            <el-input v-model="user.oldPassword" placeholder="请输入旧密码" type="password" show-password />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
            <el-input v-model="user.newPassword" placeholder="请输入新密码" type="password" show-password />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
            <el-input v-model="user.confirmPassword" placeholder="请确认新密码" type="password" show-password />
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="submit">保存</el-button>
            <el-button type="danger" @click="close">关闭</el-button>
        </el-form-item>
    </el-form>
</template>
<script lang="ts" setup>
import { updatePwd } from '@/api/system/user';
import useMessage from '@/hooks/message';
import useTab from '@/hooks/tab';
import { reactive, ref } from 'vue';

const user = reactive({
    oldPassword: undefined,
    newPassword: undefined,
    confirmPassword: undefined
})

const pwdRef = ref()

const equalPwd = (rule, value, callback) => {
    if (user.newPassword != value) {
        callback(new Error('两次密码不一致'))
    } else {
        callback()
    }
}

const rules = ref({
    oldPassword: [{ required: true, message: "旧密码不能为空", trigger: "blur" }],
    newPassword: [{ required: true, message: "新密码不能为空", trigger: "blur" }, { min: 6, max: 20, message: "长度在 6 到 20 个字符", trigger: "blur" }, { pattern: /^[^<>"'|\\]+$/, message: "不能包含非法字符：< > \" ' \\\ |", trigger: "blur" }],
    confirmPassword: [{ required: true, message: "确认密码不能为空", trigger: "blur" }, { required: true, validator: equalPwd, trigger: "blur" }]
})

/**
 * 修改密码
 */
function submit() {
    pwdRef.value.validate(valid => {
        if (valid) {
            updatePwd(user).then( ()=>{
                useMessage.success('修改成功')
            })
        }
    })
}
/**
 * 关闭窗口
 */
function close(){
    useTab.closePage(null)
}
</script>