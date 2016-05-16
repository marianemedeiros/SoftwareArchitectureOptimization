/**
 * 
 */
package aco.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SplittableRandom;

import loadModel.LoadModel;
import loadModel.Solution;
import main.Main;
import metrics.architecture.MetricCoesion;
import metrics.architecture.MetricCoupling;
import metrics.architecture.ModulatizationQuality;
import metrics.style.MetricClientServerArch;
import metrics.style.MetricLayerArch;

/**
 * <code> Ant </code> represents a solution being build. 
 * An ant after combine all elements, will contain a solution.
 * 
 * @author mariane
 *
 */
public class Ant {
	// control id of ant
	private static int idDaFormiga = 0;
	private int identidade;

	// solution of ant
	private Solution solution;
	private Solution initialSolution = null;

	// pheromone matrix
	private Matrix pheromoneMatrix;

	private Probability probability;

	private double sumPenalties;
	private double sumPenalties2;
	private Penalty possiblePenaltiesOfSolution;

	private SplittableRandom random;
	
	public Ant(Matrix pheromone, Parametro p) {
		this.pheromoneMatrix = pheromone;
		idDaFormiga = idDaFormiga + 1;
		this.identidade = idDaFormiga;
		probability = new Probability(p);
		random = new SplittableRandom();
	}

	public Ant(Matrix pheromone, Solution s, Parametro p) {
		this(pheromone, p);
		this.initialSolution = s;
	}

