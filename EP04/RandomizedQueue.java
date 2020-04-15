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
import java.lang.UnsupportedOperationException;
import java.util.NoSuchElementException;

import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;


public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] queue;
    private int max;
    private int n;

    // construct an empty randomized queue
    public RandomizedQueue() {
        max = 2;
        queue = (Item[]) new Object[max];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    private void resize(int sz) {
        Item[] aux = (Item[]) new Object[sz];
        for (int i = 0; i < n; i++) {
            aux[i] = queue[i];
        }
        queue = aux;
        max = sz;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        if (size() == max)
            resize(2*max);
        queue[n] = item;
        n++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException();
        if (size() == max/4) resize(max/2);
        int x = StdRandom.uniform(n);
        Item removed = queue[x];
        n--;
        queue[x] = queue[n];
        return removed;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException();
        int x = StdRandom.uniform(n);
        return queue[x];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int i = 0;
        private Item[] aux;

        public RandomizedQueueIterator() {
            aux = (Item[]) new Object[n];
            for(int j = 0; j < n; j++)
                aux[j] = queue[j];
            StdRandom.shuffle(aux);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public boolean hasNext() {
            return i < n;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            return aux[i++];
        }
    }


    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        if (queue.isEmpty()) StdOut.println("it's empty");
        queue.enqueue("D");
        queue.enqueue("C");
        queue.enqueue("B");
        queue.enqueue("A");
        StdOut.println("iterate");
        Iterator<String> it1 = queue.iterator();
        while (it1.hasNext())
            StdOut.print(it1.next() + " ");
        StdOut.println("\nand it has the size: " + queue.size());
        if (!queue.isEmpty()) StdOut.println("it's not empty");
        String smp = queue.sample();
        StdOut.println("give me a sample: " + smp);
        StdOut.println("iterate");
        Iterator<String> it2 = queue.iterator();
        while (it2.hasNext())
            StdOut.print(it2.next() + " ");
        StdOut.println("\nand it has the size: " + queue.size());
        String rmv = (String) queue.dequeue();
        StdOut.println("dequeue: " + rmv);
        StdOut.println("iterate");
        Iterator<String> it3 = queue.iterator();
        while (it3.hasNext())
            StdOut.print(it3.next() + " ");
        StdOut.println("\nand it has the size: " + queue.size());
    }
}
