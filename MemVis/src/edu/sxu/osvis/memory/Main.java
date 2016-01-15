//Main program for simulating and visualizing memory allocation for processes
//Created by James Vanderhyde, 22 March 2011
//Modified by James Vanderhyde, 17 March 2015
// Rolled in changes from later lab
//Modified by James Vanderhyde, 15 January 2016
// Moved functionality to new Kernel and MemoryController classes

package edu.sxu.osvis.memory;

import java.util.Scanner;
import javax.swing.JFrame;

public class Main
{
    private static Scanner in=new Scanner(System.in);
    private static JFrame window=null;

    public static void createWindow()
    {
        window = new JFrame();

        window.setTitle("Memory");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        MemoryVizComponent component = new MemoryVizComponent(Kernel.getProcessList());
        window.add(component);

        window.pack();
        window.setVisible(true);
    }

    private static void printCommands()
    {
        //Print commands
        System.out.println("Commands");
        System.out.println("0. Print commands");
        System.out.println("1. Add process");
        System.out.println("2. Remove process");
        System.out.println("3. List processes");
        System.out.println("4. Add process (first fit)");
        System.out.println("9. Quit");
    }

    public static void main(String[] args)
    {
        //Create window
        createWindow();

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

        Process p = Kernel.addProcess(base, limit);
        if (p==null)
            System.out.println("Process cannot be put in memory.");
        else
            System.out.println(""+p+" added.");
    }

    public static void removeProcess()
    {
        //Get info from user
        System.out.print("Enter process number: ");
        int num = in.nextInt();
        in.nextLine();

        //Remove process
        Kernel.removeProcess(num);
    }

    public static void printProcesses()
    {
        for (Process p:Kernel.getProcessList())
        {
            System.out.println(""+p+" base="+p.getBase()+" limit="+p.getLimit());
        }
    }

    public static void addProcessFirstFit()
    {
        System.err.println("Not yet implemented!");
        
        //Get info from user
        System.out.print("Enter limit amount: ");
        int limit = in.nextInt();
        in.nextLine();

        //Add process
        Process p = Kernel.addProcessFirstFit(limit);
        if (p==null)
            System.out.println("Process cannot be put in memory.");
        else
            System.out.println(""+p+" added at base address "+p.getBase()+".");
    }

}
