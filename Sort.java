//package tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Sort<T> {
	public static void quickSort(Comparable[] array){
		subQuickSort(array, 0, array.length-1);
	}
	public static void quickSort(double[] array){
		subQuickSort(array, 0, array.length-1);
	}
	
	public static void heapSort(Comparable[] array){
		buildMaxHeap(array);
		
		for(int i=array.length-1;i>=1;i--){
			ArrayUtils.exchangeElements(array, i, 0);
			
			maxHeap(array, i, 0);
		}
	}
	
	private static void subQuickSort(Comparable[] array, int left, int right){
		if(array.length==0 || left>=right){
			return;
		}
		
		int partition = partition(array, left, right);
		
		subQuickSort(array, left, partition-1);
		subQuickSort(array, partition+1, right);
	}
	
	private static int partition(Comparable[] array, int left, int right){
		Comparable sentry = array[right];//the value chosen to partition the array into two part
		int properIndex = left;//the index that the sentry value should be moved to
		for(int i=left;i<right;i++){
			if(array[i].compareTo(sentry)<0){
				
				ArrayUtils.exchangeElements(array, properIndex, i);
				properIndex++;
			}
		}
		
		ArrayUtils.exchangeElements(array, properIndex, right);
		
		return properIndex;
	}
	
	private static void subQuickSort(double[] array, int left, int right){
		if(array.length==0 || left>=right){
			return;
		}
		
		int partition = partition(array, left, right);
		
		subQuickSort(array, left, partition-1);
		subQuickSort(array, partition+1, right);
	}
	
	private static int partition(double[] array, int left, int right){
		double sentry = array[right];//the value chosen to partition the array into two part
		int properIndex = left;//the index that the sentry value should be moved to
		for(int i=left;i<right;i++){
			if(array[i]<sentry){
				
				ArrayUtils.exchangeElements(array, properIndex, i);
				properIndex++;
			}
		}
		
		ArrayUtils.exchangeElements(array, properIndex, right);
		
		return properIndex;
	}
	
	private static void buildMaxHeap(Comparable[] array){
		int half = (int)((array.length-1)/2);
		
		for(int i=half;i>=0;i--){
			maxHeap(array, array.length, i);
		}
	}
	
	private static void maxHeap(Comparable[] array, int heapSize, int index){
		int left = 2*index+1;
		int right = 2*index+2;
		
		int largest = index;
		if(left<heapSize && array[left].compareTo(array[index])>0){
			largest = left;
		}
		
		if(right<heapSize && array[right].compareTo(array[largest])>0){
			largest = right;
		}
		
		if(largest!=index){
			ArrayUtils.exchangeElements(array, index, largest);
			maxHeap(array, heapSize, largest);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Random random = new Random();
		ArrayList<Double> a = new ArrayList<Double>();
		for(int i=0;i<200;i++){
			a.add(random.nextDouble());
		}
		
		//Collections.shuffle(a);
		Double[] b = a.toArray(new Double[a.size()]);
		//ArrayUtils.printArray(b);
		quickSort(b);
		ArrayUtils.printArray(b);
		System.out.println(b[b.length-1]);

	}

}
