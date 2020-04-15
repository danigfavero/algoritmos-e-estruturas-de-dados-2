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
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.MinPQ;
import java.lang.IllegalArgumentException;
import java.lang.Math;
import java.awt.Color;

public class SeamCarver {
    private int width;
    private int height;
    private int[][] pixels;
    private double[] energy;
    private double[] dist;
    private int[] edgeFrom;
    private Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException();
        width = picture.width();
        height = picture.height();
        this.picture = picture;
        pixels = new int[width][height];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                pixels[i][j] = picture.getRGB(i,j);
    }

    // current picture
    public Picture picture() {
        return picture;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            throw new IllegalArgumentException();
        return Math.sqrt(delta(x, y, 0) + delta(x, y, 1));
    }

    private double delta(int x, int y, int d) {
        int xi, xf, yi, yf;
        if (d == 0) { // x
            xi = (x - 1)%width;
            xf = (x + 1)%width;
            yi = y;
            yf = y;
        }
        else { // y
            xi = x;
            xf = x;
            yi = (y - 1)%height;
            yf = (y + 1)%height;
        }
        Picture pic = picture();
        Color i = pic.get(xi, yi);
        Color f = pic.get(xf, yf);
        double b = f.getBlue() - i.getBlue();
        double r = f.getRed() - i.getRed();
        double g = f.getGreen() - i.getGreen();
        double dif = b*b + r*r + g*g;
        return dif;
    }

    private class Node {
		int vertex;
		double val;

		Node(int vertex, double val) {
			this.vertex = vertex;
			this.val = val;
		}

		int compareTo(Node node) {
			if (this.val < node.val)
                return -1;
			if (this.val > node.val)
                return 1;
			return 0;
		}
	}

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transpose();
        int[] seam = findVerticalSeam();
        transpose();
        return seam;
    }


    private void transpose() {
        width = pixels[0].length;
        height = pixels.length;
        int[][] transposed = new int[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                transposed[j][i] = pixels[i][j];
            }
        }
        this.pixels = transposed;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        energy = new double[height * width + 2];
        energy[height * width + 1] = 0; // last
        edgeFrom = new int[height * width + 2];
        dist = new double[height * width + 2];
        dist[height * width + 1] = -1; // last
            for (int i = 0; i < width; i++) {
 		        for (int j = 0; j < height; j++) {
 			        energy[i*height + j] = energy(i, j);
                    if (i == 0)
                        dist[i*height + j] = energy[i*height+j];
                    else
                        dist[i*height + j] = -1;
 		    }
 	    }
        dijkstra();
 	    int [] seam = new int[height];
        int i = height - 1;
 	    int last = height*width;
 	    while(last != -1){
 	   		 seam[i--] = edgeFrom[last];
 	   		 last = edgeFrom[last];
 	    }
 	   return seam;
    }

    private void dijkstra() {
        MinPQ<Node> pq = new MinPQ<Node>();
 	    for (int i = 0; i < height; i++){
 		    pq.insert(new Node(i, energy[i]));
 	    }
 	    while(!pq.isEmpty()) {
 		    int vertex = (pq.delMin()).vertex;
 			if (vertex == height*width)
                break;
 			int x = vertex/width;
 			int y = vertex%width;
 			int last = (x - 1)*height + y + 1;
 			if (x - 1 >= 0 && ( dist[last] == -1 || dist[last] > dist[vertex] + energy[last] )) {
 				if (y+1 >= height && dist[height*width] > dist[vertex] + energy[height*width]) {
 					dist[height*width] = dist[vertex] + energy[height*width];
 					edgeFrom[height*width] = x;
 					pq.insert(new Node(height*width, dist[height*width]));
 				}
 				else {
 					dist[last] = dist[vertex] + energy[last];
 					edgeFrom[last] = x;
 					pq.insert(new Node(last, dist[last]));
 				}
 			}
 			last += height;
 			if (dist[last] == -1 || dist[last] > dist[vertex] + energy[last]) {
 				if (y+1 >= height && dist[height*width] > dist[vertex] + energy[height*width]) {
 					dist[height*width] = dist[vertex] + energy[height*width];
 					edgeFrom[height*width] = x;
 					pq.insert(new Node(height*width, dist[height*width]));
 				}
 				else {
 					dist[last] = dist[vertex] + energy[last];
 					edgeFrom[last] = x;
 					pq.insert(new Node(last, dist[last]));
 				}
 			}
 			last += height;
 			if ((dist[last] == -1 || dist[last] > dist[vertex] + energy[last])) {
 				if(y+1 >= height && dist[height*width] > dist[vertex] + energy[height*width]){
 					dist[height*width] = dist[vertex] + energy[height*width];
 					edgeFrom[height*width] = x;
 					pq.insert(new Node(height*width, dist[height*width]));
 				}
 				else {
 					dist[last] = dist[vertex] + energy[last];
 					edgeFrom[last] = x;
 					pq.insert(new Node(last, dist[last]));
 		        }
 			}
 	    }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || height == 1 || seam.length != width)
            throw new IllegalArgumentException();
        int[][] carvedPic = new int[height-1][width];
        Picture newPic = new Picture(height-1, width);
        for (int i = 0; i < width; i++) {
            int k = 0;
            for (int j = 0; j < height; j++) {
                if (seam[i] != j) {
                    carvedPic[i-1][k] = pixels[i][j];
                    newPic.set(i-1, k, picture.get(i,j));
    				k++;
                }
            }
        }
        height--;
        pixels = carvedPic;
        picture = newPic;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null || width == 1 || seam.length != height)
            throw new IllegalArgumentException();
        int[][] carvedPic = new int[height][width-1];
        Picture newPic = new Picture(height, width-1);
        for (int j = 0; j < height; j++) {
            int k = 0;
            for (int i = 0; i < width; i++) {
                if (seam[j] != i) {
                    carvedPic[k][j] = pixels[i][j];
                    newPic.set(k, j, picture.get(i,j));
    				k++;
                }
            }
        }
        width--;
        pixels = carvedPic;
    }

    //  unit testing (required)
    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        SeamCarver sc = new SeamCarver(picture);
        StdOut.printf(picture.toString());
        StdOut.printf("\n");
        for (int i = 0; i < sc.width; i++) {
            for (int j = 0; j < sc.height; j++) {
                StdOut.printf("%f\t", sc.energy(i,j));
            }
            StdOut.printf("\n");
        }
        StdOut.printf("\n");
        int[] sh = sc.findHorizontalSeam();
        for (int i = 0; i < sh.length; i++)
            StdOut.printf("%d\t", sh[i]);
        StdOut.printf("\n");
        sc.removeHorizontalSeam(sh);
        StdOut.printf(picture.toString());
        StdOut.printf("\n");
        int[] sv = sc.findVerticalSeam();
        for (int i = 0; i < sv.length; i++)
            StdOut.printf("%d\t", sv[i]);
        StdOut.printf("\n");
        sc.removeVerticalSeam(sv);
        StdOut.printf(picture.toString());
        StdOut.printf("\n");
    }

}
