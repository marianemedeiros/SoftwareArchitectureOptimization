/**
 * 
 */
package metrics.architecture;

import loadModel.Solution;


/**
 * @author mariane
 *
 */
public class ModulatizationQuality {
	private int MAX = 1;
	private int MIN = -1;
	
	/**
	 * Calculate fitness function Modularization Quality
	 * 
	 * @param soluction
	 * @return
	 */	
	public Solution calculate(Solution soluction) {
		int k = soluction.componentClasses.size();
		if(k > 1 ){
			soluction.mMetric = (soluction.coesionMetric / k) - (soluction.acopMetric / ((k * (k-1))/2));
		}else if (k == 1){
			soluction.mMetric = soluction.coesionMetric;
		}
		//System.err.println("sem normalizar: " + soluction.mMetric);
		soluction.mMetric = normalize(soluction.mMetric);
		return soluction;
	}

	/**
	 * Normalize vaue of metric to interval between [0,1]
	 * norm = (x - MIN) / (MAX -MIN)
	 * @param x
	 * @return
	 */
	private double normalize(double x) {
		return (x - MIN) / (MAX - MIN);
	}
}
