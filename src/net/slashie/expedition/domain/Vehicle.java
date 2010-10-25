package net.slashie.expedition.domain;

import net.slashie.serf.level.AbstractLevel;
import net.slashie.utils.Util;

/**
 * A vehicle carries around expeditions units and items, allowing the expedition to 
 * go through otherwise unsurmountable terrain, and/or increasing its traveling speed
 * 
 * Will be used to model: Ships, Wagons, Airships
 * Horses are modeled differently
 * @author Slash
 *
 */
public class Vehicle extends ExpeditionItem{
	private boolean moveOnWater;
	private boolean moveOnAir;
	private int speedModifier;
	private int carryCapacity;
	private int resistance;
	private boolean fakeVehicle;
	private int maxResistance;
	
	public boolean isMoveOnWater() {
		return moveOnWater;
	}
	public void setMoveOnWater(boolean moveOnWater) {
		this.moveOnWater = moveOnWater;
	}
	public boolean isMoveOnAir() {
		return moveOnAir;
	}
	public void setMoveOnAir(boolean moveOnAir) {
		this.moveOnAir = moveOnAir;
	}
	public int getSpeedModifier() {
		return speedModifier;
	}
	public void setSpeedModifier(int speedModifier) {
		this.speedModifier = speedModifier;
	}
	public int getCarryCapacity() {
		return carryCapacity;
	}
	public void setCarryCapacity(int carryCapacity) {
		this.carryCapacity = carryCapacity;
	}
	public Vehicle(String classifierId, String description, String pluralDescription, String longDescription,
			int weight, boolean moveOnWater,
			boolean moveOnAir, int speedModifier, int carryCapacity, int resistance, boolean fakeVehicle, GoodType goodType, int europeValue, int americaValue) {
		super(classifierId, description,pluralDescription, longDescription, classifierId, weight, goodType, europeValue, americaValue);
		this.moveOnWater = moveOnWater;
		this.moveOnAir = moveOnAir;
		this.speedModifier = speedModifier;
		this.carryCapacity = carryCapacity;
		this.resistance = resistance;
		this.maxResistance = resistance;
		this.fakeVehicle = fakeVehicle;
	}
	
	public int getResistance() {
		return resistance;
	}
	public void setResistance(int resistance) {
		this.resistance = resistance;
	}
	public void dailyWearOut(AbstractLevel l, int chance) {
		if (Util.chance(chance)){
			l.addMessage("A "+getDescription()+" suffers damage!");
			resistance --;
		}
	}
	
	public boolean isDestroyed(){
		return resistance <= 0;
	}

	public boolean isFakeVehicle() {
		return fakeVehicle;
	}
	public int getMaxResistance() {
		return maxResistance;
	}
	public void recoverResistance(int recovery) {
		resistance += recovery;
		if (resistance > maxResistance)
			resistance = maxResistance;
	}
}
