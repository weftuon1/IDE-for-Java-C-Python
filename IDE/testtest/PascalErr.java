import java.util.Scanner;

public class Pascal
{
	//This is for DEMO.
	public static void main(String[] args)
	{		
		/*
		This is for Demo, too.
		
		*/
		test();
		int a;
		int a;
		int a;
		int a;
		
		System.out.print("Please input the order of Pascal: ");
		Scanner input = new Scanner(System.in);
		int n = input.nextInt();
		//char c = 'q';
		int ans[][] = calc(n);
		for(int[] row: ans)
		{
			for(int i=0;i<(n-row.length);i++)
			{
				System.out.printf("%3s", "");
			}
				
			for(int column: row)
			{
				System.out.printf("%6d", column);
			}
				
			System.out.println();
		}		
	}

	public static int[][] calc(int n)
	{		
		int arr[][] = new int[n][];
		for(int i=0; i<arr.length; i++)
		{
			arr[i] = new int[i+1];
		}
			
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<i+1;j++)
			{
				if(j==0||j==i)
				{
					arr[i][j] = 1;
				}	
				else		
				{		
					arr[i][j] = arr[i-1][j-1] + arr[i-1][j];
				}
			}
			
		}
		
		return arr;
	}

	public void test()
	{
		
	}
}
