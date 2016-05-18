/**
 * 
 */
package aco.entities;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.math.util.FastMath;

import loadModel.Solution;

/**
 * @author mariane
 *
 */
public class Probability {
	public static final double DEFAULT_VALUE_HEURISTIC = 1.0;

	private Matrix matrix;
	private Parametro parametros;
	
	public Probability(Parametro p ){
		this.parametros = p;
	}
	
	public Probability(Matrix pMatrix, Parametro p){
		this.parametros = p;
		matrix = pMatrix;
	}
	
	/**
	 * @param s , a solution already structured to verify the relationship.
	 * */
	public Matrix verifyRelation(Solution s) {
		// 1 calcular as probabilidades de combinações das classes nos componentes
		HashMap<Integer, Set<Integer>> compClasses = s.componentClasses;
		
		for (Entry<Integer, Set<Integer>> element : compClasses.entrySet()) {
			Integer component = element.getKey();
			for (Integer class_ : element.getValue()) {
				double rt = calculatesProbability(this.matrix.componentClass, class_, component, DEFAULT_VALUE_HEURISTIC);
				this.matrix.componentClass[class_][component] = rt;
			}
		}
	
		if(s.interfaces != null){
			// 2 calcular as probabilidades de relacionar classes de diferentes componentes (interfaces)
			for (Integer[] interface_ : s.interfaces) {
				double rt = calculatesProbability(this.matrix.classClass, interface_[0], interface_[1], DEFAULT_VALUE_HEURISTIC);
//				System.err.println(interface_[0] + ":" + interface_[1] + "=" + rt);
				this.matrix.classClass[interface_[0]][interface_[1]] = rt;
			}			
		}

		if(s.internalRelations != null){
			// 3 calccular as probabilidades de relacionar classes de um mesmo componente (internalRelation)
			for (Integer[] interface_ : s.internalRelations) {
				double rt = calculatesProbability(this.matrix.classClass, interface_[0], interface_[1], DEFAULT_VALUE_HEURISTIC);
//				System.err.println(interface_[0] + ":" + interface_[1] + "=" + rt);
				this.matrix.classClass[interface_[0]][interface_[1]] = rt;
			}			
		}
//		this.matrix.showMatrix();
		return this.matrix;
	}
	
	/**
	 * Calculate probability of combine a class (line) in a component (column).
	 * 
	 * @param matriz - that will be iterate to calculate the probabilities.
	 * @param line - line index.
	 * @param column - column index.
	 */
	public double calculatesProbability(double[][] matriz, int line, int column, double h) {
		float numerador = 0;
		float pow = (float) FastMath.pow(h, parametros.BETA);
		 //(float) Math.pow(h, parametros.BETA);
		//System.err.println("line " + line + "\ncolumn " + column);
		numerador = (float) Math.pow(matriz[line][column], parametros.ALPA);
		numerador = numerador * pow;

		float denominador = 0;
		for (int i = 0; i < matriz[0].length; i++) 
			denominador = denominador + ((float) ( Math.pow(matriz[line][i], parametros.ALPA) * pow));
	
		return (denominador != 0 ? (numerador / denominador) : 0);
	}
	

}
