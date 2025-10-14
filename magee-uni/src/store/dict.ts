import { getAllDictData } from "@/api/system/dict";
import { defineStore } from "pinia";

const useDictStore = defineStore('dict', {
    state: () => ({
        dict: [],
        dictType: []
    }),
    actions: {
        getDict(dictType){
            return new Promise((resolve) => {
				if (this.dict.length == 0) {
					this.init().then(data => {
						let arr = data.filter(item => item.dictType == dictType)
						arr = arr.map(item => ({ label: item.dictLabel, value: item.dictValue }))
						resolve(arr)
					})
				} else {
					let arr = this.dict.filter(item => item.dictType == dictType)
					arr = arr.map(item => ({ label: item.dictLabel, value: item.dictValue }))
					resolve(arr)
				}
			})
        },
        init(){
            return getAllDictData().then( (res: any) => {
				this.dict = res.data || []
				return this.dict
			})
        }
    }
})

export default useDictStore