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
    private final int marginX = 15;
    private final int marginY = 15;
    private final int thickness = 30;
    private final int textHeight = 12;
    private final int textWidthMin = 8;
    private final int textWidthMax = 24;
    private final boolean horizontal = false;

    public MemoryVisComponent(List<Process> processes)
    {
        this.processes=processes;
        if (horizontal)
            this.setPreferredSize(new Dimension(MemoryController.amountOfRAM+2*marginX, textHeight+thickness+2*marginY));
        else
            this.setPreferredSize(new Dimension(textWidthMax+thickness+2*marginX, MemoryController.amountOfRAM+2*marginY));
    }

    @Override
    public void paintComponent(Graphics g)
    {
        //Draw RAM, represented by a rectangle
        g.setColor(Color.BLACK);
        if (horizontal)
            g.drawRect(marginX, marginY, MemoryController.amountOfRAM, thickness);
        else
            g.drawRect(marginX, marginY, thickness, MemoryController.amountOfRAM);
        
        //Draw some numbers to show RAM size
        if (horizontal)
        {
            g.drawString("0", marginX-textWidthMin/2, marginY+thickness+textHeight+2);
            g.drawString(""+MemoryController.amountOfRAM, marginX+MemoryController.amountOfRAM-textWidthMax/2, marginY+thickness+textHeight+2);
        }
        else
        {
            g.drawString("0", marginX+thickness+4, marginY+textHeight/2);
            g.drawString(""+MemoryController.amountOfRAM, marginX+thickness+4, marginY+textHeight/2+MemoryController.amountOfRAM);
        }
    
        //Draw the memory allocated to the processes, represented by filled colored rectangles
        for (Process p:processes)
        {
            g.setColor(getColor(p.getNumber()));
            
            //Fill in the allocated memory
            if (horizontal)
                g.fillRect(p.getBase()+marginX, marginY+1, p.getLimit(), thickness-1);
            else
                g.fillRect(marginX+1, p.getBase()+marginY, thickness-1, p.getLimit());
        }
    }

    //Creates a color for a given int value.
    // The color appears to be random but is always the same for a given int.
    // There are 64 different colors.
    public static Color getColor(int c)
    {
        int r = (7*c)%64;
        return new Color((r%4)*63+30,((r/4)%4)*63+30,(r/4/4)*63+30);
    }

}
