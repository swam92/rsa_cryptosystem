import java.math.*;
import java.util.*;

public class Main{

	/*embedded class that will represent public/private key pairs
	(n,e) ->public & (n,d) ->private*/
	public static class Key{
		int first, second;

		public Key(int first, int second){
			this.first = first;
			this.second = second;
		}
	}

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

		//loop 5 times, generate a random, bitshift to get least significant, add to bitset
		for(int i=1; i <= 5; i++){
			int x = (int )(Math.random() * 1000);
			int leastSignificant = getLeastSignificantBit(x);
			if(leastSignificant == 1)
				prime.set((1+i));
		}

		return bitsetToInteger(prime);
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

	/*Finds a small number e that is relatively prime with the
	number of elements that are relatively prime with n.  This
	routine will implement the extended Euclidian algorithm.  If no e
	works, we must pick another random prime and start again.  e
	will suffice even if it is greater than (phi(n))^(1/2)*/
	public static int generatePublicKey(){

		return 0;
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

				/*if tFinal isn't between 0 and N..make it between 0 and n*/
				if(tFinal < 0){
					while(tFinal < 0)
						tFinal += r1;
				}
				else if(tFinal > r1){
					while(tFinal > r1)
						tFinal = (tFinal - r1);
				}

			}//end if relatively prime
		}//end while
		return tFinal;
	}

	public static int findPrimeDriver(){
		int p = findSevenBitPrimes();
		while(primalityTest(p) == false)
			p = findSevenBitPrimes();
		return p;
	}

	public static String makeR(int n, int e){
		StringBuilder r = new StringBuilder();
		r.append(" Alice");

		//length becomes the number of digits in n
		int length = String.valueOf(n).length();

		int temp = 4-length;
		for(int i=0; i < temp; i++){
			r.append("0");
		}
		r.append(n);

		length = String.valueOf(e).length();
		temp = 4-length;
		for(int i=0; i < temp; i++){
			r.append("0");
		}
		r.append(e);

		return r.toString();
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

		Key alicePublic = new Key(n, key[0]);
		Key alicePrivate = new Key(n, key[1]);

	}

}
