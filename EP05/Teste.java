public class Teste {
    public static void main(String[] args) {
        int[][] grid = new int[3][3];
        for (int roww = 0; roww < 3; roww++)
            for (int column = 0; column < 3; column++)
                grid[roww][column] = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
            System.out.print(grid[i][j] + " ");
            }
        System.out.println();
        }
        System.out.printf("mean()\t\t = %d\n", 2);
        System.out.printf("stddev()\t = %d\n", 3);
        System.out.printf("confidenceLow()\t = %d\n", 4);
        System.out.printf("confidenceHigh() = %d\n", 5);
        System.out.printf("elapsed time\t = %d\n", 6);
    }
}
