//package tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayUtils {
	public static double arithmeticAverage(Double[] array){
		int length = array.length;
		double average = 0;
		for(int i=0;i<length;i++){
			average += array[i];
		}
		
		average /= length;
		return average;
	}
	public static double arithmeticAverage(double[] array){
		int length = array.length;
		double average = 0;
		for(int i=0;i<length;i++){
			average += array[i];
		}
		
		average /= length;
		return average;
	}
	
	public static double median(Double[] sortedArray){ // the array must be sorted
		int length = sortedArray.length;
		if((length & 1) == 1){ // length is odd
			return sortedArray[(length-1)/2];
		}else{ // length is even
			return (sortedArray[length/2-1]+sortedArray[length/2])/2.0;
		}
	}
	public static double median(double[] sortedArray){
		int length = sortedArray.length;
		if((length & 1) == 1){ // length is odd
			return sortedArray[(length-1)/2];
		}else{ // length is even
			return (sortedArray[length/2-1]+sortedArray[length/2])/2.0;
		}
	}
	
	public static void exchangeElements(Object[] array, int indexOne, int indexTwo){
		Object temp = array[indexOne];
		array[indexOne] = array[indexTwo];
		array[indexTwo] = temp;
	}
	public static void exchangeElements(double[] array, int indexOne, int indexTwo){
		double temp = array[indexOne];
		array[indexOne] = array[indexTwo];
		array[indexTwo] = temp;
	}
	public static void exchangeElements(int[] array, int indexOne, int indexTwo){
		int temp = array[indexOne];
		array[indexOne] = array[indexTwo];
		array[indexTwo] = temp;
	}
	
	public static void printArray(Object[] array){
		for(int i=0;i<array.length;i++){
			System.out.print(array[i]+" ");
		}
		System.out.println();
	}
	public static void printArray(Double[] array){
		for(Double a : array){
			System.out.print(a+" ");
		}
		System.out.println();
	}
	public static void printArray(int[] array){
		Integer[] array1 = new Integer[array.length];
		for(int i=0;i<array.length;i++){
			array1[i] = array[i];
		}
		printArray(array1);
	}
	public static void printArray(double[] array){
		Double[] array1 = new Double[array.length];
		for(int i=0;i<array.length;i++){
			array1[i] = array[i];
		}
		printArray(array1);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Integer>[] a = new List[3];
		for(int i=0; i<a.length; i++){
			a[i] = new ArrayList<Integer>();
			a[i].add(i);
			a[i].add(i);
		}
		List<Integer>[] b = Arrays.copyOfRange(a, 0, 3);
		System.out.println(b.length);
		for(List<Integer> i : a){
			System.out.println(i);
		}
		System.out.println();
		for(List<Integer> i : b){
			System.out.println(i);
		}
		System.out.println();
		ArrayUtils.exchangeElements(b, 0, 1);
		for(List<Integer> i : a){
			System.out.println(i);
		}
		System.out.println();
		for(List<Integer> i : b){
			System.out.println(i);
		}
		System.out.println();

	}

}
