
/**
 * Write a description of class Exerpt_283To313 here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import java.util.*;
public class TestExcerpt_283To296New
{
    /*Above:  
     * Initialize the lastContours ArrayList and three entries
    */
    public static void main(String[] args) {
            double contoursProportionLeft = 0, contoursProportionRight = 0, contoursProportionCenter = 0;
            
            List<lastContours> lastValues= new ArrayList<lastContours>();
            
            lastValues.add(new lastContours(15)); //Left
            lastValues.add(new lastContours(15)); //Center
            lastValues.add(new lastContours(15)); //Right
            
            lastValues.get(0).add(contoursProportionLeft);
            lastValues.get(1).add(contoursProportionCenter);
            lastValues.get(2).add(contoursProportionRight);
            
            contoursProportionLeft      = lastValues.get(0).getAverage();
            contoursProportionCenter    = lastValues.get(1).getAverage();
            contoursProportionRight     = lastValues.get(2).getAverage();
            
            // Get the largest tally
            double largestTally = Math.max(contoursProportionLeft, Math.max(contoursProportionCenter, contoursProportionRight));

            // Divide all three tallies by the largest to get proportions
            try {
                contoursProportionLeft /= largestTally;
                contoursProportionCenter /= largestTally;
                contoursProportionRight /= largestTally;
            } catch (Exception e) {
                //telemetry.addLine("Error while dividing contour tallies by largest tally.");
            }

            // Get the smallest proportioned tally
            double smallestProportionedTally = Math.min(contoursProportionLeft, Math.min(contoursProportionCenter, contoursProportionRight));

    }
}
