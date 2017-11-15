//package machine;

import java.util.Random;

public class LDA {
	protected MatrixUtil m = new MatrixUtil();
	protected double[][] twoClass(double[][] a, double[][] b){ // 返回投影列向量
		double[][] sW = m.add(sWithin(a), sWithin(b));
		double[][] meanA = new double[a[0].length][1];
		double[][] meanB = new double[b[0].length][1];
		for(int i=0; i<a.length; i++){
			for(int j=0; j<meanA.length; j++) meanA[j][0] += a[i][j];
		}
		for(int i=0; i<b.length; i++){
			for(int j=0; j<meanB.length ;j++) meanB[j][0] += b[i][j];
		}
		for(int j=0; j<meanA.length; j++){
			meanA[j][0] /= a.length;
			meanB[j][0] /= b.length;
		}
		//m.print(sW);
		double[][] w = m.multiplication(m.inverse(sW), m.minus(meanA, meanB));
		for(double[] i : w){
			for(double j : i) System.out.print(j+" ");
			System.out.println();
		}
		return w;
	}
	protected double[][] sWithin(double[][] x){
		double[][] mean = new double[x[0].length][1];
		for(int i=0; i<x.length; i++){
			for(int j=0; j<mean.length; j++) mean[j][0] += x[i][j];
		}
		for(int j=0; j<mean.length; j++) mean[j][0] /= x.length;
		double[][] scatter = new double[mean.length][1];
		double[][] result = new double[mean.length][mean.length];
		for(int i=0; i<x.length; i++){
			for(int j=0; j<scatter.length; j++){
				scatter[j][0] = x[i][j]-mean[j][0];
			}
			/*
			for(double[] a : scatter){
				for(double b : a){
					System.out.print(b+" ");
				}
				System.out.println();
			}*/
			result = m.add(result, m.multiplication(scatter, m.transpose(scatter)));
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Random random = new Random(System.currentTimeMillis());
		double[][] a = new double[100][2];
		double[][] b = new double[100][2];
		for(int i=0; i<a.length; i++){
			a[i][0] = random.nextDouble()*(-1);
			a[i][1] = random.nextDouble()*(-1);
		}
		for(int i=0; i<b.length; i++){
			b[i][0] = random.nextDouble();
			b[i][1] = random.nextDouble();
		}
		MatrixUtil.print(a);
		System.out.println("*****************************************");
		MatrixUtil.print(b);
		System.out.println("*****************************************");
		LDA l = new LDA();
		double[][] w = l.twoClass(a, b);
		MatrixUtil m = new MatrixUtil();
		double[][] a1 = m.multiplication(m.transpose(w), m.transpose(a));
		double[][] b1 = m.multiplication(m.transpose(w), m.transpose(b));
		m.print(m.transpose(a1));
		m.print(m.transpose(b1));

	}

}
