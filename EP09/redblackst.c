/*
 * MAC0323 Estruturas de Dados e Algoritmo II
 *
 * Tabela de simbolos implementada atraves de uma BST rubro-negra
 *
 *     https://algs4.cs.princeton.edu/33balanced/RedBlackBST.java.html
 *
 * As chaves e valores desta implementação são mais ou menos
 * genéricos
 */

/* interface para o uso da funcao deste módulo */
#include "redblackst.h"

#include <stdlib.h>  /* free() */
#include <string.h>  /* memcpy() */
#include "util.h"    /* emalloc(), ecalloc() */

#undef DEBUG
#ifdef DEBUG
#include <stdio.h>   /* printf(): para debug */
#endif

/*
 * CONSTANTES
 */
#define RED   TRUE
#define BLACK FALSE

/*----------------------------------------------------------*/
/*
 * Estrutura Básica da Tabela de Símbolos:
 *
 * implementação com árvore rubro-negra
 */
typedef struct node Node;
struct redBlackST {
    struct node* root; /*root of the BST*/
    int (*compar)(const void *key1, const void *key2); /*compare func*/
};

/*----------------------------------------------------------*/
/*
 * Estrutura de um nó da árvore
 *
 */
struct node {
    void* key; /*key*/
    void* val; /*associated data*/
    struct node* left; /*link to left subtree*/
    struct node* right; /*link to right subtree*/
    Bool color; /*color of parent link*/
    int size; /*subtree count*/
};

/*------------------------------------------------------------*/
/*
 *  Protótipos de funções administrativas.
 *
 *  Entre essa funções estão isRed(), rotateLeft(), rotateRight(),
 *  flipColors(), moveRedLeft(), moveRedRight() e balance().
 *
 *  Não deixe de implmentar as funções chamadas pela função
 *  check(): isBST(), isSizeConsistent(), isRankConsistent(),
 *  is23(), isBalanced().
 *
 */
 /*---------------------------------------------------------------*/
static Node
newNode(void* key, void* val, void* key, Bool color, node* left, node* right, int size) {
    Node n = emalloc(sizeof(Node node));
    n->key = key;
    n->val = val;
    n->color = color;
    n->left = left;
    n->right = right;
    n->size = size;
}


/*---------------------------------------------------------------*/
static Bool
isBST(RedBlackST st);

/*---------------------------------------------------------------*/
static Bool
isSizeConsistent(RedBlackST st);

/*---------------------------------------------------------------*/
static Bool
isRankConsistent(RedBlackST st);

/*---------------------------------------------------------------*/
static Bool
is23(RedBlackST st);

/*---------------------------------------------------------------*/
static Bool
isBalanced(RedBlackST st);

/*---------------------------------------------------------------*/

static Bool
isRed(Node x) {
       if (x == null) return false;
       return x->color == RED;
}

/*-----------------------------------------------------------*/
/*
 *  initST(COMPAR)
 *
 *  RECEBE uma função COMPAR() para comparar chaves.
 *  RETORNA (referência/ponteiro para) uma tabela de símbolos vazia.
 *
 *  É esperado que COMPAR() tenha o seguinte comportamento:
 *
 *      COMPAR(key1, key2) retorna um inteiro < 0 se key1 <  key2
 *      COMPAR(key1, key2) retorna 0              se key1 == key2
 *      COMPAR(key1, key2) retorna um inteiro > 0 se key1 >  key2
 *
 *  TODAS OS OPERAÇÕES da ST criada utilizam a COMPAR() para comparar
 *  chaves.
 *
 */
RedBlackST
initST(int (*compar)(const void *key1, const void *key2)) {
    RedBlackST st = emalloc(sizeof(struct redBlackST));
    st->compar = compar;
    st->root = NULL;
    return NULL;
}

/*-----------------------------------------------------------*/
/*
 *  freeST(ST)
 *
 *  RECEBE uma RedBlackST  ST e devolve ao sistema toda a memoria
 *  utilizada por ST.
 *
 */
void
freeST(RedBlackST st) {
    return;
}


/*------------------------------------------------------------*/
/*
 * OPERAÇÕES USUAIS: put(), get(), contains(), delete(),
 * size() e isEmpty().
 */

