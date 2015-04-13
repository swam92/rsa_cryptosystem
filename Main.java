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
		System.out.println(bitsetToInteger(prime));
		for(int i=0; i < 8; i++){
			System.out.print(prime.get(i) + " ");
		}
		return bitsetToInteger(prime);
	}

	/*Extracts the least significant bit from the pseudo-random
	number that is passed to it.  Sub routine of findSevenBitPrime()*/
	public static int getLeastSignificantBit(int x){
		int leastSignificant = x & ((1 << 1) - 1);
		return leastSignificant;
	}

	/*Sub routine of findSevenBitPrimes() that will determine
	if the number generated is indeed a prime number that
	can be used in the cryptosystem.  A number can be declared prime if 
	for 20 random a's such that 0<a<n, a passes the miller rabin karp test
	for primality*/
	public static boolean millerRabinKarp(int prime){

		return false;
	}

	/*Checks to make sure that the p,q where p x q=n does
	not satisfy p==q*/
	public static boolean checkForEquality(int p, int q){
		return p==q;
	}

	/*Finds a small number e that is relatively prime with the
	number of elements that are relatively prime with n.  This
	routine will implement the extended Euclidian algorithm.  If no e
	works, we must pick another random prime and start again.  e
	will suffice even if it is greater than (phi(n))^(1/2)*/
	public static int generatePublicKey(){

		return 0;
	}

	public static void main(String[] args){
		findSevenBitPrimes();
	}

}
