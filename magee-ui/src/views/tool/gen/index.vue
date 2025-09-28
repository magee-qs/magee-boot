<template>
    <div class="app-container">
        <el-row :gutter="20">
            <el-col :span="6">
                <el-button type="primary" plain class="mb8" icon="Refresh" @click="getList">刷新</el-button>
                <el-table :data="table" style="width: 100%; height:100%" border @cell-click="handleSelect">
                    <el-table-column prop="tableName" label="数据库表" />
                    <el-table-column prop="tableComment" label="表注释" />
                </el-table>
            </el-col>
            <el-col :span="18">
                <el-form :model="form" :rules="rules" ref="formRef" :inline="true" label-width="80px">
                    <el-row>
                        <el-col :span="12">
                            <el-form-item label="表名" prop="name">
                                <el-input v-model="form.name" placeholder="请输入表名" />
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="表注释" prop="comment">
                                <el-input v-model="form.comment" placeholder="请输入表名" />
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="实体类" prop="entity">
                                <el-input v-model="form.entity" placeholder="请输入实体类名" />
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="包名" prop="packageName">
                                <el-input v-model="form.packageName" placeholder="请输入包名" />
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="接口" prop="url">
                                <el-input v-model="form.url" placeholder="请输入接口" />
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="    ">
                                <el-button type="primary" @click="handleGen">生成代码</el-button>
                                <el-button type="primary" @click="handleSave">保存参数</el-button>
                            </el-form-item>
                        </el-col>
                    </el-row>



                </el-form>

                <el-tabs v-model="activeName" type="border-card">
                    <el-tab-pane label="entity" name="entity">
                        <el-input v-model="code.entity" type="textarea" :rows="20" />
                        <el-button type="primary" style="margin-top: 8px;" @click="handelCopy('entity')"><el-icon>
                                <CopyDocument />
                            </el-icon>复制</el-button>
                    </el-tab-pane>
                    <el-tab-pane label="mapper" name="mapper">
                        <el-input v-model="code.mapper" type="textarea" :rows="20" />
                        <el-button type="primary" style="margin-top: 8px;" @click="handelCopy('mapper')"><el-icon>
                                <CopyDocument />
                            </el-icon>复制</el-button>
                    </el-tab-pane>
                    <el-tab-pane label="xml" name="xml">
                        <el-input v-model="code.xml" type="textarea" :rows="20" />
                        <el-button type="primary" style="margin-top: 8px;" @click="handelCopy('xml')"><el-icon>
                                <CopyDocument />
                            </el-icon>复制</el-button>
                    </el-tab-pane>
                    <el-tab-pane label="iservice" name="iservice">
                        <el-input v-model="code.iservice" type="textarea" :rows="20" />
                        <el-button type="primary" style="margin-top: 8px;" @click="handelCopy('iservice')"><el-icon>
                                <CopyDocument />
                            </el-icon>复制</el-button>
                    </el-tab-pane>
                    <el-tab-pane label="service" name="service">
                        <el-input v-model="code.service" type="textarea" :rows="20" />
                        <el-button type="primary" style="margin-top: 8px;" @click="handelCopy('service')"><el-icon>
                                <CopyDocument />
                            </el-icon>复制</el-button>
                    </el-tab-pane>
                    <el-tab-pane label="controller" name="controller">
                        <el-input v-model="code.controller" type="textarea" :rows="20" />
                        <el-button type="primary" style="margin-top: 8px;" @click="handelCopy('controller')"><el-icon>
                                <CopyDocument />
                            </el-icon>复制</el-button>
                    </el-tab-pane>
                    <el-tab-pane label="list" name="list">
                        <el-input v-model="code.list" type="textarea" :rows="20" />
                        <el-button type="primary" style="margin-top: 8px;" @click="handelCopy('list')"><el-icon>
                                <CopyDocument />
                            </el-icon>复制</el-button>
                    </el-tab-pane>
                    <el-tab-pane label="form" name="form">
                        <el-input v-model="code.form" type="textarea" :rows="20" />
                        <el-button type="primary" style="margin-top: 8px;" @click="handelCopy('form')"><el-icon>
                                <CopyDocument />
                            </el-icon>复制</el-button>
                    </el-tab-pane>
                    <el-tab-pane label="api" name="api">
                        <el-input v-model="code.api" type="textarea" :rows="20" />
                        <el-button type="primary" style="margin-top: 8px;" @click="handelCopy('api')"><el-icon>
                                <CopyDocument />
                            </el-icon>复制</el-button>
                    </el-tab-pane>
                </el-tabs>
            </el-col>
        </el-row>
    </div>

</template>
<script lang="ts" setup>
import { genCode, getTableList } from '@/api/tool/gen';
import { localCache } from '@/hooks/cache';
import useMessage from '@/hooks/message';
import { onMounted, ref } from 'vue';

const table = ref([])

const formRef = ref()
const form = ref(init())


function init() {
    let _form = {
        name: '',
        entity: '',
        comment: '',
        packageName: 'com.magee.system',
        url: '/sys/gen'
    }
    let data = localCache.getJSON('genConfig')
    if (data) {
        Object.assign(_form, data)
    }
    return _form
}
const rules = {
    name: [{ required: true, message: "表名不能为空", trigger: "blur" }],
    module: [{ required: true, message: "模块不能为空", trigger: "blur" }],
    packageName: [{ required: true, message: "包名不能为空", trigger: "blur" }],
    url: [{ required: true, message: "接口不能为空", trigger: "blur" }]
}

const activeName = ref('entity')
const code = ref({
    entity: '',
    mapper: '',
    xml: '',
    iservice: '',
    service: '',
    controller: '',
    list: '',
    form: '',
    api: ''
})

function handleSelect(row) {
    form.value.name = row.tableName
    form.value.entity = row.tableName
    form.value.comment = row.tableComment
}

function getList() {
    getTableList().then(res => {
        table.value = res.data
    })
}


function handleGen() {
    formRef.value.validate(valid => {
        if (valid) {
            genCode(form.value).then(res => {
                code.value = res.data
            })
        }
    })
}
async function handelCopy(key) {
    let content = code.value[key]
    await navigator.clipboard.writeText(content)

    useMessage.success('复制成功')
}

function handleSave() {
    localCache.setJSON('genConfig', form.value)
}
onMounted(() => {
    getList()
})
</script>