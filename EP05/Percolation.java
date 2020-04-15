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

        Depois que percola, o quadradinho imaginário conectado à última linha
        acaba enchendo outros quadrados da última linha que não deveriam ter
        esse conteúdo. Na prática, isso não altera o Stats pois o número de
        quadrados cheios não é importante para os cálculos e esse fenômeno só
        ocorre depois que percolou (ou seja, quando o programa é finalizado).

****************************************************************/
import java.lang.IllegalArgumentException;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int nOpen = 0; // number of open sites
    private int n;
    private int[][] grid;
    private WeightedQuickUnionUF uf;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        this.n = n;
        if (n <= 0)
            throw new IllegalArgumentException();
        grid = new int[n][n];
        for (int roww = 0; roww < n; roww++)
            for (int column = 0; column < n; column++)
                grid[roww][column] = 0; // blocked site = 0
        uf = new WeightedQuickUnionUF((n*n)+2);
        // the UF has 2 extra sites, one of them is connected
        // to all sites from grid's first row (n*n) an the other
        // is connected to all sites from grid's last row ((n*n)+1)
    }

    private void fulfill(int row, int col) {
        // connects UFs when necessary
        if (row-1 >= 0 && grid[row-1][col] == 1) // up
            uf.union(row*n+col, (row-1)*n+col);
        if (row+1 < n && grid[row+1][col] == 1) // down
            uf.union(row*n+col, (row+1)*n+col);
        if (col-1 >= 0 && grid[row][col-1] == 1) // left
            uf.union(row*n+col, row*n+(col-1));
        if (col+1 < n && grid[row][col+1] == 1) // right
            uf.union(row*n+col, row*n+(col+1));
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 0 || row >= n || col < 0 || col >= n)
            throw new IllegalArgumentException();
        if (isOpen(row,col))
            return;
        grid[row][col] = 1;
        if (row == 0)
            uf.union(row*n+col, n*n);
        if (row == n-1)
            uf.union(row*n+col, (n*n)+1);
        fulfill(row, col);
        nOpen++;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 0 || row >= n || col < 0 || col >= n)
            throw new IllegalArgumentException();
        return grid[row][col] != 0; // open and empty site = 1
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 0 || row >= n || col < 0 || col >= n)
            throw new IllegalArgumentException();
        return uf.connected(row*n+col, (n*n));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return nOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        // just checks if the 2 extra sites are connected
        return uf.connected(n*n, (n*n)+1);
    }

    // unit testing (required)
    public static void main(String[] args) {
        int n = Integer.parseInt (args[0]);
        int x, y;
        Percolation perc = new Percolation(n);
        while (!perc.percolates()) {
            StdOut.println("does not percolate :-(");
            x = StdRandom.uniform(n);
            y = StdRandom.uniform(n);
            perc.open(x,y);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    StdOut.print(perc.grid[i][j] + " ");
                }
                StdOut.println();
            }
        }
        StdOut.println("IT PERCOLATES!!!! :-D");
        StdOut.println((double) perc.numberOfOpenSites()/(n*n));
    }

}