/*-----------------------------------------------------------*/
/*
 *  put(ST, KEY, NKEY, VAL, NVAL)
 *
 *  RECEBE a tabela de símbolos ST e um par KEY-VAL e procura a KEY na ST.
 *
 *     - se VAL é NULL, a entrada da chave KEY é removida da ST
 *
 *     - se KEY nao e' encontrada: o par KEY-VAL é inserido na ST
 *
 *     - se KEY e' encontra: o valor correspondente é atualizado
 *
 *  NKEY é o número de bytes de KEY e NVAL é o número de bytes de NVAL.
 *
 *  Para criar uma copia/clone de KEY é usado o seu número de bytes NKEY.
 *  Para criar uma copia/clode de VAL é usado o seu número de bytes NVAL.
 *
 */

static Node putAux(Node h, void* key, void* val) {
    int cmp;
    if (h == null)
        return newNode(key, val, RED, left, right, size); /*FIXME*/
    cmp = st->compar(key, h->key));
    if (cmp < 0)
        h->left = putAux(h->left, key, val);
    else if (cmp > 0)
        h->right = putAux(h->right, key, val);
    else
        h->val = val;
    /* fix-up any right-leaning links */
    if (isRed(h->right) && !isRed(h->left))
        h = rotateLeft(h);
    if (isRed(h->left) && isRed(h->left->left))
        h = rotateRight(h);
    if (isRed(h->left) && isRed(h->right))
        flipColors(h);
    h->size = size(h->left) + size(h->right) + 1;
    return h;
}

void
put(RedBlackST st, const void *key, size_t sizeKey, const void *val, size_t sizeVal) {

}


/*-----------------------------------------------------------*/
/*
 *  get(ST, KEY)
 *
 *  RECEBE uma tabela de símbolos ST e uma chave KEY.
 *
 *     - se KEY está em ST, RETORNA NULL;
 *
 *     - se KEY não está em ST, RETORNA uma cópia/clone do valor
 *       associado a KEY.
 *
 */
void *
get(RedBlackST st, const void *key)
{
    return NULL;
}


/*-----------------------------------------------------------*/
/*
 *  CONTAINS(ST, KEY)
 *
 *  RECEBE uma tabela de símbolos ST e uma chave KEY.
 *
 *  RETORNA TRUE se KEY está na ST e FALSE em caso contrário.
 *
 */
Bool
contains(RedBlackST st, const void *key)
{
    return FALSE;
}

/*-----------------------------------------------------------*/
/*
 *  DELETE(ST, KEY)
 *
 *  RECEBE uma tabela de símbolos ST e uma chave KEY.
 *
 *  Se KEY está em ST, remove a entrada correspondente a KEY.
 *  Se KEY não está em ST, faz nada.
 *
 */
void
delete(RedBlackST st, const void *key)
{
}


/*-----------------------------------------------------------*/
/*
 *  SIZE(ST)
 *
 *  RECEBE uma tabela de símbolos ST.
 *
 *  RETORNA o número de itens (= pares chave-valor) na ST.
 *
 */
int
size(RedBlackST st) {
    return st->root->size;
}


/*-----------------------------------------------------------*/
/*
 *  ISEMPTY(ST, KEY)
 *
 *  RECEBE uma tabela de símbolos ST.
 *
 *  RETORNA TRUE se ST está vazia e FALSE em caso contrário.
 *
 */
Bool
isEmpty(RedBlackST st) {
    return size(st) == 0;
}

/*------------------------------------------------------------*/
/*
 * OPERAÇÕES PARA TABELAS DE SÍMBOLOS ORDENADAS:
 * min(), max(), rank(), select(), deleteMin() e deleteMax().
 */

/*-----------------------------------------------------------*/
/*
 *  MIN(ST)
 *
 *  RECEBE uma tabela de símbolos ST e RETORNA uma cópia/clone
 *  da menor chave na tabela.
 *
 *  Se ST está vazia RETORNA NULL.
 *
 */
void *
min(RedBlackST st)
{
    return NULL;
}


/*-----------------------------------------------------------*/
/*
 *  MAX(ST)
 *
 *  RECEBE uma tabela de símbolos ST e RETORNA uma cópia/clone
 *  da maior chave na tabela.
 *
 *  Se ST está vazia RETORNA NULL.
 *
 */
void *
max(RedBlackST st)
{
    return NULL;
}


/*-----------------------------------------------------------*/
/*
 *  RANK(ST, KEY)
 *
 *  RECEBE uma tabela de símbolos ST e uma chave KEY.
 *  RETORNA o número de chaves em ST menores que KEY.
 *
 *  Se ST está vazia RETORNA NULL.
 *
 */
int
rank(RedBlackST st, const void *key)
{
    return 0;
}


