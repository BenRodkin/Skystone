
/**
 * Write a description of class data_test here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import java.util.*;
public class lastContours
{
    private List<Double> lastVals = new ArrayList<Double>();
    private int numValues;
    
    public lastContours(int numValues){
        this.numValues = numValues;
    }
    
    public void add(double d) {
        if(lastVals.size() == numValues) {
            for(int i = 1; i < lastVals.size(); i++) {
                lastVals.set(i-1, lastVals.get(i));
            }
            lastVals.set(lastVals.size()-1,d);
        } else {
            lastVals.add(d);
        }
    }
    
    public int getNumValues() {
        return numValues;
    }
    
    public double[] getVals() {
        double[] temp = new double[lastVals.size()];
        for(int i = 0; i < lastVals.size(); i++) {
            temp[i] = lastVals.get(i);
        }
        return temp;
    }
    
    public double getAverage() {
        double total = 0.0;
        for(Double d : lastVals) {
            total += d;
        }
        return total/lastVals.size();
    }
    
    public void clearData() {
        lastVals.clear();
    }
}
