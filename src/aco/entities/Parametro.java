/**
 * 
 */
package aco.entities;

/**
 * @author mariane
 *
 */
public class Parametro {

	public int ITERATIONS = 20;
	public int ANTS = 10;
	public double RO = 0.2; //used in uptadePheromone() at class Colony.
	public double ALPA = 0.9; //used in calculatesProbability() at class Probability. 
	public double BETA = 0.0;  //used in calculatesProbability() at class Probability. (heuristic information)
	
	/**
	 * 
	 */
	public Parametro(int it, int ants, double ro, double alpa, double beta) {
		this.ITERATIONS = it;
		this.ANTS = ants;
		this.RO = ro;
		this.ALPA = alpa;
		this.BETA = beta;
	}
}
