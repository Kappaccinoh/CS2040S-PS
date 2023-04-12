import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

public class TSPGraph implements IApproximateTSP {

    public final double INFINITY = Double.MAX_VALUE;
    @Override
    public void MST(TSPMap map) {
        TreeMapPriorityQueue<Double, Integer> pQueue = new TreeMapPriorityQueue<Double, Integer>();
        for (Integer i = 0; i < map.getCount(); i++) {
            pQueue.add(i, INFINITY);
        }
        Integer startingNode = 0;
        pQueue.decreasePriority(startingNode, 0.0);

        HashMap<Integer, Integer> parent = new HashMap<>();

        HashSet<Integer> visited = new HashSet<>();
        visited.add(startingNode);
        Integer node;
        parent.put(startingNode, startingNode);

        while (!pQueue.isEmpty()) {
            node = pQueue.extractMin();
            visited.add(node);
            map.setLink(node, parent.get(node));

            for (Integer targetNode = 0; targetNode < map.getCount(); targetNode++) {
                if (!visited.contains(targetNode)) {
                    double distance = map.pointDistance(targetNode, node);
                    if (distance < pQueue.lookup(targetNode)) {
                        map.eraseLink(targetNode);
                        pQueue.decreasePriority(targetNode, distance);
                        parent.put(targetNode, node);
                    }
                }
            }
        }
    }

    @Override
    public void TSP(TSPMap map) {
        MST(map);
        Stack<Integer> stack = new Stack<Integer>();
        HashSet<Integer> visited = new HashSet<Integer>();

        int start = 0;
        int curr = start;
        int lastNode = start;
        stack.push(start);

        while (!stack.isEmpty()) {
            curr = stack.pop();
            map.setLink(curr, lastNode);
            lastNode = curr;
            visited.add(curr);

            for (int i = 0; i < map.getCount(); i++) {
                if (map.getLink(i) == curr && !visited.contains(i)) {
                    stack.push(i);
                }
            }
        }
        map.setLink(start, lastNode, false);
        map.redraw();
    }

    @Override
    public boolean isValidTour(TSPMap map) {
        int root = 0;
        int start = 0;
        int end = 0;
        HashSet<Integer> visited = new HashSet<>();

        for (int i = 0; i < map.getCount() + 1 && root != -1; i++) {
            if (!visited.contains(root)) {
                visited.add(root);
            }
            end = root;
            root = map.getLink(root);
        }
        if (visited.size() == map.getCount() && start == end) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public double tourDistance(TSPMap map) {
        if (!isValidTour(map)) {
            return -1;
        }
        double distance = 0;
        int root = 0;
        for (int i = 0; i < map.getCount() && root != -1; i++) {
            int next = map.getLink(root);
            distance += map.pointDistance(root, next);
            root = next;
        }
        return distance;
    }

    public static void main(String[] args) {
        TSPMap map = new TSPMap(args.length > 0 ? args[0] : "twentypoints.txt");
        TSPGraph graph = new TSPGraph();

//        graph.MST(map);
         graph.TSP(map);
        System.out.println(graph.isValidTour(map));
        System.out.println(graph.tourDistance(map));
    }
}
