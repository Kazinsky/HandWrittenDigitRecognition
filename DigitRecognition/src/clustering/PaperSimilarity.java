package clustering;

import java.util.Arrays;

import interfaces.ClusteringSimilarity;

public class PaperSimilarity implements ClusteringSimilarity {
	private float alpha;
	private float beta;
	
	public PaperSimilarity(float a, float b) {
		alpha = a;
		beta = b;
	}
	public float compute(int[] mat) {
		return ((alpha * mat[0]) + (beta * mat[3])) / ((alpha * mat[0]) + (beta * mat[3]) + 2 * mat[1] + 2 * mat[2]);
	}
}
