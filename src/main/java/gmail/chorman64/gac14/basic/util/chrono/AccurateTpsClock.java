package gmail.chorman64.gac14.basic.util.chrono;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;

import gmail.chorman64.gac14.basic.Core;

public class AccurateTpsClock {
	private static Instant lastTickTime;
	private static Instant startupTime;
	private static Duration sinceLastTick;
	private static long runningTicks;
	private static Duration runtime;
	private static BigDecimal runningTps;
	private static BigDecimal currentTps;
	private static final Duration EXPECTED_TICK_TIME = Duration.of(1, MCChronoUnit.TICK);

	private AccurateTpsClock() {
		// TODO Auto-generated constructor stub
	}

	public static void tick() {
		if(lastTickTime ==null) {
			lastTickTime = Instant.now();
			startupTime = Instant.now();
			sinceLastTick = Duration.ZERO;
			runtime = Duration.ZERO;
			runningTps = BigDecimal.valueOf(20);
			currentTps = BigDecimal.valueOf(20);
		}else {
			runningTicks++;
			Instant now = Instant.now();
			sinceLastTick = Duration.between(lastTickTime, now);
			currentTps = BigDecimal.valueOf(1000).divide(BigDecimal.valueOf(sinceLastTick.toMillis()),BigDecimal.ROUND_CEILING);
			runtime = Duration.between(startupTime,now);
			runningTps = BigDecimal.valueOf(runtime.getSeconds()).divide(BigDecimal.valueOf(runningTicks), BigDecimal.ROUND_CEILING);
		}
	}

	public static BigDecimal getCurrentTps() {
		return currentTps;
	}

	public static BigDecimal getTotalTps() {
		return runningTps;
	}


	public static Duration estimatedOverallTickDuration(long ticks) {
		return Duration.ofMillis((long)(runningTps.doubleValue()*50*ticks));
	}
	public static Duration estimatedInstantaniousTickDuration(long ticks){
		return Duration.ofMillis((long)(currentTps.doubleValue()*50*ticks));
	}



}
