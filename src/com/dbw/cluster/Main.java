package com.dbw.cluster;

import com.dbw.lib.FileUtil;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private String inFilename = null;
    private String outFilename = null;
    // assumption: it takes this many datapoint to count as a significant feature
    private Integer minClusterSize = 5;
    // assumption: datapoints closer than this value belong in the same cluster
    private Double threshold = 20.0;

    public static void main(String[] args) {
        Main m = new Main();
        // interpret the command line
        m.processCommandLine(args);

        // read input
        List<DataPoint> dataPoints = DataPoint.list(m.inFilename);

        // get the business logic engine
        ClusterLogic logic = new ClusterLogic(dataPoints);

        // separate into clusters
        List<List<DataPoint>> clusters = logic.calcClusters(m.threshold);

        // remove outliers
        List<List<DataPoint>> prunedClusters = logic.pruneClusters(clusters, m.minClusterSize);

        // calculate cluster centers
        List<Double> centers = new ArrayList<>();
        for (List<DataPoint> cluster: prunedClusters) {
            try {
                double center = DataPoint.average(cluster);
                centers.add(center);
            } catch (Exception ex) {
                System.err.println("Cannot calculate center of cluster of size 0.  Returning -1 in solution set.");
                centers.add(-1.0);
            }
        }

        // print output
        FileUtil.toFile(centers, m.outFilename);
    }

    private void processCommandLine(String[] args) {
        for (int i=0; i<args.length; i++) {
            String arg = args[i];
            if ("-i".equals(arg)) {
                inFilename = args[i + 1];
                i += 1;
            } else if ("-o".equals(arg)) {
                outFilename = args[i + 1];
                i += 1;
            } else if ("-m".equals(arg)) {
                try {
                    minClusterSize = Integer.parseInt(args[i + 1]);
                } catch (Exception ex) {
                    System.err.println("Minimum cluster size argument -m is not an integer");
                }
                i += 1;
            } else if ("-t".equals(arg)) {
                try {
                    threshold = Double.parseDouble(args[i + 1]);
                } catch (Exception ex) {
                    System.err.println("Threshold argument -t is not a double");
                }
                i += 1;
            } else {
                System.err.println("Illegal command line argument: " + arg);
                System.exit(1);
            }
        }

        if (inFilename == null || outFilename == null) {
            System.err.println("You must supply input and output file parameters.");
            System.exit(2);
        }
    }
}
