//A class to represent the OS kernel
//Created by James Vanderhyde, 15 January 2016

package edu.sxu.osvis.memory;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Kernel
{
    private static final ArrayList<Process> processes=new ArrayList<Process>();
    
    public static List<Process> getProcessList()
    {
        return Collections.unmodifiableList(processes);
    }
    
    public static Process addProcess(int base, int limit)
    {
        //Check for conflicts (student implementation)
        //return null if there is a conflict.

        //If no conflicts, add process
        Process p=new Process(base,limit);
        processes.add(p);
        return p;
    }
    
    public static Process addProcessFirstFit(int limit)
    {
        System.err.println("Not implemented");
        
        //Figure out base address (student implementation)
        int base=0;
        //return null if the process does not fit anywhere.

        //Add process
        Process p=new Process(base,limit);
        processes.add(p);
        return p;
    }
    
    public static void removeProcess(int number)
    {
        Iterator<Process> it=processes.iterator();
        while (it.hasNext())
        {
            Process p=it.next();
            if (p.getNumber()==number)
                it.remove();
        }
    }
}
