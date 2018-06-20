package gmail.chorman64.gac14.basic.players;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import net.minecraft.entity.ai.attributes.AttributeModifier;

public class TemporaryAttributeModifier extends AttributeModifier {
	private Instant start;
	private Duration time;
	private static final UUID NIL = UUID.fromString("00000000-0000-0000-0000-000000000000");
	public TemporaryAttributeModifier(String nameIn, double amountIn, int operationIn,Duration time) {
		super(nameIn, amountIn, operationIn);
		this.start = Instant.now();
		this.time = time;
	}

	public TemporaryAttributeModifier(UUID idIn, String nameIn, double amountIn, int operationIn,Duration time) {
		super(idIn, nameIn, amountIn, operationIn);
		this.start = Instant.now();
		this.time = time;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.entity.ai.attributes.AttributeModifier#getID()
	 */
	@Override
	public UUID getID() {
		if(hasExpired())
			return NIL;
		return super.getID();
	}

	/* (non-Javadoc)
	 * @see net.minecraft.entity.ai.attributes.AttributeModifier#getOperation()
	 */
	@Override
	public int getOperation() {
		if(hasExpired())
			return 0;
		return super.getOperation();
	}

	/* (non-Javadoc)
	 * @see net.minecraft.entity.ai.attributes.AttributeModifier#getAmount()
	 */
	@Override
	public double getAmount() {
			if(hasExpired())
				return 0.0;
		return super.getAmount();
	}

	public boolean hasExpired() {
		// TODO Auto-generated method stub
		return Instant.now().isAfter(start.plus(time));
	}



}
