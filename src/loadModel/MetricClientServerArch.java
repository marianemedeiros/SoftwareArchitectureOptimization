/**
 * 
 */
package loadModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

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
				}else{
					classBreak.put(element[0], classBreak.get(element[0])+1);
				}
				System.out.println(element[0] + ": " + classBreak.get(element[0]));
				listBadRel.add(element);
			}
		}
		
		ArrayList<Double> totalBreakList = new ArrayList<Double>();
		ArrayList<Integer[]> elementList = new ArrayList<Integer[]>();
		double soma = 0;
		double maior = 0;
		int index = 0;
		
		for (Integer[] badElement : listBadRel) {
			double totalBreak = classBreak.get(badElement[0]); // total times that badElement[0] break the rule
			totalBreakList.add(index, totalBreak);
			elementList.add(index, badElement);
			soma += totalBreak;
			index++;
			if(totalBreak > maior)
				maior = totalBreak;
		}
		for (int i = 0; i < totalBreakList.size(); i++) {
			double penalty = totalBreakList.get(i)/soma;
			//TODO esse valor da penalidade será descontado (evaporado) da matriz de feromonio
			//matrix.classClass[elementList.get(i)[0]][elementList.get(i)[1]] = penalty/maior;
			System.out.println("Penalidade da classe <<" + elementList.get(i)[0] + ":" + elementList.get(i)[1] + ">>, " + penalty/maior);
		}
		
		double cont;
		HashMap<Integer, Set<Integer>> compBadEl = new HashMap<Integer,Set<Integer>>();
		
		// total de classes que quebraram a regra do componente x / total de classes que o componente x possui
		for (Entry<Integer, Set<Integer>> mapElement : soluction.componentClasses.entrySet()) {
			cont = 0.0;
			Set<Integer> list = new HashSet<Integer>();
			for (Integer[] badEl : listBadRel) {
				if(mapElement.getValue().contains(badEl[0])){
					cont++;
					list.add(badEl[0]);
					compBadEl.put(mapElement.getKey(), list);
				}
			}
			double penalty = cont / mapElement.getValue().size();
			
			//TODO esse valor da penalidade será descontado (evaporado) da matriz de feromonio
			System.out.println("Penalidade do componente <<" +cont + "/" + mapElement.getValue().size() +">> " + penalty);
		}
		

	}
}

