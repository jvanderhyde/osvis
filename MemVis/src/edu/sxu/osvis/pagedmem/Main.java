//Main program for simulating and visualizing memory allocation for processes
//Created by James Vanderhyde, 22 March 2011
//Modified by James Vanderhyde, 17 March 2015
// Rolled in changes from later lab
//Modified by James Vanderhyde, 20 March 2015
// Added commands for paged processes

package edu.sxu.osvis.pagedmem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import javax.swing.JFrame;

public class Main
{
    //Constants and variables related to RAM and the MMU
    public static final int amountOfRAM = 512;
    public static final int pageSize = 32;
    private static ArrayList<Process> processes=new ArrayList<Process>();
    private static boolean[] frameTable=new boolean[amountOfRAM/pageSize];
    private static PagedProcess[] frameTableProcesses=new PagedProcess[amountOfRAM/pageSize];
    private static int[] frameTableProcessPages=new int[amountOfRAM/pageSize];
    
    //Objects related to the simulator interface
    private static Scanner in=new Scanner(System.in);
    private static JFrame window=null;

    public static void createWindow()
    {
        window = new JFrame();

        window.setSize(amountOfRAM+30, 100);
        window.setTitle("Memory");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        MemoryVizComponent component = new MemoryVizComponent(processes);
        window.add(component);

        window.setVisible(true);
    }

    private static void printCommands()
    {
        //Print commands
        System.out.println("Commands");
        System.out.println("0. Print commands");
        System.out.println("1. Add process (contiguous)");
        System.out.println("2. Remove process");
        System.out.println("3. List processes");
        System.out.println("4. Add process (first fit)");
        System.out.println("5. Add process (paged)");
        System.out.println("6. Add process (demand paging)");
        System.out.println("7. Simulate vm processes (FIFO)");
        System.out.println("8. Simulate vm processes (LRU)");
        System.out.println("9. Quit");
    }

    public static void main(String[] args)
    {
        //Create window
        createWindow();

        //Read commands from user
        int choice=0;
        while (choice != 9)
        {
            //handle command
            switch (choice)
            {
            case 0:
                printCommands();
                break;
            case 1:
                addProcess();
                break;
            case 2:
                removeProcess();
                break;
            case 3:
                printProcesses();
                break;
            case 4:
                addProcessFirstFit();
                break;
            case 5:
                addProcessPaged();
                break;
            case 6:
                addProcessDemandPaging();
                break;
            case 7:
                simulateProcessesFIFO();
                break;
            case 8:
                simulateProcessesLRU();
                break;
            default:
                System.out.println("Command "+choice+" not understood.");
            }

            //repaint window
            if (window==null || !window.isVisible())
                createWindow();
            window.repaint();

            //Get next command
            System.out.print("Command: ");
            if (in.hasNextInt())
            {
                choice=in.nextInt();
                in.nextLine();
            }
            else
            {
                if (in.hasNext())
                {
                    System.out.println("Command "+in.nextLine()+" not understood.");
                    choice=0;
                }
                else
                {
                    //input stream is closed, so quit
                    choice=9;
                }
            }
        }

        //dispose of window
        window.setVisible(false);
        window.dispose();
    }

    public static void addProcess()
    {
        //Get info from user
        System.out.print("Enter base address: ");
        int base = in.nextInt();
        in.nextLine();
        System.out.print("Enter limit amount: ");
        int limit = in.nextInt();
        in.nextLine();

        //Check for conflicts (student implementation)


        //If no conflicts, add process
        Process p=new Process(base,limit);
        processes.add(p);
        System.out.println(""+p+" added.");
    }

    public static void removeProcess()
    {
        //Get info from user
        System.out.print("Enter process number: ");
        int num = in.nextInt();
        in.nextLine();

        //Find and remove matching process
        Iterator<Process> it=processes.iterator();
        while (it.hasNext())
        {
            Process p=it.next();
            if (p.getNumber()==num)
            {
                //Remove process from the list
                it.remove();
                
                //Check whether the process is a paged process
                if (p instanceof PagedProcess)
                {
                    //Mark the used frames free
                    PagedProcess pp=(PagedProcess)p;
                    for (int i=0; i<pp.getNumPages(); i++)
                    {
                        int f=pp.getPageFrame(i);
                        frameTable[f]=false;
                    }
                }
            }
        }
    }

