/**
 * 
 */
package aco.entities;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import loadModel.Solution;
import main.Main;

/**
 * @author mariane
 *
 */
public class Probability {
	public static final Double DEFAULT_VALUE_HEURISTIC = 1.0;

	private Matrix matrix;
	
	public Probability(){
		
	}
	
	public Probability(Matrix pMatrix){
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
	public double calculatesProbability(Double[][] matriz, int line, int column, Double h) {
		double numerador = 0;
		//System.err.println("line " + line + "\ncolumn " + column);
		numerador = Math.pow(matriz[line][column], Main.ALPA);
		numerador = numerador * Math.pow(h, Main.BETA);

		double denominador = 0;
		for (int i = 0; i < matriz[0].length; i++) 
			denominador = denominador + (( Math.pow(matriz[line][i], Main.ALPA) * Math.pow(h, Main.BETA)));
	
		return (denominador != 0 ? (numerador / denominador) : 0);
	}
	

}
