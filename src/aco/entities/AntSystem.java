package aco.entities;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import loadModel.Solution;
import main.Main;


public class AntSystem {
	public NumberFormat formatter = new DecimalFormat("#0.00000");
	public Colony colony;

	public double mediaMq;
	public double[] evolutionMq;
	
	public double[] penalties;
	public double[] penalties2;
	public int[] relationPenalized;
	
	private Parametro parametros;
	/**
	 * Constructor when doesn't have an initial architecture
	 * 
	 * @param ants number of ants will work in the colony
	 * @param components number of components
	 * @param classes number of classes
	 * @param iteration number of iterations of ant system
	 * */
	public AntSystem(int components, int classes, Parametro p) {
		this.parametros = p;
		this.colony = new Colony(components, classes,parametros); // 1.8 = ro
	}


	/**
	 * Constructor to an initial architecture
	 * 
	 * @param s soluction of initial architecture
	 * @param p pheromone matrix of initial architecture
	 * @param ants number of ants will work in the colony
	 * @param it number of iterations of ant system
	 * */
	public AntSystem(Matrix p, Solution s, Parametro params) {
		this.parametros = params;
		this.colony = new Colony(s,p,parametros);
	}


	/**
	 * 
	 * This function execute the colony, put ant to work and build soluctions.
	 * 
	 * */
	public Solution execute() throws Exception {
		evolutionMq = new double[parametros.ITERATIONS];
		penalties = new double[parametros.ITERATIONS];
		penalties2 = new double[parametros.ITERATIONS];
		relationPenalized = new int[parametros.ITERATIONS];
		
		double sum = 0.0;

		for (int i = 0; i < parametros.ITERATIONS; i++) {
			//System.out.println("\n---------------    Iteration number " + i + " -----------------");

			long startTime = System.currentTimeMillis();
			colony.putAntsToWork();
			long stopTime = System.currentTimeMillis();
			if(Main.SHOW_LOGS)
				System.out.println("Execution time is " + formatter.format((stopTime - startTime) / 1000d) + " seconds to one iteration.");
			sum = sum + colony.getBestAnt().getSolution().mMetric;
			evolutionMq[i] = colony.getValueOfBestValueFound();
			penalties[i] = colony.getBestAnt().getSolution().totalOfPenalties; 
			penalties2[i] = colony.getBestAnt().getSolution().totalOfPenalties2; 
			relationPenalized[i] = colony.getBestAnt().getSolution().penaltysOfSolution.classBreak.size();
		}

		//System.out.println("\n!!!!!!!!!!   Best Soluction Found    !!!!!!!!!!");
		//System.out.println("Winner ant: " + colony.getBestAnt().getIdentidade());

		this.mediaMq = (sum/parametros.ITERATIONS);
		//System.out.println("MQ: " + colony.getBestAnt().getSolution().mMetric);
		//System.out.println("Average of fitness function (MQ): " + this.mediaMq);

		return colony.getBestAnt().getSolution();
	}
}
