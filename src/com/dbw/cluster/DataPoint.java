package com.dbw.cluster;

import com.dbw.lib.FileUtil;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Coordinates of a spacial feature.
 */
public class DataPoint {
    final private double theta;

    public DataPoint(double theta) {
        this.theta = theta;
    }

    public double getValue() {
        return theta;
    }

    public static double distance(DataPoint dp1, DataPoint dp2) {
        return Math.abs(dp1.getValue() - dp2.getValue());
    }

    public static double average(List<DataPoint> points) throws Exception {
        if (points == null || points.size() == 0) {
            throw new Exception("Collection of DataPoint must contain at least one value.");
        }
        double first = points.get(0).getValue();
        double last = points.get(points.size()-1).getValue();
        boolean fix = first <= 180.0 && last > 180.0;
        double sum = 0.0;
        for (DataPoint pt: points) {
            sum += pt.getValue();
            if (fix && pt.getValue() <= 180.0) {
                sum += 360.0;
            }
        }

        double avg = sum/Double.valueOf(points.size());
        if (avg > 360.0) avg -= 360.0;
        return avg;
    }

    public static List<DataPoint> list(String filename) {
        List<DataPoint> list = new ArrayList<>();
        String data = FileUtil.readFileAsString(filename);
        if (data == null) return list;
        String[] splitData = data.split(",");
        for (String s: splitData) {
            try {
                double  d = Double.valueOf(s);
                list.add(new DataPoint(d));
            } catch (Exception ex) {
                System.err.println("Input contains value which is not a double");
            }
        }

        return list;
    }



    public String toString() {
        return Double.toString(theta);
    }

}
