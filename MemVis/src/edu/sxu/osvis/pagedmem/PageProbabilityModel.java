//A probabilistic model of memory page requests
//Created by James Vanderhyde, 28 March 2011

package edu.sxu.osvis.pagedmem;

public class PageProbabilityModel
{
    //Random number generator
    private static java.util.Random rng = new java.util.Random();

    //Probabilities of staying on each page
    private double[] probabilities;

    //Number of pages
    private int numberOfPages;

    //Current page
    private int currentPage;

    /**
     * Constructs a generator of page requests made by a modeled CPU
     * @param numberOfPages the number of pages of memory used by the process
     */
    public PageProbabilityModel(int numberOfPages)
    {
        this.numberOfPages = numberOfPages;
        this.probabilities = new double[numberOfPages];

        //initialize the probabilities
        for (int i=0; i<numberOfPages; i++)
        {
            this.probabilities[i] = 0.8;
        }

        this.currentPage = rng.nextInt(numberOfPages);
    }

    /**
     * Generates the next page required by the CPU
     * @return the number of the next page
     */
    public int nextPage()
    {
        if (rng.nextDouble() >= probabilities[currentPage])
        {
            //select a new page from among the other pages
            int selection = rng.nextInt(numberOfPages-1);
            if (selection >= currentPage)
                currentPage = selection+1;
            else
                currentPage = selection;
        }
        return currentPage;
    }

    /**
     * Generates the next several pages required by the CPU
     * @param howMany how many pages to generate
     * @return an array of page numbers
     */
    public int[] nextPages(int howMany)
    {
        int[] result=new int[howMany];
        for (int i=0; i<howMany; i++)
            result[i]=this.nextPage();
        return result;
    }
}
