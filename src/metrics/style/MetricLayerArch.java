/**
 * 
 */
package metrics.style;

import aco.entities.Penalty;

/**
 * @author mariane
 *
 */
public class MetricLayerArch {
	public double totalPenalty = 0.0;

	/**
	 * Verify if Solution break the rules of layered architecture style
	 * 	Rule 1. layer n can't make a communication with layer (n-2)
	 * */
	public Penalty verifyStyle(Integer class1, Integer class2, Integer layer1, Integer layer2, Penalty penalty){
			if(layer2 > (layer1+1) || layer1 > (layer2+1)){
				if(!penalty.classBreak.containsKey(class1)){
					penalty.classBreak.put(class1, 1);
				}else{
					penalty.classBreak.put(class1, penalty.classBreak.get(class1)+1);
				}
				if(!penalty.classBreak.containsKey(class2)){
					penalty.classBreak.put(class2, 1);
				}else{
					penalty.classBreak.put(class1, penalty.classBreak.get(class2)+1);
				}
				Integer[] r = new Integer[2];
				r[0] = class1; r[1] = class2;
					penalty.listBadRel.add(r);
			}
		return penalty;
	}
}
