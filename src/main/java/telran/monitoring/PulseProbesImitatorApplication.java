package telran.monitoring;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import telran.monitoring.dto.PulseProbe;

@SpringBootApplication
public class PulseProbesImitatorApplication {
@Value("${app.amount.patients: 10}")
	int nPatients;
@Value("${app.min.pulse.value: 40}")
int minPulseValue;
@Value("${app.max.pulse.value: 240}")
int maxPulseValue;
@Value("${app.jump.probability: 10}")
int jumpProb;
@Value("${app.jump.multiple: 0.6}")
double jumpMultiple;
@Value("${app.value.multiple: 0.1}")
double valueMultiple;
@Value("${app.delta.increase.probability: 80}")
int deltaIncreaseProb;

ThreadLocalRandom tlr = ThreadLocalRandom.current();
HashMap<Long, Integer> patientsPulseValues = new HashMap<>();

	public static void main(String[] args) throws InterruptedException {
		 SpringApplication.run(PulseProbesImitatorApplication.class, args);
		
	}
	@Bean
	Supplier<PulseProbe> pulseProbesSupplier() {
		return this::getRandomProbe;
	}
	PulseProbe getRandomProbe() {
		
		long patientId = tlr.nextInt(1, nPatients + 1);
		int value = getValue(patientId);
		patientsPulseValues.put(patientId, value);
		return new PulseProbe( patientId  , value);
	}
	private int getValue(long patientId) {
		Integer value = patientsPulseValues.get(patientId);
		return value == null ? tlr.nextInt(minPulseValue, maxPulseValue) : getNewValue(value);
	}
	private int getNewValue(Integer value) {
		int delta = (int) (isChance(jumpProb) ? value * jumpMultiple :
			value * tlr.nextDouble(0, valueMultiple));
		if (value + delta > maxPulseValue || !isChance(deltaIncreaseProb)) {
			delta = -delta;
		}
		return value + delta;
	}
	private boolean isChance(int prob) {
		
		return tlr.nextInt(0, 100) < prob;
	}

}
