/**
 * 
 */
package metrics.architectureM;

import static org.junit.Assert.assertEquals;
import loadModel.Solution;
import metrics.architecture.MetricCoesion;

import org.junit.Test;

import architecture.ArchitectureExample;

/**
 * @author mariane
 *
 */
public class TestCoesionMetric {

	@Test
	public void test() {
		ArchitectureExample architectureExample = new ArchitectureExample();
		Solution s =  architectureExample.generateSoluction1();
		MetricCoesion metricCoesion = new MetricCoesion();
		s = metricCoesion.calculate(s);
		assertEquals((Object)s.coesionMetric, (Object)0.5972222222222222);
		
		s = architectureExample.generateSoluction5();
		s = metricCoesion.calculate(s);
		assertEquals((Object)s.coesionMetric, (Object)0.3333333333333333);
	}
	
	

}
