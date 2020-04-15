#include <string.h>
#include <stdio.h>   /* fopen(), fclose(), fscanf(), ... */
#include <stdlib.h>
int main() {
  char str1[] = "This is ", str2[] = ".com";
  strcat(str1, str2);
  puts(str1);
  return 0;
}
