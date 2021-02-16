package com.dbw.cluster;

import java.util.ArrayList;
import java.util.List;

/**
 * Make calculations upon a series of DataPoint.
 * Assumptions:
 *   DataPoints are already sorted in ascending order.
 */
public class ClusterLogic {
    private final List<DataPoint> data;

    public ClusterLogic(List<DataPoint> data) {
        this.data = data;
    }

    public double calcThreshold() {
        return 4.0;
    }

    public List<List<DataPoint>> calcClusters(double threshold) {
        List<List<DataPoint>> allClusters = new ArrayList<>();
        List<DataPoint> currentCluster = new ArrayList<>();
        allClusters.add(currentCluster);
        for (int i=0; i<data.size(); i++) {
            DataPoint dp = data.get(i);
            if (shouldStartNewCluster(currentCluster, dp, threshold)) {
                currentCluster = new ArrayList<>();
                allClusters.add(currentCluster);
            }
            currentCluster.add(dp);
        }

        // Handle edge case where last cluster is close to 360 degrees
        // and is also close to the first cluster.
        if (allClusters.size() >=2) {
            List<DataPoint> lastCluster = allClusters.get(allClusters.size()-1);
            DataPoint lastData = lastCluster.get(lastCluster.size()-1);
            List<DataPoint> firstCluster = allClusters.get(0);
            DataPoint first = firstCluster.get(0);
            DataPoint shifted = new DataPoint(first.getValue() + 360.0);
            if (DataPoint.distance(lastData, shifted) <= threshold) {
                List<DataPoint> combined = new ArrayList<>();
                combined.addAll(firstCluster);
                combined.addAll(lastCluster);
                allClusters.remove(0);
                allClusters.add(combined);
            }

        }

        return allClusters;
    }

    public List<List<DataPoint>> pruneClusters(List<List<DataPoint>> clusters, int minClusterSize) {
        List<List<DataPoint>> pruned = new ArrayList<>();
        for (List<DataPoint> cluster: clusters) {
            if (cluster.size() >= minClusterSize) {
                pruned.add(cluster);
            }
        }

        return pruned;
    }

    private boolean shouldStartNewCluster(List<DataPoint> currentCluster, DataPoint dataPoint, double threshold) {
        if (currentCluster.size() == 0) return false;
        DataPoint previous = currentCluster.get(currentCluster.size()-1);
        boolean isFarAway = DataPoint.distance(previous, dataPoint) > threshold;
        return isFarAway;
    }
}
