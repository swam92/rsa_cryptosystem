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
		BitSet prime = new BitSet(32);
		//set the 7th bit to 1
		prime.set(6);
		//set the first bit to 1
		prime.set(0);

		//loop 5 times, generate a random, bitshift to get least significant, add to bitset
		for(int i=0; i < 5; i++){
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
	public static boolean millerRabinKarp(int a, int x, int n){
		int y = 1;
		int z;
		for(int j=6; j<=0; j++){
			z = y;
			y = ((y * y) % n);
			if((y==1) && (z != 1) && (z != (n-1)))
				return false;
			if(checkIfBitIsSet(x,j))
				y = ((y * a) % n);
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
		System.out.println("n is " + n);
		boolean isPrime = true;
		for(int i=0; i < 20; i++){
			int a = (int)(Math.random() * 128);
			boolean b = millerRabinKarp(a, n-1, n);
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
	which we will call e */
	public static int findMultiplicativeInverse(int phi){
		for(int i=3; i < phi; i++){
			int greatestCD = gcd(i, phi);
			
		}
		return 0;
	}

	public static int gcd(int r1, int r2){
		
		return 0;
	}

	public static void main(String[] args){
		//find p
		int p = findSevenBitPrimes();
		while(primalityTest(p) == false)
			p = findSevenBitPrimes();
		//find q
		int q = findSevenBitPrimes();
		while(primalityTest(q) == false)
			q = findSevenBitPrimes();

		//lets just hope this passes for now...
		checkForEquality(p,q);
	
		int n = (p*q);

		int phi = computePhi(p,q);

		
	}

}
