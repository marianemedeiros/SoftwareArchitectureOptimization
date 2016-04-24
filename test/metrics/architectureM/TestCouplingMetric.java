package metrics.architectureM;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import loadModel.Solution;
import metrics.architecture.MetricCoupling;

import org.junit.Test;

import architecture.ArchitectureExample;

public class TestCouplingMetric {
	

	@Test
	public void test() {
		ArchitectureExample architectureExample = new ArchitectureExample();
		
		Solution s = architectureExample.generateSoluction2();
		assertEquals(s.componentClasses.size(), 3);
		assertEquals(s.interfaces.size(), 4);
		assertNotNull(s);
		
		MetricCoupling metricCoupling = new MetricCoupling();
		s = metricCoupling.calculate(s);
		assertEquals((Object)s.acopMetric, (Object)0.2708333333333333);
		
		s = architectureExample.generateSoluction3();
		MetricCoupling metricCoupling2 = new MetricCoupling();
		s = metricCoupling2.calculate(s);
		assertEquals((Object)s.acopMetric, (Object)0.16666666666666666);
		
		s = architectureExample.generateSoluction4();
		MetricCoupling metricCoupling3 = new MetricCoupling();
		s = metricCoupling3.calculate(s);
		assertEquals((Object)s.acopMetric, (Object)0.0);
	}


}
