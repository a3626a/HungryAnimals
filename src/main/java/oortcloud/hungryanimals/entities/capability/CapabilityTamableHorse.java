package oortcloud.hungryanimals.entities.capability;

import net.minecraft.entity.passive.horse.AbstractHorseEntity;

public class CapabilityTamableHorse extends CapabilityTamableAnimal {

	private static final double interval_start = 0.0;
	private static final double interval_end = 2.0;

	protected AbstractHorseEntity entity;

	public CapabilityTamableHorse(AbstractHorseEntity entity) {
		super(entity);
	}

	@Override
	public double setTaming(double taming) {
		if (entity != null) {
			// horse can be null during construction
			entity.setTemper(tamingToTemper(taming));
		}
		return super.setTaming(taming);
	}

	private int tamingToTemper(double taming) {
		// temper = 0 where taming = interval_start
		// temper = horse.getMaxTemper() where taming = interval_end
		double M = entity.getMaxTemper();
		double B = interval_end;
		double A = interval_start;
		int temper = (int) ((M / (B - A)) * taming - (M * A / (B - A)));

		// Clipping temper
		if (temper > entity.getMaxTemper()) {
			temper = entity.getMaxTemper();
		}
		if (temper < 0) {
			temper = 0;
		}
		return temper;
	}

}
