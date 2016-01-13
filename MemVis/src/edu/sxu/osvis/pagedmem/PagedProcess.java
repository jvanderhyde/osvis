//Paged process class with page table
//Created by James Vanderhyde, 28 March 2011

package edu.sxu.osvis.pagedmem;

public class PagedProcess extends Process
{
    //These arrays make up the page table for this process
    private int[] pageTable;        //the frame where each page is loaded
    private boolean[] pageValid;    //whether the page is loaded in RAM

    private PageProbabilityModel ppModel;

    int numPageFaults;

    //This constructor is for pure demand paging (all pages initially invalid)
    public PagedProcess(int limit)
    {
        super(0,limit);

        int numberOfPages = (limit+Main.pageSize-1)/Main.pageSize;
        this.ppModel = new PageProbabilityModel(numberOfPages);
        this.pageTable = new int[numberOfPages];
        this.pageValid = new boolean[numberOfPages];
        for (int i=0; i<numberOfPages; i++)
        {
            pageValid[i]=false;
        }
        this.numPageFaults = 0;
    }

    //This constructor is for a process with all pages assigned to frames
    public PagedProcess(int limit, int[] frames)
    {
        this(limit);
        for (int i=0; i<this.pageTable.length; i++)
        {
            pageTable[i]=frames[i];
            pageValid[i]=true;
        }
    }

    public void simulate() throws PageFault
    {
        int nextPage = ppModel.nextPage();
        if (!pageValid[nextPage])
        {
            numPageFaults++;
            throw new PageFault(nextPage);
        }
    }

    public int getPageFrame(int p)
    {
        return pageTable[p];
    }

    public boolean isPageValid(int p)
    {
        return pageValid[p];
    }

    public int getNumPages()
    {
        return (this.getLimit()+Main.pageSize-1)/Main.pageSize;
    }

    void loadPage(int page, int frame)
    {
        pageTable[page] = frame;
        pageValid[page] = true;
    }
    
    void unloadPage(int page)
    {
        pageValid[page] = false;
    }

}
