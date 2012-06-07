package calculator;

import java.util.ArrayList;
import java.util.List;

import com.travelsky.rmilbclient.RmiLbServiceConfig;
import com.travelsky.rmilbclient.RmiProxyFactory;

public class MainClient {

	public static void main(String[] args) {
		List<String> serviceUrls = new ArrayList<String>();
		serviceUrls.add("rmi://localhost:8098/CalculatorRMIService");
		serviceUrls.add("rmi://localhost:8099/CalculatorRMIService");
		RmiLbServiceConfig<CalculatorService> config = new RmiLbServiceConfig<CalculatorService>(
				serviceUrls, CalculatorService.class);
		config.setMonitorPeriod(60L);
		RmiProxyFactory factory = RmiProxyFactory.getInstance();
		CalculatorService as = factory.create(config);
		for (int i = 0; i < 1000; i++) {
			try {
				Thread.sleep(300);
				System.out.println(as.add(1, 2));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}