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

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

public class MoveToFront {

    private static class Node {
        char c;
        Node next;

        private Node(char c, Node next) {
            this.c = c;
            this.next = next;
        }
    }


    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        // writes alphabet
        Node head = null;
        Node current = null;
        for (int i = 255; i >= 0; i--) {
            head = new Node((char)i, current);
            current = head;
        }
        String input = BinaryStdIn.readString();
        for (int i = 0; i < input.length(); i++) {
            // get char
            current = head;
            Node previous = null;
            int j = 0;
            while (current != null && input.charAt(i) != current.c) {
                previous = current;
                current = current.next;
                j++;
            }
            BinaryStdOut.write(j, 8);
            // move to front
            if (j != 0) { // current == head
                previous.next = current.next;
                current.next = head;
                head = current;
            }
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        // writes alphabet
        Node head = null;
        Node current = null;
        for (int i = 255; i >= 0; i--) {
            head = new Node((char)i, current);
            current = head;
        }
        while (!BinaryStdIn.isEmpty()) {
            int input = BinaryStdIn.readInt(8);
            char c = (char)input;
            Node previous = null;
            current = head;
            int j;
            for (j = 0; current != null && j < input; j++) {
                previous = current;
                current = current.next;
            }
            BinaryStdOut.write(current.c);
            // move to front
            if (j != 0) {
                previous.next = current.next;
                current.next = head;
                head = current;
            }
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].compareTo("-") == 0)
            encode();
        if (args[0].compareTo("+") == 0)
            decode();
    }

}
