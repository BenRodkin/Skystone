package org.firstinspires.ftc.teamcode.miscellaneous;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class SkystonePatternPipeline extends OpenCvPipeline {

    // HSV Threshold input variables
    public static final double THRESHOLD_STEP = 1.0;

    public static final double HUE_MAX = 180.0;
    public static final double SAT_MAX = 255.0;
    public static final double VAL_MAX = 255.0;
    public static final double HSV_MIN = 0.0;

    private static double[] hsvHue = new double[]{80.0, 119.0};     // Starts with red outside of threshold (red is hue of 120)
    private static double[] hsvSat = new double[]{175.0, 255.0};
    private static double[] hsvVal = new double[]{50.0, 255.0};      // Testing showed 50 (min) to be enough to cut out most of the Skystone


    private static double rectTop   = 435.0;
    private static double rectLeft  = 55.0;
    private static double rectBot   = 533.0;
    private static double rectRight = 480.0;

    public static final double RECT_STEP = 0.04;
    public static final double RECT_MIN = 0.0;

    public static final int IMG_WIDTH = 480;
    public static final int IMG_HEIGHT = 640;

    private static boolean returnHSV = false;
    private static boolean drawRect = true;

    private static double leftBound = 0;
    private static double centerBound = 0;

    public static final double SKYSTONE_CONFIDENCE_THRESHOLD = 0.65;



    //Outputs
    private Mat hsvThresholdOutput = new Mat();
    private ArrayList<MatOfPoint> findContoursOutput = new ArrayList<MatOfPoint>();
    private ArrayList<MatOfPoint> filterContoursOutput = new ArrayList<MatOfPoint>();

    @Override
    public Mat processFrame(Mat input) {

        // Step HSV_Threshold0:
        Mat hsvThresholdInput = input;

        if(drawRect) {
            Imgproc.rectangle(
                    input,
                    new Point(  // Top left corner
                            rectLeft,   // Left value
                            rectTop),   // Top value
                    new Point( // Bottom right corner
                            rectRight,  // Right value
                            rectBot),   // Bottom value
                    new Scalar(0, 255, 0), 4);
        }
        Imgproc.line(
                input,
                new Point(
                        leftBound,
                        rectBot),
                new Point(
                        leftBound,
                        rectTop),
                new Scalar(0, 0, 255), 4);
        Imgproc.line(
                input,
                new Point(
                        centerBound,
                        rectBot),
                new Point(
                        centerBound,
                        rectTop),
                new Scalar(255, 0, 0), 4);

        double[] hsvThresholdHue =          hsvHue;
        double[] hsvThresholdSaturation =   hsvSat;
        double[] hsvThresholdValue =        hsvVal;
        hsvThreshold(hsvThresholdInput, hsvThresholdHue, hsvThresholdSaturation, hsvThresholdValue, hsvThresholdOutput);


        // Step Find_Contours0:
        Mat findContoursInput = hsvThresholdOutput;
        boolean findContoursExternalOnly = false;
        findContours(findContoursInput, findContoursExternalOnly, findContoursOutput);

        // Step Filter_Contours0:
        ArrayList<MatOfPoint> filterContoursContours = findContoursOutput;
        double filterContoursMinArea = 100.0;
        double filterContoursMinPerimeter = 0.0;
        double filterContoursMinWidth = 0.0;
        double filterContoursMaxWidth = 2.147483647E9;
        double filterContoursMinHeight = 0.0;
        double filterContoursMaxHeight = 2.147483647E9;
        double[] filterContoursSolidity = {0, 100};
        double filterContoursMaxVertices = 2.147483647E9;
        double filterContoursMinVertices = 0.0;
        double filterContoursMinRatio = 0.0;
        double filterContoursMaxRatio = 2.147483647E9;
        filterContours(filterContoursContours, filterContoursMinArea, filterContoursMinPerimeter, filterContoursMinWidth, filterContoursMaxWidth, filterContoursMinHeight, filterContoursMaxHeight, filterContoursSolidity, filterContoursMaxVertices, filterContoursMinVertices, filterContoursMinRatio, filterContoursMaxRatio, filterContoursOutput);



        return (returnHSV ? hsvThresholdOutput : input);
    }


    /**
     * This method is a generated getter for the output of a HSV_Threshold.
     * @return Mat output from HSV_Threshold.
     */
    public Mat hsvThresholdOutput() {
        return hsvThresholdOutput;
    }

    /**
     * This method is a generated getter for the output of a Find_Contours.
     * @return ArrayList<MatOfPoint> output from Find_Contours.
     */
    public ArrayList<MatOfPoint> findContoursOutput() {
        return findContoursOutput;
    }

    /**
     * This method is a generated getter for the output of a Filter_Contours.
     * @return ArrayList<MatOfPoint> output from Filter_Contours.
     */
    public ArrayList<MatOfPoint> filterContoursOutput() {
        return filterContoursOutput;
    }


    /**
     * Segment an image based on hue, saturation, and value ranges.
     *
     * @param input The image on which to perform the HSL threshold.
     * @param hue The min and max hue
     * @param sat The min and max saturation
     * @param val The min and max value
     * @param output The image in which to store the output.
     */
    private void hsvThreshold(Mat input, double[] hue, double[] sat, double[] val,
                              Mat out) {
        Imgproc.cvtColor(input, out, Imgproc.COLOR_BGR2HSV);
        Core.inRange(out, new Scalar(hue[0], sat[0], val[0]),
                new Scalar(hue[1], sat[1], val[1]), out);
    }

    /**
     * Sets the values of pixels in a binary image to their distance to the nearest black pixel.
     * @param input The image on which to perform the Distance Transform.
     * @param type The Transform.
     * @param maskSize the size of the mask.
     * @param output The image in which to store the output.
     */
    private void findContours(Mat input, boolean externalOnly,
                              List<MatOfPoint> contours) {
        Mat hierarchy = new Mat();
        contours.clear();
        int mode;
        if (externalOnly) {
            mode = Imgproc.RETR_EXTERNAL;
        }
        else {
            mode = Imgproc.RETR_LIST;
        }
        int method = Imgproc.CHAIN_APPROX_SIMPLE;
        Imgproc.findContours(input, contours, hierarchy, mode, method);
    }


    /**
     * Filters out contours that do not meet certain criteria.
     * @param inputContours is the input list of contours
     * @param output is the the output list of contours
     * @param minArea is the minimum area of a contour that will be kept
     * @param minPerimeter is the minimum perimeter of a contour that will be kept
     * @param minWidth minimum width of a contour
     * @param maxWidth maximum width
     * @param minHeight minimum height
     * @param maxHeight maximimum height
     * @param Solidity the minimum and maximum solidity of a contour
     * @param minVertexCount minimum vertex Count of the contours
     * @param maxVertexCount maximum vertex Count
     * @param minRatio minimum ratio of width to height
     * @param maxRatio maximum ratio of width to height
     */
    private void filterContours(List<MatOfPoint> inputContours, double minArea,
                                double minPerimeter, double minWidth, double maxWidth, double minHeight, double
                                        maxHeight, double[] solidity, double maxVertexCount, double minVertexCount, double
                                        minRatio, double maxRatio, List<MatOfPoint> output) {
        final MatOfInt hull = new MatOfInt();
        output.clear();
        //operation
        for (int i = 0; i < inputContours.size(); i++) {
            final MatOfPoint contour = inputContours.get(i);
            final Rect bb = Imgproc.boundingRect(contour);
            if (bb.width < minWidth || bb.width > maxWidth) continue;
            if (bb.height < minHeight || bb.height > maxHeight) continue;
            final double area = Imgproc.contourArea(contour);
            if (area < minArea) continue;
            if (Imgproc.arcLength(new MatOfPoint2f(contour.toArray()), true) < minPerimeter) continue;
            Imgproc.convexHull(contour, hull);
            MatOfPoint mopHull = new MatOfPoint();
            mopHull.create((int) hull.size().height, 1, CvType.CV_32SC2);
            for (int j = 0; j < hull.size().height; j++) {
                int index = (int)hull.get(j, 0)[0];
                double[] point = new double[] { contour.get(index, 0)[0], contour.get(index, 0)[1]};
                mopHull.put(j, 0, point);
            }
            final double solid = 100 * area / Imgproc.contourArea(mopHull);
            if (solid < solidity[0] || solid > solidity[1]) continue;
            if (contour.rows() < minVertexCount || contour.rows() > maxVertexCount)	continue;
            final double ratio = bb.width / (double)bb.height;
            if (ratio < minRatio || ratio > maxRatio) continue;
            output.add(contour);
        }
    }



    //----------------------------------------------------------------------------------------------
    // Getters
    //----------------------------------------------------------------------------------------------

    public double[] gethsvHue() { return hsvHue;}
    public double[] getHsvSat() { return hsvSat;}
    public double[] getHsvVal() { return hsvVal;}

    public double getRectTop()      { return rectTop;}
    public double getRectLeft()     { return rectLeft;}
    public double getRectBot()      { return rectBot;}
    public double getRectRight()    { return rectRight;}

    public double getLeftBound()    { return leftBound;}
    public double getCenterBound()  { return centerBound;}

    public List<MatOfPoint> getFilteredContours()   {return filterContoursOutput;}




    //----------------------------------------------------------------------------------------------
    // Setters
    //----------------------------------------------------------------------------------------------

    public void setHsvHueMin(double hsvHueMin) { hsvHue[0] = hsvHueMin;}
    public void setHsvHueMax(double hsvHueMax) { hsvHue[1] = hsvHueMax;}

    public void setHsvSatMin(double hsvSatMin) { hsvSat[0] = hsvSatMin;}
    public void setHsvSatMax(double hsvSatMax) { hsvSat[1] = hsvSatMax;}

    public void setHsvValMin(double hsvValMin) { hsvVal[0] = hsvValMin;}
    public void setHsvValMax(double hsvValMax) { hsvVal[1] = hsvValMax;}

    public void setRectTop(double top)      { rectTop   =     top;}
    public void setRectLeft(double left)    { rectLeft  =    left;}
    public void setRectBot(double bot)      { rectBot   =     bot;}
    public void setRectRight(double right)  { rectRight =   right;}

    public void setLeftBound(double lBound)     { leftBound     = lBound;}
    public void setCenterBound(double cBound)   { centerBound   = cBound;}

    public void setReturnHSV(boolean retHsv)    { returnHSV = retHsv;}
    public void setDrawRect(boolean draw)       { drawRect  = draw;}

}

