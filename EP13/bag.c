/*
 * MAC0323 Algoritmos e Estruturas de Dados II
 *
 * ADT Bag implementada com lista ligada de itens.
 *
 *    https://algs4.cs.princeton.edu/13stacks/
 *    https://www.ime.usp.br/~pf/estruturas-de-dados/aulas/bag.html
 *
 * ATENÇÃO: por simplicidade Bag contém apenas inteiros (int) não
 * negativos (>=0) que são 'nomes' de vértices (vertex) de um
 * digrafo.
 */

/* interface para o uso da funcao deste módulo */
#include "bag.h"

#include <stdlib.h>  /* free() */
#include <string.h>  /* memcpy() */
#include "util.h"    /* emalloc() */

#undef DEBUG
#ifdef DEBUG
#include <stdio.h>   /* printf(): para debuging */
#endif

/*---------------------------------------------------------*/
/*
 * Structs auxiliares
 */

struct node {
  int item;
  struct node *next;
};
typedef struct node *Node;
/*----------------------------------------------------------*/
/*
 * Estrutura Básica da Bag
 *
 * Implementação com listas ligada dos itens.
 */

struct bag {
  Node first;
  int n;
  int previous;
};

/*------------------------------------------------------------*/
/*
 * Protótipos de funções administrativas: tem modificador 'static'
 *
 */

/*-----------------------------------------------------------*/
/*
 *  newBag()
 *
 *  RETORNA (referência/ponteiro para) uma bag vazia.
 *
 */
Bag
newBag()
{
  Bag bag = emalloc(sizeof(struct bag));
  bag->first = NULL;
  bag->n = 0;
  return bag;
}

/*-----------------------------------------------------------*/
/*
 *  freeBag(BAG)
 *
 *  RECEBE uma Bag BAG e devolve ao sistema toda a memoria
 *  utilizada.
 *
 */
void
freeBag(Bag bag)
{
  int i;
  Node oldfirst;
  if (bag->first != NULL) {
    for (i = 0; i < bag->n; i++) {
      oldfirst = bag->first;
      bag->first = oldfirst->next;
      free(oldfirst);
    }
  }
  free(bag);
}

/*------------------------------------------------------------*/
/*
 * OPERAÇÕES USUAIS: add(), size(), isEmpty() e itens().
 */

/*-----------------------------------------------------------*/
/*
 *  add(BAG, ITEM, NITEM)
 *
 *  RECEBE uma bag BAG e um ITEM e insere o ITEM na BAG.
 *  NITEM é o número de bytes de ITEM.
 *
 *  Para criar uma copia/clone de ITEM é usado o seu número de bytes NITEM.
 *
 */
void
add(Bag bag, vertex item)
{
  Node oldfirst;
  oldfirst = bag->first;
  bag->first = emalloc(sizeof(Node));
  bag->first->item = item;
  if (oldfirst != NULL)
    bag->first->next = oldfirst;
  bag->n++;
}

/*-----------------------------------------------------------*/
/*
 *  SIZE(BAG)
 *
 *  RECEBE uma bag BAG
 *
 *  RETORNA o número de itens em BAG.
 */
int
size(Bag bag)
{
  return bag->n;
}

/*-----------------------------------------------------------*/
/*
 *  ISEMPTY(BAG)
 *
 *  RECEBE uma bag BAG.
 *
 *  RETORNA TRUE se BAG está vazia e FALSE em caso contrário.
 *
 */
Bool
isEmpty(Bag bag)
{
  return bag->n == 0;
}

/*-----------------------------------------------------------*/
/*
 *  ITENS(BAG, INIT)
 *
 *  RECEBE uma bag BAG e um Bool INIT.
 *
 *  Se INIT é TRUE,  ITENS() RETORNA uma cópia/clone do primeiro item na lista de itens na BAG.
 *  Se INIT é FALSE, ITENS() RETORNA uma cópia/clone do item sucessor do último item retornado.
 *  Se BAG está vazia ou não há sucessor do último item retornado, ITENS() RETORNA -1.
 *
 *  Se entre duas chamadas de ITENS() a BAG é alterada, o comportamento é  indefinido.
 *
 */
vertex
itens(Bag bag, Bool init)
{
  int i;
  Node crawler = bag->first;
  if (isEmpty(bag) || bag->previous > size(bag))
    return -1;
  if (init) {
    bag->previous = 0;
  }
  for (i = 0; i < bag->previous; i++) {
    crawler = crawler->next;
  }
  bag->previous++;
  return crawler->item;
}

/*------------------------------------------------------------*/
/*
 * Implementaçao de funções administrativas
 */
