//package machine;

import java.util.Random;

public class Markov {
	protected static int numOfHidden = 5; // number of possible hidden states
	protected static int numOfObserved = 5; // number of possible observed states
	protected static int sequenceLength = 10;
	
	int[] hiddenSequence = new int[sequenceLength];
	int[] observedSequence = new int[sequenceLength];
	double[] initDistribution = new double[numOfHidden];
	double[][] transformMatrix = new double[numOfHidden][numOfHidden];
	double[][] emissionMatrix = new double[numOfHidden][numOfObserved];
	double[][] alpha = new double[sequenceLength][numOfHidden];;
	double[][] beta = new double[sequenceLength][numOfHidden];
	
	protected static Random random = new Random(System.currentTimeMillis());
	
	protected void initialProbability(){
		observedSequence = new int[sequenceLength];
		for(int i=0; i<sequenceLength; i++) observedSequence[i] = random.nextInt(numOfObserved);
		initializeNormalized(initDistribution);
		for(double[] a : transformMatrix) initializeNormalized(a);
		for(double[] a : emissionMatrix) initializeNormalized(a);
	}
	protected void initializeNormalized(double[] a){
		double total = 0;
		for(int i=0; i<a.length; i++){
			a[i] = random.nextDouble();
			total += a[i];
		}
		for(int i=0; i<a.length; i++){
			a[i] /= total;
		}
	}
	protected void initialViterbi(){
		numOfHidden = 3;
		numOfObserved = 2;
		sequenceLength = 3;
		observedSequence = new int[]{0,1,0};
		initDistribution = new double[]{0.2,0.4,0.4};
		transformMatrix = new double[][]{
			{0.5,0.2,0.3},
			{0.3,0.5,0.2},
			{0.2,0.3,0.5}
		};
		emissionMatrix = new double[][]{
			{0.5,0.5},
			{0.4,0.6},
			{0.7,0.3}
		};
	}
	
	protected void BaumWelch(){
		int numOfIterations = 10000;
		double[][] gamma = new double[sequenceLength][numOfHidden];
		double[][][] kesi = new double[sequenceLength-1][numOfHidden][numOfHidden];
		for(int n=0; n<numOfIterations; n++){
			System.out.println(forward());
			System.out.println(backward());
			System.out.println("\n**********************\n");
			for(int t=0; t<sequenceLength; t++){
				for(int i=0; i<numOfHidden; i++){
					double total = 0;
					for(int j=0; j<numOfHidden; j++){
						total += alpha[t][j]*beta[t][j];
					}
					gamma[t][i] = alpha[t][i]*beta[t][i]/total;
				}
			}
			for(int t=0; t<sequenceLength-1; t++){
				double total = 0;
				for(int i=0; i<numOfHidden; i++){
					for(int j=0; j<numOfHidden; j++){
						total += alpha[t][i]*transformMatrix[i][j]*emissionMatrix[j][observedSequence[t+1]]*beta[t+1][j];
					}
				}
				for(int i=0; i<numOfHidden; i++){
					for(int j=0; j<numOfHidden; j++){
						kesi[t][i][j] = alpha[t][i]*transformMatrix[i][j]*emissionMatrix[j][observedSequence[t+1]]*beta[t+1][j]/total;
					}
				}
			}
			for(int i=0; i<numOfHidden; i++){
				for(int j=0; j<numOfHidden; j++){
					double kesiSum = 0;
					double gammaSum = 0;
					for(int t=0; t<sequenceLength-1; t++){
						kesiSum += kesi[t][i][j];
						gammaSum += gamma[t][i];
					}
					transformMatrix[i][j] = kesiSum/gammaSum;
				}
				
				for(int k=0; k<numOfObserved; k++){
					double gammaSum1 = 0;
					double gammaSum2 = 0;
					for(int t=0; t<sequenceLength; t++){
						if(observedSequence[t]==k) gammaSum1 += gamma[t][i];
						gammaSum2 += gamma[t][i];
					}
					emissionMatrix[i][k] = gammaSum1/gammaSum2;
				}
				
				initDistribution[i] = gamma[0][i];
			}
			
		}
	}
	protected double forward(){
		alpha = new double[sequenceLength][numOfHidden];
		for(int i=0; i<numOfHidden; i++){
			alpha[0][i] = initDistribution[i]*emissionMatrix[i][observedSequence[0]];
			//System.out.print(alpha[0][i]+"\t");
		}
		//System.out.println();
		for(int j=1; j<sequenceLength; j++){
			for(int i=0; i<numOfHidden; i++){
				double total = 0;
				for(int k=0; k<numOfHidden; k++){
					total += alpha[j-1][k]*transformMatrix[k][i];
				}
				alpha[j][i] = total*emissionMatrix[i][observedSequence[j]];
				//System.out.print(alpha[j][i]+"\t");
			}
			//System.out.println();
		}
		double total = 0;
		for(double x : alpha[sequenceLength-1]) total += x;
		return total;
	}
	protected double backward(){
		beta = new double[sequenceLength][numOfHidden];
		for(int i=0; i<numOfHidden; i++) {
			beta[sequenceLength-1][i] = 1;
			//System.out.print(beta[sequenceLength-1][i]+"\t");
		}
		//System.out.println();
		for(int j=sequenceLength-2; j>=0; j--){
			for(int i=0; i<numOfHidden; i++){
				double total = 0;
				for(int k=0; k<numOfHidden; k++){
					total += transformMatrix[i][k]*emissionMatrix[k][observedSequence[j+1]]*beta[j+1][k];
				}
				beta[j][i] = total;
				//System.out.print(beta[j][i]+"\t");
			}
			//System.out.println();
		}
		double total = 0;
		for(int i=0; i<numOfHidden; i++){
			total += beta[0][i]*initDistribution[i]*emissionMatrix[i][observedSequence[0]];
		}
		return total;
	}
	
	protected void decodeViterbi(){
		double[][] delta = new double[sequenceLength][numOfHidden];
		int[][] psy = new int[sequenceLength][numOfHidden];
		for(int i=0; i<numOfHidden; i++){
			delta[0][i] = initDistribution[i]*emissionMatrix[i][observedSequence[0]];
			psy[0][i] = 0;
		}
		
		for(int t=1; t<sequenceLength; t++){
			for(int i=0; i<numOfHidden; i++){
				int maxHidden = 0;
				double maxProb = -1;
				for(int j=0; j<numOfHidden; j++){
					double p = delta[t-1][j]*transformMatrix[j][i];
					if(p>maxProb){
						maxProb = p;
						maxHidden = j;
					}
				}
				delta[t][i] = maxProb*emissionMatrix[i][observedSequence[t]];
				psy[t][i] = maxHidden;
			}
			
		}
		int maxHidden = 0;
		double maxProb = delta[sequenceLength-1][0];
		for(int i=0; i<numOfHidden; i++){
			if(maxProb<delta[sequenceLength-1][i]){
				maxProb = delta[sequenceLength-1][i];
				maxHidden = i;
			}
		}
		System.out.print(maxHidden+" ");
		for(int i=sequenceLength-1; i>0; i--){
			maxHidden = psy[i][maxHidden];
			System.out.print(maxHidden+" ");
		}
		System.out.println();
		System.out.println(maxProb);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Markov m = new Markov();
		m.initialProbability();
		//System.out.println(m.forward());
		//System.out.println("\n***********************************************************************************************\n");
		//System.out.println(m.backward());
		//m.BaumWelch();
		m.initialViterbi();
		m.decodeViterbi();

	}

}