    public static void printProcesses()
    {
        for (Process p:processes)
        {
            System.out.println(""+p+" base="+p.getBase()+" limit="+p.getLimit());
        }
    }

    public static void addProcessFirstFit()
    {
        System.err.println("Not yet implemented!"); //remove
        
        //Get info from user
        System.out.print("Enter limit amount: ");
        int limit = in.nextInt();
        in.nextLine();

        //Figure out base address (student implementation)
        int base=0;
        

        //Add process
        Process p=new Process(base,limit);
        processes.add(p);
        System.out.println(""+p+" added at base address "+base+".");
    }

    public static void addProcessPaged()
    {
        //Get info from user
        System.out.print("Enter limit amount: ");
        int limit = in.nextInt();
        in.nextLine();

        //Find open frames and construct a page table
        int numberOfFrames = (limit+Main.pageSize-1)/Main.pageSize;
        int[] processFrames = new int[numberOfFrames];
        int memoryNeeded = limit;
        int f=0, page=0;
        while ((memoryNeeded > 0) && (f<frameTable.length))
        {
            if (!frameTable[f])
            {
                //assign page to frame
                frameTable[f]=true;
                processFrames[page]=f;
                memoryNeeded -= pageSize;
                page++;
            }
            f++;
        }

        //Check whether the process was loaded completely
        if (f<frameTable.length)
        {
            //Add process
            Process p=new PagedProcess(limit, processFrames);
            processes.add(p);
            System.out.println(""+p+" added.");
        }
        else
        {
            //Roll back allocation of frames
            for (int j=0; j<page; j++)
                frameTable[processFrames[j]] = false;
        }
    }

    public static void addProcessDemandPaging()
    {
        //Get info from user
        System.out.print("Enter limit amount: ");
        int limit = in.nextInt();
        in.nextLine();

        //Add process
        Process p=new PagedProcess(limit);
        processes.add(p);
        System.out.println(""+p+" added.");
    }
    
    public static void simulateProcessesFIFO()
    {
        //Get info from user
        System.out.print("Enter the length of the simulation: ");
        int length = in.nextInt();
        in.nextLine();

        //Run simulation
        for (int i=0; i<length; i++)
        {
            for (Process p:processes)
            {
                if (p instanceof PagedProcess)
                {
                    PagedProcess pp=(PagedProcess)p;
                    try
                    {
                        pp.simulate();
                    }
                    catch (PageFault pf)
                    {
                        int requestedPage = pf.getRequestedPage();

                        //try to find an empty frame
                        int f = (int)(frameTable.length*Math.random());

                        //free up a frame if necessary
                        if (frameTableProcesses[f] != null)
                            frameTableProcesses[f].unloadPage(frameTableProcessPages[f]);
                        
                        //assign page to frame
                        pp.loadPage(requestedPage,f);
                        frameTableProcesses[f]=pp;
                        frameTableProcessPages[f]=requestedPage;
                    }
                    if (window!=null && window.isVisible())
                        window.repaint();
                }
            }
        }

        //Display number of page faults
        int totalPageFaults = 0;
        for (Process p:processes)
            if (p instanceof PagedProcess)
                totalPageFaults += ((PagedProcess)p).numPageFaults;
        System.out.println("The total number of page faults for LRU is "+totalPageFaults);
    }

    public static void simulateProcessesLRU()
    {
        //Get info from user
        System.out.print("Enter the length of the simulation: ");
        int length = in.nextInt();
        in.nextLine();

        //Run simulation
        for (int i=0; i<length; i++)
        {
            for (Process p:processes)
            {
                if (p instanceof PagedProcess)
                {
                    PagedProcess pp=(PagedProcess)p;
                    try
                    {
                        pp.simulate();
                    }
                    catch (PageFault pf)
                    {
                        int requestedPage = pf.getRequestedPage();

                        //try to find an empty frame

                        //free up a frame if necessary

                        //assign page to frame
                    }
                }
            }
        }

        //Display number of page faults
    }

    public static void testPPM()
    {
        PageProbabilityModel ppm=new PageProbabilityModel(8);
        System.out.println(java.util.Arrays.toString(ppm.nextPages(100)));
    }


}
