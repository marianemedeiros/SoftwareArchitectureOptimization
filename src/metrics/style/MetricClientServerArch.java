/**
 * 
 */
package metrics.style;

import loadModel.LoadModel;
import aco.entities.Penalty;

/**
 * @author mariane
 *
 */
public class MetricClientServerArch {

	public Penalty verifyStyle(Integer class1, Integer class2, String type1, String type2, Penalty penalty){
		//Regra 1: um elemento CLIENTE só pode se conectar a um elemento SERVIDOR
		//Regra 2: um elemento SERVIDOR só pode se conectar a um elemento SERVIDOR.
		if(type1.equals(LoadModel.CLIENT) && type2.equals(LoadModel.CLIENT) ||
				type1.equals(LoadModel.SERVER) && type2.equals(LoadModel.CLIENT)){
			if(!penalty.classBreak.containsKey(class1)){
				penalty.classBreak.put(class1, 1);
			}else{
				Integer sum = penalty.classBreak.get(class1)+1;
				penalty.classBreak.put(class1, sum);
			}
//			System.out.println("---- " + class1 +":"+ type1 + " ---" +class2 + ":"+ type2);
//			System.out.println("QUEBROU REGRA: " + class1 + ": " + penalty.classBreak.get(class1));

			int r[] = new int[2];
			r[0] = class1; r[1] = class2;
			penalty.listBadRel.add(r);
		}
		return penalty;
	}
}


