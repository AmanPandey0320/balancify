package com.kabutar.balancify.util;

import com.kabutar.balancify.config.Server;

public class SchedulerUtil {
	
	/**
    *
    * @description return number of virtual nodes on hash ring
    * Log2 on CPU: Handles the diminishing returns of adding many cores.
    * Sqrt on Memory: Prevents one server with tons of RAM from dominating.
    * Scalability: scalingFactor helps fine-tune the number of virtual nodes.
    *
    * @param size
    * @return
    */
   public static int getVirtualNodeCount(Server.Size size, double scalingFactor){
       double cpuScore = (Math.log(size.getCpu() + 1.0) / Math.log(2));
       double memoryScore = Math.sqrt(size.getMemory());

       return  (int) Math.round((cpuScore + memoryScore) * scalingFactor);
   }

}
