package monitor;

import com.sun.management.OperatingSystemMXBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.management.*;
import java.util.List;

public class MonitorJvm {
    private final static Logger logger = LogManager.getLogger(MonitorJvm.class);

    public static void main(String[] args) {
        int freeMemory = (int) Runtime.getRuntime().freeMemory() / 1024 / 1024;
        logger.debug("Free Memory={}M", freeMemory);
        int totalMemory = (int) Runtime.getRuntime().totalMemory() / 1024 / 1024;
        logger.debug("Total Memory={}M", totalMemory);
        logger.debug("Max Memory={}M", Runtime.getRuntime().maxMemory() / 1024 / 1024);

        logger.debug("\n==OperatingSystemMXBean==");
        OperatingSystemMXBean osm = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        logger.debug("osm.getArch():{}", osm.getArch());
        logger.debug("osm.getAvailableProcessors():{}", osm.getAvailableProcessors());
        logger.debug("osm.getCommittedVirtualMemorySize():{}", osm.getCommittedVirtualMemorySize());
        logger.debug("osm.getName():{}", osm.getName());
        logger.debug("osm.getProcessCpuTime():{}", osm.getProcessCpuTime());
        logger.debug("osm.getVersion():{}", osm.getVersion());
        logger.debug("FreeSwapSpaceSize={}M", osm.getFreeSwapSpaceSize() / 1024/ 1024);
        logger.debug("FreePhysicalMemorySize={}M", osm.getFreePhysicalMemorySize() / 1024/ 1024);
        logger.debug("TotalPhysicalMemorySize={}M", osm.getTotalPhysicalMemorySize() / 1024/ 1024);

        logger.debug("\n==MemoryMXBean==");
        MemoryMXBean mm = (MemoryMXBean) ManagementFactory.getMemoryMXBean();
        logger.debug("HeapMemoryUsage={}", mm.getHeapMemoryUsage());
        logger.debug("NonHeapMemoryUsage={}", mm.getNonHeapMemoryUsage());

        logger.debug("\n==ThreadMXBean==");
        ThreadMXBean tm = (ThreadMXBean) ManagementFactory.getThreadMXBean();
        logger.debug("ThreadCount={}", tm.getThreadCount());
        logger.debug("getPeakThreadCount={}", tm.getPeakThreadCount());
        logger.debug("getCurrentThreadCpuTime={}", tm.getCurrentThreadCpuTime());
        logger.debug("getDaemonThreadCount={}", tm.getDaemonThreadCount());
        logger.debug("getCurrentThreadUserTime={}", tm.getCurrentThreadUserTime());

        logger.debug("\n==CompilationMXBean==");
        CompilationMXBean gm = (CompilationMXBean) ManagementFactory.getCompilationMXBean();
        logger.debug("Name={}", gm.getName());
        logger.debug("getTotalCompilationTime={}", gm.getTotalCompilationTime());

        logger.debug("\n==MemoryPoolMXBean==");
        List<MemoryPoolMXBean> mpmList = ManagementFactory.getMemoryPoolMXBeans();
        for (MemoryPoolMXBean mpm : mpmList) {
            logger.debug("Usage={}", mpm.getUsage());
            logger.debug("MemoryManagerNames={}", mpm.getMemoryManagerNames().toString());
        }
        logger.debug("\n==MemoryPoolMXBean==");
        List<GarbageCollectorMXBean> gcmList = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcm : gcmList) {
            logger.debug("Name={}", gcm.getName());
            logger.debug("MemoryPoolNames={}", gcm.getMemoryPoolNames());
        }
        logger.debug("\n==RuntimeMXBean==");
        RuntimeMXBean rmb = (RuntimeMXBean) ManagementFactory.getRuntimeMXBean();
        logger.debug("ClassPath={}", rmb.getClassPath());
        logger.debug("LibraryPath={}", rmb.getLibraryPath());
        logger.debug("VmVersion={}", rmb.getVmVersion());
    }
}