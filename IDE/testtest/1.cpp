#include <stdio.h>
#include <iostream>

int main ()
{	
	float a,c;
	int b;
	printf("Please type a real number!!!\n\n");
	printf("The input value is ");
	scanf("%f",&a);
	b=(int) a;
	printf("\nThe whole part is %d\n\n",b);
	c=a-b;
	printf("The decimal(fraction) part is %f\n\n",c);
	
	system("pause");
	return 0;
}
