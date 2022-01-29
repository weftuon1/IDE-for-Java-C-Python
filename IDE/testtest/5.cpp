#include<stdio.h>
#include<string.h>

void input(char *);
void calculate(char*,char *,char *);
void print(char *);

int main()
{
    char numa[41] = "\0";
    char numb[41] = "\0";
    char numr[42] = "\0";
    printf("Please enter two integers (at most 40 digits):\n");
    input(numa);
    input(numb);

    calculate(numa,numb,numr);  
    print(numr);

    return 0;
}

void input(char *num)
{
    char data[41] = "\0";
    int i,j;    
    scanf("%s",data);
    
    for((i = strlen(data) - 1,j = 0);i>=0;(i--,j++))
        num[j] = data[i];
    num[j] = '\0'; 
}

void calculate(char *a,char *b,char *r)
{    
    int c[42] = {0},i,la = strlen(a),lb = strlen(b);
    for(i=0;i<(la>lb?la:lb);i++)
    {
        if(i>=la)
            a[i] = '0';
        if(i>=lb)
            b[i] = '0';
        r[i] = ((a[i]+b[i]+c[i])-96)%10+48;
        c[i+1] =((a[i]+b[i]+c[i])-96)/10;                
    }

    a[la] = '\0';
    b[la] = '\0';

    if(c[i]==0)
        r[i] = '\0';
    else
    {
        r[i] = c[i]+48;
        r[i+1] = '\0';
        if(i>=39)
            printf("sum is over 40 digits\n");
    }
}

void print(char *num)
{
    printf("\nThe sum is:\n");
    for(int i=strlen(num)-1;i>=0;i--)
        printf("%c",num[i]);
    printf("\n");
}
