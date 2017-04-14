package clustering;

import interfaces.ClusteringSimilarity;

public class JaccardSimilarity implements ClusteringSimilarity {
	public float compute(int[] mat) {
		return (float)mat[0] / (mat[0] + mat[1] + mat[2]);
	}
}
