package com.kabutar.balancify.util;

import com.kabutar.balancify.constants.SchedulerType;

public class ResolverUtil {
    public static SchedulerType resolveScheduler(String type) throws Exception {
        switch (type){
            case "RR":
                return SchedulerType.ROUND_ROBIN;
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
