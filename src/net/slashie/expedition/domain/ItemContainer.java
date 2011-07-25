package net.slashie.expedition.domain;

import java.util.List;

import net.slashie.serf.baseDomain.AbstractItem;
import net.slashie.serf.game.Equipment;
import net.slashie.serf.ui.Appearance;

public interface ItemContainer {
	Appearance getAppearance();
	int getFoodDays();
	int getWaterDays();
	int getTotalShips();
	int getCurrentlyCarrying();
	int getCarryCapacity();
	int getTotalUnits();
	String getDescription();
	int getCarryable(ExpeditionItem eitem);
	boolean canCarry(ExpeditionItem item, int quantity);
	void addItem(ExpeditionItem item, int quantity);
	void reduceQuantityOf(AbstractItem item, int quantity);
	List<Equipment> getItems();
	List<Equipment> getGoods(GoodType goodType);
	int getItemCountBasic(String fullID);
}