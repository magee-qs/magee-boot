package com.magee.monitor.controller;

import com.magee.framework.core.domain.Server;
import com.magee.framework.core.vo.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能描述: 服务器监控
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Api(tags = "服务监控")
@RestController
@RequestMapping("/monitor")
public class ServerController {

    @GetMapping("/server")
    @RequiresPermissions("monitor:server:query")
    @ApiOperation("获取服务信息")
    public AjaxResult getInfo() throws Exception {
        Server server = new Server();
        server.copyTo();
        return AjaxResult.ok(server);
    }
}
