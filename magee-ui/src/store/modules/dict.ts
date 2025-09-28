
import { getAllDictData } from "@/api/system/dict";
import { defineStore } from "pinia";

/** 数据字典 
 * {
 *  'sys_data_scope': []
 *  ...
 * }
 * 
*/
const useDictStore = defineStore('dict', {
    state: () => ({
        dict: [],
        dictType: []
    }),
    actions: {
        /** 获取数据
         * @param dictType  sys_data_scope
         */
        getDictData(dictType) : Promise<Array<Object>> {
            return new Promise((resovle) => {
                if (this.dict.length == 0) { 
                    this.init().then(data => {
                        let arr = data.filter(item => item.dictType == dictType)
                        arr = arr.map(item => ({ label: item.dictLabel, value: item.dictValue }))
                        resovle(arr)
                    }) 
                } else {
                    let arr = this.dict.filter(item => item.dictType == dictType)
                    arr =  arr.map(item => ({ label: item.dictLabel, value: item.dictValue }))
                    resovle(arr)
                }
            }) 
        },
        init() {
            return getAllDictData().then(res => {
                this.dict = res.data || []
                return this.dict
            })
        }
    }
})

export default useDictStore



/** dict工具 */
export const dictTypeConfig = {

    /** 用户性别 */
    sys_user_sex: 'sys_user_sex',

    /** 菜单状态 */
    sys_show_hide: 'sys_show_hide',

    /** 系统开关 */
    sys_normal_disable: 'sys_normal_disable',

    /** 任务状态 */
    sys_job_status: 'sys_job_status',

    /** 任务分组 */
    sys_job_group: 'sys_job_group',

    /** 系统是否 */
    sys_yes_no: 'sys_yes_no',

    /** 通知类型 */
    sys_notice_type: 'sys_notice_type',

    /** 通知状态 */
    sys_notice_status: 'sys_notice_status',

    /** 操作类型 */
    sys_oper_type: 'sys_oper_type',

    /** 系统状态 */
    sys_common_status: 'sys_common_status',

    /** 数据访问 */
    sys_data_scope: 'sys_data_scope'
}


