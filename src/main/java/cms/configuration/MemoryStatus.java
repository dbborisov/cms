package cms.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemoryStatus {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public String memoryStatus() {
        Runtime gfg = Runtime.getRuntime();
        StringBuilder builder = new StringBuilder();
        double memory1, memory2;

        builder.append(System.lineSeparator()).append("Total memory is: ").append(gfg.totalMemory() * 0.000001).append(" mb.");

        // checking free memory
        memory1 = ((gfg.freeMemory() * 0.000001 ));
        builder.append(System.lineSeparator()).append("Initial free memory: ").append(memory1).append(" mb.");

        // calling the garbage collector on demand
        gfg.gc();

        memory1 = gfg.freeMemory() * 0.000001;

        builder.append(System.lineSeparator()).append("Free memory after garbage " + "collection: ").append(memory1).append(" mb.");



        memory2 = gfg.freeMemory() * 0.000001;
        builder.append(System.lineSeparator()).append("Free memory after allocation: ").append(memory2).append("mb.");

        builder.append(System.lineSeparator()).append("Memory used by allocation: ").append(memory1 - memory2).append(" mb.");



        gfg.gc();

        memory2 = gfg.freeMemory() * 0.000001;
        builder.append(System.lineSeparator()).append("Free memory after  " + "collecting discarded Integers: ").append(memory2).append(" mb.");

        builder.append(System.lineSeparator()).append("---").append(System.lineSeparator()).append("Processors available ").append(gfg.availableProcessors()).append(System.lineSeparator()).append("---");
        return builder.toString();
    }
}
