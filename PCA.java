//package machine;

import java.util.Random;

public class PCA {
	protected MatrixUtil MU = new MatrixUtil();
	
	protected void twoD(double[][] data){
		double meanX = 0;
		double meanY = 0;
		for(int i=0; i<data.length; i++){
			meanX += data[i][0];
			meanY += data[i][1];
		}
		meanX /= data.length;
		meanY /= data.length;
		for(int i=0; i<data.length; i++){
			data[i][0] -= meanX;
			data[i][1] -= meanY;
		}
		
		double[][] cov = new double[2][2];
		for(int i=0; i<data.length; i++){
			cov[0][0] += data[i][0]*data[i][0];
			cov[0][1] += data[i][0]*data[i][1];
			cov[1][0] += data[i][0]*data[i][1];
			cov[1][1] += data[i][1]*data[i][1];
		}
		
		double[][] eigenvector = MU.exponentiation(cov);
		System.out.println();
		MU.print(eigenvector);
		System.out.println();
		double[][] transformed = MU.multiplication(data, eigenvector);
		MU.print(transformed);
	}
	
	protected double[][] initial(){
		Random random = new Random(System.currentTimeMillis());
		double[][] data = {
				{2.5,2.4},{0.5,0.7},{2.2,2.9},{1.9,2.2},{3.1,3.0},{2.3,2.7},{2,1.6},{1,1.1},{1.5,1.6},{1.1,0.9}
		};
		//for(int i=0; i<data.length; i++){
			//data[i][0] = random.nextDouble();
			//data[i][1] = random.nextDouble();
		//}
		MU.print(data);
		//System.out.println("\n***********************************\n");
		return data;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PCA p = new PCA();
		double[][] data = p.initial();
		p.twoD(data);

	}

}
