//package machine;

public class MatrixUtil {
	public double determinant(double[][] matrix){ // 行列式
		int dim = matrix.length;
		double det = 0;
		if(dim==2){
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
	
	public double[][] minor(double[][] matrix){ // 余子矩阵
		int dim = matrix.length;
		double[][] minor = new double[dim][dim];
		for(int i=0; i<dim; i++){
			for(int j=0; j<dim; j++){
				minor[i][j] = Math.pow(-1, i+j)*determinant(subMatrix(matrix, i, j));
			}
		}
		return minor;
	}
	
	public double[][] adjugate(double[][] matrix){ // 伴随矩阵
		return transpose(minor(matrix));
	}
	
	public double[][] transpose(double[][] matrix){ // 转置
		int dim = matrix.length;
		double[][] transpose = new double[dim][dim];
		for(int i=0; i<dim; i++){
			for(int j=0; j<dim; j++){
				transpose[i][j] = matrix[j][i];
			}
		}
		return transpose;
	}
	
	public double[][] inverse(double[][] matrix){ // 逆矩阵
		double det = determinant(matrix);
		double[][] adj = adjugate(matrix);
		int dim = matrix.length;
		double[][] inverse = new double[dim][dim];
		for(int i=0; i<dim; i++){
			for(int j=0; j<dim; j++){
				inverse[i][j] = adj[i][j]/det;
			}
		}
		
		return inverse;
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//double[][] a = {{2,1,-1},{4,-1,1},{201,102,-99}}; // -18
		//double[][] a = {{1,1,-1,3},{-1,-1,2,1},{2,5,2,4},{1,2,3,2}}; // 33
		//double[][] a = {{1,0,-1,2},{-2,1,3,1},{0,1,0,-1},{1,3,4,-2}}; // 31
		//double[][] matrix = {{4,3,0,0},{1,4,3,0},{0,1,4,3},{0,0,1,4}};
		MatrixUtil m = new MatrixUtil();
		double[][] a = {{1,2},{3,4}};
		double[][] b = {{2},{1}};
		double[][] r = m.multiplication(a, b);
		//double[][] inverse = m.inverse(matrix);
		for(int i=0; i<r.length; i++){
			for(double j : r[i]){
				System.out.print(j+"\t");
			}
			System.out.println();
		}
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
