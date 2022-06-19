package telran.monitoring;

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
private static final long TIMEOUT = 30000;
@Value("${app.amount.patients: 10}")
	int nPatients;
@Value("${app.min.pulse.value: 40}")
int minPulseValue;
@Value("${app.max.pulse.value: 240}")
int maxPulseValue;
int sequenceNumber = 1;
ThreadLocalRandom tlr = ThreadLocalRandom.current();

	public static void main(String[] args) throws InterruptedException {
		ConfigurableApplicationContext ctx = SpringApplication.run(PulseProbesImitatorApplication.class, args);
		Thread.sleep(TIMEOUT);
		ctx.close();
	}
	@Bean
	Supplier<PulseProbe> pulseProbesSupplier() {
		return this::getRandomProbe;
	}
	PulseProbe getRandomProbe() {
		int value = tlr.nextInt(minPulseValue, maxPulseValue + 1);
		long patientId = tlr.nextInt(1, nPatients + 1);
		return new PulseProbe(sequenceNumber++, patientId  , value);
	}

}
