import { createSSRApp } from "vue";
import App from "./App.vue";
import store from "./store";
import { setupPermission } from "./permission";



export function createApp() {
  const app = createSSRApp(App);

  app.use(store)

  setupPermission()
  return {
    app,
  };
}

 

