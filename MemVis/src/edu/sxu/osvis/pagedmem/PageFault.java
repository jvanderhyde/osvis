//An exception class to model page faults
//Created by James Vanderhyde, 28 March 2011

package edu.sxu.osvis.pagedmem;

public class PageFault extends Exception
{
    private int requestedPage;

    public PageFault(int requestedPage)
    {
        this.requestedPage = requestedPage;
    }

    public int getRequestedPage()
    {
        return requestedPage;
    }

}
