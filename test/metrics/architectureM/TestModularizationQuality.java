/**
 * 
 */
package metrics.architectureM;

import static org.junit.Assert.assertEquals;
import loadModel.Solution;
import metrics.architecture.MetricCoesion;
import metrics.architecture.MetricCoupling;

import org.junit.Test;

import architecture.ArchitectureExample;

/**
 * @author mariane
 *
 */
public class TestModularizationQuality {

	@Test
	public void test() {
		ArchitectureExample architectureExample = new ArchitectureExample();
		Solution s = architectureExample.generateSoluction1();
		Solution s1 = architectureExample.generateSoluction5();
		
		MetricCoesion metricCoesion = new MetricCoesion();
		s = metricCoesion.calculate(s);
		s1 = metricCoesion.calculate(s1);
		
		MetricCoupling metricCoupling = new MetricCoupling();
		s = metricCoupling.calculate(s);
		s1 = metricCoupling.calculate(s1);
		
		int k = s.componentClasses.size();
		s.mMetric = (s.coesionMetric / k)
				- (s.acopMetric / ((k * (k-1))/2));
		
		assertEquals((Object)s.mMetric, 0.1087962962962963);
		
		k = s1.componentClasses.size();
		s1.mMetric = (s1.coesionMetric / k)
				- (s1.acopMetric / ((k * (k-1))/2));
		System.out.println(s1.mMetric);
	}
}
