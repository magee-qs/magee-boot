 
import { defineStore } from "pinia";

const useConfigStore = defineStore('config', {
    state: () => ({
        items: []
    }),
    actions: {
        getConfig(key) {
            return new Promise((resolve) => {
                resolve('')
            })
        }
    }
})

export default useConfigStore