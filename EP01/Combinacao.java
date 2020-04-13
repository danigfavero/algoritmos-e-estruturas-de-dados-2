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

    "https://moometric.com/development/java/reverse-a-string-in-java/" me ajudou a inverter string


    Se for o caso, descreva a seguir 'bugs' e limitações do seu programa:

****************************************************************/

/******************************************************************************
 *  Compilation:  javac-algs4 Combinacao.java
 *  Execution:    java Combinacao n k [opcao]
 *
 *  Enumera todas as combinações dos números em {1,2,...,n} k a k.
 *  Se opcao = 0 (defaul), gera e exibe todas as permutações em ordem
 *  lexicográfica
 *  Se opcao = 1 apenas, __gera todas__ as combinações, mas __não__ as
 *  exibe; apenas exibe o total de combinações.
 *
 * % java Combinacao 5 3 1
 * 10
 * elapsed time = 0.002
 * % java Combinacao 5 3
 * 1 2 3
 * 1 2 4
 * 1 2 5
 * 1 3 4
 * 1 3 5
 * 1 4 5
 * 2 3 4
 * 2 3 5
 * 2 4 5
 * 3 4 5
 * 10
 * elapsed time = 0.002
 * % java Combinacao 100 3 1
 * 161700
 * elapsed time = 0.004
 * % java Combinacao 1000 3 1
 * 166167000
 * elapsed time = 0.726
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.algs4.Stack;

public class Combinacao {
    private static int count = 0; // contador de combinações
    private static int opcao = 0;
    private static Stack<Integer> stack = new Stack<>();
        // 0 imprimir as combinações e o número de combinações (default)
        // 1 imprimir apenas o número de combinações

   public static void imprime(Stack stack) { // imprime na ordem certa
        String aux; String comb = "";
        aux = stack.toString();
        int i;
        for (i = aux.length()-1; i >= 0; i--) { // inverte a string
            comb = comb + aux.charAt(i);
        }
        StdOut.println(comb);
    }

    public static void combinacao(int ini, int fim, int k) {
        if (k == 0) {
            count++;
            if (opcao == 0)
                imprime(stack);
        }
        else {
            for(int i = ini+1; i < fim+1; i++) {
                stack.push(i);
                combinacao(i, fim, k-1);
                stack.pop();
            }
        }
    }


    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int k = Integer.parseInt(args[1]);
        if (args.length == 3) {
            opcao = Integer.parseInt(args[2]);
        }
        Stopwatch timer = new Stopwatch();
        combinacao(0, n, k);
        StdOut.println(count);
        StdOut.println("elapsed time = " + timer.elapsedTime());
    }
}
