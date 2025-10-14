import { defineConfig } from "vite";
import uni from "@dcloudio/vite-plugin-uni";
import path from 'path'
// https://vitejs.dev/config/
export default defineConfig({
  plugins: [uni()],
  resolve: {
     alias: {
        // 设置路径
        '~/': path.resolve(__dirname, './'),
        // 设置别名
        '@': path.resolve(__dirname, './src')
     }
  },
  modules: { 
     rules: [
      {
        test: /\.scss$/,
        use: [
          'sass-loader',
          {
            loader: 'sass-loader',
            options: {
              sassOptions: {
                silentDeprecation: true // 隐藏警告
              }
            }
          }
        ]
      }
    ]
  }
});
