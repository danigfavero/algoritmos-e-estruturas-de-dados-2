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

        Utilizei consideravelmente o código de inspiração 'Permutations.java'

    Se for o caso, descreva a seguir 'bugs' e limitações do seu programa:

        Só faz comparações 2 a 2 no laço, portanto só funciona para n<=3.

****************************************************************/

/******************************************************************************
 *  Compilation:  javac-algs4 STPerms.java
 *  Execution:    java STPerms n s t opcao
 *
 *  Enumera todas as (s,t)-permutações das n primeiras letras do alfabeto.
 *  As permutações devem ser exibidas em ordem lexicográfica.
 *  Sobre o papel da opcao, leia o enunciado do EP.
 *
 *  % java STPerms 4 2 2 0
 *  badc
 *  bdac
 *  cadb
 *  cdab
 *  4
 *  % java STPerms 4 2 2 1
 *  4
 *  % java STPerms 4 2 2 2
 *  badc
 *  bdac
 *  cadb
 *  cdab
 *  4
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;

public class STPerms {
    private static int count = 0;

    private static boolean atendeS(String str, int s) {
        int n = str.length();
        if (s >= n) return(true); // pequena condição pra acelerar
        for (int i = 0; i < n; i++) {
            int sAux = 1;
            char max = str.charAt(i);
            int j = 1;
            while ((i+j) < n && sAux <= s) {
                if (max < str.charAt(i+j)) {
                    sAux++;
                    max = str.charAt(i+j);
                }
                j++;
            }
            if (sAux > s)
                return(false);
        }
        return(true);
    }

    atendeS(string a, char últimoQueEuPeguei, int posiçãoDele, int tamanhoAtualDaSeqCrescente)

    private static boolean atendeT(String str, int t) {
        int n = str.length();
        if (t >= n) return(true); // pequena condição pra acelerar
        for (int i = n-1; i >= 0; i--) {
            int tAux = 1;
            char max = str.charAt(i);
            int j = 1;
            while ((i-j) >= 0 && tAux <= t) {
                if (max < str.charAt(i-j)) {
                    tAux++;
                    max = str.charAt(i-j);
                }
                j++;
            }
            if (tAux > t)
                return(false);
        }
        return(true);
    }

    public static void perm(String prefix, String str, int s, int t, int opcao) {
        int n = str.length();
        if (n == 0) {
            if (atendeS(prefix, s) && atendeT(prefix, t)) {
                if (opcao != 1)
                    StdOut.println(prefix);
                count++;
            }
        }
        else {
            for (int i = 0; i < n; i++)
                perm(prefix + str.charAt(i), str.substring(0, i) + str.substring(i+1, n), s, t, opcao);
        }
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int s = Integer.parseInt(args[1]);
        int t = Integer.parseInt(args[2]);
        int opcao = Integer.parseInt(args[3]);
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        String elements = alphabet.substring(0, n);
        perm("", elements, s, t, opcao);
        if (opcao != 0) {
            StdOut.println(count);
        }
    }
}
