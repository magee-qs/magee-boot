import setting from "@/setting";
import useSettingStore from "@/store/modules/setting";

/**
 * 动态修改标题
 */
export function useDynamicTitle(){
    const settingStore = useSettingStore()
    if(settingStore.dynamicTitle){
        document.title = settingStore.title + ' - ' + setting.title
    }else{
        document.title =  setting.title
    }
}