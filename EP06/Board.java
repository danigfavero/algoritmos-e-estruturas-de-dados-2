/****************************************************************
    Nome: Daniela Gonzalez Favero
    NUSP: 10277443

    Ao preencher esse cabeçalho com o meu nome e o meu número USP,
    declaro que todas as partes originais desse exercício programa (EP)
    foram desenvolvidas e implementadas por mim e que portanto não
    constituem desonestidade acadêmica ou plágio.
    Declaro também que sou responsável por todas as cópias desse
    programa e que não distribui ou facilitei a sua distribuição.
    Estou ciente que os casos de plágio e desonestidade acadêmica
    serão tratados segundo os critérios divulgados na página da
    disciplina.
    Entendo que EPs sem assinatura devem receber nota zero e, ainda
    assim, poderão ser punidos por desonestidade acadêmica.

    Abaixo descreva qualquer ajuda que você recebeu para fazer este
    EP.  Inclua qualquer ajuda recebida por pessoas (inclusive
    monitoras e colegas). Com exceção de material de MAC0323, caso
    você tenha utilizado alguma informação, trecho de código,...
    indique esse fato abaixo para que o seu programa não seja
    considerado plágio ou irregular.

    Exemplo:

        A monitora me explicou que eu devia utilizar a função xyz().

        O meu método xyz() foi baseada na descrição encontrada na
        página https://www.ime.usp.br/~pf/algoritmos/aulas/enumeracao.html.

    Descrição de ajuda ou indicação de fonte:

    Se for o caso, descreva a seguir 'bugs' e limitações do seu programa:

****************************************************************/
import java.lang.IllegalArgumentException;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Queue;


public class Board {
    private int board[][];
    private int n;
    private int ham;
    private int man;
    private String str;

    // to be used at manhattan
    private int distance(int cur, int row, int col) {
        int dist = 0;
        cur--;
        if (cur/n < row)
            dist += row - cur/n;
        else if (cur/n > row)
            dist += cur/n - row;
        if (cur%n < col)
            dist += col - cur%n;
        else if (cur%n > col)
            dist += cur%n - col;
        return dist;
    }

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles[0].length;
        board = tiles;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (tiles[row][col] != 0 && tiles[row][col] != (row*n+col)+1)
                    ham++;
                if (tiles[row][col] != 0)
                    man += distance(tiles[row][col], row, col);
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(n + "\n");
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                str.append(board[row][col]);
                str.append(" ");
            }
            str.append("\n");
        }
        return str.toString();
    }

    // tile at (row, col) or 0 if blank
    public int tileAt(int row, int col) {
        if (row < 0 && row >= n && col < 0 && col >= n)
            throw new IllegalArgumentException();
        return board[row][col];
    }

    // board size n
    public int size() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        return ham;
    }


    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return man;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return ham == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if(this == y)
            return true;
        if(y == null || y.getClass()!= this.getClass())
           return false;
        Board obj = (Board) y;
        if (n != obj.size())
                return false;
        for (int row = 0; row < n; row++)
            for (int col = 0; col < n; col++)
                if (this.tileAt(row,col) != obj.tileAt(row,col))
                    return false;
        return true;
    }

    private Board swap(int rowB, int colB, int rowN, int colN) {
        int [][] copy = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == rowB && j == colB)
                    copy[i][j] = board[rowN][colN];
                else if (i == rowN && j == colN)
                    copy[i][j] = board[rowB][colB];
                else
                    copy[i][j] = board[i][j];
            }
        }
        Board obj = new Board(copy);
        return obj;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue queue = new Queue();
        int row = 0; int col = 0;
        boolean zero = false;
        while (row < n && !zero) {
            col = 0;
            while (col < n && !zero) {
                if (board[row][col] == 0)
                    zero = true;
                col++;
            }
            row++;
        }
        col--; row--;
        if (row + 1 < n)
            queue.enqueue(swap(row, col, row+1, col));
        if (col + 1 < n)
            queue.enqueue(swap(row, col, row, col+1));
        if (row - 1 >= 0)
            queue.enqueue(swap(row, col, row-1, col));
        if (col - 1 >= 0)
            queue.enqueue(swap(row, col, row, col-1));
        return queue;
    }

    // is this board solvable?
    public boolean isSolvable() {
        int inv = 0;
        int blank;
        int [] arr = new int[n*n];
        // representing the matrix in an array:
        // index = row*n + col
        for (int row = 0; row < n; row++)
            for (int col = 0; col < n; col++)
                arr[row*n + col] = board[row][col];
        for (int i = 0; i < n*n; i++) {
            for (int j = i + 1; j < n*n; j++) {
                if (arr[i] == 0)
                    blank = i;
                else if (arr[j] != 0) {
                    if (arr[i] > arr[j])
                        inv++;
                }
            }
        }
        if (n%2 == 1)
            return inv%2 == 0;
        else {
            int k;
            for (k = 0; k < n*n && arr[k] != 0; k++)
                ;
            return (k/n + inv)%2 == 1;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        int goal[][] = new int[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                goal[i][j] = i*3+j+1;
        goal[2][2] = 0;
        Board goalB = new Board(goal);
        int a[][] = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        Board aB = new Board(a);
        StdOut.println("My board:");
        StdOut.println(aB.toString());
        StdOut.println("Size: " + aB.size());
        StdOut.println("Manhattan: " + aB.manhattan());
        StdOut.println("Hamming: " + aB.hamming());
        if(!aB.equals(goalB))
            StdOut.println("B equals goal?: NO!");
        if (aB.isGoal())
            StdOut.println("B is goal?: YES!");
        else
            StdOut.println("B is goal?: NO!");
        if (goalB.isGoal())
            StdOut.println("goal is goal?: YES!");
        else
            StdOut.println("goal is goal?: NO!");
        StdOut.println("tile at 2, 1: " + aB.tileAt(2,1));
        if (aB.isSolvable())
            StdOut.println("is B solvable?: YES!");
        else
            StdOut.println("is B solvable?: NO!");
        StdOut.print("\nIterate through neighbors:\n");
        for (Board obj : aB.neighbors())
            StdOut.println(obj.toString());
    }
}
