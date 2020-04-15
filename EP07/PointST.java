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
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Queue;

public class PointST<Value> {
    public RedBlackBST<Point2D, Value> bst;

    // construct an empty symbol table of points
    public PointST() {
        bst = new RedBlackBST<Point2D, Value>();
    }

    // is the symbol table empty?
    public boolean isEmpty() {
        return bst.isEmpty();
    }

    // number of points
    public int size() {
        return bst.size();
    }

    // associate the value val with point p
    public void put(Point2D p, Value val) {
        if (p == null || val == null)
            throw new IllegalArgumentException();
        bst.put(p, val);
    }

    // value associated with point p
    public Value get(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return bst.get(p);
    }

    // does the symbol table contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return bst.contains(p);
    }

    // all points in the symbol table
    public Iterable<Point2D> points() {
        return bst.keys();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        Queue<Point2D> queue = new Queue<Point2D>();
        for (Point2D point : bst.keys())
            if (rect.contains(point))
                queue.enqueue(point);
        return queue;
    }

    // a nearest neighbor of point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        double min = -1;
        double dist;
        Point2D near = null;
        for (Point2D point : bst.keys()) {
            dist = p.distanceSquaredTo(point);
            if (dist < min || min == -1) {
                min = dist;
                near = point;
            }
        }
        return near;
    }

    // unit testing
    public static void main(String[] args) {
        PointST<Integer> st = new PointST<Integer>();
        st.put(new Point2D(2,3), 5);
        st.put(new Point2D(1,2), 6);
        st.put(new Point2D(5,8), 7);
        StdOut.println("> get(1,2)");
        StdOut.println(st.get(new Point2D(1,2)));
        StdOut.println("> contains(2,3)");
        if (st.contains(new Point2D(2,3)))
            StdOut.println("TEM O PONTO 2,3");
        StdOut.println("> contains(0,3)");
        if (!st.contains(new Point2D(0,3)))
            StdOut.println("NAO TEM O PONTO 0,3");
        StdOut.println("> st.points()");
        for (Point2D point : st.points())
            StdOut.println(point.x() + ", " + point.y());
        StdOut.println("> nearest(2,3)");
        Point2D a = st.nearest(new Point2D(2,3));
        StdOut.println(a.x() + ", " + a.y());
        StdOut.println("> nearest(2.1,3.1)");
        Point2D b = st.nearest(new Point2D(2.1,3.1));
        StdOut.println(b.x() + ", " + b.y());
        StdOut.println("> st.range((1,1),(3,5))");
        RectHV rect = new RectHV(1,1,3,5);
        for (Point2D point : st.range(rect))
            StdOut.println(point.x() + ", " + point.y());
    }

}
