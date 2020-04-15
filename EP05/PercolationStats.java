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

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
    private double [] prob;
    private static int n;
    private static int trials;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        this.n = n;
        this.trials = trials;
        if (n <= 0 || trials <= 0)
            throw new java.lang.IllegalArgumentException();
        int x, y;
        prob = new double[trials];
        for (int test = 0; test < trials; test++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                x = StdRandom.uniform(n);
                y = StdRandom.uniform(n);
                perc.open(x,y);
            }
            prob[test] = ((double) perc.numberOfOpenSites())/(double)(n*n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(prob);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(prob);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return (StdStats.mean(prob) - 1.96*Math.sqrt(StdStats.stddev(prob)/trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return (StdStats.mean(prob) + 1.96*Math.sqrt(StdStats.stddev(prob)/trials));
    }

   // test client (see below)
   public static void main(String[] args) {
      int n = Integer.parseInt (args[0]);
      int trials = Integer.parseInt (args[1]);
      PercolationStats stats = new PercolationStats(n, trials);
      Stopwatch timer = new Stopwatch();
      StdOut.printf("mean()\t\t = %f\n", stats.mean());
      StdOut.printf("stddev()\t = %f\n", stats.stddev());
      StdOut.printf("confidenceLow()\t = %f\n", stats.confidenceLow());
      StdOut.printf("confidenceHigh() = %f\n", stats.confidenceHigh());
      StdOut.printf("elapsed time\t = %f\n", timer.elapsedTime());
   }

}
