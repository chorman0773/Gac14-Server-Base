package gmail.chorman64.gac14.basic.util.chrono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;

public enum MCChronoUnit implements TemporalUnit {
	TICK(1),DAY(24000),YEAR(8766000), TECHNUS(1L<<32);

	MCChronoUnit(long ticks){
		this.nticks = ticks;
	}

	private static final long TICK_ACHOR = 50;
	private long nticks = 0;
	@Override
	public Temporal addTo(Temporal arg0, long arg1) {
		return arg0.plus(arg1*TICK_ACHOR*nticks, ChronoUnit.MILLIS);
	}

	@Override
	public long between(Temporal arg0, Temporal arg1) {
		Duration nanos = Duration.between(arg0, arg1);
		return nanos.toMillis()/(TICK_ACHOR*nticks);
	}

	@Override
	public Duration getDuration() {
		// TODO Auto-generated method stub
		return Duration.ofMillis(nticks*TICK_ACHOR);
	}

	@Override
	public boolean isDateBased() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDurationEstimated() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTimeBased() {
		// TODO Auto-generated method stub
		return true;
	}

}
