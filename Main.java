import java.math.*;
import java.util.*;

public class Main{

	public static boolean printFlag1 = false;
	public static boolean eflag = false;

	/*returns n=(p-1)(q-1)*/
	public static int computePhi(int p, int q){
		return (p-1)*(q-1);
	}


	public static int bitsetToInteger(BitSet b){
		int acc = 0;
    	for (int i = 0; i < b.length(); ++i) {
      		acc += b.get(i) ? (1 << i) : 0;
    	}
   		return acc;
  	}

  	//u is less than n.  u is 32 bit integer
  	public static int bobPicksU(int n){
  		BitSet u = new BitSet(32);
  		System.out.println("--------------");

  		boolean flag = false;
  		int bitToSet = 0;
  		for(int i=31; i >= 0; i--){

  			/*SET k-1 to 1, the rest are random*/
  			if( (flag == true) && (i == bitToSet) ){
  				u.set(bitToSet);
  			}
  			/*SETTING CONSEQUENT RANDOM BITS*/
  			else if(flag == true){
				int x = (int )(Math.random() * 1000);
				int leastSignificant = getLeastSignificantBit(x);
				if(leastSignificant == 1)
					u.set(i);
  			}

  			/*FIND FIRST 1 BIT AND PREPARE TO SET IT IN NEXT ITERATION*/
  			boolean checker = checkIfBitIsSet(n, i);
  			if(checker == true){
  				bitToSet = i-1;
  				flag = true;
  			}
  			/********************************************************/

  		}
  		int returnU = bitsetToInteger(u);
  		System.out.println("n, u " + n + " " + returnU);
  		return returnU;
  	}
	
	/*Primes will be 7 bits.  First and last bits will be 1.
	Internal 5 bits should be randomly choosen 1 by 1. 
	This will be done by generating random numbers and extracting
	the least significant bit to use as the random bit*/
	public static int findSevenBitPrimes(){
		BitSet prime = new BitSet(6);
		//set the 7th bit to 1
		prime.set(6);
		//set the first bit to 1
		prime.set(0);

		int[] randomNums = new int[5];
		int[] randomBits = new int[5];
		//loop 5 times, generate a random, bitshift to get least significant, add to bitset
		for(int i=0; i < 5; i++){
			int x = (int )(Math.random() * 1000);
			int leastSignificant = getLeastSignificantBit(x);
			randomNums[i] = x;
			randomBits[i] = leastSignificant;
			if(leastSignificant == 1)
				prime.set((1+i));
		}

		int convertedToInt = bitsetToInteger(prime);

		if(primalityTest(convertedToInt)){

			if(printFlag1 == false){
				System.out.println("prime is: " +convertedToInt);
				for(int i=0;i<5;i++){
					System.out.println(randomNums[i] + "    " + randomBits[i]);
				}
				System.out.println("-------------------------");
				printFlag1=true;
			}
			return convertedToInt;
		}
		else{
			findSevenBitPrimes();
		}

		return convertedToInt;
	}

	/*Extracts the least significant bit from the pseudo-random
	number that is passed to it.  Sub routine of findSevenBitPrime()*/
	public static int getLeastSignificantBit(int x){
		int leastSignificant = x & ((1 << 1) - 1);
		return leastSignificant;
	}

	public static boolean checkIfBitIsSet(int x, int bit){
		if((x & 1 << bit) != 0)
			return true;
		else
			return false;
	}

	/*A number can be declared prime if for 20 random a's such that 0<a<n,
	 a passes the miller rabin karp test for primality
		-k is number of bits, so in this case it is 7
		-x is n-1, where n is the number in question
		-n is the number number we are checking for primality in 
	 */
	public static boolean millerRabin(int a, int x){
		int y = 1;
		int z;

		for(int j=6; j>=0; j--){
			z = y;
			y = ((y * y) % (x+1));
			if((y==1) && (z != 1) && (z != (x)))
				return false;
			if(checkIfBitIsSet(x,j)){
				y = ((y * a) % (x+1));
			}
		}
		if(y != 1)
			return false;
		else
			return true;
	}

	/*Checks to make sure that the p,q where p x q=n does
	not satisfy p==q*/
	public static boolean checkForEquality(int p, int q){
		return p==q;
	}

	public static boolean primalityTest(int n){
		boolean isPrime = true;

		for(int i=0; i < 20; i++){
			int a = (int)(Math.random() * 128);
			boolean b = millerRabin(a, n-1);
			if(b==false){
				isPrime = false;
				break;
			}
		}
		return isPrime;
	}


	/*Pick a small number e to be the public key.  e must be
	relatively prime with phi(n) (calculated above).  This method 
	will use the extended Euclidian algorithm to check if
	e is relatively prime and to find a multiplicative inverse
	which we will call e. 
	pair[e,d]
	*/
	public static int[] findMultiplicativeInverse(int phi){
		int[] pair = new int[2];
		Arrays.fill(pair,-10);
		for(int i=3; i < phi; i++){
			int greatestCD = gcd(phi, i);
			if(greatestCD != -10){
				pair[0] = i;
				pair[1] = greatestCD;
				return pair;
			}
		}
		return pair;
	}