	/**
	 * This function build and solution based on pheromone matrix.
	 * The solution are composed by components and classes, this two elements
	 * are controled by pheromone matrix (we have two pheromone matrix to control that, component
	 * matrix and class matrix).
	 *
	 * @return solution built by ant.
	 * */
	public synchronized Solution buildSolution() {
		sumPenalties = 0.0; sumPenalties2 = 0.0;
		Double probs[] = new Double[this.pheromoneMatrix.components];
		HashMap<Integer, Set<Integer>> mapComponentClass = new HashMap<Integer, Set<Integer>>();
		HashMap<Integer, Integer> mapClassComponent = new HashMap<Integer,Integer>();
		Penalty penaltiesOfCurrentSolution = new Penalty();
		
		// build matrix of Component x Classes
		for (int cl = 0; cl < this.pheromoneMatrix.classes; cl++) {
			int i = 0;
			for (int comp = 0; comp < this.pheromoneMatrix.components; comp++) {
				double prob = this.probability.calculatesProbability(this.pheromoneMatrix.componentClass, cl, comp, Probability.DEFAULT_VALUE_HEURISTIC);
				probs[i] = prob;
				i++;
			}

			int solutionChoose = roulette(probs);// roulette return index of best prob
			if(mapComponentClass.get(solutionChoose) == null){ // need declare ArrayList of classes
				Set<Integer> classes = new HashSet<Integer>();
				classes.add(cl);
				mapComponentClass.put(solutionChoose, classes);
				mapClassComponent.put(cl, solutionChoose);
			}else{
				mapComponentClass.get(solutionChoose).add(cl); // updates an element in the list of classes
				mapClassComponent.put(cl, solutionChoose);
			}
		}

		//verify before class combination which are combinations that break rules and how many times each class break the rule.
		possiblePenaltiesOfSolution = new Penalty();
		if(this.initialSolution != null && !this.initialSolution.type.equals("")){
			for (int i = 0; i < this.pheromoneMatrix.classes; i++) {
				for (int j = 0; j < this.pheromoneMatrix.classes; j++) {
					if(this.initialSolution.type.equals(LoadModel.LAYER)){
						Integer comp1 = mapClassComponent.get(i);
						Integer comp2 = mapClassComponent.get(j);
						
						if(this.initialSolution.mapNewId2OldId != null){
							int aux = this.initialSolution.mapNewId2OldId.get(comp1);
							comp1 = aux;
							int aux2 = this.initialSolution.mapNewId2OldId.get(comp2);
							comp2 = aux2;
						}
						
						Integer l1 = this.initialSolution.componentLayer.get(comp1);
						Integer l2 = this.initialSolution.componentLayer.get(comp2);
						
						if(l1 != null && l2 != null){
							MetricLayerArch metricLayerArch = new MetricLayerArch();
							possiblePenaltiesOfSolution = metricLayerArch.verifyStyle(i, j, l1, l2, possiblePenaltiesOfSolution);
						}
						// TODO fazer para quando uma classe se comunicar com uma outra classe que nao esta em nenhuma camada
					}else if(this.initialSolution.type.equals(LoadModel.CLIENT_SERVER)){
						String type_element0 = initialSolution.mapClassClient.get(i);
						if(type_element0 == null) type_element0 = initialSolution.mapClassServer.get(i);

						String type_element1 = initialSolution.mapClassClient.get(j);
						if(type_element1 == null) type_element1 = initialSolution.mapClassServer.get(j);

						MetricClientServerArch clientServerArch = new MetricClientServerArch();
						possiblePenaltiesOfSolution = clientServerArch.verifyStyle(i, j, type_element0, type_element1, possiblePenaltiesOfSolution);
					}
				}
			}
			
			if(Main.SHOW_LOGS)
				System.out.println("INFORMAÇÂO DE PENALIDADE ---- " + possiblePenaltiesOfSolution.classBreak.size() + " classes que quebraram a regra, " + possiblePenaltiesOfSolution.listBadRel.size() + " mÃ¡s relaÃ§Ãµes.");
		}

		// build matrix of Interfaces (Class x Class)
		Double probsClass[] = new Double[this.pheromoneMatrix.classes];
		HashMap<Integer, Integer[]> mapVetProbToMatrix = new HashMap<Integer, Integer[]>(); //this HashMap maps an prob of vector 'prob' to an index of matrix
		ArrayList<Integer[]> interfaces = new ArrayList<Integer[]>();

		// build matrix of relations between classes of same component (Class x Class)
		Double probsClassIntR[] = new Double[this.pheromoneMatrix.classes];
		HashMap<Integer, Integer[]> mapVetProbToMatrixIntR = new HashMap<Integer, Integer[]>(); //this HashMap maps an prob of vector 'prob' to an index of matrix
		ArrayList<Integer[]> internalRelation = new ArrayList<Integer[]>();

		for (int i = 0; i < this.pheromoneMatrix.classes; i++) {
			int x = 0; // interfaces
			int y = 0; // internal relations
			double sumProbs = 0;
			double sumProbsIntR = 0;
			for (int j = 0; j < this.pheromoneMatrix.classClass[0].length; j++) {
				if(i != j && !twoClassInComponent(i,j,mapComponentClass)){
					double prob = 0.0;
					/* based on the list of classBreak and badRealation i calculate how many penalty will be
					 * attribute to the combination of two classes, so pass the value to the function calculatesProbability()
					 * so that value can be considered in the calculation of probability.
					 * */
					prob = verifyStylerArchAndCalcHeuristic(i, j);
					
					probsClass[x] = prob;
					sumProbs += prob;
					Integer indices[] = new Integer[2];
					indices[0] = i; indices[1] = j;
					mapVetProbToMatrix.put(x, indices);
					x++;
				}else if (i != j && twoClassInComponent(i,j,mapComponentClass)){
					double probIntR = 0.0;
					/* if the architectural style is LAYERED, the calculation of probability, to relation between classes which are in the same
					 * component, will not consider the heuristic information. Because, we are considering relation between two classes in the same component
					 * and one component only can be in one layer.
					 * 
					 *  if the architectural style is CLIENT/SERVER, heuristic information will be considered, because classes in the same component
					 *  can be client or server.
					 * */
					if(this.initialSolution == null || this.initialSolution.type.equals(LoadModel.LAYER))
						probIntR = this.probability.calculatesProbability(this.pheromoneMatrix.classClass, i, j, Probability.DEFAULT_VALUE_HEURISTIC);
					else if (this.initialSolution.type.equals(LoadModel.CLIENT_SERVER))	
						probIntR = verifyStylerArchAndCalcHeuristic(i,j);
					else if(this.initialSolution != null || this.initialSolution.type.equals(""))// without architecture style
						probIntR = this.probability.calculatesProbability(this.pheromoneMatrix.classClass, i, j, Probability.DEFAULT_VALUE_HEURISTIC);

					probsClassIntR[y] = probIntR;
					sumProbsIntR += probIntR;
					Integer indices[] = new Integer[2];
					indices[0] = i; indices[1] = j;
					mapVetProbToMatrixIntR.put(y, indices);
					y++;
				}
			}

			if(sumProbs != 0){
				int solutionSelectedInterfaces = rouletteIn(probsClass,sumProbs);
				Integer elements[] = mapVetProbToMatrix.get(solutionSelectedInterfaces);
				if(elements[1] != (this.pheromoneMatrix.classClass[0].length - 1)){
					interfaces.add(mapVetProbToMatrix.get(solutionSelectedInterfaces));
					
					if (this.initialSolution != null && possiblePenaltiesOfSolution.classBreak.get(elements[0]) == null
							&& possiblePenaltiesOfSolution.classBreak.get(elements[1]) != null){//doesn't have i, but has j
						penaltiesOfCurrentSolution.addToClassBreak(elements[1]);
						penaltiesOfCurrentSolution.addToListBasRel(elements);
						sumPenalties = 	sumPenalties +
								Double.valueOf(possiblePenaltiesOfSolution.classBreak.get(elements[1])) / Double.valueOf(possiblePenaltiesOfSolution.listBadRel.size());
						sumPenalties2 = sumPenalties2 + 
								(1 - Double.valueOf(possiblePenaltiesOfSolution.classBreak.get(elements[1])) / Double.valueOf(possiblePenaltiesOfSolution.listBadRel.size()));

					}else if (this.initialSolution != null && possiblePenaltiesOfSolution.classBreak.get(elements[0]) != null
							&& possiblePenaltiesOfSolution.classBreak.get(elements[1]) == null){//doesn't have j, but has i
						penaltiesOfCurrentSolution.addToClassBreak(elements[0]);
						penaltiesOfCurrentSolution.addToListBasRel(elements);
						sumPenalties = 	sumPenalties +
								Double.valueOf(possiblePenaltiesOfSolution.classBreak.get(elements[0])) / Double.valueOf(possiblePenaltiesOfSolution.listBadRel.size());
						sumPenalties2 = sumPenalties2 + 
								(1 - Double.valueOf(possiblePenaltiesOfSolution.classBreak.get(elements[0])) / Double.valueOf(possiblePenaltiesOfSolution.listBadRel.size()));

					}else if (this.initialSolution != null && possiblePenaltiesOfSolution.classBreak.get(elements[0]) != null
							&& possiblePenaltiesOfSolution.classBreak.get(elements[1]) != null){//has i and j
						penaltiesOfCurrentSolution.addToClassBreak(elements[0]);
						penaltiesOfCurrentSolution.addToClassBreak(elements[1]);
						penaltiesOfCurrentSolution.addToListBasRel(elements);
						sumPenalties =  sumPenalties +
									Double.valueOf((possiblePenaltiesOfSolution.classBreak.get(elements[0]) + possiblePenaltiesOfSolution.classBreak.get(elements[1]))) / Double.valueOf(possiblePenaltiesOfSolution.listBadRel.size());
						sumPenalties2 =  sumPenalties2 +
								(1 - Double.valueOf((possiblePenaltiesOfSolution.classBreak.get(elements[0]) + possiblePenaltiesOfSolution.classBreak.get(elements[1]))) / Double.valueOf(possiblePenaltiesOfSolution.listBadRel.size()));
					}

					if(Main.SHOW_LOGS)
						System.out.println("---Interface " + mapVetProbToMatrix.get(solutionSelectedInterfaces)[0]
							+ "-" + mapVetProbToMatrix.get(solutionSelectedInterfaces)[1]);
				}else if(Main.SHOW_LOGS){
					System.out.println("---Classe " + elements[0] + " nÃ£o vai ser combinada com ninguÃ©m.");
				}
			}

			if(sumProbsIntR != 0){
				int solutionSelectedInternalRelation = rouletteIn(probsClassIntR,sumProbsIntR);
				Integer elements[] = mapVetProbToMatrixIntR.get(solutionSelectedInternalRelation);
				if(elements[1] != (this.pheromoneMatrix.classClass[0].length - 1)){
					internalRelation.add(mapVetProbToMatrixIntR.get(solutionSelectedInternalRelation));
					
					if (this.initialSolution.type.equals(LoadModel.CLIENT_SERVER)){
						if (this.initialSolution != null && possiblePenaltiesOfSolution.classBreak.get(elements[0]) == null
								&& possiblePenaltiesOfSolution.classBreak.get(elements[1]) != null){//doesn't have i, but has j
							penaltiesOfCurrentSolution.addToClassBreak(elements[1]);
							penaltiesOfCurrentSolution.addToListBasRel(elements);
							sumPenalties = 	sumPenalties +
									Double.valueOf(possiblePenaltiesOfSolution.classBreak.get(elements[1])) / Double.valueOf(possiblePenaltiesOfSolution.listBadRel.size());
							sumPenalties2 = sumPenalties2 + 
									(1 - Double.valueOf(possiblePenaltiesOfSolution.classBreak.get(elements[1])) / Double.valueOf(possiblePenaltiesOfSolution.listBadRel.size()));

						}else if (this.initialSolution != null && possiblePenaltiesOfSolution.classBreak.get(elements[0]) != null
								&& possiblePenaltiesOfSolution.classBreak.get(elements[1]) == null){//doesn't have j, but has i
							penaltiesOfCurrentSolution.addToClassBreak(elements[0]);
							penaltiesOfCurrentSolution.addToListBasRel(elements);
							sumPenalties = 	sumPenalties +
									Double.valueOf(possiblePenaltiesOfSolution.classBreak.get(elements[0])) / Double.valueOf(possiblePenaltiesOfSolution.listBadRel.size());
							sumPenalties2 = sumPenalties2 + 
									(1 - Double.valueOf(possiblePenaltiesOfSolution.classBreak.get(elements[0])) / Double.valueOf(possiblePenaltiesOfSolution.listBadRel.size()));

						}else if (this.initialSolution != null && possiblePenaltiesOfSolution.classBreak.get(elements[0]) != null
								&& possiblePenaltiesOfSolution.classBreak.get(elements[1]) != null){//has i and j
							penaltiesOfCurrentSolution.addToClassBreak(elements[0]);
							penaltiesOfCurrentSolution.addToClassBreak(elements[1]);
							penaltiesOfCurrentSolution.addToListBasRel(elements);
							sumPenalties =  sumPenalties +
									Double.valueOf((possiblePenaltiesOfSolution.classBreak.get(elements[0]) + possiblePenaltiesOfSolution.classBreak.get(elements[1]))) / Double.valueOf(possiblePenaltiesOfSolution.listBadRel.size());
							sumPenalties2 =  sumPenalties2 +
								(1 - Double.valueOf((possiblePenaltiesOfSolution.classBreak.get(elements[0]) + possiblePenaltiesOfSolution.classBreak.get(elements[1]))) / Double.valueOf(possiblePenaltiesOfSolution.listBadRel.size()));
						}						
					}
					if(Main.SHOW_LOGS)
						System.out.println("---Internal Relation " + mapVetProbToMatrixIntR.get(solutionSelectedInternalRelation)[0]
							+ "-" + mapVetProbToMatrixIntR.get(solutionSelectedInternalRelation)[1]);
				}else if(Main.SHOW_LOGS){
					System.out.println("---Classe " + elements[0] + " nÃ£o vai ser combinada com ninguÃ©m.");
				}
			}
		}

		if(this.initialSolution != null && this.initialSolution.type.equals(LoadModel.LAYER)){
			this.solution = new Solution(mapComponentClass,interfaces, internalRelation,
					this.initialSolution.componentLayer,mapClassComponent); 
			this.solution.penaltysOfSolution = penaltiesOfCurrentSolution;
		}else if(this.initialSolution != null && this.initialSolution.type.equals(LoadModel.CLIENT_SERVER)){
			this.solution = new Solution(mapComponentClass, interfaces, 
					this.initialSolution.mapClassServer, 
					this.initialSolution.mapClassClient, 
					internalRelation, mapClassComponent);
			this.solution.penaltysOfSolution = penaltiesOfCurrentSolution;
		}else{
			this.solution = new Solution(mapComponentClass,interfaces, internalRelation,null,mapClassComponent); 
		}

		// calculate metrics to this solution (pheromone information)
		MetricCoupling coupling = new MetricCoupling();
		this.solution = coupling.calculate(solution);

		MetricCoesion coesion = new MetricCoesion();
		this.solution = coesion.calculate(solution);

		ModulatizationQuality  modulatizationQuality = new ModulatizationQuality();
		this.solution = modulatizationQuality.calculate(solution);
		this.solution.totalOfPenalties = sumPenalties;
		this.solution.totalOfPenalties2 =  sumPenalties2;
		return  solution;
		
	}


