package gmail.chorman64.gac14.basic.players;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.util.math.MathHelper;

public class CustomAttribute implements IAttribute {
	private final String name;
	private double min;
	private double max;
	private double def;
	public static final IAttribute RESISTANCE = new CustomAttribute("custom.resistance");
	public static final IAttribute REGENERATION = new CustomAttribute("custom.regeneration");

	public CustomAttribute(String name) {
		this.name = name;
		this.min = Double.NEGATIVE_INFINITY;
		this.max = Double.POSITIVE_INFINITY;
	}
	public CustomAttribute setMin(double min) {
		this.min = min;
		return this;
	}
	public CustomAttribute setMax(double max) {
		this.max = max;
		return this;
	}
	public CustomAttribute setDefault(double def) {
		this.def = def;
		return this;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public double clampValue(double value) {
		// TODO Auto-generated method stub
		return MathHelper.clamp(value, min, max);
	}

	@Override
	public double getDefaultValue() {
		// TODO Auto-generated method stub
		return def;
	}

	@Override
	public boolean getShouldWatch() {
		return true;
	}
	@Override
	public IAttribute getParent() {
		// TODO Auto-generated method stub
		return null;
	}




}
