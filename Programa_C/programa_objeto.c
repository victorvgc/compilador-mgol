#include <stdio.h>
typedef char lit[256];

void main(void) {
/*----Variaveis Temporarias----*/
	int t1;
	int t2;
	int t3;
	int t4;
	int t5;
	int t6;
	int t7;
	int t8;
	int t9;
/*-----------------------------*/
	int A;
	int B;
	int D;
	double C;
	printf("Digite B:");
	scanf("%d", &B);
	printf("Digite A:");
	scanf("%d", &A);
	t1 = B > 2;
	if (t1) {
		t2 = B <= 4;
		if (t2) {
			printf("B esta entre 2 e 4");
		}
		t3 = B > 4;
		if (t3) {
			t4 = B <= 10;
			if (t4) {
				printf("B esta entre 4 e 10");
			}
			t5 = B > 10;
			if (t5) {
				printf("B e maior que 10");
			}
		}
	}
	t6 = B + 1;
	B = t6;
	t7 = B + 2;
	B = t7;
	t8 = B + 3;
	B = t8;
	D = B;
	C = 5.0;
	t9 = 10 * 2;
	A = t9;
	printf("\nB=\n");
	printf(D);
	printf("\n");
	printf(C);
	printf("\n");
	printf(A);
}