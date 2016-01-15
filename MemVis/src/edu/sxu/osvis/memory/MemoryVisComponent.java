//A JComponent that draws the memory allocated for processes
//Created by James Vanderhyde, 22 March 2011
//Modified by James Vanderhyde, 5 April 2011
// Added a distinct color for each process
//Modified by James Vanderhyde, 17 March 2015
// Rolled in changes from later lab
// Made code more readable

package edu.sxu.osvis.memory;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;
import javax.swing.JComponent;

public class MemoryVisComponent extends JComponent
{
    private final List<Process> processes;
    private final int startX = 15;
    private final int startY = 10;
    private final int height = 30;
    private final int baseline = startY+height+14;

    public MemoryVisComponent(List<Process> processes)
    {
        this.processes=processes;
        this.setPreferredSize(new Dimension(MemoryController.amountOfRAM+2*startX, baseline+startY));
    }

    @Override
    public void paintComponent(Graphics g)
    {
        //Draw RAM, represented by a rectangle
        g.setColor(Color.BLACK);
        g.drawRect(startX, startY, MemoryController.amountOfRAM, height);
        
        //Draw some numbers to show RAM size
        g.drawString("0", startX-4, baseline);
        g.drawString(""+MemoryController.amountOfRAM, startX+MemoryController.amountOfRAM-12, baseline);
        
        //Draw the memory allocated to the processes, represented by filled colored rectangles
        for (Process p:processes)
        {
            g.setColor(getColor(p.getNumber()));
            
            //Fill in the allocated memory
            g.fillRect(p.getBase()+startX, startY+1, p.getLimit(), height-1);
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