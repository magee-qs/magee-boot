package com.magee.common.utils;


import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.*;

/**
 * 线程相关工具类
 * */
@Slf4j
public class ThreadUtils extends org.apache.commons.lang3.ThreadUtils {

    /**
     * sleep等待,单位为毫秒
     */
    public static void sleep(long millis){
        try {
            sleep(Duration.ofMillis(millis));

        }catch (InterruptedException e){
            log.error("thread sleep error:{}", e.getMessage());
            return ;
        }
    }

    /**
     * 停止线程池
     * 先使用shutdown, 停止接收新任务并尝试完成所有已存在任务
     * 如果超时, 则调用shutdownNow, 取消在workQueue中Pending的任务,并中断所有阻塞函数.
     * */
    public static void shutDown(ExecutorService pool){
        if (pool != null && !pool.isShutdown()){
            pool.shutdown();
            try{
                if(!pool.awaitTermination(120, TimeUnit.SECONDS)){
                    pool.shutdown();
                    pool.shutdownNow();
                    if (!pool.awaitTermination(120, TimeUnit.SECONDS))
                    {
                        log.info("Pool did not terminate");
                    }
                }
            }catch (InterruptedException e){
                pool.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 打印线程异常信息
     * */
    public static void printException(Runnable runnable, Throwable throwable){
        if(throwable == null && runnable instanceof Future<?>){
            try
            {
                Future<?> future = (Future<?>) runnable;
                if (future.isDone())
                {
                    future.get();
                }
            }
            catch (CancellationException ce)
            {
                throwable = ce;
            }
            catch (ExecutionException ee)
            {
                throwable = ee.getCause();
            }
            catch (InterruptedException ie)
            {
                Thread.currentThread().interrupt();
            }
        }
        if(throwable != null){
            log.error(throwable.getMessage(), throwable);
        }
    }
}
