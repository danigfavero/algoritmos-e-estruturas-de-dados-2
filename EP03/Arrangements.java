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

        Código de permutações inspirado em:
        "https://www.geeksforgeeks.org/print-all-permutations-of-a-string-with-duplicates-allowed-in-input-string/"

    Se for o caso, descreva a seguir 'bugs' e limitações do seu programa:

****************************************************************/

// excessões pedidas
import java.lang.IllegalArgumentException;
import java.lang.UnsupportedOperationException;
import java.util.NoSuchElementException;

// pode ser útil
import java.util.Arrays; // Arrays.sort(), Arrays.copyOf(), ...

import java.util.Iterator; // passo 0 para criarmos um iterador

import edu.princeton.cs.algs4.StdOut;

public class Arrangements implements Iterable<String> {
    public boolean primeiro = true;
    public String current;
    public char [] vet;
    public Arrangements(String s) { // construtor dos arranjos
        if(s==null)
            throw new IllegalArgumentException();
        vet = s.toCharArray();
        Arrays.sort(vet);
        current = String.valueOf(vet);
    }

    public Iterator<String> iterator() { // construtor do iterador
        return new ArrangementsIterator();
    }

    private class ArrangementsIterator implements Iterator<String> {
        public boolean hasNext() {
            int size = current.length();
            if(size == 1 && primeiro) return(true); // corner case: single char
            for(int i = 0; i < size-1; i++) {
                if(vet[i]<vet[i+1])
                    return(true);
            }
            return(false);
        }

        private void swap(int i, int j) {
            char aux = vet[i];
            vet[i] = vet[j];
            vet[j] = aux;
        }

        int encontraTeto(char first, int j, int k) {
            int ceilIndex = j;
            for (int i = j + 1; i <= k; i++) {
                if (vet[i] > first && vet[i] < vet[ceilIndex])
                    ceilIndex = i;
            }
            return ceilIndex;
        }

        public String next() {
            if(!hasNext())
                throw new NoSuchElementException();
            if(primeiro) {
                primeiro = false;
                return(current);
            }
            int size = current.length();
            int i;
            for (i = size - 2; i >= 0; --i) {
                if (vet[i] < vet[i + 1])
                    break;
            }
            int ceilIndex = encontraTeto(vet[i], i + 1, size - 1);
            swap(i, ceilIndex);
            Arrays.sort(vet, i+1, size);
            current = String.valueOf(vet);
            return(current);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // Unit test
    public static void main(String[] args) {
        String s = args[0];

        Arrangements arr = new Arrangements(s);

        StdOut.println("Teste 1: imprime no máximo os 10 primeiros arranjos");
        Iterator<String> it = arr.iterator();
        for (int i = 0; it.hasNext() && i < 10; i++) {
            StdOut.println(i + " : " + it.next());
        }

        arr = new Arrangements(s);
        StdOut.println("Teste 2: imprime todos os arranjos");
        int i = 0;
        for (String arranjo: arr) {
            StdOut.println(i + " : " + arranjo);
            i++;
        }
    }
}
