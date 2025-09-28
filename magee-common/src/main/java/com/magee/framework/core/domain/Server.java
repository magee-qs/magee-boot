package com.magee.framework.core.domain;

import com.magee.common.utils.NumberUtils;
import com.magee.common.utils.ServletUtils;
import com.magee.common.utils.StringUtils;
import com.magee.common.utils.ip.IpUtils;
import com.magee.common.utils.text.StrFormatter;
import com.magee.framework.core.domain.server.*;
import lombok.Data;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

import java.lang.management.ManagementFactory;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * 功能描述: 服务器相关信息
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Data
public class Server {
    private static final  int OSHI_WAIT_SECOND = 1000;

    /**
     * CPU相关信息
     */
    private Cpu cpu = new Cpu();

    /**
     * 內存相关信息
     */
    private Mem mem = new Mem();

    /**
     * JVM相关信息
     */
    private Jvm jvm = new Jvm();

    /**
     * 服务器相关信息
     */
    private Sys sys = new Sys();

    /**
     * 磁盘相关信息
     */
    private List<SysFile> sysFiles = new LinkedList<SysFile>();

    public void copyTo() throws Exception {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();

        setCpuInfo(hal.getProcessor());

        setMemInfo(hal.getMemory());

        setSysInfo();

        setJvmInfo();

        setSysFiles(si.getOperatingSystem());
    }

    /**
     * 设置CPU信息
     */
    private void setCpuInfo(CentralProcessor processor) throws InterruptedException {
        // CPU信息
        /* 1. 核心数量 */
        int logicalCores = processor.getLogicalProcessorCount();

        /* 2. 必须前后两次采样，才能算出百分比 */
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Thread.sleep(OSHI_WAIT_SECOND);                 // 间隔 1 秒
        long[] usage = processor.getSystemCpuLoadTicks( ); // 返回 7 维数组

        /* 3. 转换百分比 */
        long total = 0;
        for (long t : usage) total += t;
        double user  = usage[CentralProcessor.TickType.USER.getIndex()] * 100d / total;
        double sys   = usage[CentralProcessor.TickType.SYSTEM.getIndex()] * 100d / total;
        double idle  = usage[CentralProcessor.TickType.IDLE.getIndex()] * 100d / total;


        cpu.setCpuNum(logicalCores);
        cpu.setSys(sys );
        cpu.setUsed(user);
        cpu.setFree(idle);
    }

    /**
     * 设置内存信息
     */
    private void setMemInfo(GlobalMemory memory)
    {
        mem.setTotal(memory.getTotal());
        mem.setUsed(memory.getTotal() - memory.getAvailable());
        mem.setFree(memory.getAvailable());

        mem.setUsage(0);
        if(mem.getUsed() > 0){
            mem.setUsage(mem.getUsed()/ mem.getTotal());
        }

    }

    /**
     * 设置服务器信息
     */
    private void setSysInfo()
    {
        Properties props = System.getProperties();
        sys.setComputerName(IpUtils.getHostName());
        sys.setComputerIp(IpUtils.getIpAddress(ServletUtils.getRequest()));
        sys.setOsName(props.getProperty("os.name"));
        sys.setOsArch(props.getProperty("os.arch"));
        sys.setUserDir(props.getProperty("user.dir"));
    }

    /**
     * 设置Java虚拟机
     */
    private void setJvmInfo() throws UnknownHostException
    {
        Properties props = System.getProperties();
        jvm.setTotal(Runtime.getRuntime().totalMemory());
        jvm.setMax(Runtime.getRuntime().maxMemory());
        jvm.setFree(Runtime.getRuntime().freeMemory());
        jvm.setUsed(jvm.getTotal() - jvm.getFree());
        jvm.setVersion(props.getProperty("java.version"));
        jvm.setHome(props.getProperty("java.home"));

        jvm.setUsage(0);
        if(jvm.getUsed() > 0){
            jvm.setUsage(jvm.getUsed()/ jvm.getTotal());
        }


        long startTimeMs = ManagementFactory.getRuntimeMXBean().getStartTime();
        String start = Instant.ofEpochMilli(startTimeMs)
                .atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        jvm.setStart(start);

        long uptimeMs = ManagementFactory.getRuntimeMXBean().getUptime();

        long second = uptimeMs / 1000;
        long day = second / 86400; second %= 86400;
        long hour = second / 3600; second %= 3600;
        long minute = second / 60; second %= 60;
        String running =String.format("已运行 %d 天 %02d:%02d:%02d", day, hour, minute, second);
        jvm.setRunning(running);
    }

    /**
     * 设置磁盘信息
     */
    private void setSysFiles(OperatingSystem os) {
        FileSystem fileSystem = os.getFileSystem();
        List<OSFileStore> fsArray = fileSystem.getFileStores();
        for (OSFileStore fs : fsArray)
        {
            long free = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            long used = total - free;
            SysFile sysFile = new SysFile();
            sysFile.setDirName(fs.getMount());
            sysFile.setSysTypeName(fs.getType());
            sysFile.setTypeName(fs.getName());
            sysFile.setTotal(convertFileSize(total));
            sysFile.setFree(convertFileSize(free));
            sysFile.setUsed(convertFileSize(used));
            sysFile.setUsage(NumberUtils.mul(NumberUtils.div(used, total, 4), 100));
            sysFiles.add(sysFile);
        }
    }

    /**
     * 字节转换
     *
     * @param size 字节大小
     * @return 转换后值
     */
    public String convertFileSize(long size)  {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb)
        {
            return String.format("%.1f GB", (float) size / gb);
        }
        else if (size >= mb)
        {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        }
        else if (size >= kb)
        {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        }
        else
        {
            return String.format("%d B", size);
        }
    }
}
