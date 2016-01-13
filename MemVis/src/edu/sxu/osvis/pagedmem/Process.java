//Process class with a base address and limit.
//Created by James Vanderhyde, 22 March 2011
//Modified by James Vanderhyde, 17 March 2015
// Changed the toString method to something more readable

package edu.sxu.osvis.pagedmem;

public class Process
{
    private static int nextNumber = 0;

    private int number; //a label for the address, automatically generated

    private int base;   //address where process begins in memory
    private int limit;  //the amount of memory required by the process

    public Process(int base, int limit)
    {
        this.number = ++nextNumber;

        this.base=base;
        this.limit=limit;
    }

    @Override
    public String toString()
    {
        return "Process-"+number;
    }

    public int getNumber()
    {
        return this.number;
    }

    public int getBase()
    {
        return base;
    }

    public int getLimit()
    {
        return limit;
    }
}
