<!-- 权限配置表单 -->
<template>
    <el-dialog v-model="dialogVisible" :title="title" width="500" append-to-body :close-on-click-modal="false"
        destroy-on-close>
        <div class="form-container">
            <el-form ref="form">
                <el-checkbox v-model="menuExpand" @change="handleCheckedExpand">展开/折叠</el-checkbox>
                <el-tree class="tree-wrapper" :data="menus" show-checkbox node-key="menuId" empty-text="加载中，请稍候"
                    :props="{ label: 'label', children: 'children' }" ref="menuRef" :checkStrictly="checkStrictly"
                    />
            </el-form>
        </div>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="cancel">取 消</el-button>
                <el-button type="primary" @click="submit"> 确 定</el-button>
            </div>
        </template>
    </el-dialog>
</template>
<script lang="ts" setup>
import { getItemPermission, menuTreeSelect, saveItemPermission } from '@/api/system/menu';
import useMessage from '@/hooks/message';
import useTree from '@/hooks/useTree';
import { reactive, ref } from 'vue';
defineProps({
    title: {
        type: String,
        default: '权限配置'
    },
    checkStrictly: {
        default: false
    }
})

const dialogVisible = ref(false)
const menuExpand = ref(false)
const menuRef = ref()
 
const menus = ref([])
const form = reactive({
    itemId: undefined,
    itemType: undefined,
    permIds: []
})

/** 权限表单 
 * @param itemId 权限对象   
 * @param itemType 权限类型 Role, User, UserGroup, Depart
 * */
function open(itemId, itemType) {
    dialogVisible.value = true
    form.itemId = itemId
    form.itemType = itemType

    // 加载下拉菜单
    menuTreeSelect().then(res => {
        menus.value = []
        if (res.data) {
            menus.value  = res.data.map(item => ({
                menuId: item.menuId,
                label: item.label,
                children: item.children,
                menuType: item.menuType
            }))

            // 加载选中的项
            getItemPermission(itemId).then(res => {
                let items = []
                // 遍历菜单
                if (res.data) {
                    items = res.data.map(c => c.menuId)
                }  
                // 设定(父节点默认不能选中)
                let data = useTree.setCheckedKeys(menus.value,items , 'menuId')  
                console.log('checkedKeys:', data[0])
                menuRef.value.setCheckedKeys(data[0]) 
            })
        } else {
            menus.value = []
        }
    })
}

function handleCheckedExpand() {
    useTree.expandAll(menuRef.value, menuExpand.value)
}

function cancel() {
    dialogVisible.value = false
}

function submit() {
    const checkIds = menuRef.value.getCheckedKeys()
    const halfIds = menuRef.value.getHalfCheckedKeys()
    form.permIds = [...halfIds, ...checkIds]
    console.log(form)
    saveItemPermission(form.itemType, form.itemId, form.permIds).then(() => {
        dialogVisible.value = false
        useMessage.success("操作成功")
    })
}

defineExpose({ open })
</script>

<style scoped lang="scss">
.form-container {
    height: 450px;
}

.tree-wrapper {
    /* 最低 300 px，可视区域高时可继续往上撑 */
    min-height: 300px;
    /* 最高 400 px，超过就出现滚动条 */
    max-height: 400px;
    overflow: auto;
    /* 自动滚动 */
    border: 1px solid #e4e7ed;
    /* 与 el-tree 默认边框同色，可去掉 */
}
</style>