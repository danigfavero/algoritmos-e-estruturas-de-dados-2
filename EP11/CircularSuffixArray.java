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

      Minha função sort() foi inspirada no Quick3way.java do algs4

    Se for o caso, descreva a seguir 'bugs' e limitações do seu programa:

****************************************************************/
import java.lang.IllegalArgumentException;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

public class CircularSuffixArray {
    private int n;
    private int[] index;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
      if (s == null)
        throw new IllegalArgumentException();
      n = s.length();
      index = new int[n];
      for (int i = 0; i < n; i++)
          index[i] = i;
      sort(s.toCharArray());
    }

    // length of s
    public int length() {
      return n;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
      if (i < 0 || i > n-1)
        throw new IllegalArgumentException();
      return index[i];
    }

    private void sort(char[] a) {
      sort(a, 0, a.length - 1, 0);
    }

    // quicksort the subarray a[lo .. hi] using 3-way partitioning
    private void sort(char[] a, int lo, int hi, int d) {
        if (hi <= lo) return;
        int lt = lo, gt = hi;
        int v;
        if ((index[lo]+d) >= a.length)
          v = a[(index[lo]+d) - a.length];
        else
          v = a[index[lo]+d];
        int i = lo + 1;
        while (i <= gt) {
            int cmp;
            if ((index[i]+d) >= a.length)
              cmp = a[(index[i]+d) - a.length];
            else
              cmp = a[index[i]+d];
            if (cmp < v)
              exch(lt++, i++);
            else if (cmp > v)
              exch(i, gt--);
            else
              i++;
        }
        sort(a, lo, lt-1, d);
        if (v >= 0)
          sort(a, lt, gt, d+1);
        sort(a, gt+1, hi, d);
    }

    // exchange index[i]
    private void exch(int i, int j) {
       int aux = index[i];
       index[i] = index[j];
       index[j] = aux;
   }

    // unit testing (required)
    public static void main(String[] args) {
      String s = StdIn.readString();
    	CircularSuffixArray circ = new CircularSuffixArray(s);
    	for (int i = 0; i < circ.length(); i++)
    		StdOut.println(circ.index(i));
    }

}
