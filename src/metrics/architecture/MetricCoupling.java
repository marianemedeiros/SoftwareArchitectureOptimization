package metrics.architecture;

import loadModel.Solution;



public class MetricCoupling {


	/**
	 * This function calculate how coupling a soluction it is;
	 * 
	 * @param soluction to calculate the coupling metric 
	 *   
	 * @return Soluction with coupling value metric
	 */
	public Solution calculate(Solution soluction) {
		double classesCp1 = 0.0; // number of classes in cp1
		double classesCp2 = 0.0; // number of classes in cp2
		double connectionsBCp1Cp2; // connections between cp1 and cp2
		double coupling = 0; // var that save coupling value
		Object[] components = soluction.componentClasses.keySet().toArray();// components array
		Integer cp1 , cp2;
		for (int i = 0; i < components.length+1; i++) { // to combine components
			for (int j = i+1; j < components.length; j++) { // to combine components
				connectionsBCp1Cp2 = 0.0;
				cp1 = (Integer) components[i];// id of component
				cp2 = (Integer) components[j];
				classesCp1 = soluction.componentClasses.get(cp1).size();// number of classes in component
				classesCp2 = soluction.componentClasses.get(cp2).size();
				
//				System.out.println("--- components " + cp1 + cp2);
				for (int clCp1 : soluction.componentClasses.get(cp1)) {// to roam classes of clcp1
					for (int clCp2 : soluction.componentClasses.get(cp2)) {// to roam classes of clcp2
						for (int[] interfaces : soluction.interfaces) {// to roam interfaces
//							System.out.println(clCp1 + ", " + clCp2 + "int " + interfaces[0] + " " + interfaces[1]);
							if((interfaces[0] == clCp1 && interfaces[1] == clCp2) 
									|| interfaces[0] == clCp2 && interfaces[1] == clCp1){// verify if has connection between clCp1 and clCp2 of components c1 and c2
								//System.out.println("---- " + clCp1 + ", " + clCp2);
								connectionsBCp1Cp2++;
							}
						}
					}
				}
				coupling = coupling + (connectionsBCp1Cp2/ (2 * classesCp1 * classesCp2));
//				System.out.println("acop parcial" + coupling);
			}
		}

		soluction.acopMetric = coupling;
//		System.out.println("acop " + coupling);
		return soluction;	
	}
	


}
