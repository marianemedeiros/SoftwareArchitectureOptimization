/**
 * 
 */
package metrics.architecture;

import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.math.util.FastMath;

import loadModel.Solution;


/**
 * @author mariane
 *
 */
public class MetricCoesion {

	/**
	 * This function calculate how coesion a soluction it is;
	 * 
	 * @param soluction to calculate the coesion metric 
	 *   
	 * @return Soluction with coesion value metric
	 */

	public Solution calculate(Solution soluction) {
		double internalRelation;
		double coesion = 0.0;
		for (Entry<Integer, Set<Integer>> component : soluction.componentClasses.entrySet()) {
			internalRelation = 0.0;
			for (Integer[] element : soluction.internalRelations) {
				if(component.getValue().contains(element[0]) && component.getValue().contains(element[1])){
					internalRelation++;
				}				
			}
			coesion = coesion + (internalRelation/ (FastMath.pow(component.getValue().size(), 2)));
		}
		soluction.coesionMetric = coesion;
		return soluction;
	}

}
