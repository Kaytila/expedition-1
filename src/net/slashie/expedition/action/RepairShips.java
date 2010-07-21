package net.slashie.expedition.action;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.slashie.expedition.domain.Expedition;
import net.slashie.expedition.domain.Vehicle;
import net.slashie.expedition.domain.Expedition.MovementMode;
import net.slashie.serf.action.Action;
import net.slashie.serf.action.Actor;

/**
 * Recovers resistance to the expedition ships
 * Carpenters are ten times better than other crew for this
 * This action will take one day (must consume food for the whole expedition)
 * This action requires 5 wood for each man to work
 * The amount recovered depends on the number of carpenters
 * If the repairs are done off-shore, recuperation is decimated  
 * @author Slash
 *
 */
public class RepairShips extends Action{

	private static final double RECOVERY_INDEX = 0.1;
	private static final int WOOD_PER_REPAIRMEN = 1;
	private static final int CARPENTER_MULTIPLIER = 10;
	private boolean actionCancelled;

	@Override
	public void execute() {
		Expedition expedition = (Expedition) performer;
		if (expedition.getMovementMode() != MovementMode.SHIP){
			expedition.getLevel().addMessage("You are not on a ship.");
			actionCancelled = true;
			return;
		}
		
		boolean someShipDamaged = false;
		List<Vehicle> ships = expedition.getCurrentVehicles();
		for (Vehicle ship: ships){
			int damage = ship.getMaxResistance() - ship.getResistance();
			if (damage > 0){
				someShipDamaged = true;
				break;
			}
		}
		
		if (!someShipDamaged){
			expedition.getLevel().addMessage("Your ships are in perfect shape.");
			actionCancelled = true;
			return;
		}
		
		int availableWood = expedition.getItemCount("WOOD");
		
		if (availableWood == 0){
			expedition.getLevel().addMessage("You need some wood to repair the ships");
			actionCancelled = true;
			return;
		}
			
		int maxRepairmen = availableWood / WOOD_PER_REPAIRMEN;
		int remainingRepairmen = maxRepairmen;
		int carpenters = expedition.getItemCount("CARPENTER");
		int normalCrew = expedition.getTotalUnits() - carpenters;
		
		if (carpenters >= remainingRepairmen){
			carpenters = remainingRepairmen;
			remainingRepairmen = 0;
		} else {
			remainingRepairmen -= carpenters;
		}
		
		if (normalCrew >= remainingRepairmen){
			normalCrew = remainingRepairmen;
			remainingRepairmen = 0;
		} else {
			remainingRepairmen -= normalCrew;
		}
		
		int recoveryPower = carpenters * CARPENTER_MULTIPLIER + normalCrew;
		
		int recovery = (int)Math.round(recoveryPower*RECOVERY_INDEX);
		int maxCarpenterRecovery = (int)Math.round((carpenters * CARPENTER_MULTIPLIER)*RECOVERY_INDEX);
		
		int remainingRecovery = recovery;
		
		// Select ships, starting from the most damaged ones, to repair
		
		Collections.sort(ships, new Comparator<Vehicle>(){
			public int compare(Vehicle o1, Vehicle o2) {
				return o1.getResistance() - o2.getResistance();
			}
		});
		for (Vehicle ship: ships){
			int damage = ship.getMaxResistance() - ship.getResistance();
			if (damage >= remainingRecovery){
				ship.recoverResistance(remainingRecovery);
				remainingRecovery = 0;
			} else {
				ship.recoverResistance(damage);
				remainingRecovery -= damage;
			}
		}
		
		// Calculate wood cost, we need to know how many repairmen worked.
		// Let's take all the used "recovery", and split between carpenters and other crew
		// then sum and we have the data
		
		int usedRecovery = recovery - remainingRecovery;
		
		int carpenterRecovery = usedRecovery;
		if (usedRecovery > maxCarpenterRecovery){
			carpenterRecovery = maxCarpenterRecovery;
		}
		int crewRecovery = usedRecovery - carpenterRecovery;
		
		int usedCarpenters = (int)Math.round(carpenterRecovery / (CARPENTER_MULTIPLIER*RECOVERY_INDEX)); 
		int usedCrew = (int)Math.round(crewRecovery / (RECOVERY_INDEX));
		
		int totalRepairmen = usedCarpenters+usedCrew;
		
		int usedWood = totalRepairmen * WOOD_PER_REPAIRMEN;
		expedition.reduceQuantityOf("WOOD", usedWood);
		
		String message = "You repair your ships with ";
		if (usedCarpenters > 0)
			message += usedCarpenters+" carpenters and ";
		message += usedCrew+" crew members, spending "+usedWood+" wood.";
		expedition.getLevel().addMessage(message);

		
	}

	@Override
	public String getID() {
		return "RepairShips";
	}

	@Override
	public int getCost() {
		if (actionCancelled){
			actionCancelled = false;
			return 0;
		}
		return 1000;
	}
}
