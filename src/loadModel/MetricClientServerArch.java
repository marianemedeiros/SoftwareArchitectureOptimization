/**
 * 
 */
package loadModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author mariane
 *
 */
public class MetricClientServerArch {


	public void verifyStyle(Solution soluction){
		HashMap<Integer,Integer> classBreak = new HashMap<Integer,Integer>();
		ArrayList<Integer[]> listBadRel = new ArrayList<Integer[]>();

		for (Integer[] element : soluction.interfaces) {
			String type_element0 = soluction.mapClassClient.get(element[0]);
			if(type_element0 == null) type_element0 = soluction.mapClassServer.get(element[0]);
		
			String type_element1 = soluction.mapClassClient.get(element[1]);
			if(type_element1 == null) type_element1 = soluction.mapClassServer.get(element[1]);
			
			//Regra 1: um elemento CLIENTE só pode se conectar a um elemento SERVIDOR
			//Regra 2: um elemento SERVIDOR só pode se conectar a um elemento SERVIDOR.
			if(type_element0.equals(LoadModel.CLIENT) && type_element1.equals(LoadModel.CLIENT) ||
					type_element0.equals(LoadModel.SERVER) && type_element1.equals(LoadModel.CLIENT)){
				if(!classBreak.containsKey(element[0])){
					classBreak.put(element[0], 1);
					System.out.println(element[0] + ": " + classBreak.get(element[0]));
				}else{
					classBreak.put(element[0], classBreak.get(element[0])+1);
					System.out.println(classBreak.get(element[0]));
				}
				listBadRel.add(element);
			}
		}

	}
}