/*-----------------------------------------------------------*/
/*
 *  SELECT(ST, K)
 *
 *  RECEBE uma tabela de símbolos ST e um inteiro K >= 0.
 *  RETORNA a (K+1)-ésima menor chave da tabela ST.
 *
 *  Se ST não tem K+1 elementos RETORNA NULL.
 *
 */
void *
select(RedBlackST st, int k)
{
    return NULL;
}


/*-----------------------------------------------------------*/
/*
 *  deleteMIN(ST)
 *
 *  RECEBE uma tabela de símbolos ST e remove a entrada correspondente
 *  à menor chave.
 *
 *  Se ST está vazia, faz nada.
 *
 */
void
deleteMin(RedBlackST st)
{
}


/*-----------------------------------------------------------*/
/*
 *  deleteMAX(ST)
 *
 *  RECEBE uma tabela de símbolos ST e remove a entrada correspondente
 *  à maior chave.
 *
 *  Se ST está vazia, faz nada.
 *
 */
void
deleteMax(RedBlackST st)
{
}


/*-----------------------------------------------------------*/
/*
 *  KEYS(ST, INIT)
 *
 *  RECEBE uma tabela de símbolos ST e um Bool INIT.
 *
 *  Se INIT é TRUE, KEYS() RETORNA uma cópia/clone da menor chave na ST.
 *  Se INIT é FALSE, KEYS() RETORNA a chave sucessora da última chave retornada.
 *  Se ST está vazia ou não há sucessora da última chave retornada, KEYS() retorna NULL.
 *
 *  Se entre duas chamadas de KEYS() a ST é alterada, o comportamento é
 *  indefinido.
 *
 */
void *
keys(RedBlackST st, Bool init)
{
    return NULL;
}



/*------------------------------------------------------------*/
/*
 * Funções administrativas
 */

/***************************************************************************
 *  Utility functions.
 ***************************************************************************/

/*
 * HEIGHT(ST)
 *
 * RECEBE uma RedBlackST e RETORNA a sua altura.
 * Uma BST com apenas um nó tem altura zero.
 *
 */
int
height(RedBlackST st)
{
        return 0;
}


/***************************************************************************
 *  Check integrity of red-black tree data structure.
 ***************************************************************************/

/*
 * CHECK(ST)
 *
 * RECEBE uma RedBlackST ST e RETORNA TRUE se não encontrar algum
 * problema de ordem ou estrutural. Em caso contrário RETORNA
 * FALSE.
 *
 */
Bool
check(RedBlackST st)
{
    if (!isBST(st))            ERROR("check(): not in symmetric order");
    if (!isSizeConsistent(st)) ERROR("check(): subtree counts not consistent");
    if (!isRankConsistent(st)) ERROR("check(): ranks not consistent");
    if (!is23(st))             ERROR("check(): not a 2-3 tree");
    if (!isBalanced(st))       ERROR("check(): not balanced");
    return isBST(st) && isSizeConsistent(st) && isRankConsistent(st) && is23(st) && isBalanced(st);
}


/*
 * ISBST(ST)
 *
 * RECEBE uma RedBlackST ST.
 * RETORNA TRUE se a árvore é uma BST.
 *
 */
static Bool
isBST(RedBlackST st)
{
    return FALSE;
}


/*
 *  ISSIZECONSISTENT(ST)
 *
 *  RECEBE uma RedBlackST ST e RETORNA TRUE se para cada nó h
 *  vale que size(h) = 1 + size(h->left) + size(h->right) e
 *  FALSE em caso contrário.
 */
static Bool
isSizeConsistent(RedBlackST st)
{
    return FALSE;
}


/*
 *  ISRANKCONSISTENT(ST)
 *
 *  RECEBE uma RedBlackST ST e RETORNA TRUE se seus rank() e
 *  select() são consistentes.
 */
/* check that ranks are consistent */
static Bool
isRankConsistent(RedBlackST st)
{
    return FALSE;
}

/*
 *  IS23(ST)
 *
 *  RECEBE uma RedBlackST ST e RETORNA FALSE se há algum link RED
 *  para a direta ou se ha dois links para esquerda seguidos RED
 *  Em caso contrário RETORNA TRUE (= a ST representa uma árvore 2-3).
 */
static Bool
is23(RedBlackST st)
{
    return FALSE;
}


/*
 *  ISBALANCED(ST)
 *
 *  RECEBE uma RedBlackST ST e RETORNA TRUE se st satisfaz
 *  balanceamento negro perfeiro.
 */
static Bool
isBalanced(RedBlackST st)
{
    return FALSE;
}
