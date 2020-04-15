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
import java.util.Comparator;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.MaxPQ;

public class KdTreeST<Value> {
    public Node root;
    public int n;
    public static Point2D pointCompared = null;
    private static final boolean VER = true;
    private static final boolean HOR = false;
    private static final double infPos = Double.POSITIVE_INFINITY;
    private static final double infNeg = Double.NEGATIVE_INFINITY;

    private class Node {
        private Point2D p;      // the point
        private Value value;    // the symbol table maps the point to this value
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subt (required)ree

        private Node(Point2D p, Value value, Node lb, Node rt) {
            this.p = p;
            this.value = value;
            this.lb = lb;
            this.rt = rt;
        }
    }

    // construct an empty symbol table of KdTrees
    public KdTreeST() {
        root = null;
        n = 0;
    }

    // is the symbol table empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of KdTrees
    public int size() {
        return n;
    }

    // associate the value val with point p
    public void put(Point2D p, Value val) {
        if (p == null || val == null)
            throw new IllegalArgumentException();
        root = put(p, val, root, VER);
        n++;
    }

    private Node put(Point2D point, Value val, Node root, boolean ori) {
        if (root == null)
            return new Node(point, val, null, null);
        if (ori == VER) {
            if (point.x() < root.p.x())
                root.lb = put(point, val, root.lb, HOR);
            else
                root.rt = put(point, val, root.rt, HOR);
        }
        else {
            if (point.y() < root.p.y())
                root.lb = put(point, val, root.lb, VER);
            else
                root.rt = put(point, val, root.rt, VER);
        }
        return root;
    }

    // value associated with point p
    public Value get(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        Node r = get(p, root, VER);
        if (r == null)
            return null;
        return r.value;
    }

    private Node get(Point2D point, Node root, boolean ori) {
        if (root == null)
            return null;
        if (root.p.x() == point.x() && root.p.y() == point.y())
            return root;
        if (ori == VER) {
            if (point.x() < root.p.x())
                return get(point, root.lb, HOR);
            return get(point, root.rt, HOR);
        }
        if (point.y() < root.p.y())
            return get(point, root.lb, VER);
        return get(point, root.rt, VER);
    }

    // does the symbol table contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return this.get(p) != null;
    }

    // all points in the symbol table
    public Iterable<Point2D> points() {
        Queue<Node> queue = new Queue<Node>();
        Queue<Point2D> list = new Queue<Point2D>();
        queue.enqueue(root);
        Node aux;
        while (!queue.isEmpty()) {
            aux = queue.dequeue();
            if (aux.lb != null)
                queue.enqueue(aux.lb);
            if (aux.rt != null)
                queue.enqueue(aux.rt);
            list.enqueue(aux.p);
        }
        return list;
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        Queue<Point2D> queue = new Queue<Point2D>();
        range(rect, root, new RectHV(infNeg, infNeg, infPos, infPos), VER, queue);
        return queue;
    }

    private void range(RectHV rect,
                       Node root,
                       RectHV area,
                       boolean ori,
                       Queue<Point2D> queue) {
        if (root == null || !rect.intersects(area))
            return;
        if (rect.contains(root.p)){
            queue.enqueue(root.p);
        }
        if (ori == VER) {
            RectHV left = new RectHV(area.xmin(), area.ymin(), root.p.x(), area.ymax());
            range(rect, root.lb, left, HOR, queue);
            RectHV right = new RectHV(root.p.x(), area.ymin(), area.xmax(), area.ymax());
            range(rect, root.rt, right, HOR, queue);
        }
        else {
            RectHV top = new RectHV(area.xmin(), root.p.y(), area.xmax(), area.ymax());
            range(rect, root.lb, top, VER, queue);
            RectHV bottom = new RectHV(area.xmin(), area.ymin(), area.xmax(), root.p.y());
            range(rect, root.rt, bottom, VER, queue);
        }
    }

    // a nearest neighbor of point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        double min = -1;
        double dist;
        Point2D near = null;
        for (Point2D point : this.points()) {
            dist = p.distanceSquaredTo(point);
            if (dist < min || min == -1) {
                min = dist;
                near = point;
            }
        }
        return near;
    }

    // returns the k points that are closest to the query point (in any order);
    // return all n points in the data structure if n <= k
    public Iterable<Point2D> nearest(Point2D p, int k) {
        if (p == null)
            throw new IllegalArgumentException();
        if (this.isEmpty())
            return null;
        pointCompared = p;
        MaxPQ<Point2D> pq = new MaxPQ<Point2D>(new Comparing());
        nearest(p, root, VER, pq);
        Queue<Point2D> queue = new Queue<Point2D>();
        int i = 0;
        for (Point2D point : pq) {
            queue.enqueue(point);
            i++;
            if (i >= k)
                break;
        }
        return queue;
    }

    private void nearest(Point2D point, Node root, boolean ori, MaxPQ<Point2D> pq) {
        if (root == null)
            return;
        nearest(point, root.lb, !ori, pq);
        nearest(point, root.rt, !ori, pq);
        pq.insert(root.p);
    }

    public static class Comparing implements Comparator<Point2D> {
        public int compare(Point2D p1, Point2D p2) {
            double dist1 = pointCompared.distanceSquaredTo(p1);
            double dist2 = pointCompared.distanceSquaredTo(p2);
            if (dist1 < dist2) return 1;
            if (dist1 > dist2) return -1;
            return 0;
        }
    }

    // unit testing
    public static void main(String[] args) {
        KdTreeST<Integer> st = new KdTreeST<Integer>();
        st.put(new Point2D(2,3), 5);
        st.put(new Point2D(5,8), 7);
        st.put(new Point2D(1,2), 6);
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
        StdOut.println("> st.nearest((1.1,2.1), 1)");
        for (Point2D point : st.nearest(new Point2D(1.1,2.1), 1))
            StdOut.println(point.x() + ", " + point.y());
        StdOut.println("> st.nearest((5.1,8), 2)");
        for (Point2D point : st.nearest(new Point2D(5.1,8), 2))
            StdOut.println(point.x() + ", " + point.y());
    }

}
