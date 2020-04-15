/*
 * MAC0323 Algoritmos e Estruturas de Dados II
 *
 * ADT Digraph implementada atrevés de vetor de listas de adjacência.
 * As listas de adjacência são bag de ints que são mais restritos
 * que as bags genéricas do EP12. Veja a api bag.h e simplifique
 * o EP12 de acordo.
 *
 * Busque inspiração em:
 *
 *    https://algs4.cs.princeton.edu/42digraph/ (Graph representation)
 *    https://algs4.cs.princeton.edu/42digraph/Digraph.java.html
 *
 * DIGRAPH
 *
 * Digraph representa um grafo orientado de vértices inteiros de 0 a V-1.
 *
 * As principais operações são: add() que insere um arco no digrafo, e
 * adj() que itera sobre todos os vértices adjacentes a um dado vértice.
 *
 * Arcos paralelos e laços são permitidos.
 *
 * Esta implementação usa uma representação de _vetor de listas de adjacência_,
 * que  é uma vetor de objetos Bag indexado por vértices.

 * ATENÇÃO: Por simplicidade esses Bag podem ser int's e não de Integer's.
 *
 * Todas as operações consomen no pior caso tempo constante, exceto
 * iterar sobre os vértices adjacentes a um determinado vértice, cujo
 * consumo de tempo é proporcional ao número de tais vértices.
 *
 * Para documentação adicional, ver
 * https://algs4.cs.princeton.edu/42digraph, Seção 4.2 de
 * Algorithms, 4th Edition por Robert Sedgewick e Kevin Wayne.
 *
 */

/* interface para o uso da funcao deste módulo */
#include "digraph.h"


#include "bag.h"     /* add() e itens() */
#include <stdio.h>   /* fopen(), fclose(), fscanf(), ... */
#include <stdlib.h>  /* free() */
#include <string.h>  /* memcpy() */
#include "util.h"    /* emalloc(), ecalloc() */
#include <ctype.h>

#undef DEBUG
#ifdef DEBUG
#include <stdio.h>   /* printf(): para debuging */
#endif


/*----------------------------------------------------------*/
/*
 * Estrutura básica de um Digraph
 *
 * Implementação com vetor de listas de adjacência.
 */
struct digraph
{
  int V;          /* number of vertices in this digraph */
  int E;          /* number of edges in this digraph */
  Bag *adj;      /* adj[v] = adjacency list for vertex v */
  int *indegree; /* indegree[v] = indegree of vertex v */
};

/*------------------------------------------------------------*/
/*
 * Protótipos de funções administrativas: tem modificador 'static'
 *
 */
static Bag cloneBag(Bag bag, Bag bog);
static void validateVertex(Digraph G, vertex v);
/*-----------------------------------------------------------*/
/*
 *  newDigraph(V)
 *
 *  RECEBE um inteiro V.
 *  RETORNA um digrafo com V vértices e 0 arcos.
 *
 */
Digraph
newDigraph(int V)
{
  int v;
  Digraph G;
  if (V < 0) {
      ERROR("vertex " + v + " is not between 0 and " + (G->V-1));
      return NULL;
  }
  G = emalloc(sizeof(struct digraph));
  G->V = V;
  G->E = 0;
  G->indegree = emalloc(V*sizeof(int));
  G->adj = emalloc(V*sizeof(Bag));
  for (v = 0; v < V; v++) {
      G->adj[v] = newBag();
  }
  return G;
}

/*-----------------------------------------------------------*/
/*
 *  cloneDigraph(G)
 *
 *  RECEBE um digrafo G.
 *  RETORNA um clone de G.
 *
 */
Digraph
cloneDigraph(Digraph G)
{
  int i;
  Digraph H = newDigraph(G->V);
  H->E = G->E;
  for (i = 0; i < G->V; i++)
    H->indegree[i] = G->indegree[i];
  for (i = 0; i < G->V; i++)
    H->adj[i] = cloneBag(G->adj[i], H->adj[i]);
  return H;
}

/*-----------------------------------------------------------*/
/*
 *  reverseDigraph(G)
 *
 *  RECEBE um digrafo G.
 *  RETORNA o digrafo R que é o reverso de G:
 *
 *      v-w é arco de G <=> w-v é arco de R.
 *
 */
Digraph
reverseDigraph(Digraph G)
{
  int i;
  vertex v, w;
  Digraph H = newDigraph(G->V);
  for (v = 0; v < G->V; v++) {
    w = adj(G, v, TRUE);
    if (w != -1)
      addEdge(H, w, v);
    for (i = 1; i < size(G->adj[v]); i++) {
      w = adj(G, v, FALSE);
      if (w == -1)
        break;
      addEdge(H, w, v);
    }
  }
  return H;
}

/*-----------------------------------------------------------*/
/*
 *  readDigraph(NOMEARQ)
 *
 *  RECEBE uma stringa NOMEARQ.
 *  RETORNA o digrafo cuja representação está no arquivo de nome NOMEARQ.
 *  O arquivo contém o número de vértices V, seguido pelo número de arestas E,
 *  seguidos de E pares de vértices, com cada entrada separada por espaços.
 *
 *  Veja os arquivos  tinyDG.txt, mediumDG.txt e largeDG.txt na página do
 *  EP e que foram copiados do algs4,
 *
 */
 Digraph
 readDigraph(String nomeArq)
 {
   int V, i, j, E;
   vertex v = 0, w = 0;
   String pair;
   Digraph G;

   FILE *file = fopen(nomeArq, "r");

   pair = getLine(file);
   V = atoi(pair);
   G = newDigraph(V);
   free(pair);

   pair = getLine(file);
   E = atoi(pair);
   free(pair);
   for (i = 0; i < E; i++) {
     v = 0, w = 0;
     pair = getLine(file);
     j = 0;
     while (!isalnum(pair[j]))
       j++;
     while (isalnum(pair[j])) {
       v = 10*v + (pair[j]-48);
       j++;
     }
     while (!isalnum(pair[j]))
       j++;
     while (pair[j] != EOF && isalnum(pair[j])) {
       w = 10*w + (pair[j]-48);
       j++;
     }
     addEdge(G, v, w);
     free(pair);
   }
   fclose(file);
   return G;
 }
