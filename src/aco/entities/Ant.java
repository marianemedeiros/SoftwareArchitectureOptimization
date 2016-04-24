/**
 * 
 */
package aco.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

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

	public Ant(Matrix pheromone) {
		this.pheromoneMatrix = pheromone;
		idDaFormiga = idDaFormiga + 1;
		this.identidade = idDaFormiga;
		probability = new Probability();
	}

	public Ant(Matrix pheromone, Solution s) {
		this.pheromoneMatrix = pheromone;
		idDaFormiga = idDaFormiga + 1;
		this.identidade = idDaFormiga;
		this.initialSolution = s;
		probability = new Probability();
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
		Double probs[] = new Double[this.pheromoneMatrix.components];
		HashMap<Integer, Set<Integer>> mapComponentClass = new HashMap<Integer, Set<Integer>>();
		HashMap<Integer, Integer> mapClassComponent = new HashMap<Integer,Integer>();

		// build matrix of Component x Classes
		for (int cl = 0; cl < this.pheromoneMatrix.classes; cl++) {
			int i = 0;
			for (int comp = 0; comp < this.pheromoneMatrix.components; comp++) {
				double prob = this.probability.calculatesProbability(this.pheromoneMatrix.componentClass, cl, comp, Probability.DEFAULT_VALUE_HEURISTIC);
				//System.out.println("- class " + cl + " in component " + comp + " prob is : " + prob);
				probs[i] = prob;
				i++;
			}

			int solutionChoose = roulette(probs);// roulette return index of best prob
			//System.out.println("-- solutionChoosed: " + solutionChoose);
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

		//DONE verificar antes da combinação das classes, quais as combinações quebram a regra e quantas vezes cada classe quebra a regra
		Penalty penalty = new Penalty();
		if(this.initialSolution != null){
			for (int i = 0; i < this.pheromoneMatrix.classes; i++) {
				for (int j = 0; j < this.pheromoneMatrix.classes; j++) {
					if(this.initialSolution.type.equals(LoadModel.LAYER)){
						Integer comp1 = this.initialSolution.classComponent.get(i);
						Integer comp2 = this.initialSolution.classComponent.get(j);

						Integer l1 = this.initialSolution.componentLayer.get(comp1);
						Integer l2 = this.initialSolution.componentLayer.get(comp2);

						MetricLayerArch metricLayerArch = new MetricLayerArch();
						penalty = metricLayerArch.verifyStyle(i, j, l1, l2, penalty);

					}else if(this.initialSolution.type.equals(LoadModel.CLIENT_SERVER)){
						String type_element0 = initialSolution.mapClassClient.get(i);
						if(type_element0 == null) type_element0 = initialSolution.mapClassServer.get(i);

						String type_element1 = initialSolution.mapClassClient.get(j);
						if(type_element1 == null) type_element1 = initialSolution.mapClassServer.get(j);

						MetricClientServerArch clientServerArch = new MetricClientServerArch();
						penalty = clientServerArch.verifyStyle(i, j, type_element0, type_element1, penalty);
					}
				}
			}
			if(Main.SHOW_LOGS)
				System.out.println("INFORMAÇÂO DE PENALIDADE ---- " + penalty.classBreak.size() + " classes que quebraram a regra, " + penalty.listBadRel.size() + " más relações.");
		}

		// build matrix of Interfaces (Class x Class)
		Double probsClass[] = new Double[this.pheromoneMatrix.classes];
		HashMap<Integer, Integer[]> mapVetProbToMatrix = new HashMap<Integer, Integer[]>(); // this HashMap maps an prob of vector 'prob' to an index of matrix
		ArrayList<Integer[]> interfaces = new ArrayList<Integer[]>();

		// build matrix of relations between classes of same component (Class x Class)
		Double probsClassIntR[] = new Double[this.pheromoneMatrix.classes];
		HashMap<Integer, Integer[]> mapVetProbToMatrixIntR = new HashMap<Integer, Integer[]>(); // this HashMap maps an prob of vector 'prob' to an index of matrix
		ArrayList<Integer[]> internalRelation = new ArrayList<Integer[]>();

		for (int i = 0; i < this.pheromoneMatrix.classes; i++) {
			int x = 0; // interfaces
			int y = 0; // internal relations
			double sumProbs = 0;
			double sumProbsIntR = 0;
			for (int j = 0; j < this.pheromoneMatrix.classClass[0].length; j++) {
				if(i != j && !twoClassInComponent(i,j,mapComponentClass)){
					double prob = 0.0;
					//DONE baseado na lista de classBreak e badRelation calcular quanto de penalidade será
					// aplicado a combinação, passar este valor para a função calculatesProbability
					// para que seja levado em consideração o valor da penalidade no calculo da probabilidade.
					int aux = this.pheromoneMatrix.classClass[0].length - 1;
					if(j != aux){// if para nao calcular penalidade caso esteja considerando
						// a combinação com a ultima coluna, que é a coluna de não combinar a classe i com ninguém.
						if(Main.SHOW_LOGS)
							System.out.println("Quantas vezes a classe <<" + i + ">> quebrou a regra: <<" + penalty.classBreak.get(i) +
									">> \nQuantas vezes a classe<<" + j + ">> quebrou a regra: <<" + penalty.classBreak.get(j) + ">>");
					
					Double h = ((penalty.classBreak.get(i) + penalty.classBreak.get(j)) / Double.valueOf(penalty.listBadRel.size()));
					prob = this.probability.calculatesProbability(this.pheromoneMatrix.classClass, i, j, (1 - h));
						if(Main.SHOW_LOGS)
							System.out.println("Valor da penalidade para combinação " + i +"-"+ j  +" = "+ h);
					}else{// se esta considerando a ultima coluna, não combinação com nenhuma classe, então a informação
						// da penalidade não precisa ser calculada.
						prob = this.probability.calculatesProbability(this.pheromoneMatrix.classClass, i, j, Probability.DEFAULT_VALUE_HEURISTIC);
						if(Main.SHOW_LOGS)
							System.out.println("Não combinar classe " + i + " com ninguém.");
					}
					
					probsClass[x] = prob;
					sumProbs += prob;
					Integer indices[] = new Integer[2];
					indices[0] = i; indices[1] = j;
					mapVetProbToMatrix.put(x, indices);
					x++;
				}else if (i != j && twoClassInComponent(i,j,mapComponentClass)){
					double probIntR = 0.0;
					
					/**
					 * Considera o cálculo da penalidade para classes de um mesmo componente, pois no caso de CLIENTE/SERVIDOR
					 * um componente pode ter classes SERVIDORAS e CLIENTES.
					 * A condição do if para nao calcular a penalidade caso estaja considerando a ultima coluna da matriz
					 * permanece.
					 * */
					if(j != (this.pheromoneMatrix.classClass[0].length - 1)){
						if(Main.SHOW_LOGS)
							System.out.println("Quantas vezes a classe <<" + i + ">> quebrou a regra: <<" + penalty.classBreak.get(i) +
								">> \nQuantas vezes a classe<<" + j + ">> quebrou a regra: <<" + penalty.classBreak.get(j) + ">>");

						Double h =  Double.valueOf((penalty.classBreak.get(i) + penalty.classBreak.get(j))) / Double.valueOf(penalty.listBadRel.size());
						
						if(Main.SHOW_LOGS)
							System.out.println("Valor da penalidade para combinação " + i +"-"+ j  +" = "+ h);

						probIntR = this.probability.calculatesProbability(this.pheromoneMatrix.classClass, i, j, (1 - h));
					}else{
						probIntR = this.probability.calculatesProbability(this.pheromoneMatrix.classClass, i, j, Probability.DEFAULT_VALUE_HEURISTIC);
					}

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
					if(Main.SHOW_LOGS)
						System.out.println("---Interface " + mapVetProbToMatrix.get(solutionSelectedInterfaces)[0]
							+ "-" + mapVetProbToMatrix.get(solutionSelectedInterfaces)[1]);
				}else if(Main.SHOW_LOGS){
					System.out.println("---Classe " + elements[0] + " não vai ser combinada com ninguém.");
				}
			}

			if(sumProbsIntR != 0){
				int solutionSelectedInternalRelation = rouletteIn(probsClassIntR,sumProbsIntR);
				Integer elements[] = mapVetProbToMatrixIntR.get(solutionSelectedInternalRelation);
				if(elements[1] != (this.pheromoneMatrix.classClass[0].length - 1)){
					internalRelation.add(mapVetProbToMatrixIntR.get(solutionSelectedInternalRelation));
					if(Main.SHOW_LOGS)
						System.out.println("---Internal Relation " + mapVetProbToMatrixIntR.get(solutionSelectedInternalRelation)[0]
							+ "-" + mapVetProbToMatrixIntR.get(solutionSelectedInternalRelation)[1]);
				}else if(Main.SHOW_LOGS){
					System.out.println("---Classe " + elements[0] + " não vai ser combinada com ninguém.");
				}
			}
		}

		if(this.initialSolution != null && this.initialSolution.type.equals(LoadModel.LAYER)){
			this.solution = new Solution(mapComponentClass,interfaces, internalRelation,
					this.initialSolution.componentLayer,mapClassComponent); 
		}else if(this.initialSolution != null && this.initialSolution.type.equals(LoadModel.CLIENT_SERVER)){
			this.solution = new Solution(mapComponentClass, interfaces, 
					this.initialSolution.mapClassServer, 
					this.initialSolution.mapClassClient, 
					internalRelation, mapClassComponent);
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

		return  solution;
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
		double randomNum = new Random().nextDouble();
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
		Random random = new Random();
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

