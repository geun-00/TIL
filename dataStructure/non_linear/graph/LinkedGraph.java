package dataStructure.non_linear.graph;

import dataStructure.linear.Array.ArrayList;

public class LinkedGraph {
    public static void main(String[] args) {
        int n = 6;
        ArrayList<ArrayList<Integer>> graph = new ArrayList<>();

        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }

        putEdge(graph, 1, 2);
        putEdge(graph, 1, 3);
        putEdge(graph, 2, 3);
        putEdge(graph, 2, 4);
        putEdge(graph, 3, 4);
        putEdge(graph, 3, 5);
        putEdge(graph, 4, 5);
        putEdge(graph, 4, 6);

        print(graph);
    }

    public static void putEdge(ArrayList<ArrayList<Integer>> graph, int x, int y) {
        graph.get(x).add(y);
        graph.get(y).add(x);
    }

    public static void print(ArrayList<ArrayList<Integer>> graph) {
        for (int i = 1; i < graph.size(); i++) {
            ArrayList<Integer> node = graph.get(i);
            System.out.print("node" + "[" + i + "] : ");
            for (int j = 0; j < graph.get(i).size(); j++) {
                System.out.print(node.get(j) + " -> ");
            }
            System.out.println();
        }
    }
}
