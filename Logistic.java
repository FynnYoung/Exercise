package machine;

import java.util.Random;

public class Logistic {
	double[][] x = new double[10][4];
	double[] y = new double[x.length];
	double[] o = new double[y.length];
	double[] w = new double[x[0].length];
	double speed = 0.01;
	static Random random = new Random(System.currentTimeMillis());
	
	protected void init(){
		for(int i=0; i<x.length/2; i++){
			for(int j=0; j<x[0].length-1; j++){
				x[i][j] = -random.nextDouble();
			}
			x[i][x[0].length-1] = 1;
		}
		for(int i=x.length/2; i<x.length; i++){
			for(int j=0; j<x[0].length-1; j++){
				x[i][j] = random.nextDouble();
			}
			x[i][x[0].length-1] = 1;
		}
		for(int i=0; i<y.length/2; i++){
			y[i] = 0;
		}
		for(int i=y.length/2; i<y.length; i++){
			y[i] = 1;
		}
		for(int i=0; i<w.length; i++){
			w[i] = random.nextGaussian();
		}
	}
	
	protected void compute(){
		for(int index=0; index<x.length; index++){
			double net = 0;
			for(int i=0; i<x[index].length; i++){
				net += w[i]*x[index][i];
			}
			o[index] = sigmoid(net);
			//System.out.println(y[index]+" "+o[index]);
		}
		//System.out.println();
	}
	
	protected void update(){
		for(int j=0; j<w.length; j++){
			double delta = 0;
			for(int i=0; i<x.length; i++){
				delta += (o[i]-y[i])*x[i][j];
			}
			delta = delta*speed/x.length;
			w[j] -= delta;
		}
	}
	
	protected double getLoss(){
		double loss = 0;
		for(int i=0; i<x.length; i++){
			loss += y[i]*Math.log(o[i])+(1-y[i])*Math.log(1-o[i]);
		}
		loss = -loss/x.length;
		return loss;
	}
	
	protected double sigmoid(double x){
		return 1.0/(1+Math.exp(-x));
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Logistic l = new Logistic();
		l.init();
		for(int i=0; i<10000; i++){
			l.compute();
			l.update();
			System.out.println(i+":\t"+l.getLoss());
		}

	}

}
