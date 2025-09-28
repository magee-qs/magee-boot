<template>
  <div class="app-container home">
    <el-row>
      <el-col :span="24"> <h2>magee-framework快速开发框架</h2></el-col>
     <el-col :span="24"> <h3> magee-framework是一套自带权限管理的快速开发框架,用于企业管理系统快速开发</h3></el-col>
      <el-col :span="24" style="margin-left: 20px; line-height: 24px;">
      * 前端采用Vue3、Element-Plus <br />
      * 后端采用Spring Boot、shiro、Redis & Jwt、mybatisPlus<br />
      * shiro权限控制,可以手动注释@Anonymous忽略权限控制<br />
      * 支持动态权限菜单，多种权限控制（用户+ 角色 + 数据访问权限 ）<br />
      * 支持复杂sql动态查询,支持单表查询、多表连接查询，支持多字段排序,避免手写if语句, 三行代码实现复杂查询<br />
      * 数据字典字段自动映射,减少前后端大量条件判断<br />
      * 高效率开发，使用代码生成器一键生成前后端代码<br />
      * excel自动导入导出<br />
      * 敏感字段过滤，支持密码、银行卡、身份证、用户名等多种方式<br />

      内置功能<br />
      1. 用户管理：用户是系统操作者，该功能主要完成系统用户配置、用户权限配置<br />
      2. 部门管理：配置系统组织机构（公司、部门、小组），树结构展现支持数据权限<br />
      3. 岗位管理：配置系统用户所属担任职务<br />
      4. 菜单管理：配置系统菜单，操作权限，按钮权限标识等<br />
      5. 角色管理：角色菜单权限分配、设置角色按机构进行数据范围权限划分<br />
      6. 字典管理：对系统中经常使用的一些较为固定的数据进行维护<br />
      7. 参数管理：对系统动态配置常用参数 <br />
      8. 通知公告：系统通知公告信息发布维护<br />
      9. 操作日志：系统正常操作日志记录和查询；系统异常信息日志记录和查询<br />
      10. 定时任务：在线（添加、修改、删除）任务调度包含执行结果日志<br />
      11. 代码生成：前后端代码的生成 <br />
      12. 系统接口：根据业务代码自动生成相关的api接口文档<br />
      13. 服务监控：监视当前系统CPU、内存、磁盘、堆栈等相关信息<br />
      14. 缓存监控：对系统的缓存信息查询，命令统计等<br />
      15. 连接池监视：监视当前系统数据库连接池状态，可进行分析SQL找出系统性能瓶颈<br />
      </el-col> 
    </el-row>
    <el-row>
      <el-button type="primary" icon="Cloudy" plain style="margin-top: 8px;"
          @click="goTarget('https://gitee.com/magee-qs/magee-boot')">访问码云</el-button>
    </el-row> 
  </div>
</template>
<script lang="ts" setup>
  
function goTarget(url) {
  window.open(url, '__blank')
}
</script>

<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }

  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }

  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans",
  "Helvetica Neue",
  Helvetica,
  Arial,
  sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>
