package aco.entities;

import org.apache.log4j.Logger;

import loadModel.LoadModel;
import loadModel.Solution;
import main.Main;


public class AntSystem {
	private Logger logger = Logger.getLogger(LoadModel.class);
    public Colony colony;
    
	/**
	 * Constructor when doesn't have an initial architecture
	 * 
	 * @param ants number of ants will work in the colony
	 * @param components number of components
	 * @param classes number of classes
	 * @param iteration number of iterations of ant system
	 * */
    public AntSystem(int components, int classes) {
    	this.colony = new Colony(components, classes); // 1.8 = ro
	}

    
	/**
	 * Constructor to an initial architecture
	 * 
	 * @param s soluction of initial architecture
	 * @param p pheromone matrix of initial architecture
	 * @param ants number of ants will work in the colony
	 * @param it number of iterations of ant system
	 * */
	public AntSystem(Matrix p, Solution s) {
    	this.colony = new Colony(s,p);
	}


	/**
     * 
     * This function execute the colony, put ant to work and build soluctions.
     * 
     * */
    public Solution execute() throws Exception {
    	double sum = 0.0;
        for (int i = 0; i < Main.ITERATIONS; i++) {
        	//if(Main.SHOW_LOGS)
        		System.out.println("\n---------------    Iteration number " + i + " -----------------");
            
        	long startTime = System.currentTimeMillis();
 
        	colony.putAntsToWork();
                   	
        	long stopTime = System.currentTimeMillis();
        	long elapsedTime = stopTime - startTime;
        	    
        	if(elapsedTime > 1000 && elapsedTime < 6000)
        	   System.out.println((int) ((elapsedTime / 1000) % 60) + " seconds.");
        	else if(elapsedTime >= 6000)
        		System.out.println((int) ((elapsedTime / 1000) / 60) + " minutes.");
        	else
        	   System.out.println(elapsedTime + " miliseconds.");
        	    
            sum = sum + colony.getBestAnt().getSolution().mMetric;
        }

        System.out.println("\n!!!!!!!!!!   Best Soluction Found    !!!!!!!!!!");
        System.out.println("Winner ant: " + colony.getBestAnt().getIdentidade());
        logger.info("Fitness function of best solution: " + colony.getValueOfBestValueFound());
        System.out.println("Average of fitness function (MQ): " + (sum/Main.ITERATIONS));

        //colony.getBestAnt().getSolution().showSolution();
        return colony.getBestAnt().getSolution();
    }
}
