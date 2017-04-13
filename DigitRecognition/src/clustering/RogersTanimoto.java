package clustering;

import java.util.Arrays;

import interfaces.ClusteringSimilarity;

public class RogersTanimoto implements ClusteringSimilarity {
	public float compute(int[] mat) {
		return ((float)(mat[0] + mat[3])) / (mat[0] + mat[3] + 2 * (mat[1] + mat[2]));
	}
}
