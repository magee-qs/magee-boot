package com.magee.monitor.controller;

import com.alibaba.fastjson.JSON;
import com.magee.common.constant.CacheConstant;
import com.magee.framework.core.vo.AjaxResult;
import com.magee.monitor.domain.SysCache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 功能描述: 缓存监控
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@RestController
@RequestMapping("/monitor/cache")
@Api(tags = "缓存监控")
public class CacheController {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    private final static List<SysCache> caches = new ArrayList<SysCache>();

    {
        caches.add(new SysCache(CacheConstant.SYS_USERINFO_CACHE, "用户信息"));
        caches.add(new SysCache(CacheConstant.SYS_CONFIG_KEY, "配置信息"));
        caches.add(new SysCache(CacheConstant.SYS_DICT_CACHE, "数据字典"));
        caches.add(new SysCache(CacheConstant.SYS_CAPTCHA_CODE_KEY, "验证码"));
        caches.add(new SysCache(CacheConstant.SYS_DEPART_CACHE, "防重提交"));
        caches.add(new SysCache(CacheConstant.SYS_MENU_CACHE, "限流处理"));
        caches.add(new SysCache(CacheConstant.PWD_ERR_CNT_KEY, "密码错误次数"));
    }

    @ApiOperation("缓存信息")
    @GetMapping("/list")
    @RequiresPermissions("monitor:cache:query")
    public AjaxResult getInfo(){
        Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info());
        Properties commandStats = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info("commandstats"));
        Object dbSize = redisTemplate.execute((RedisCallback<Object>) connection -> connection.dbSize());

        Map<String, Object> result = new HashMap<>(3);
        result.put("info", info);
        result.put("dbSize", dbSize);

        List<Map<String, String>> pieList = new ArrayList<>();
        commandStats.stringPropertyNames().forEach(key -> {
            Map<String, String> data = new HashMap<>(2);
            String property = commandStats.getProperty(key);
            data.put("name", StringUtils.removeStart(key, "cmdstat_"));
            data.put("value", StringUtils.substringBetween(property, "calls=", ",usec"));
            pieList.add(data);
        });
        result.put("commandStats", pieList);
        return AjaxResult.ok(result);
    }

    @ApiOperation("查询缓存")
    @GetMapping("/getNames")
    @RequiresPermissions("monitor:cache:query")
    public AjaxResult getNames(){
        return AjaxResult.ok(caches );
    }

    @ApiOperation("查询缓存")
    @GetMapping("/getKeys/{cacheName}")
    @RequiresPermissions("monitor:cache:query")
    public AjaxResult getCacheKeys(@PathVariable String cacheName){
        Set<String> cacheKeys = redisTemplate.keys(cacheName+"*");
        return AjaxResult.ok(new TreeSet<>(cacheKeys));
    }

    @ApiOperation("查询缓存")
    @GetMapping("/getValue/{cacheName}/{cacheKey}")
    @RequiresPermissions("monitor:cache:query")
    public AjaxResult getCacheValue(@PathVariable String cacheName, @PathVariable String cacheKey){
        Object cacheValue =  redisTemplate.opsForValue().get(cacheKey);
        String content = StringUtils.EMPTY;

        if(cacheValue  != null){
            if(cacheValue instanceof String){
                content = (String) cacheValue;
            }else{
                content  = JSON.toJSONString(cacheValue);
            }
        }
        SysCache sysCache = new SysCache(cacheName, cacheKey, content);
        return AjaxResult.ok(sysCache);
    }
    @ApiOperation("清除缓存")
    @GetMapping("/clearCacheName/{cacheName}")
    @RequiresPermissions("monitor:cache:remove")
    public AjaxResult clearCacheName(@PathVariable String cacheName)
    {
        Collection<String> cacheKeys = redisTemplate.keys(cacheName + "*");
        redisTemplate.delete(cacheKeys);
        return AjaxResult.ok();
    }

    @ApiOperation("清除缓存")
    @GetMapping("/clearCacheKey/{cacheKey}")
    @RequiresPermissions("monitor:cache:remove")
    public AjaxResult clearCacheKey(@PathVariable String cacheKey)
    {
        redisTemplate.delete(cacheKey);
        return AjaxResult.ok();
    }

    @ApiOperation("清除缓存")
    @GetMapping("/clearCacheAll")
    @RequiresPermissions("monitor:cache:remove")
    public AjaxResult clearCacheAll()  {
        Collection<String> cacheKeys = redisTemplate.keys("*");
        redisTemplate.delete(cacheKeys);
        return AjaxResult.ok();
    }
}