/*-----------------------------------------------------------*/
/*
 *  freeDigraph(G)
 *
 *  RECEBE um digrafo G e retorna ao sistema toda a memória
 *  usada por G.
 *
 */
void
freeDigraph(Digraph G)
{
  int v;
  for (v = 0; v < G->V; v++)
    freeBag(G->adj[v]);
  free(G->adj);
  free(G->indegree);
  free(G);
}

/*------------------------------------------------------------*/
/*
 * OPERAÇÕES USUAIS:
 *
 *     - vDigraph(), eDigraph(): número de vértices e arcos
 *     - addEdge(): insere um arco
 *     - adj(): itera sobre os vizinhos de um dado vértice
 *     - outDegree(), inDegree(): grau de saída e de entrada
 *     - toString(): usada para exibir o digrafo
 */

/*-----------------------------------------------------------*/
/*
 *  VDIGRAPH(G)
 *
 *  RECEBE um digrafo G e RETORNA seu número de vertices.
 *
 */
int
vDigraph(Digraph G)
{
    return G->V;

}

/*-----------------------------------------------------------*/
/*
 *  EDIGRAPH(G)
 *
 *  RECEBE um digrafo G e RETORNA seu número de arcos (edges).
 *
 */
int
eDigraph(Digraph G)
{
  return G->E;
}

/*-----------------------------------------------------------*/
/*
 *  addEdge(G, V, W)
 *
 *  RECEBE um digrafo G e vértice V e W e INSERE o arco V-W
 *  em G.
 *
 */
void
addEdge(Digraph G, vertex v, vertex w)
{
  validateVertex(G, v);
  validateVertex(G, w);
  add(G->adj[v], w);
  G->indegree[w]++;
  G->E++;
}


/*-----------------------------------------------------------*/
/*
 *  ADJ(G, V, INIT)
 *
 *  RECEBE um digrafo G, um vértice v de G e um Bool INIT.
 *
 *  Se INIT é TRUE,  ADJ() RETORNA o primeiro vértice na lista de adjacência de V.
 *  Se INIT é FALSE, ADJ() RETORNA o sucessor na lista de adjacência de V do
 *                   último vértice retornado.
 *  Se a lista de adjacência de V é vazia ou não há sucessor do último vértice
 *  retornada, ADJ() RETORNA -1.
 *
 *  Se entre duas chamadas de ADJ() a lista de adjacência de V é alterada,
 *  o comportamento é  indefinido.
 *
 */
int
adj(Digraph G, vertex v, Bool init)
{
    return itens(G->adj[v], init);
}

/*-----------------------------------------------------------*/
/*
 *  outDegree(G, V)
 *
 *  RECEBE um digrafo G e vértice V.
 *  RETORNA o número de arcos saindo de V.
 *
 */
int
outDegree(Digraph G, vertex v)
{
    validateVertex(G, v);
    return size(G->adj[v]);
}

/*-----------------------------------------------------------*/
/*
 *  inDegree(G, V)
 *
 *  RECEBE um digrafo G e vértice V.
 *  RETORNA o número de arcos entrando em V.
 *
 */
int
inDegree(Digraph G, vertex v)
{
    validateVertex(G, v);
    return G->indegree[v];
}


/*-----------------------------------------------------------*/
/*
 *  toString(G)
 *
 *  RECEBE um digrafo G.
 *  RETORNA uma string que representa G. Essa string será usada
 *  para exibir o digrafo: printf("%s", toString(G));
 *
 *  Sigestão: para fazer esta função inspire-se no método
 *  toString() da classe Digraph do algs4.
 */
String
toString(Digraph G)
{
  int i, j;
  String aux, str;

  str = emalloc((G->V*G->V*12+100)*sizeof(char));
  sprintf(str, "%d", G->V);
  strcat(str," vertices, ");

  aux = emalloc(12*sizeof(char));
  memset(aux, 0, 12*sizeof(char));
  sprintf(aux, "%d", G->E);
  strcat(str, aux);
  strcat(str, " edges\n");

  for (i = 0; i < G->V; i++) {

    memset(aux, 0, 12*sizeof(char));
    sprintf(aux, "%d: ", i);
    strcat(str, aux);

    for (j = adj(G, i, TRUE); j >= 0; j = adj(G, i, FALSE))
    {
        memset(aux, 0, 12*sizeof(char));
        sprintf(aux, "%d", j);
        strcat(str, (aux));
        strcat(str, (" "));
    }
    strcat(str, "\n");

  }
  free(aux);
  return str;
}


/*------------------------------------------------------------*/
/*
 * Implementaçao de funções administrativas: têm o modificador
 * static.
 */
static Bag
cloneBag(Bag bag, Bag bog)
{
  int i, item;
  if (size(bag) > 0) {
    item = itens(bag, TRUE);
    add(bog, item);
  }
  for (i = 1; i < size(bag); i++) {
    item = itens(bag, FALSE);
    add(bog, item);
  }
  return bog;
}

static void
validateVertex(Digraph G, vertex v)
{
  if (v < 0 || v >= G->V) {
      ERROR("vertex " + v + " is not between 0 and " + (G->V-1));
      return;
  }
}
