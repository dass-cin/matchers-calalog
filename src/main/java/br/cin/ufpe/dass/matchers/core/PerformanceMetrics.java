package br.cin.ufpe.dass.matchers.core;

/**
 * Created by diego on 08/03/17.
 */
public class PerformanceMetrics {

    private long executionTimeInMillis;

    private long memoryUsed;

    private long diskSpaceUsed;

    public long getExecutionTimeInMillis() {
        return executionTimeInMillis;
    }

    public void setExecutionTimeInMillis(long executionTimeInMillis) {
        this.executionTimeInMillis = executionTimeInMillis;
    }

    public long getMemoryUsed() {
        return memoryUsed;
    }

    public void setMemoryUsed(long memoryUsed) {
        this.memoryUsed = memoryUsed;
    }

    public long getDiskSpaceUsed() {
        return diskSpaceUsed;
    }

    public void setDiskSpaceUsed(long diskSpaceUsed) {
        this.diskSpaceUsed = diskSpaceUsed;
    }
}
