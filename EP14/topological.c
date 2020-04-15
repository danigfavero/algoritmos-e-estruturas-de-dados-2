/*
 * MAC0323 Algoritmos e Estruturas de Dados II
 *
 * ADT Topological é uma "representação topológica" de digrafo.
 * Esta implementação usa ADT Digraph do EP13.
 *
 * Busque inspiração em:
 *
 *   https://algs4.cs.princeton.edu/42digraph/
 *   https://algs4.cs.princeton.edu/42digraph/DepthFirstOrder.java
 *   https://algs4.cs.princeton.edu/42digraph/Topological.java
 *   https://algs4.cs.princeton.edu/42digraph/DirectedCycle.java
 *
 * TOPOLOGICAL
 *
 * Topological é uma ¨representação topológica" de um dado digrafo.
 *
 * As principais operações são:
 *
 *      - hasCycle(): indica se o digrafo tem um ciclo (DirectedCycle.java)
 *      - isDag(): indica se o digrafo é acyclico (Topological.java)
 *
 *      - pre(): retorna a numeração pré-ordem de um vértice em relação a uma dfs
 *               (DepthFirstOrder.java)
 *      - pos(): retorna a numareção pós-ordem de um vértice em relação a uma dfs
 *               (DepthFirstOrder.java)
 *      - rank(): retorna a numeração topológica de um vértice (Topological.java)
 *
 *      - preorder(): itera sobre todos os vértices do digrafo em pré-ordem
 *                    (em relação a uma dfs, DepthFirstOrder.java)
 *      - postorder(): itera sobre todos os vértices do digrafo em pós-ordem
 *                    (em relação a uma dfs, ordenação topologica reversa,
 *                     DepthFirstOrder.java)
 *      - order(): itera sobre todos os vértices do digrafo em ordem
 *                 topologica (Topological.java)
 *      - cycle(): itera sobre os vértices de um ciclo (DirectedCycle.java)
 *
 * O construtor e "destrutor" da classe consomem tempo linear..
 *
 * Cada chama das demais operações consome tempo constante.
 *
 * O espaço gasto por esta ADT é proporcional ao número de vértices V do digrafo.
 *
 * Para documentação adicional, ver
 * https://algs4.cs.princeton.edu/42digraph, Seção 4.2 de
 * Algorithms, 4th Edition por Robert Sedgewick e Kevin Wayne.
 *
 */

/* interface para o uso da funcao deste módulo */
#include "topological.h"

#include "digraph.h" /* Digraph, vDigraph(), eDigraph(), adj(), ... */
#include "bag.h"     /* add() e itens() */
#include "util.h"    /* emalloc(), ecalloc(), ERRO(), AVISO() */

#include <stdlib.h>  /* free() */

#undef DEBUG
#ifdef DEBUG
#include <stdio.h>   /* printf(): para debugging */
#endif

/*----------------------------------------------------------*/
/*
 * Estrutura básica de um Topological
 *
 */
struct topological {
    int V;
    int *cycle;
    int curCycle;
    int *edgeTo;
    Bool *onStack;
    int stacked;
    int *pre;
    int *preOrder;
    int curPre;
    int preCounter;
    int *post;
    int *postOrder;
    int curPost;
    int postCounter;
    int curOrder;
    Bool *marked;

};

/*------------------------------------------------------------*/
/*
 * Protótipos de funções administrativas: tem modificador 'static'
 *
 */
static void dfs();

/*-----------------------------------------------------------*/
/*
 *  newTopologica(G)
 *
 *  RECEBE um digrafo G.
 *  RETORNA uma representação topológica de G.
 *
 */
