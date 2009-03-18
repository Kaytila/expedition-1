package net.slashie.expedition.world;

import java.util.List;

import net.slashie.serf.game.Equipment;

public interface FoodConsumer {
	public void consumeFood();

	public void checkDeath();

	public List<Equipment> getInventory();
	
	public int getDailyFoodConsumption();
	
}
