###How to run
java -jar cluster.jar -i input.txt -o output.txt
### Example output
There are 5 sample output files int he output directory.
### Discussion of Solution
The core of my solution is this:
* Read the data
* Identify the clusters
* Remove any outlying data
* Calculate the centers of any remaining data
* Write out the results

Identifying the clusters is done by comparing successive values in the input data.
If the difference in successive values is greater than a threshold, then a new cluster is detected.
A default threshold value of 20 was used, but as this value would be likely to change when experimenting with more datasets,
it was made into a command line parameter.
Code was added to handle the special case where there are datapoints slightly before and slightly
after the origin.  In this case, adjacent clusters had to be joined into a single cluster.

Removal of outlying data was done by removing clusters with fewer than some minimum number of values.
The number 5 was chosen to get my results to match the example results, but again, as this number is likely to change, I made it into a command line parameter.

Finally centers were calculated which raised the problem of how do we define a center, and how do we calculate it, especially around the origin.
For the sake of simplicity, I chose to average the values in a cluster to define the center.

### Critique of Problem Statement
The problem statement could be clarified by defining what is meant by the "cluster center".
It could easily be a mean, median, or perhaps some other weighted average type of calculation.

### Assumptions Made
I made the following assumptions:
* Input data was expected as a single line of comma-separated values.
* Input file size was "small" enough to read the entire file into memory.
* No negative values, no values over 360.0
* Values are pre-sorted in ascending order.

### Critique of Obstacle Avoidance
I wonder about the choice of attempting to detect the center of an object for obstacle avoidance.
Another approach might be to determine the extent of an object by calculating a bounding shape, 
perhaps a bounding-box or bounding-sphere.  The approach focuses more on the edge of an object,
which is what you will collide with, not the center.

### Asymptotic Time Complexity
My solution transverses the input data once to identify clusters and one more time to calculate cluster centers.
The time complexity is therefore linear, or in Big-O notation, order O(n).
I don't see any way reaching O(log n) which usually involves progressively dividing
a problem into smaller and smaller parts.

One thing to consider would be only analysing objects in the direction the vehicle is moving
as these are the only things you can collide with, well, until you have to back up.
However, you could eliminate say half of the objects in view by filtering those between 90 and 270 degrees.
This would still result in a linear O(n) complexity.


