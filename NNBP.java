package machine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NNBP {
	double[][] x = new double[5][5];
	double[][] t = new double[5][3];
	double[][] o = new double[5][3];
	int hiddenLayer = 2;
	int[] hiddenUnitNumber = new int[hiddenLayer];
	List<double[]> hiddenUnitValues = new ArrayList<double[]>(hiddenLayer);
	List<double[][]> w = new ArrayList<double[][]>(hiddenLayer+1);
	List<double[]> delta = new ArrayList<double[]>(hiddenLayer+1);
	double speed = 0.01;
	static Random random = new Random(System.currentTimeMillis());
	
	protected void init(){
		for(int i=0; i<x.length; i++){
			for(int j=0; j<x[0].length; j++){
				x[i][j] = random.nextInt(10)/5.0;
			}
			t[i][0] = sigmoid(x[i][0]+x[i][1]);
			t[i][1] = sigmoid(x[i][2]-x[i][3]+x[i][4]);
			t[i][2] = sigmoid(x[i][1]-x[i][4]);
		}
		
		hiddenUnitNumber[0] = 5;
		hiddenUnitNumber[1] = 5;
		//hiddenUnitNumber[2] = 3;
		double[][] a = new double[5][5];
		initWeights(a);
		//w.add(new double[x[0].length][hiddenUnitNumber[0]]);
		w.add(a);
		for(int i=1; i<hiddenLayer; i++){
			double[][] c = new double[hiddenUnitNumber[i-1]][hiddenUnitNumber[i]];
			for(int j=0; j<c.length; j++){
				for(int k=0; k<c[0].length; k++){
					c[j][k] = random.nextGaussian();
				}
			}
			w.add(c);
		}
		double[][] b = new double[5][3];
		initWeights(b);
		w.add(b);
		//w.add(new double[hiddenUnitNumber[0]][t[0].length]);
		//hiddenUnitValues.add(new double[hiddenUnitNumber[0]]);
		for(int i=0; i<hiddenLayer; i++){
			hiddenUnitValues.add(new double[hiddenUnitNumber[i]]);
			delta.add(new double[hiddenUnitNumber[i]]);
		}
		delta.add(new double[o[0].length]);
	}
	
	protected void forward(int index){
		for(int i=0; i<hiddenUnitNumber[0]; i++){
			double net = 0;
			for(int j=0; j<x[0].length; j++){
				net += x[index][j]*w.get(0)[j][i];
			}
			hiddenUnitValues.get(0)[i] = sigmoid(net);
		}
		for(int k=1; k<hiddenLayer; k++){
			for(int i=0; i<hiddenUnitNumber[k]; i++){
				double net = 0;
				for(int j=0; j<hiddenUnitNumber[k-1]; j++){
					net += hiddenUnitValues.get(k-1)[j]*w.get(k)[j][i];
				}
				hiddenUnitValues.get(k)[i] = sigmoid(net);
			}
		}
		
		for(int i=0; i<o[0].length; i++){
			int last = hiddenLayer-1;
			double net = 0;
			for(int j=0; j<hiddenUnitNumber[last]; j++){
				net += hiddenUnitValues.get(last)[j]*w.get(hiddenLayer)[j][i];
			}
			o[index][i] = sigmoid(net);
			System.out.print(o[index][i]+" ");
		}
		System.out.println();
		for(double i : t[index]){
			System.out.print(i+" ");
		}
		System.out.println("\n");
	}
	
	protected void backward(int index){
		for(int i=0; i<o[0].length; i++){
			delta.get(hiddenLayer)[i] = o[index][i]*(1-o[index][i])*(t[index][i]-o[index][i]);
		}
		
		for(int i=0; i<hiddenUnitNumber[hiddenLayer-1]; i++){
			double sum = 0;
			for(int j=0; j<o[index].length; j++){
				sum += w.get(hiddenLayer)[i][j]*delta.get(hiddenLayer)[j];
			}
			double oi = hiddenUnitValues.get(hiddenLayer-1)[i];
			delta.get(hiddenLayer-1)[i] = sum*oi*(1-oi);
		}
		for(int k=hiddenLayer-2; k>=0; k--){
			int next = k+1;
			for(int i=0; i<hiddenUnitNumber[k]; i++){
				double sum = 0;
				for(int j=0; j<hiddenUnitNumber[next]; j++){
					sum += w.get(next)[i][j]*delta.get(next)[j];
				}
				double oi = hiddenUnitValues.get(k)[i];
				delta.get(k)[i] = sum*oi*(1-oi);
			}
		}
		for(int i=0; i<x[0].length; i++){
			for(int j=0; j<hiddenUnitNumber[0]; j++){
				double deltaW = speed*delta.get(0)[j]*x[index][i];
				w.get(0)[i][j] += deltaW;
			}
		}
		for(int k=1; k<hiddenLayer; k++){
			int prio = k-1;
			for(int i=0; i<hiddenUnitNumber[prio]; i++){
				for(int j=0; j<hiddenUnitNumber[k]; j++){
					double deltaW = speed*delta.get(k)[j]*hiddenUnitValues.get(prio)[i];
					w.get(k)[i][j] += deltaW;
				}
			}
		}
		
		for(int i=0; i<hiddenUnitNumber[hiddenLayer-1]; i++){
			for(int j=0; j<t[0].length; j++){
				double deltaW = speed*delta.get(hiddenLayer)[j]*hiddenUnitValues.get(hiddenLayer-1)[i];
				w.get(hiddenLayer)[i][j] += deltaW;
			}
		}
		
	}
	
	protected void initWeights(double[][] w){
		for(int i=0; i<w.length; i++){
			for(int j=0; j<w[0].length; j++){
				w[i][j] = random.nextGaussian();
			}
		}
	}
	protected double sigmoid(double x){
		return 1.0/(1+Math.exp(-x));
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NNBP nn = new NNBP();
		nn.init();
		for(int i=0; i<100000; i++){
			for(int j=0; j<3; j++){
				nn.forward(j);
				nn.backward(j);
			}
			
		}

	}

}
