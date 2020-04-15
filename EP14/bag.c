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

/*----------------------------------------------------------*/
/*
 * Estrutura Básica da Bag
 *
 * Implementação com listas ligada dos itens.
 */

typedef struct node *Node;

struct node {

    vertex item;
    Node next;
};

struct bag {

    Node head;
    int size;
    Node position_it;
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
Bag newBag() {

    Bag new_bag = emalloc(sizeof(*new_bag));
    new_bag->head = NULL;
    new_bag->size = 0;

    return new_bag;
}


/*-----------------------------------------------------------*/
/*
 *  freeBag(BAG)
 *
 *  RECEBE uma Bag BAG e devolve ao sistema toda a memoria
 *  utilizada.
 *
 */
 void freeBag(Bag bag) {

    Node p = bag->head;

    /* Traversing LL */
    while (p != NULL) {
        Node aux = p->next;
        free(p);
        p = aux;
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
void add(Bag bag, vertex item) {

    Node new;

    /* New node of LL */
    new = emalloc(sizeof(struct node));
    new->item = item;
    new->next = bag->head;

    /* Making it new head */
    bag->head = new;
    bag->size += 1;
}

/*-----------------------------------------------------------*/
/*
 *  SIZE(BAG)
 *
 *  RECEBE uma bag BAG
 *
 *  RETORNA o número de itens em BAG.
 */
int size(Bag bag) {

    if (bag == NULL)
        return 0;

    return bag->size;
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
Bool isEmpty(Bag bag) {

    return (bag == NULL || bag->size == 0);
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
vertex itens(Bag bag, Bool init) {

    vertex aux;

    if (isEmpty(bag))
        return -1;

    /* First item of the list */
    if (init)
        bag->position_it = bag->head;

    /* Stop condition */
    if (bag->position_it == NULL)
        return -1;

    /* Cloning */
    aux = bag->position_it->item;

    /* For the next iteration */
    bag->position_it = bag->position_it->next;

    return aux;
}
/*------------------------------------------------------------*/
/*
 * Implementaçao de funções administrativas
 */
