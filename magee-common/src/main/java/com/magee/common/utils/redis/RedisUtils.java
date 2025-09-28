package com.magee.common.utils.redis;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * RedisCache工具类
 * @author magee
 * @version 1.0
 */
@Slf4j
@Component
public class RedisUtils {

    @Resource
    private  RedisTemplate<String,Object> redisTemplate ;


    private static RedisTemplate<String,Object> template;

    @PostConstruct
    public void init(){
        template = redisTemplate;
    }
    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     */
    public static boolean expire(String key, long time) {
        try {
            if (time > 0) {
                template.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
           log.error("操作redis键{}失败:{}",key, e.getMessage());
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public static long getExpire(String key) {
        return template.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public static boolean hasKey(String key) {
        try {
            return template.hasKey(key);
        } catch (Exception e) {
            log.error("操作redis键{}失败:",key, e.getMessage());
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    public static void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                template.delete(key[0]);
            } else {
                template.delete(Arrays.asList(key));
            }
        }
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public static Object get(String key) {
        return key == null ? null :  template.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public static boolean set(String key, Object value) {
        try {
            template.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("写入redis缓存key:{} value: {}失败:{}",key, value,e.getMessage());
            return false;
        }

    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public static boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                template.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("写入redis缓存key:{} value: {},失败:{}",key, value, e.getMessage());
            return false;
        }
    }

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public static Object hashGet(String key, String item){
        return template.opsForHash().get(key, item);
    }
    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public static Map<Object,Object> hashGet(String key){
        return template.opsForHash().entries(key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public static boolean hashSet(String key,Map<String,Object> map){
        try {
            template.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error("写入hash缓存key:{} value: {} ,失败:{}",key, map, e.getMessage());
            return false;
        }
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @param time 时间(秒)
     * @return true 成功 false 失败
     */
    public static boolean hashSet(String key,Map<String,Object> map, long time){
        try {
            template.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("写入hash缓存key:{} value: {} ,失败:{}",key, map, e.getMessage());
            return false;
        }
    }


    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public static boolean hashSet(String key, String item, Object value) {
        try {
            template.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            log.error("写入hash缓存key:{} value: {} ,失败:{}",key, value, e.getMessage());
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public static boolean hashSet(String key, String item, Object value, long time) {
        try {
            template.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("写入hash缓存key:{} value: {},失败:{}",key, value, e.getMessage());
            return false;
        }
    }


    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public static void hashDel(String key, Object... item) {
        template.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public static boolean hashHasKey(String key, String item) {
        return template.opsForHash().hasKey(key, item);
    }

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public static Set<Object> setGet(String key) {
        try {
            return template.opsForSet().members(key);
        } catch (Exception e) {
            log.error("写入set缓存key:{}",key);
            return null;
        }
    }
    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public static long setSet(String key, Object... values) {
        try {
            return template.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error("写入set缓存key:{},values:{}, 失败: {} ",key, values , e.getMessage());
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public static long setSet(String key, long time, Object... values) {
        try {
            Long count = template.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            log.error("写入set缓存key:{},values:{} , 失败: {}",key, values, e.getMessage());
            return 0;
        }
    }
    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public static  long getSetSize(String key) {
        try {
            return template.opsForSet().size(key);
        } catch (Exception e) {
            log.error("读取set缓存key:{}, 失败: {}",key, e.getMessage());
            return 0;
        }
    }
    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public static long setRemove(String key, Object... values) {
        try {
            Long count = template.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            log.error("删除set缓存key:{}, 失败: {}",key, e.getMessage());
            return 0;
        }
    }

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1 代表所有值
     * @return
     */
    public static List<Object> listGet(String key, long start, long end) {
        try {
            return template.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error("读取list缓存key:{}, start:{},end:{}, 失败: {}",key ,start, end, e.getMessage());
            return null;
        }
    }
    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public static Object listGet(String key, long index) {
        try {
            return template.opsForList().index(key, index);
        } catch (Exception e) {
            log.error("读取list缓存key:{}, index:{},失败: {}",key, index, e.getMessage());
            return null;
        }
    }
    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public static long getListSize(String key) {
        try {
            return template.opsForList().size(key);
        } catch (Exception e) {
            log.error("读取list缓存key:{}, 失败: {}",key, e.getMessage());
            return 0;
        }
    }
    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public static boolean rightPush(String key, Object value) {
        try {
            template.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            log.error("写入list缓存key:{}, 失败: {}",key , e.getMessage());
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public static boolean rightPush(String key, Object value, long time) {
        try {
            template.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("写入list缓存key:{}, 失败: {}",key, e.getMessage());
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public static boolean rightPush(String key, List<Object> value) {
        try {
            template.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            log.error("写入list缓存key:{},value:{}, 失败: {}",key,value , e.getMessage());
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public static boolean rightPush(String key, List<Object> value, long time) {
        try {
            template.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("写入list缓存key:{},value:{} , 失败: {}",key,value, e.getMessage());
            return false;
        }
    }
    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public static boolean listUpdate(String key,long index, Object value){
        try {
            template.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error("写入list缓存key:{},index:{},value:{} ,失败:{}",key, index,value, e.getMessage());
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public static long listRemove(String key, long count, Object value) {
        try {
            Long remove = template.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            log.error("删除list缓存key:{},count:{},value:{}, 失败: {}",key, count,value, e.getMessage());
            return 0;
        }
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public static Collection<String> keys(String pattern) {
        return template.keys(pattern);
    }

}
