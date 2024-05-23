package com.kabutar.balancify.util;

import com.kabutar.balancify.scheduler.SchedulerType;

public class Resolver {
    public static SchedulerType resolveScheduler(String type) throws Exception {
        switch (type){
            case "RR":
                return SchedulerType.ROUND_ROBIN;
            case "FF":
                return SchedulerType.FIFO;
            case "LC":
                return SchedulerType.LEAST_CONNECTION;
            case "LH":
                return SchedulerType.LINEAR_HASH;
            case "CH":
                return SchedulerType.CONSISTANT_HASH;
        }

        throw new Exception("Undefined scheduling type: "+type);
    }
}