	/**
	 * Consider penalty calculation to classes in the same component, because in CLIENT/SERVER style one component may have
	 * server and clients classes.
	 *
	 * Not calculate penalty if j are the last column of matrix (this column represent probability of does not combine the class with 
	 * anyone).
	 * 
	 * @param penalty
	 * @param i
	 * @param j
	 * @return
	 */
	private double verifyStylerArchAndCalcHeuristic(int i, int j) {
		double probIntR = 0.0;
		if(j == (this.pheromoneMatrix.classClass[0].length - 1) || (possiblePenaltiesOfSolution.classBreak.size() == 0 && possiblePenaltiesOfSolution.listBadRel.size() == 0)){
			probIntR = this.probability.calculatesProbability(this.pheromoneMatrix.classClass, i, j, Probability.DEFAULT_VALUE_HEURISTIC);
			if(Main.SHOW_LOGS)
				System.out.println("NÃ£o combinar classe " + i + " com ninguÃ©m. Ou, a arquitetura nÃ£o quebra nenhuma regra do estilo.");
			
		}else if(j != (this.pheromoneMatrix.classClass[0].length - 1)){
			if(Main.SHOW_LOGS)
				System.out.println("Quantas vezes a classe <<" + i + ">> quebrou a regra: <<" + possiblePenaltiesOfSolution.classBreak.get(i) +
					">> \nQuantas vezes a classe<<" + j + ">> quebrou a regra: <<" + possiblePenaltiesOfSolution.classBreak.get(j) + ">>");
			Double h = 0.0;
			if(possiblePenaltiesOfSolution.classBreak.get(i) == null &&  possiblePenaltiesOfSolution.classBreak.get(j) == null)
				h = Probability.DEFAULT_VALUE_HEURISTIC;
			else if (possiblePenaltiesOfSolution.classBreak.get(i) == null){
				h =  Double.valueOf(possiblePenaltiesOfSolution.classBreak.get(j)) / Double.valueOf(possiblePenaltiesOfSolution.listBadRel.size());
			}
			else if (possiblePenaltiesOfSolution.classBreak.get(j) == null){
				h =  Double.valueOf(possiblePenaltiesOfSolution.classBreak.get(i)) / Double.valueOf(possiblePenaltiesOfSolution.listBadRel.size());
			}else {
				h =  Double.valueOf((possiblePenaltiesOfSolution.classBreak.get(i) + possiblePenaltiesOfSolution.classBreak.get(j))) / Double.valueOf(possiblePenaltiesOfSolution.listBadRel.size());
			}
			
			if(Main.SHOW_LOGS)
				System.out.println("Valor da penalidade para combinaÃ§Ã£o " + i +"-"+ j  +" = "+ h + "(1-h=" + (1-h) + ")");
			probIntR = this.probability.calculatesProbability(this.pheromoneMatrix.classClass, i, j, (1 - h));
		}
		return probIntR;
	}

