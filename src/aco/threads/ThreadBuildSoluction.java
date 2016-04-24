package aco.threads;

import aco.entities.Ant;

public class ThreadBuildSoluction implements Runnable{
	Ant ant;
	
	public ThreadBuildSoluction(Ant a) {
		this.ant = a;
	}
	public void run() {
		this.ant.buildSolution();
	}

}
