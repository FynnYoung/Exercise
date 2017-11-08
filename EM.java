//package machine;

import java.util.Random;

public class EM {
	protected static Random random = new Random(System.currentTimeMillis());
	protected static double var = 1.0;
	protected static double criteria = 0;
	protected double[] a;
	protected int k;
	protected double[][] distribution;
	protected double[] means;
	
	protected void init(){
		a = new double[1000];
		for(int i=0; i<500; i++){
			a[i] = random.nextGaussian()+1;
			a[i+500] = random.nextGaussian()+2;
		}
		k = 2;
		distribution = new double[a.length][k];
		means = new double[k];
		for(int i=0; i<k; i++){
			means[i] = random.nextDouble();
		}
	}
	protected void multiGauss(){
		Mark:
			while(true){
				double[] prior = means.clone();
				expectation();
				maximization();
				for(int i=0; i<k; i++){
					if(Math.abs(prior[i]-means[i])>criteria){
						break;
					}
					if(i==k-1){
						break Mark;
					}
				}
			}
		
	}
	protected void expectation(){
		for(int i=0; i<a.length; i++){
			double sum = 0;
			for(int j=0; j<k; j++){
				double probability = probOfGauss(means[j], a[i]);
				
				sum += probability;
				distribution[i][j] = probability;
			}
			for(int j=0; j<k; j++){
				distribution[i][j] /= sum;
			}
		}
	}
	protected void maximization(){
		for(int i=0; i<k; i++){
			double sum1 = 0;
			double sum2 = 0;
			for(int j=0; j<a.length; j++){
				sum1 += distribution[j][i]*a[j];
				sum2 += distribution[j][i];
			}
			means[i] = sum1/sum2;
			System.out.print(means[i]+" ");
		}
		System.out.println();
	}
	
	protected double probOfGauss(double mean, double x){
		return Math.exp(-1.0*Math.pow(x-mean, 2)/2.0/Math.pow(var, 2));
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EM e = new EM();
		e.init();
		e.multiGauss();

	}

}
