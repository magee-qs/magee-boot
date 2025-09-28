import vue from '@vitejs/plugin-vue'
 
import createSvgIcon from './svgIcon'
import createCompression from './compression'
import createSetupExtend from './setupExtend'

export default function createVitePlugins(viteEnv:any, isBuild = false) {
    const vitePlugins: any = [vue()] 
	vitePlugins.push(createSetupExtend())
    vitePlugins.push(createSvgIcon(isBuild))
	isBuild && vitePlugins.push(...createCompression(viteEnv))
  
    return vitePlugins
}
