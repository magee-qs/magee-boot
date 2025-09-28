<template>
    <BaseForm v-model="form" ref="formRef" :rules="rules" width="680px" label-width="100px" :titile="title"
        :add="addMenu" :update="updateMenu" :load="getMenuById" :init="init" @add="emit('add')" @update="emit('update')" @reset-form="resetForm">
        <el-row>
            <el-col :span="24">
                <el-form-item label="上级菜单">
                    <el-tree-select v-model="form.parentId" :data="menuOptions"
                        :props="{ value: 'menuId', label: 'name', children: 'children' }" value-key="menuId"
                        placeholder="选择上级菜单" check-strictly>
                    </el-tree-select>
                </el-form-item>
            </el-col>
            <el-col ::span="24">
                <el-form-item label="菜单类型" prop="menuType">
                    <el-radio-group v-model="form.menuType">
                        <el-radio value="M">目录</el-radio>
                        <el-radio value="C">菜单</el-radio>
                        <el-radio value="F">按钮</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-col>
            <el-col :span="12" v-if="form.menuType != 'F'">
                <el-form-item label="菜单图标" prop="icon">
                    <el-popover placement="bottom-start" :width="540" trigger="click">
                        <template #reference>
                            <el-input v-model="form.icon" placeholder="点击选择图标" @blur="showSelectIcon" readonly>
                                <template #prefix>
                                    <svg-icon v-if="form.icon" :icon-class="form.icon" class="el-input__icon"
                                        style="height: 32px;width: 16px;" />
                                    <el-icon v-else style="height: 32px;width: 16px;">
                                        <search />
                                    </el-icon>
                                </template>
                            </el-input>
                        </template>
                        <IconSelect ref="iconSelectRef" @selected="selected" :active-icon="form.icon" />
                    </el-popover>
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="显示排序" prop="orderNum">
                    <el-input-number v-model="form.orderNum" :controls="false" :min="0" align="left"
                        style="width: 100%;" />
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="菜单名称" prop="name">
                    <el-input v-model="form.name" placeholder="请输入菜单名称" />
                </el-form-item>
            </el-col>
            <el-col :span="12" v-if="form.menuType == 'C'">
                <el-form-item prop="routeName">
                    <template #label>
                        <span>
                            <el-tooltip
                                content="默认不填则和路由地址相同：如地址为：`user`，则名称为`User`（注意：因为router会删除名称相同路由，为避免名字的冲突，特殊情况下请自定义，保证唯一性）"
                                placement="top">
                                <el-icon><question-filled /></el-icon>
                            </el-tooltip>
                            路由名称
                        </span>
                    </template>
                    <el-input v-model="form.routeName" placeholder="请输入路由名称" />
                </el-form-item>
            </el-col>
            <el-col :span="12" v-if="form.menuType != 'F'">
                <el-form-item>
                    <template #label>
                        <span>
                            <el-tooltip content="选择是外链则路由地址需要以`http(s)://`开头" placement="top">
                                <el-icon><question-filled /></el-icon>
                            </el-tooltip>是否外链
                        </span>
                    </template>
                    <el-radio-group v-model="form.frame">
                        <el-radio :value="1">是</el-radio>
                        <el-radio :value="0">否</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-col>
            <el-col :span="12" v-if="form.menuType != 'F'">
                <el-form-item prop="path">
                    <template #label>
                        <span>
                            <el-tooltip content="访问的路由地址，如：`user`，如外网地址需内链访问则以`http(s)://`开头" placement="top">
                                <el-icon><question-filled /></el-icon>
                            </el-tooltip>
                            路由地址
                        </span>
                    </template>
                    <el-input v-model="form.path" placeholder="请输入路由地址" />
                </el-form-item>
            </el-col>
            <el-col :span="12" v-if="form.menuType == 'C'">
                <el-form-item prop="component">
                    <template #label>
                        <span>
                            <el-tooltip content="访问的组件路径，如：`system/user/index`，默认在`views`目录下" placement="top">
                                <el-icon><question-filled /></el-icon>
                            </el-tooltip>
                            组件路径
                        </span>
                    </template>
                    <el-input v-model="form.component" placeholder="请输入组件路径" />
                </el-form-item>
            </el-col>
            <el-col :span="12" v-if="form.menuType != 'M'">
                <el-form-item>
                    <el-input v-model="form.perms" placeholder="请输入权限标识" maxlength="100" />
                    <template #label>
                        <span>
                            <el-tooltip content="控制器中定义的权限字符，如'system:user:list'" placement="top">
                                <el-icon><question-filled /></el-icon>
                            </el-tooltip>
                            权限字符
                        </span>
                    </template>
                </el-form-item>
            </el-col>
            <el-col :span="12" v-if="form.menuType == 'C'">
                <el-form-item>
                    <el-input v-model="form.query" placeholder="请输入路由参数" maxlength="255" />
                    <template #label>
                        <span>
                            <el-tooltip content='访问路由的默认传递参数，如：`{"id": 1, "name": "ry"}`' placement="top">
                                <el-icon><question-filled /></el-icon>
                            </el-tooltip>
                            路由参数
                        </span>
                    </template>
                </el-form-item>
            </el-col>
            <el-col :span="12" v-if="form.menuType == 'C'">
                <el-form-item>
                    <template #label>
                        <span>
                            <el-tooltip content="选择是则会被`keep-alive`缓存，需要匹配组件的`name`和地址保持一致" placement="top">
                                <el-icon><question-filled /></el-icon>
                            </el-tooltip>
                            是否缓存
                        </span>
                    </template>
                    <el-radio-group v-model="form.cache">
                        <el-radio :value="1">缓存</el-radio>
                        <el-radio :value="0">不缓存</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-col>
            <el-col :span="12" v-if="form.menuType != 'F'">
                <el-form-item>
                    <template #label>
                        <span>
                            <el-tooltip content="选择隐藏则路由将不会出现在侧边栏，但仍然可以访问" placement="top">
                                <el-icon><question-filled /></el-icon>
                            </el-tooltip>
                            显示状态
                        </span>
                    </template>
                    <el-radio-group v-model="form.hidden">
                        <el-radio v-for="dict in sys_show_hide" :key="dict.value" :value="Number(dict.value)">{{
                            dict.label
                            }}</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item>
                    <template #label>
                        <span>
                            <el-tooltip content="选择停用则路由将不会出现在侧边栏，也不能被访问" placement="top">
                                <el-icon><question-filled /></el-icon>
                            </el-tooltip>
                            菜单状态
                        </span>
                    </template>
                    <el-radio-group v-model="form.status">
                        <el-radio v-for="dict in sys_normal_disable" :key="dict.value" :value="Number(dict.value)">{{
                            dict.label
                        }}</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-col>
        </el-row>
    </BaseForm>
