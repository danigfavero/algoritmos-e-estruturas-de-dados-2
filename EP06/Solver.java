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
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;

public class Solver {
    private Queue<Board> solution;
    private MinPQ<Node> queue;
    private int totalMoves = -1;

    private class Node implements Comparable<Node> {
        private Board board;
        private int moves;
        private Node previous;

        Node(Board board, int moves, Node previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
        }

        public int compareTo(Node that) {
            int thisValue = this.board.manhattan() + this.moves;
            int thatValue = that.board.manhattan() + that.moves;
            if (thisValue > thatValue) return 1;
            if (thisValue < thatValue) return -1;
            return 0; // thisValue == thatValue
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null || !initial.isSolvable())
            throw new IllegalArgumentException();
        Node first = new Node(initial, 0, null);
        Node current = first;  // initializing with any value
        queue = new MinPQ<Node>();
        solution = new Queue<Board>();
        queue.insert(first);
        while (!queue.isEmpty() && !current.board.isGoal()) {
            current = queue.delMin();
            solution.enqueue(current.board);
            totalMoves++;
            for (Board obj : current.board.neighbors()) {
                if ((current.previous == null) || (current.previous != null
                && obj != current.previous.board)) {
                    Node nei = new Node(obj, current.moves++, current);
                    queue.insert(nei);
                }
            }
        }
    }

    // min number of moves to solve initial board
    public int moves() {
        return totalMoves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return solution;
    }

    // test client (see below)
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int row = 0; row < n; row++)
            for (int col = 0; col < n; col++)
                blocks[row][col] = in.readInt();
        Board initial = new Board(blocks);
        Solver solver = new Solver(initial);
        if (initial.isSolvable()) {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board.toString());
        }
        else
            StdOut.println("Unsolvable puzzle");
    }

}
