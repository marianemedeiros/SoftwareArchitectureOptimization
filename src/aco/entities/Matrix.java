/**
 * 
 */
package aco.entities;


/**
 * @author mariane
 *
 */
public class Matrix {
	
	 int components;
	 int classes;
	
	 public double componentClass[][];
	 public double classClass[][];

	/**
	 * Constructor
	 */
	public Matrix(int comp, int cl) {
		this.components = comp;
		this.classes = cl;
		
		this.componentClass = new double[cl][comp];
		this.classClass= new double[cl][++cl];

		init(0.5);
	}
	
	public Matrix(int comp, int cl, double value) {
		this.components = comp;
		this.classes = cl;
		
		this.componentClass = new double[cl][comp];
		this.classClass= new double[cl][cl];
		
		init(value);
	}

	/**
	 * Inicialize matrix of pheromone
	 */
	private void init(double value) {
		for (int i = 0; i < this.classes; i++) {
			for (int j = 0; j < this.components; j++) {
				if(this.componentClass[i][j] == 0)
					this.componentClass[i][j] = value;
			}
		}
		
		for (int i = 0; i < this.classes; i++) {
			for (int j = 0; j < classClass[0].length; j++) {
				if(this.classClass[i][j] == 0)
					this.classClass[i][j] = value;
			}
		}
	}
	
	public void showMatrix(){
		System.out.println("\n Class x Component \n");
		for (int i = 0; i < classes; i++) {
			for (int j = 0; j < components; j++) {
				System.out.print(componentClass[i][j] + " ");
			}
			System.out.println();
		}
		
		System.out.println("\n Class x Class \n");
		for (int i = 0; i < classes; i++) {
			for (int j = 0; j < classClass[0].length ; j++) {
				System.out.print(classClass[i][j] + " ");
			}
			System.out.println();
		}
	}


}
