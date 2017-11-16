//package machine;

public class MatrixUtil {
	public double determinant(double[][] matrix){ // ÐÐÁÐÊ½
		int dim = matrix.length;
		double det = 0;
		if(dim==1){
			return matrix[0][0];
		}else if(dim==2){
			det = matrix[0][0]*matrix[1][1]-matrix[0][1]*matrix[1][0];
		}else{
			for(int i=0; i<dim; i++){
				double sign = Math.pow(-1, i);
				double[][] sub = subMatrix(matrix, 0, i);
				det += matrix[0][i]*sign*determinant(sub);
			}
		}
		return det;
	}
	public double[][] subMatrix(double[][] matrix, int i, int j){
		int dim = matrix.length;
		double[][] sub = new double[dim-1][dim-1];
		for(int ii=0; ii<i; ii++){
			for(int jj=0; jj<j; jj++){
				sub[ii][jj] = matrix[ii][jj];
			}
			for(int jj=j; jj<dim-1; jj++){
				sub[ii][jj] = matrix[ii][jj+1];
			}
		}
		for(int ii=i; ii<dim-1; ii++){
			for(int jj=0; jj<j; jj++){
				sub[ii][jj] = matrix[ii+1][jj];
			}
			for(int jj=j; jj<dim-1; jj++){
				sub[ii][jj] = matrix[ii+1][jj+1];
			}
		}
		return sub;
	}
	
	public double[][] minor(double[][] matrix){ // Óà×Ó¾ØÕó
		int dim = matrix.length;
		double[][] minor = new double[dim][dim];
		for(int i=0; i<dim; i++){
			for(int j=0; j<dim; j++){
				minor[i][j] = Math.pow(-1, i+j)*determinant(subMatrix(matrix, i, j));
			}
		}
		return minor;
	}
	
	public double[][] adjugate(double[][] matrix){ // °éËæ¾ØÕó
		return transpose(minor(matrix));
	}
	
	public double[][] transpose(double[][] matrix){ // ×ªÖÃ
		int dimX = matrix.length;
		int dimY = matrix[0].length;
		double[][] transpose = new double[dimY][dimX];
		for(int i=0; i<dimY; i++){
			for(int j=0; j<dimX; j++){
				transpose[i][j] = matrix[j][i];
			}
		}
		return transpose;
	}
	
	public double[][] inverse(double[][] matrix){ // Äæ¾ØÕó
		double det = determinant(matrix);
		//System.out.println(det);
		double[][] adj = adjugate(matrix);
		//print(adj);
		int dim = matrix.length;
		double[][] inverse = new double[dim][dim];
		for(int i=0; i<dim; i++){
			for(int j=0; j<dim; j++){
				inverse[i][j] = adj[i][j]/det;
			}
		}
		
		return inverse;
	}
	
	public double[][] upperHessenberg(double[][] matrix){ // Householder transform
		if(matrix.length!=matrix[0].length){
			System.out.println("wrong dimensions!");
			return null;
		}
		
		int dim = matrix.length;
		double[][] I = identity(dim);
		
		double[][] result = matrix.clone();
		for(int i=0; i<dim-2; i++){
			double[] x = new double[dim];
			double[] w = new double[dim];
			double[][] v = new double[dim][1];
			for(int j=0; j<=i; j++) x[j] = 0;
			for(int j=i+1; j<dim; j++) x[j] = result[j][i];
			double xNorm = 0;
			for(double j : x) xNorm += Math.pow(j, 2);
			xNorm = Math.sqrt(xNorm);
			w[i+1] = xNorm;
			double sign = x[0]>0?-1:1;
			for(int j=0; j<dim; j++) v[j][0] = w[j]+sign*x[j];
			
			double[][] h = new double[dim][dim];
			double[][] p = new double[dim][dim];
			p = multiplication(multiplication(v, transpose(v)), 1.0/multiplication(transpose(v), v)[0][0]);
			h = minus(I, multiplication(p, 2.0));
			result = multiplication(multiplication(h, result), h);
			for(int j=i+2; j<dim; j++) result[j][i] = 0;
		}
		return result;
	}
	
	public double[][] QRDecomposition(double[][] matrix, int iterations){ // Givens transform on upper Hessenberg matrix
		if(matrix.length!=matrix[0].length){
			System.out.println("wrong dimensions!");
			return null;
		}
		
		int dim = matrix.length;
		double[][] result = matrix.clone();
		
		for(int it=0; it<iterations; it++){
			double[][] H = identity(dim);
			for(int i=0; i<dim-1; i++){
				if(result[i+1][i]!=0){
					double r = Math.sqrt(Math.pow(result[i][i], 2)+Math.pow(result[i+1][i], 2));
					double[][] I = identity(dim);
					I[i][i] = result[i][i]/r;
					I[i][i+1] = result[i+1][i]/r;
					I[i+1][i] = -I[i][i+1];
					I[i+1][i+1] = I[i][i];
					result = multiplication(I, result);
					H = multiplication(H, transpose(I));
				}
			}
			result = multiplication(result, H);
		}
		return result;
	}
	
