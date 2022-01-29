#include <stdio.h>
#include <iostream>

int main ()
{
	const float PI=3.14159;
	printf("A circle with a radius of 6.75\n\n");
	printf("Diameter %10.5f\n\n",2*6.75);
	printf("The circumference %10.5f\n\n",2*PI*6.75);
	printf("The Area %10.5f\n\n",PI*6.75*6.75);
		
	system("pause");
	return 0;
}
