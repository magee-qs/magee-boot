/**
 * tabel组件操作  
 */ 
export default {
    /** table选中ids  多选 */
    getSelectedKeys(selection, key = 'id') {
        let keys = []
        if (selection) {
            keys = selection.map(item => item[key])
        }
        return keys || []
    },
    /** table选中id 单选 */
    getSelectedKey(selection, key = 'id') {
        let keys = []
        if (selection) {
            keys = selection.map(item => item[key])
        }
        if (keys && keys.length > 0) {
            return keys[0]
        } else {
            return null
        }
    },
    getOrderColumn(row) {
        return ([{
            column: row.prop,
            asc: row.order === 'ascending'
        }])
    }

}
 

 