	/**
	 * Verify in mapComponentClass if class i and x are contained in the same component.
	 *
	 * @param i
	 * @param x
	 * @param mapComponentClass
	 * 
	 * @return true if they are in the same component
	 * @return false they are not in the same component
	 */
	private boolean twoClassInComponent(int i, int x, HashMap<Integer, Set<Integer>> mapComponentClass) {
		for (Entry<Integer, Set<Integer>> component : mapComponentClass.entrySet()) {
			if(component.getValue().contains(i) && component.getValue().contains(x)){
				return true; // i and x same component
			}
		}
		return false; // i and x not same component
	}


	/**
	 * @param probs - vector of probabilities of choice.
	 */
	private synchronized int roulette(Double[] probs) {
		double randomNum = random.nextDouble();
		double accumulation = 0;
		int solutionSelected = 0;
		for (int i = 0; i < probs.length; i++) {
			if(probs[i] != null && probs[i] != -1.0){
				accumulation = accumulation + probs[i];
				if (accumulation > randomNum) {
					solutionSelected = i;
					break;
				}
			}
		}
		return solutionSelected;
	}

	/**
	 * @param probs - vector of probabilities of choice.
	 * @param max - sum of vector probabilities.
	 */
	private synchronized int rouletteIn(Double[] probs, double max) {
		// sort a number between 0 and the sum of vector probabilities. 
		double range = max - 0.0;
		double scaled = random.nextDouble() * range;
		double shifted = scaled + 0.0; // (rand.nextDouble() * (max-min)) + min

		double accumulation = 0;
		int solutionSelected = 0;
		for (int i = 0; i < probs.length; i++) {
			if(probs[i] != null){
				accumulation = accumulation + probs[i];
				if (accumulation > shifted) {
					solutionSelected = i;
					break;
				}
			}
		}
		return solutionSelected;
	}

	public Solution getSolution() {
		return solution;
	}

	public int getIdentidade() {
		return identidade;
	}
}