	/*
	values[][]:

	 | q | s | t
	 |-----------
   1 |   |   |
	 |-----------
   2 |   |   |
	 |-----------
   3 |   |   |
	
	This is a model of the 3x3 array that will be used to keep 
	previous values of q,s,t.  This needs to be done because 
	in order we must keep 2 previous values of q,s,t to calculate
	s&t which will eventually be used to calculate d.
	*/
	public static int gcd(int r1, int r2){
		int remainder = Integer.MAX_VALUE;
		int alpha, beta;
		int p = r1;
		int q = r2;
		int count = 0;
		int[][] values = new int[3][3];
		int tFinal = -10;
		while(remainder > 0){
			remainder = (p%q);

			/*The following conditions are responsbile for keeping
			track of q,s, and t.*/

			if(count == 0){
				values[count][0] = (int)Math.floor(p/q);
				values[count][1] = 1;
				values[count][2] = 0;
			}
			else if(count == 1){
				values[count][0] = (int)Math.floor(p/q);
				values[count][1] = 0;
				values[count][2] = 1;
			}
			else if(count == 2){
				values[count][0] = (int)Math.floor(p/q);
				values[count][1] = (values[0][1] - (values[0][0] * values[1][1]));
				values[count][2] = (values[0][2] - (values[0][0] * values[1][2]));	
			}
			else{
				for(int i=0;i<3;i++){
					values[0][i] = values[1][i];
				}
				for(int i=0;i<3;i++){
					values[1][i] = values[2][i];
				}
				values[2][0] = (int)Math.floor(p/q);
				values[2][1] = (values[0][1] - (values[0][0] * values[1][1]));
				values[2][2] = (values[0][2] - (values[0][0] * values[1][2]));
			}
			/*****************************************************/

			p = q;
			q = remainder;
			count++;
			if( (q==0) && (p==1) ){
				tFinal = (values[1][2] - (values[1][0] * values[2][2]));

				if(eflag == false){
					System.out.println("remainder is 0..." + r1 + " " + r2 + " are relatively prime");
				}
				/*if tFinal isn't between 0 and N..make it between 0 and n*/
				if(tFinal < 0){
					while(tFinal < 0)
						tFinal += r1;
						if(eflag==false)
							System.out.println(tFinal);
				}
				else if(tFinal > r1){
					while(tFinal > r1)
						tFinal = (tFinal - r1);
						if(eflag==false)
							System.out.println(tFinal);
				}

				System.out.println("d is " + tFinal);
				eflag = true;

			}//end if relatively prime
			else if(eflag == false){
				System.out.println("remainder is " + q);
			}
		}//end while
		return tFinal;
	}

	public static int hashFunction(byte[] b){
		int finalByte = 0;

		for(int i=0; i < (b.length - 1); i++){
			finalByte = (b[i] ^ b[i+1]);
		}

		return finalByte;
	}

	public static int fastExponentiation(int m, int n, int e){
		int y = 1;
		for(int i=11; i >=0; i--){
			y = ((y*y) % n);
			if(checkIfBitIsSet(e,i))
				y = ((m*y) % n);
		}
		System.out.println(y);
		return y;
	}

	public static byte[] makeS(int n, int e){
		byte[] ret = new byte[14];

		ret[0] = 0;
		ret[1] = 'A';
		ret[2] = 'l';
		ret[3] = 'i';
		ret[4] = 'c';
		ret[5] = 'e';

		//length becomes the number of digits in n
		int length = String.valueOf(n).length();
		int temp = 4-length;
		
		for(int i=0; i < temp; i++){
			ret[6+i] = '0';
		}
		int start = (6+temp);
		String n1 = Integer.toString(n);
		for(int i =0; i < length; i++){
			ret[start+i] = (byte)n1.charAt(i);
		}

		length = String.valueOf(e).length();
		temp = 4-length;
		for(int i=0; i < temp; i++){
			ret[10+i] = 0;
		}

		int index = 10+temp;
		String e1 = Integer.toString(e);
		for(int i=0; i < length; i++){
			ret[index+i] = (byte)e1.charAt(i);
		}

		return ret;
	}

	public static int findPrimeDriver(){
		int p = findSevenBitPrimes();
		return p;
	}

	public static void main(String[] args){
		//find p and q that are 7 bit prime
		int p = findPrimeDriver();
		int q = findPrimeDriver();

		//if p==q, find new q until they are different!
		while(checkForEquality(p,q)){
			q = findPrimeDriver();
		}
	
		int n = (p*q);
		int phi = computePhi(p,q);

		int[] key = findMultiplicativeInverse(phi);

		System.out.println("p= " + p + " q= " + q + " n = " + n + " e = " + key[0] + " d = " + key[1]);

		byte[] prac = makeS(n, key[0]);
		int hashValue = hashFunction(prac);
		System.out.println(hashValue);

		int cipher = fastExponentiation(hashValue, n, key[0]);

		int u =bobPicksU(n);


	}

}