Topological newTopological(Digraph G) {
    int v, i;
    Topological ts = emalloc(sizeof *ts);
    ts->V = vDigraph(G);
    ts->curPre = 0; ts->curPost = 0;
    ts->curOrder = ts->V; ts->curCycle = 0;
    ts->preCounter = 0; ts->postCounter = 0;
    ts->marked = ecalloc((ts->V)*sizeof(int), sizeof(int));
    ts->pre = emalloc((ts->V)*sizeof(int));
    ts->preOrder = emalloc((ts->V)*sizeof(int));
    ts->post = emalloc((ts->V)*sizeof(int));
    ts->postOrder = emalloc((ts->V)*sizeof(int));
    for (i = 0; i < ts->V; i++)
        ts->postOrder[i] = 1337;
    ts->edgeTo = emalloc((ts->V)*sizeof(int));
    ts->onStack = ecalloc((ts->V)*sizeof(int), sizeof(int));
    ts->stacked = 0;
    ts->cycle = NULL;
    for (v = 0; v < (ts->V); v++)
            if (!ts->marked[v] && ts->cycle == NULL)
                dfs(ts, G, v);
    return ts;
}

/*-----------------------------------------------------------*/
/*
 *  freeTopological(TS)
 *
 *  RECEBE uma representação topologica TS.
 *  DEVOLVE ao sistema toda a memória usada por TS.
 *
 */
void freeTopological(Topological ts) {
    free(ts->marked);
    free(ts->pre);
    free(ts->post);
    free(ts->edgeTo);
    free(ts->cycle);
    free(ts->onStack);
    free(ts->preOrder);
    free(ts->postOrder);
    free(ts);
}

/*------------------------------------------------------------*/
/*
 *  OPERAÇÕES:
 *
 */

/*-----------------------------------------------------------*/
/*
 *  HASCYCLE(TS)
 *
 *  RECEBE uma representação topológica TS de um digrafo;
 *  RETORNA TRUE seu o digrafo possui um ciclo e FALSE em caso
 *  contrário.
 *
 */
Bool hasCycle(Topological ts) {
    return ts->cycle != NULL;
}

/*-----------------------------------------------------------*/
/*
 *  ISDAG(TS)
 *
 *  RECEBE um representação topológica TS de um digrafo.
 *  RETORNA TRUE se o digrafo for um DAG e FALSE em caso
 *  contrário.
 *undercut
 */
Bool isDag(Topological ts) {
    return !hasCycle(ts);
}

/*-----------------------------------------------------------*/
/*
 *  PRE(TS, V)
 *
 *  RECEBE uma representação topológica TS de um digrafo e um
 *  vértice V.
 *  RETORNA a numeração pré-ordem de V em TS.
 *
 */
int pre(Topological ts, vertex v) {
    return ts->pre[v];
}

/*-----------------------------------------------------------*/
/*
 *  POST(TS, V)
 *
 *  RECEBE uma representação topológica TS de um digrafo e um
 *  vértice V.
 *  RETORNA a numeração pós-ordem de V em TS.
 *
 */
int post(Topological ts, vertex v) {
    return ts->post[v];
}

/*-----------------------------------------------------------*/
/*
 *  RANK(TS, V)
 *
 *  RECEBE uma representação topológica TS de um digrafo e um
 *  vértice V.
 *  RETORNA a posição de V na ordenação topológica em TS;
 *  retorna -1 se o digrafo não for um DAG.
 *
 */
int rank(Topological ts, vertex v) {
    if (!isDag(ts))
        return -1;
    return ts->V - ts->post[v] - 1;
}

/*-----------------------------------------------------------*/
/*
 *  PREORDER(TS, INIT)
 *
 *  RECEBE uma representação topológica TS de um digrafo e um
 *  Bool INIT.
 *
 *  Se INIT é TRUE,  PREORDER() RETORNA o primeiro vértice na ordenação pré-ordem de TS.
 *  Se INIT é FALSE, PREORDER() RETORNA o vértice sucessor do último vértice retornado
 *                   na ordenação pré-ordem de TS; se todos os vértices já foram retornados,
 *                   a função retorna -1.
 */
vertex preorder(Topological ts, Bool init) {
    if (ts->curPre >= ts->V-1)
        return -1;
    if (init)
        ts->curPre = -1;
    ts->curPre++;
    return ts->preOrder[ts->curPre];
}

