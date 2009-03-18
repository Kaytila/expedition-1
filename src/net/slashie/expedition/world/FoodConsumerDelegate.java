package net.slashie.expedition.world;

import java.io.Serializable;
import java.util.List;

import net.slashie.expedition.domain.ExpeditionUnit;
import net.slashie.expedition.domain.Good;
import net.slashie.expedition.domain.GoodType;
import net.slashie.serf.game.Equipment;
import net.slashie.utils.Util;

public class FoodConsumerDelegate implements Serializable{
	private FoodConsumer foodConsumer;
	
	private int starveResistance;
	public FoodConsumerDelegate(FoodConsumer foodConsumer) {
		super();
		this.foodConsumer = foodConsumer;
	}

	public void setStarveResistance(int starveResistance) {
		this.starveResistance = starveResistance;
	}

	public int getDailyFoodConsumption(){
		int dailyFoodConsumption = 0;
		List<Equipment> inventory = this.foodConsumer.getInventory();
		for (Equipment equipment: inventory){
			if (equipment.getItem() instanceof ExpeditionUnit){
				ExpeditionUnit unit = (ExpeditionUnit)equipment.getItem();
				dailyFoodConsumption += unit.getDailyFoodConsumption() * equipment.getQuantity();
			}
		}
		return dailyFoodConsumption;
	}
	
	public void consumeFood(){
		int remainder = reduceFood(getDailyFoodConsumption());
		if (remainder > 0){
			//Reduce expedition resistance
			starveResistance --;
			if (starveResistance <= 0){
				killUnits((double)Util.rand(5, 40)/100.0d);
			}
		} else {
			if (starveResistance < 5)
				starveResistance++;
		}
	}
	
	public int reduceFood(int quantity){
		int foodToSpend = quantity;
		List<Equipment> inventory = this.foodConsumer.getInventory();
		for (Equipment equipment: inventory){
			if (equipment.getItem() instanceof Good){
				Good good = (Good)equipment.getItem();
				if (good.getGoodType() == GoodType.FOOD){
					int unitsToSpend = (int)Math.ceil((double)foodToSpend / (double)good.getUnitsFedPerGood());
					if (unitsToSpend > equipment.getQuantity()){
						unitsToSpend = equipment.getQuantity();
					}
					foodToSpend -= unitsToSpend * good.getUnitsFedPerGood();
					equipment.reduceQuantity(unitsToSpend);
					if (foodToSpend <= 0){
						return 0;
					}
				}
			}
		}
		return foodToSpend;
	}
	
	public void killUnits(double proportion){
		List<Equipment> inventory = this.foodConsumer.getInventory();
		for (Equipment equipment: inventory){
			if (equipment.getItem() instanceof ExpeditionUnit){
				int killUnits = (int)Math.ceil(equipment.getQuantity() * proportion);
				equipment.reduceQuantity(killUnits);
			}
		}
		this.foodConsumer.checkDeath();
	}
}