</template>
<script lang="ts" setup>
import { addMenu, getMenuById, getMenuList, updateMenu } from '@/api/system/menu';
import BaseForm from '@/components/form/BaseForm.vue';
import IconSelect from '@/components/IconSelect/IconSelect.vue';
import useDictStore, { dictTypeConfig } from '@/store/modules/dict';
import { handleTree } from '@/utils';
import { onMounted, ref } from 'vue';

const title = ref('菜单')
const dictStore = useDictStore() 

const formRef = ref()
const iconSelectRef = ref() 
const form = ref(init())
const rules = ref({
    name: [{ required: true, message: "菜单名称不能为空", trigger: "blur" }],
    orderNum: [{ required: true, message: "菜单顺序不能为空", trigger: "blur" }],
    path: [{ required: true, message: "路由地址不能为空", trigger: "blur" }]
})

function init() {
    return {
        menuId: '',
        name: '',
        parentId: 0,
        path: '',
        routeName: '',
        frame: 0,
        component: '',
        hidden: 0,
        icon: '',
        menuType: 'M',
        perms: '',
        query: '',
        remark: '',
        status: 1,
        orderNum: 1,
        cache: 1
    }
}

const menuOptions = ref([])
const sys_show_hide = ref([])
const sys_normal_disable = ref([])

const emit = defineEmits(['add', 'update'])

function getTreeSelect() {
    menuOptions.value = []
    getMenuList().then(res => {
        const menu = { menuId: '0', name: '主类目', children: [] }
        menu.children = handleTree(res.data, 'menuId')
        menuOptions.value.push(menu)
    })
}


/** 展示下拉图标 */
function showSelectIcon() {
    iconSelectRef.value.reset()
}
/** 选中图标 */
function selected(name) {
    form.value.icon = name
}

function openForm(menuId, opp = 'add') {
    if (opp == 'add') {
        if (menuId) {
            form.value.parentId = menuId
        }
        formRef.value.openForm()
    } else if (opp == 'update') {
        formRef.value.openForm(menuId)
    }

}

function resetForm(){
    form.value = init()
}

onMounted(() => {
    // 加载菜单下拉
    getTreeSelect()

    dictStore.getDictData(dictTypeConfig.sys_show_hide).then(data => {
        sys_show_hide.value = data
    })
    dictStore.getDictData(dictTypeConfig.sys_normal_disable).then(data => {
        sys_normal_disable.value = data
    })
})

defineExpose({ openForm })

</script>