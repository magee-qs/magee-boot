import { isArray } from "@/utils/validate" 

export default {
    /** 展开/收缩下拉树*/
    expandAll(tree, state) {
        // 所有节点映射
        const nodes = tree.store.nodesMap
        Object.keys(nodes).forEach(key => {
            nodes[key].expanded = state
        })
    },
    /** 从选项树中筛选出 checked  和 halfChecked  */
    setCheckedKeys(tree, keys, keyName) { 
        const keySet = new Set(keys)
        const keySetChecked = new Set()
        const keySetHalf = new Set()
        // 深度遍历
        function dfs(node) { 
            let newNode   = [...node] 
            for (let i = 0; i < newNode.length; i++) {
                let cNode = newNode[i]
                if (keySet.has(cNode[keyName])) {
                    if (cNode.children && cNode.children.length > 0) {
                        keySetHalf.add(cNode[keyName])
                    } else {
                        keySetChecked.add(cNode[keyName])
                    }
                }
                // 遍历子节点
                if (cNode.children && cNode.children.length > 0) {
                     dfs(cNode.children)
                }
            } 
        }
        // 遍历数据
        dfs(tree)
        return [keySetChecked, keySetHalf]
    }
}