/*-----------------------------------------------------------*/
/*
 *  POSTORDER(TS, INIT)
 *
 *  RECEBE uma representação topológica TS de um digrafo e um
 *  Bool INIT.
 *
 *  Se INIT é TRUE,  POSTORDER() RETORNA o primeiro vértice na ordenação pós-ordem de TS.
 *  Se INIT é FALSE, POSTORDER() RETORNA o vértice sucessor do último vértice retornado
 *                   na ordenação pós-ordem de TS; se todos os vértices já foram retornados,
 *                   a função retorna -1.
 */
vertex postorder(Topological ts, Bool init) {
    if (ts->curPost >= ts->V-1)
        return -1;
    if (init)
        ts->curPost = -1;
    ts->curPost++;
    return ts->postOrder[ts->curPost];
}

/*-----------------------------------------------------------*/
/*
 *  ORDER(TS, INIT)
 *
 *  RECEBE uma representação topológica TS de um digrafo e um Bool INIT.
 *
 *  Se INIT é TRUE,  ORDER() RETORNA o primeiro vértice na ordenação topológica
 *                   de TS.
 *  Se INIT é FALSE, ORDER() RETORNA o vértice sucessor do último vértice retornado
 *                   na ordenação topológica de TS; se todos os vértices já foram
 *                   retornados, a função retorna -1.
 *
 *  Se o digrafo _não_ é um DAG, ORDER() RETORNA -1.
 */
vertex order(Topological ts, Bool init) {
    if (!isDag(ts) || ts->curOrder <= 0)
        return -1;
    if (init)
        ts->curOrder = ts->V;
    ts->curOrder--;
    return ts->postOrder[ts->curOrder];
}

/*-----------------------------------------------------------*/
/*
 *  CYCLE(TS, INIT)
 *
 *  RECEBE uma representação topológica TS de um digrafo e um Bool INIT.
 *
 *  Se INIT é TRUE,  CYCLE() RETORNA um vértice em um ciclo do digrafo.
 *  Se INIT é FALSE, CYCLE() RETORNA o vértice  no ciclo que é sucessor do
 *                   último vértice retornado; se todos os vértices no ciclo já
 *                   foram retornados, a função retorna -1.
 *
 *  Se o digrafo é um DAG, CYCLE() RETORNA -1.
 *
 */
vertex cycle(Topological ts, Bool init) {
    if (isDag(ts) || ts->curCycle >= ts->V-1)
        return -1;
    if (init)
        ts->curCycle = -1;
    ts->curCycle++;
    return ts->cycle[ts->curCycle];
}


/*------------------------------------------------------------*/
/*
 * Implementaçao de funções administrativas: têm o modificador
 * static.
 */

static void dfs(Topological ts, Digraph G, vertex v) {
    int w, x, i;
    ts->marked[v] = TRUE;
    ts->onStack[v] = TRUE;
    ts->stacked++;
    i = ts->stacked - 1;
    ts->pre[v] = ts->preCounter;
    ts->preOrder[ts->preCounter++] = v;
    for (w = adj(G, v, TRUE); w >= 0; w = adj(G, v, FALSE)) {
        if (ts->cycle != NULL)
            return;
        if (!ts->marked[w]) {
            ts->edgeTo[w] = v;
            dfs(ts, G, w);
        }
        else if (ts->onStack[w]) {
            ts->cycle = emalloc(ts->stacked*sizeof(int));
            ts->cycle[0] = w;
            for (x = v; x != w; x = ts->edgeTo[x]) {
                    if (i == 0) {
                        ERROR("cycle[0] has already been taken!\n");
                        return;
                    }
                    ts->cycle[i] = x;
                    i--;
                }
        }
    }
    ts->postOrder[ts->postCounter] = v;
    ts->post[v] = ts->postCounter++;
    ts->onStack[v] = FALSE;
    ts->stacked--;
}
