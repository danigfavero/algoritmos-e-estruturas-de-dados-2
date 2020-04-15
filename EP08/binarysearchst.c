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

    Segmentation Fault :-(

****************************************************************/



/*
 * MAC0323 Estruturas de Dados e Algoritmo II
 *
 * Tabela de simbolos implementada atraves de vetores ordenados
 * redeminsionaveis
 *
 *     https://algs4.cs.princeton.edu/31elementary/BinarySearchST.java.html
 *
 * As chaves e valores desta implementacao sao mais ou menos
 * genericos
 */

/* interface para o uso da funcao deste modulo */
#include "binarysearchst.h"

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
#define INIT_CAPACITY 2

/*----------------------------------------------------------*/
/*
 * Estrutura Basica da Tabela de Simbolos:
 *
 * implementacao com vetores ordenados
 */
struct binarySearchST {
    void* *keys;
    void* *vals;
    int n;
    int max;
    int (*compar)(const void *key1, const void *key2);
    int previous;
};

/*------------------------------------------------------------*/
/*
 * Funcoes administrativas
 */

void resize(BinarySearchST st, int new_size) {
    realloc(st->keys, new_size*sizeof(void*));
    realloc(st->vals, new_size*sizeof(void*));
    st->max = new_size;
}

/*-----------------------------------------------------------*/
/*
 *  initST(COMPAR)
 *
 *  RECEBE uma funçao COMPAR() para comparar chaves.
 *  RETORNA (referencia/ponteiro para) uma tabela de si­mbolos vazia.
 *
 *  E esperado que COMPAR() tenha o seguinte comportamento:
 *
 *      COMPAR(key1, key2) retorna um inteiro < 0 se key1 <  key2
 *      COMPAR(key1, key2) retorna 0              se key1 == key2
 *      COMPAR(key1, key2) retorna um inteiro > 0 se key1 >  key2
 *
 *  TODAS OS OPERACOES da ST criada utilizam a COMPAR() para comparar
 *  chaves.
 *
 */
BinarySearchST
initST(int (*compar)(const void *key1, const void *key2)) {
    BinarySearchST st = emalloc(sizeof(struct binarySearchST));
    st->keys = emalloc(sizeof(void*) * INIT_CAPACITY);
    st->vals = emalloc(sizeof(void*) * INIT_CAPACITY);
    st->n = 0;
    st->compar = compar;
    return st;
}

/*-----------------------------------------------------------*/
/*
 *  freeST(ST)
 *
 *  RECEBE uma BinarySearchST  ST e devolve ao sistema toda a memoria
 *  utilizada por ST.
 *
 */
void
freeST(BinarySearchST st) {
    int i;
    for (i = 0; i < st->n; i++)
        free(st->keys[i]);
    free(st->vals);
    for (i = 0; i < st->n; i++)
        free(st->vals[i]);
    free(st->vals);
    free(st);
}

/*------------------------------------------------------------*/
/*
 * OPERACOES USUAIS: put(), get(), contains(), delete(),
 * size() e isEmpty().
 */

/*-----------------------------------------------------------*/
/*
 *  put(ST, KEY, NKEY, VAL, NVAL)
 *
 *  RECEBE a tabela de si­mbolos ST e um par KEY-VAL e procura a KEY na ST.
 *
 *     - se VAL E' NULL, a entrada da chave KEY e' removida da ST
 *
 *     - se KEY nao e' encontrada: o par KEY-VAL e' inserido na ST
 *
 *     - se KEY e' encontra: o valor correspondente e' atualizado
 *
 *  NKEY e' o numero de bytes de KEY e NVAL e' o numero de bytes de NVAL.
 *
 *  Para criar uma copia/clone de KEY e' usado o seu numero de bytes NKEY.
 *  Para criar uma copia/clode de VAL e' usado o seu numero de bytes NVAL.
 *
 */
void
put(BinarySearchST st, const void *key, size_t nKey, const void *val, size_t nVal) {
    int i, j;
    void *clone = emalloc(sizeof(void*));
    if (key == NULL) {
        ERROR("put(): argument key is null");
        return;
    }
    if (val == NULL) {
        delete(st, key);
        return;
    }
    memcpy(clone, val, sizeof(void*));
    i = rank(st, key);
    /* key is already in table*/
    if (i < st->n && st->compar(st->keys[i], key) == 0) {
        st->vals[i] = clone;
        return;
    }
    /* insert new key-value pair */
    if (st->n == st->max)
        resize(st, 2*st->max);
    for (j = st->n; j > i; j--)  {
        st->keys[j] = st->keys[j-1];
        st->vals[j] = st->vals[j-1];
    }
    st->vals[i] = clone;
    st->n++;
}

/*-----------------------------------------------------------*/
/*
 *  get(ST, KEY)
 *
 *  RECEBE uma tabela de simbolos ST e uma chave KEY.
 *
 *     - se KEY esta em ST, RETORNA NULL;
 *
 *     - se KEY nao esta em ST, RETORNA uma copia/clone do valor
 *       associado a KEY.
 *
 */
void *
get(BinarySearchST st, const void *key) {
    int i;
    void *clone = emalloc(sizeof(void*));
    if (key == NULL) {
        ERROR("put(): argument key is null");
        return NULL;
    }
    if (isEmpty(st)) {
        return NULL;
    }
    i = rank(st, key);
    if (i < st->n && st->compar(st->keys[i], key) == 0) {
        clone = st->vals[i];
        return clone;
    }
    return NULL;
}

/*-----------------------------------------------------------*/
/*
 *  CONTAINS(ST, KEY)
 *
 *  RECEBE uma tabela de simbolos ST e uma chave KEY.
 *
 *  RETORNA TRUE se KEY esta na ST e FALSE em caso contrario.
 *
 */
Bool
contains(BinarySearchST st, const void *key) {
    if (key == NULL) {
        ERROR("put(): argument key is null");
        return 0;
    }
    return get(st, key) != NULL;
}

/*-----------------------------------------------------------*/
/*
 *  DELETE(ST, KEY)
 *
 *  RECEBE uma tabela de simbolos ST e uma chave KEY.
 *
 *  Se KEY esta em ST, remove a entrada correspondente a KEY.
 *  Se KEY nao esta em ST, faz nada.
 *
 */
void
delete(BinarySearchST st, const void *key) {
    int i, j;
    if (key == NULL) {
        ERROR("put(): argument key is null");
        return;
    }
    if (isEmpty(st))
        return;
    /* compute rank */
    i = rank(st, key);
    /* key not in table */
    if (i == st->n || st->compar(st->keys[i], key) != 0) {
        return;
    }
    for (j = i; j < st->n-1; j++)  {
        st->keys[j] = st->keys[j+1];
        st->vals[j] = st->vals[j+1];
    }
    st->n--;
    free(st->keys[st->n]);
    free(st->vals[st->n]);
    /* resize if 1/4 full */
    if (st->n > 0 && st->n == (st->max)/4)
        resize(st, st->max/2);
}

/*-----------------------------------------------------------*/
/*
 *  SIZE(ST)
 *
 *  RECEBE uma tabela de simbolos ST.
 *
 *  RETORNA o numero de itens (= pares chave-valor) na ST.
 *
 */
int
size(BinarySearchST st) {
    return st->n;
}

/*-----------------------------------------------------------*/
/*
 *  ISEMPTY(ST, KEY)
 *
 *  RECEBE uma tabela de simbolos ST.
 *
 *  RETORNA TRUE se ST esta vazia e FALSE em caso contrario.
 *
 */
Bool
isEmpty(BinarySearchST st) {
    return size(st) == 0;
}


/*------------------------------------------------------------*/
/*
 * OPERACOES PARA TABELAS DE SIMBOLOS ORDENADAS:
 * min(), max(), rank(), select(), deleteMin() e deleteMax().
 */

/*-----------------------------------------------------------*/
/*
 *  MIN(ST)
 *
 *  RECEBE uma tabela de si­mbolos ST e RETORNA uma copia/clone
 *  da menor chave na tabela.
 *
 *  Se ST esta vazia RETORNA NULL.
 *
 */
void *
min(BinarySearchST st) {
    void *clone = emalloc(sizeof(void*));
    memcpy(clone, st->keys[0], sizeof(void*));
    return clone;
}

/*-----------------------------------------------------------*/
/*
 *  MAX(ST)
 *
 *  RECEBE uma tabela de simbolos ST e RETORNA uma copia/clone
 *  da maior chave na tabela.
 *
 *  Se ST esta vazia RETORNA NULL.
 *
 */
void *
max(BinarySearchST st) {
    void *clone = emalloc(sizeof(void*));
    memcpy(clone, st->keys[st->n-1], sizeof(void*));
    return clone;
}

/*-----------------------------------------------------------*/
/*
 *  RANK(ST, KEY)
 *
 *  RECEBE uma tabela de si­mbolos ST e uma chave KEY.
 *  RETORNA o numero de chaves em ST menores que KEY.
 *
 *  Se ST esta vazia RETORNA NULL.
 *
 */
int
rank(BinarySearchST st, const void *key) {
    int lo = 0, hi = st->n-1;
    int mid, cmp;
    if (st == NULL || isEmpty(st))
        return 0;
    if (key == NULL) {
        ERROR("put(): argument key is null");
        return -1;
    }
    while (lo <= hi) {
       mid = lo + (hi - lo) / 2;
       cmp = st->compar(key, st->keys[mid]);
       if (cmp < 0) hi = mid - 1;
       else if (cmp > 0) lo = mid + 1;
       else return mid;
    }
    return lo;
}

/*-----------------------------------------------------------*/
/*
 *  SELECT(ST, K)
 *
 *  RECEBE uma tabela de simbolos ST e um inteiro K >= 0.
 *  RETORNA a (K+1)-esima menor chave da tabela ST.
 *
 *  Se ST nao tem K+1 elementos RETORNA NULL.
 *
 */
void *
select(BinarySearchST st, int k) {
    if (st == NULL) {
        ERROR("put(): argument st is null");
        return NULL;
    }
    if (k+1 > size(st))
        return NULL;
    return st->vals[k];
}

/*-----------------------------------------------------------*/
/*
 *  deleteMIN(ST)
 *
 *  RECEBE uma tabela de si­mbolos ST e remove a entrada correspondente
 *  a menor chave.
 *
 *  Se ST esta vazia, faz nada.
 *
 */
void
deleteMin(BinarySearchST st) {
    if (!isEmpty(st))
        delete(st, min(st));
}

/*-----------------------------------------------------------*/
/*
 *  deleteMAX(ST)
 *
 *  RECEBE uma tabela de sÃ­mbolos ST e remove a entrada correspondente
 *  a maior chave.
 *
 *  Se ST esta vazia, faz nada.
 *
 */
void
deleteMax(BinarySearchST st) {
    if (!isEmpty(st))
        delete(st, max(st));
}

/*-----------------------------------------------------------*/
/*
 *  KEYS(ST, INIT)
 *
 *  RECEBE uma tabela de si­mbolos ST e um Bool INIT.
 *
 *  Se INIT E' TRUE, KEYS() RETORNA uma copia/clone da menor chave na ST.
 *  Se INIT E' FALSE, KEYS() RETORNA a chave sucessora da ultima chave retornada.
 *  Se ST esta vazia ou nao ha sucessora da ultima chave retornada, KEYS() RETORNA NULL.
 *
 *  Se entre duas chamadas de KEYS() a ST e' alterada, o comportamento e'
 *  indefinido.
 *
 */
void *
keys(BinarySearchST st, Bool init) {
    if (st == NULL) {
        ERROR("put(): argument st is null");
        return NULL;
    }
    if (isEmpty(st) || st->previous >= size(st))
        return NULL;
    if (init) {
        st->previous = 0;
    }
    return st->keys[st->previous++];
}

/*-----------------------------------------------------------*/
/*
  Visit each entry on the ST.

  The VISIT function is called, in-order, with each pair key-value in the ST.
  If the VISIT function returns zero, then the iteration stops.

  visitST returns zero if the iteration was stopped by the visit function,
  nonzero otherwise.
*/
int
visitST(BinarySearchST st, int (*visit)(const void *key, const void *val)) {
    int i, stop;
    while (i < size(st) && visit != 0) {
        stop = visit(st->keys[i], st->vals[i]);
    i++;
    }
    return stop;

}


/*------------------------------------------------------------*/
/*
 * Funcoes administrativas
 */