	public double[] eigenvalues(double[][] matrix){ // all eigenvalues
		double[] values = new double[matrix.length];
		double[][] h = upperHessenberg(matrix);
		double[][] q = QRDecomposition(h, 1000);
		for(int i=0; i<values.length; i++) values[i] = q[i][i];
		return values;
	}
	
	public double exponentiation(double[][] matrix){ // the eigenvalue with the largest abs value and the corresponding eigenvector
		int dim = matrix.length;
		double[][] u = new double[dim][1];
		u[0][0] = 1;
		double betaPre = 0;
		double[] mp = maxAbs(u);
		double[][] y = new double[dim][1];
		for(int i=0; i<dim; i++) y[i][0] = u[i][0]/mp[1];
		u = multiplication(matrix, y);
		double[] mn = maxAbs(u);
		double betaNext = mp[0]*mn[0]*mn[1];
		while(Math.abs((betaNext-betaPre)/betaNext)>0.000000000001){
			betaPre = betaNext;
			for(int i=0; i<dim; i++){
				y[i][0] = u[i][0]/mn[1];
				System.out.print(y[i][0]+"\t");
			}
			System.out.println();
			u = multiplication(matrix, y);
			mp = mn.clone();
			mn = maxAbs(u);
			//System.out.println(betaNext);
			betaNext = mp[0]*mn[0]*mn[1];
		}
		
		return betaNext;
	}
	private double[] maxAbs(double[][] u){
		double[] result = new double[2];
		result[0] = u[0][0]<0?-1:1;
		result[1] = Math.abs(u[0][0]);
		for(int i=1; i<u.length; i++){
			if(Math.abs(u[i][0])>result[1]){
				result[1] = Math.abs(u[i][0]);
				result[0] = u[i][0]<0?-1:1;
			}
		}
		return result;
	}
	
	public double[][] add(double[][] a, double[][] b){
		if(a.length!=b.length || a[0].length!=b[0].length){
			System.out.println("wrong dimensions!");
			return null;
		}
		double[][] c = new double[a.length][a[0].length];
		for(int i=0; i<c.length; i++){
			for(int j=0; j<c[0].length; j++){
				c[i][j] = a[i][j]+b[i][j];
			}
		}
		
		return c;
	}
	public double[][] minus(double[][] a, double[][] b){
		if(a.length!=b.length || a[0].length!=b[0].length){
			System.out.println("wrong dimensions!");
			return null;
		}
		double[][] c = new double[a.length][a[0].length];
		for(int i=0; i<c.length; i++){
			for(int j=0; j<c[0].length; j++){
				c[i][j] = a[i][j]-b[i][j];
			}
		}
		
		return c;
	}
	public double[][] multiplication(double[][] a, double[][] b){
		if(a[0].length!=b.length){
			System.out.println("wrong dimensions!");
			return null;
		}
		double[][] result = new double[a.length][b[0].length];
		for(int i=0; i<result.length; i++){
			for(int j=0; j<result[0].length; j++){
				double sum = 0;
				for(int k=0; k<a[0].length; k++){
					sum += a[i][k]*b[k][j];
				}
				result[i][j] = sum;
			}
		}
		return result;
	}
	public double[][] multiplication(double[][] a, double b){
		double[][] result = new double[a.length][a[0].length];
		for(int i=0; i<result.length; i++){
			for(int j=0; j<result[0].length; j++){
				result[i][j] = a[i][j]*b;
			}
		}
		return result;
	}
	
	public double[][] identity(int dim){
		double[][] I = new double[dim][dim];
		for(int i=0; i<dim; i++) I[i][i] = 1;
		return I;
	}
	
	public static void print(double[][] a){
		for(double[] i : a){
			for(double j : i) System.out.print(j+"\t");
			System.out.println();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//double[][] a = {{2,1,-1},{4,-1,1},{201,102,-99}}; // -18
		//double[][] a = {{1,1,-1,3},{-1,-1,2,1},{2,5,2,4},{1,2,3,2}}; // 33
		//double[][] a = {{1,0,-1,2},{-2,1,3,1},{0,1,0,-1},{1,3,4,-2}}; // 31
		//double[][] matrix = {{2.5,-1.5},{-1.5,1}};
		//double[][] matrix = {{4,3,0,0},{1,4,3,0},{0,1,4,3},{0,0,1,4}};
		double[][] matrix = {{5,-3,2},{6,-4,4},{4,-4,5}};
		MatrixUtil m = new MatrixUtil();
		//double[][] a = {{1,2}};
		//double[][] b = {{2},{1}};
		//double[][] r = m.multiplication(a, b);
		//System.out.println(r[0][0]);
		//m.print(r);
		//double[][] I = m.identity(3);
		//double[] values = m.eigenvalues(matrix);
		//for(double i : values) System.out.println(i);
		System.out.println(m.exponentiation(matrix));
		//double[][] inverse = m.inverse(matrix);
		//m.print(inverse);
		//System.out.println(m.determinant(a));
		//double[][] sub = m.subMatrix(a, 2, 1);
		//for(int i=0; i<sub.length; i++){
			//for(double j : sub[i]){
				//System.out.print(j+" ");
			//}
			//System.out.println();
		//}

	}

}
