package aco.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import loadModel.Solution;
import main.Main;

public class Colony {

	private double valueOfBestValueFound = 0;
	private Ant bestAnt;

	private Matrix pheromoneMatrix;
	private Solution initialSoluction = null;
	private Parametro parametros;
	/**
	 * Constructor when doesn't have an initial architecture
	 * 
	 * @param ants number of ants will work in the colony
	 * @param r rho constant
	 * @param comp number of components
	 * @param cl number of classes
	 * */
	public Colony(int comp, int cl, Parametro p) {
		this.parametros = p;
		// declare pheromeMatrix (class x compontet and class x class) and init that.
		this.pheromoneMatrix = new Matrix(comp, cl);
	}

	/**
	 * Constructor to an initial architecture
	 * 
	 * @param s solution of initial architecture
	 * @param p pheromone matrix of initial architecture
	 * @param ants number of ants will work in the colony
	 * @param r rho constant
	 * */
	public Colony(Solution s, Matrix p, Parametro param){
		this.parametros = param;
		this.pheromoneMatrix = p;
		this.initialSoluction = s;
	}

	/**
	 * This functions instantiates ants to build solutions (architectures), and verify what 
	 * solution are the best, in sequence the pheromone matrix are updated with the best
	 * solution found.
	 * */
	public Colony putAntsToWork(){   
		for (int i = 0; i < parametros.ANTS; i++){
			//           ExecutorService threadExecutor = Executors.newFixedThreadPool(4);
			Ant f1;
			if(this.initialSoluction != null)
				f1 = new Ant(this.getPheromoneMatrix(),this.initialSoluction,this.parametros);
			else
				f1 = new Ant(this.getPheromoneMatrix(),this.parametros);
			f1.buildSolution();
			//			ThreadBuildSoluction t1 = new ThreadBuildSoluction(f1);

			//			Ant f2 = new Ant(this.pheromoneMatrix);
			//			ThreadBuildSoluction t2 = new ThreadBuildSoluction(f2);

			//			Ant f3 = new Ant(this.pheromoneMatrix);
			//			ThreadBuildSoluction t3 = new ThreadBuildSoluction(f3);

			//			Ant f4 = new Ant(this.pheromoneMatrix);
			//			ThreadBuildSoluction t4 = new ThreadBuildSoluction(f4);

			//			threadExecutor.execute(t1);
			//			threadExecutor.execute(t2);
			//			threadExecutor.execute(t3);
			//			threadExecutor.execute(t4);

			//           threadExecutor.shutdown();

			if (f1.getSolution().mMetric > this.valueOfBestValueFound){
				valueOfBestValueFound = f1.getSolution().mMetric;
				bestAnt = f1;
			}

			//           if (f2.getSolution().mMetric < this.valueOfBestValueFound){
			//           	valueOfBestValueFound = f2.getSolution().mMetric;
			//           	bestAnt = f2;
			//           }

			//           if (f3.getSolution().mMetric < this.valueOfBestValueFound){
			//           	valueOfBestValueFound = f3.getSolution().mMetric;
			//           	bestAnt = f3;
			//           }

			//           if (f4.getSolution().mMetric < this.valueOfBestValueFound){
			//           	valueOfBestValueFound = f4.getSolution().mMetric;
			//           	bestAnt = f4;
			//           }
			
			if(Main.SHOW_LOGS)
				System.out.println("Solution value found by ant " + f1.getIdentidade() + " : " + f1.getSolution().mMetric);
			//           System.out.println("Valor da solucao encontrada pela formiga " + f2.getIdentidade() + " : " + f2.getSolution().mMetric);
			//           System.out.println("Valor da solucao encontrada pela formiga " + f3.getIdentidade() + " : " + f3.getSolution().mMetric);
			//           System.out.println("Valor da solucao encontrada pela formiga " + f4.getIdentidade() + " : " + f4.getSolution().mMetric);

		}

		if(Main.SHOW_LOGS)
			System.out.println("Best solution value found: " + valueOfBestValueFound);

		Matrix aux = new Matrix(this.pheromoneMatrix.components, this.pheromoneMatrix.classes, 0);
		uptadePheromone(aux);

		return this;
	}


	/**
	 * Evaporation of pheromone in positions of matrix that didn't was chosen by ant.
	 */
	private void evaporationPheromone(Matrix aux) {
		// component x class matrix
		for (int i = 0; i < aux.componentClass.length; i++) {
			for (int j = 0; j < aux.componentClass[0].length; j++) {
				if(aux.componentClass[i][j] != 1.0){
					double tau = this.getPheromoneMatrix().componentClass[i][j];
					this.pheromoneMatrix.componentClass[i][j] = (1 - parametros.RO) * tau;
				}
			}
		}
		// class x class matrix
		for (int i = 0; i < aux.classClass.length; i++) {
			for (int j = 0; j < aux.classClass.length; j++) {
				if(aux.classClass[i][j] != 1.0){
					double tau = this.getPheromoneMatrix().classClass[i][j];
					this.pheromoneMatrix.classClass[i][j] = (1 - parametros.RO) * tau;
				}
			}
		}

	}

	/**
	 * update pheromone matrix with the best solution found by ants.
	 */
	private void uptadePheromone(Matrix aux) {
		HashMap<Integer, Set<Integer>> mapCompClass = this.bestAnt.getSolution().componentClasses;
		ArrayList<Integer[]> interfaces = this.bestAnt.getSolution().interfaces;
		ArrayList<Integer[]> internalRelations = this.bestAnt.getSolution().internalRelations;

		// atualizando na metriz de feromonio a solução escolhida.
		for (Entry<Integer, Set<Integer>> element : mapCompClass.entrySet()) {
			int indexComp = element.getKey();

			for (Integer indexClass : element.getValue()) {
				this.getPheromoneMatrix().componentClass[indexClass][indexComp] = 
						(1 -  parametros.RO) * this.getPheromoneMatrix().componentClass[indexClass][indexComp]
								+  this.bestAnt.getSolution().mMetric;
				aux.componentClass[indexClass][indexComp] = 1.0;
			}
		}

		for (Integer[] integers : interfaces) {
			this.getPheromoneMatrix().classClass[integers[0]][integers[1]] = 
					(1 -  parametros.RO) * this.getPheromoneMatrix().classClass[integers[0]][integers[1]]
							+  this.bestAnt.getSolution().mMetric;
			aux.classClass[integers[0]][integers[1]] = 1.0;
		}

		for (Integer[] integers : internalRelations) {
			this.getPheromoneMatrix().classClass[integers[0]][integers[1]] = 
					(1 -  parametros.RO) * this.getPheromoneMatrix().classClass[integers[0]][integers[1]]
							+  this.bestAnt.getSolution().mMetric;
			aux.classClass[integers[0]][integers[1]] = 1.0;
		}
		// fim da atualização na solução escolhida

		//atualização do feromônio na outras posiçoes da matriz.
		evaporationPheromone(aux);
	}

	public double getValueOfBestValueFound() {
		return valueOfBestValueFound;
	}

	public Ant getBestAnt() {
		return bestAnt;
	}

	public Matrix getPheromoneMatrix() {
		return pheromoneMatrix;
	}

}
