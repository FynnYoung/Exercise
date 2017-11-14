//package machine;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Random;

public class Gibbs {
	protected static Random random = new Random(System.currentTimeMillis());
	
	protected void twoDNormal() throws IOException{
		int sampleSize = 5000;
		int K = 20; // number of iterations in each sampling
		double[] xs = new double[sampleSize];
		double[] ys = new double[sampleSize];
		double m1 = 10;
		double m2 = 5;
		double s1 = 5;
		double s2 = 2;
		double rho = 0.5;
		double x = 0, y = m2;
		
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("D:/normal.txt")));
		
		for(int i=0; i<sampleSize; i++){
			for(int j=0; j<K; j++){
				x = p_XGivenY(y, m1, m2, s1, s2, rho);
				y = p_YGivenX(x, m1, m2, s1, s2, rho);
			}
			out.write(x+"\t"+y+"\n");
			
			xs[i] = x;
			ys[i] = y;
		}
		
		out.close();
	}
	protected double p_YGivenX(double x, double m1, double m2, double s1, double s2, double rho){
		return random.nextGaussian()*(Math.sqrt(1-Math.pow(rho, 2))*s2)+(m2+rho*s2/s1*(x-m1));
	}
	protected double p_XGivenY(double y, double m1, double m2, double s1, double s2, double rho){
		return random.nextGaussian()*(Math.sqrt(1-Math.pow(rho, 2))*s1)+(m1+rho*s1/s2*(y-m2));
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Gibbs g = new Gibbs();
		g.twoDNormal();

	}

}
