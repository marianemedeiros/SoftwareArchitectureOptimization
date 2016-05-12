package aco.entities;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import loadModel.Solution;
import main.Main;


public class AntSystem {
    public NumberFormat formatter = new DecimalFormat("#0.00000");
    public Colony colony;
    
    public double mediaMq;
    public ArrayList<Double> evolutionMq;
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
    	evolutionMq = new ArrayList<Double>();
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
    	evolutionMq = new ArrayList<Double>();
	}


	/**
     * 
     * This function execute the colony, put ant to work and build soluctions.
     * 
     * */
    public Solution execute() throws Exception {
    	double sum = 0.0;
    	int interval = (Main.ITERATIONS/10);
    	int aux = 0;
    	
        for (int i = 0; i < Main.ITERATIONS; i++) {
        	//if(Main.SHOW_LOGS)
        		System.out.println("\n---------------    Iteration number " + i + " -----------------");
            
        	long startTime = System.currentTimeMillis();
        	colony.putAntsToWork();
        	long stopTime = System.currentTimeMillis();
    	    System.out.println("Execution time is " + formatter.format((stopTime - startTime) / 1000d) + " seconds to one iteration.");
    	    sum = sum + colony.getBestAnt().getSolution().mMetric;
            if(this.evolutionMq.isEmpty() || aux == interval){
            	this.evolutionMq.add(colony.getValueOfBestValueFound());
            	aux = 0;
            }
            aux++;
        }

        System.out.println("\n!!!!!!!!!!   Best Soluction Found    !!!!!!!!!!");
        System.out.println("Winner ant: " + colony.getBestAnt().getIdentidade());
        
        this.mediaMq = (sum/Main.ITERATIONS);
        System.out.println("MQ: " + colony.getBestAnt().getSolution().mMetric);
        System.out.println("Average of fitness function (MQ): " + this.mediaMq);
        
        return colony.getBestAnt().getSolution();
    }
}
