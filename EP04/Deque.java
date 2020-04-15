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
import edu.princeton.cs.algs4.StdOut;


public class Deque<Item> implements Iterable<Item> {

    private Node head;
    private Node tail;
    private Node current;
    private int n;

    private class Node {
        private Item item;
        private Node next;
        private Node previous;
    }

    // construct an empty deque
    public Deque() {
        head = tail = null;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the deque
    public int size() {
        return n;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        Node oldHead = head;
        head = new Node();
        head.item = item;
        head.next = oldHead;
        if (n == 0)
            tail = head;
        if (oldHead != null)
            oldHead.previous = head;
        n++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        Node oldTail = tail;
        tail = new Node();
        tail.item = item;
        tail.previous = oldTail;
        if (n == 0)
            head = tail;
        if (oldTail != null)
            oldTail.next = tail;
        n++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException();
        Item removed = head.item;
        head = head.next;
        head.previous = null;
        n--;
        return removed;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException();
        Item removed = tail.item;
        tail = tail.previous;
        tail.next = null;
        n--;
        return removed;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = head;

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }


    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> queue = new Deque<String>();
        if (queue.isEmpty()) StdOut.println("it's empty");
        queue.addFirst("B");
        queue.addFirst("A");
        StdOut.println("add AB");
        StdOut.println("iterate");
        Iterator<String> it1 = queue.iterator();
        while (it1.hasNext())
            StdOut.print(it1.next() + " ");
        StdOut.println("\nand it has the size: " + queue.size());
        queue.addLast("C");
        queue.addLast("D");
        if (!queue.isEmpty()) StdOut.println("it's not empty");
        StdOut.println("add CD");
        StdOut.println("iterate");
        Iterator<String> it2 = queue.iterator();
        while (it2.hasNext())
            StdOut.print(it2.next() + " ");
        String frt = queue.removeFirst();
        StdOut.println("\nremoves A: " + frt);
        StdOut.println("iterate");
        Iterator<String> it4 = queue.iterator();
        while (it4.hasNext())
            StdOut.print(it4.next() + " ");
        StdOut.println("\nand it has the size: " + queue.size());
        String lst = (String) queue.removeLast();
        StdOut.println("removes D: " + lst);
        StdOut.println("iterate");
        Iterator<String> it3 = queue.iterator();
        while (it3.hasNext())
            StdOut.print(it3.next() + " ");
        StdOut.println("\nand it has the size: " + queue.size());
    }

}
