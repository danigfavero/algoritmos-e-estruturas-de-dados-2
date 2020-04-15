/*
 * MAC0323 Algoritmos e Estruturas de Dados II
 *
 * NAO EDITE OU MODIFIQUE NADA QUE ESTA ESCRITO NESTE ARQUIVO
 *
 * Interface levemente modificada para a tabela de simbolos
 * implementada atraves de vetores ordenados (BinarySearchST)
 * descrita em
 *
 *    https://algs4.cs.princeton.edu/31elementary/
 */


/*
 * BINARYSEARCHST
 *
 * Representa uma tabela de simbolos (ST) ordenada "generica".
 *
 * Permite as operaçoes usuais: put(), get(), contains(), delete(), size()
 * e isEmpty().
 *
 * Fornece algumas operacoes para tabelas de simbolos ordenadas: min(),
 * max(), rank(), select(), deleteMin() e deleteMax().
 *
 * Tambem fornece a operaçao keys() para iterar todas as chaves.
 *
 * Uma tabela de si­mbolos implementa a abstraçao "array associativo":
 * ao associar um valor a uma chave que ja esta na tabela de si­mbolos,
 * a convencao e substituir o valor antigo pelo novo valor.
 *
 * Ao contrario de java.util.Map, essa implementaçao usa a convençao de que
 * os valores nao podem ser NULL - associar o valor de uma chave para NULL
 * e equivalente a excluir a chave da tabela de simbolos.
 *
 * Esta implementacao usa vetores ordenadados.
 * Requer que uma funcao compar() seja argumento do construtor.
 *
 * As operacoes put() e delete() consomem tempo linear, no pior caso.
 * As operacoes get(), contains(), select(), rank() consomem tempo logari­tmico.
 * Finalmente, as operacoes size(), isEmpty(), min(), max() e select() consomem
 * tempo constante.
 *
 * Para documentacao adicional, veja
 * https://algs4.cs.princeton.edu/31elementary/BinarySearchST.java.html
 * Algorithms, 4th Edition, por Robert Sedgewick e Kevin Wayne.
 */

#ifndef _BINARYSEARCHST_H
#define _BINARYSEARCHST_H

#include <stddef.h> /* size_t */
#include "util.h"   /* Bool */

typedef struct binarySearchST *BinarySearchST;

/*-----------------------------------------------------------*/
/*  prototipos das funcoes que manipulam a tabela de simbolos */

/*-----------------------------------------------------------*/
/*
 *  Construtor e "destrutor"
 */
/*
 *  INIT(COMPAR)
 *
 *  cria uma ST vazia
 *  compar() e ponteiro para uma funcao que retorna um inteiro tal que
 *
 *      compar(key1, key2) retorna um inteiro < 0 se key1 <  key2
 *      compar(key1, key2) retorna um 0           se key1 == key2
 *      compar(key1, key2) retorna um inteiro > 0 se key1 >  key2
 */
BinarySearchST
initST(int (*compar)(const void *key1, const void *key2));

/* libera todo o espaco usado pela ST */
void
freeST(BinarySearchST st);

/*------------------------------------------------------------*/
/*
 * Permite as operacoes usuais: put(), get(), contains(), delete(),
 * size() e isEmpty().
 */

/* coloca um par KEY-VAL na ST */
void
put(BinarySearchST st, const void *key, size_t nKey, const void *val, size_t nVal);

/* retorna o valor associado a KEY */
void *
get(BinarySearchST st, const void *key);

/* retorna TRUE se KEY esta na ST e FALSE em caso contrario */
Bool
contains(BinarySearchST st, const void *key);

/* remove da ST a chave KEY */
void
delete(BinarySearchST st, const void *key);

/* retorna o numero de itens na ST */
int
size(BinarySearchST st);

/* retorna TRUE se ST esta vazia e FALSE em caso contrario */
Bool
isEmpty(BinarySearchST st);

/*------------------------------------------------------------*/
/*
 * Fornece algumas operacoes para tabelas de simbolos ordenadas:
 * min(), max(), rank(), select(), deleteMin() e deleteMax().
 */

/* menor chave na ST */
void *
min(BinarySearchST st);

/* maior chave na ST */
void *
max(BinarySearchST st);

/* numero de chaves menores que key */
int
rank(BinarySearchST st, const void *key);

/* remove menor chave */
void
deleteMin(BinarySearchST st);

/* remove menor chave */
void
deleteMax(BinarySearchST st);

/* a k-esima chave da ST */
void *
select(BinarySearchST st, int k);

/*------------------------------------------------------------*/
/*
 * Tambem fornece a operacao keys() para iterar todas as chaves.
 */

/* all of the keys, as an Iterable */
void *
keys(BinarySearchST st, Bool init);

/*
  Visit each entry on the ST.

  The VISIT function is called, in-order, with each pair key-value in the ST.
  If the VISIT function returns zero, then the iteration stops.

  visitST returns zero if the iteration was stopped by the visit function,
  nonzero otherwise.
*/
int
visitST(BinarySearchST st, int (*visit)(const void *key, const void *val));

#endif /* _BINARYSEARCHST_H */
