//A JComponent that draws the memory allocated for processes
//Created by James Vanderhyde, 22 March 2011
//Modified by James Vanderhyde, 5 April 2011
// Added a distinct color for each process
//Modified by James Vanderhyde, 17 March 2015
// Rolled in changes from later lab
// Made code more readable
//Modified by James Vanderhyde, 20 March 2015
// Added support for paged processes

package edu.sxu.osvis.pagedmem;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JComponent;

public class MemoryVizComponent extends JComponent
{
    private ArrayList<Process> processes;

    public MemoryVizComponent(ArrayList<Process> processes)
    {
        this.processes=processes;
    }

    @Override
    public void paintComponent(Graphics g)
    {
        final int startX = 10;
        final int startY = 10;
        final int height = 30;
        
        //Draw RAM, represented by a rectangle
        g.setColor(Color.BLACK);
        g.drawRect(startX, startY, Main.amountOfRAM, height);
        
        //Draw some numbers to show RAM size
        final int baseline = startY+height+14;
        g.drawString("0", startX-4, baseline);
        g.drawString(""+Main.amountOfRAM, startX+Main.amountOfRAM-12, baseline);
        
        //Draw the memory allocated to the processes, represented by filled colored rectangles
        for (Process p:processes)
        {
            g.setColor(getColor(p.getNumber()));
            if (p instanceof PagedProcess)
            {
                //Fill in the allocated frames
                PagedProcess pp=(PagedProcess)p;
                for (int i=0; i<pp.getNumPages(); i++)
                {
                    int f=pp.getPageFrame(i);
                    if (pp.isPageValid(i))
                        g.fillRect(10+f*Main.pageSize, 10+1, Main.pageSize, 30-1);
                }
            }
            else
            {
                //Fill in the allocated memory
            g.fillRect(p.getBase()+startX, startY+1, p.getLimit(), height-1);
            }
        }
        
        //Draw frames (partitions of RAM)
        g.setColor(Color.BLACK);
        int numFrames = Main.amountOfRAM/Main.pageSize;
        for (int f=0; f<numFrames; f++)
        {
            g.drawRect(startX+f*Main.pageSize, startY, Main.pageSize, height);
        }
        
    }

    //Creates a color for a given int value.
    // The color appears to be random but is always the same for a given int.
    // There are 64 different colors.
    public static Color getColor(int c)
    {
        int r = (7*c)%64;
        return new Color((r%4)*63,((r/4)%4)*63,(r/4/4)*63);
    }

}
