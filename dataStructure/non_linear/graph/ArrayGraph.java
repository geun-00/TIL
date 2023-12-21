package dataStructure.non_linear.graph;

public class ArrayGraph {
    public static void main(String[] args) {
        int n = 6; // 정점(노드)의 개수
        int[][] graph = new int[n+1][n+1]; // 그래프를 인접행렬로 표현

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

    public static void putEdge(int[][] graph, int x, int y) {
        graph[x][y] = graph[y][x] = 1;
    }

    public static void print(int[][] graph) {
        for(int i=1; i<graph.length; i++) {
            for(int j=1; j<graph[i].length; j++) {
                System.out.print(graph[i][j] + " ");
            }
            System.out.println();
        }
    }
}
