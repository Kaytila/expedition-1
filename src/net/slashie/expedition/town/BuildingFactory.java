package net.slashie.expedition.town;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.slashie.expedition.domain.Expedition;
import net.slashie.expedition.domain.Town;
import net.slashie.expedition.game.ExpeditionGame;
import net.slashie.expedition.world.agents.DayShiftAgent;
import net.slashie.serf.ui.ActionCancelException;
import net.slashie.serf.ui.UserInterface;
import net.slashie.utils.OutParameter;

public class BuildingFactory {
	private static Map<String, Building> buildingsMap = new HashMap<String, Building>();
	private static List<Building> buildingsList = new ArrayList<Building>();
	
	public static Building createBuilding(String id){
		try {
			return (Building)buildingsMap.get(id).clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void executeConstructionPlan(Town town, 
			List<Building> plan, 
			Expedition builders
			) throws ActionCancelException {
		int woodCost = 0;
		for (Building building: plan){
			woodCost += building.getWoodCost();
		}
		
		//Check resources availability
		if (builders.getItemCount("WOOD") < woodCost){
			UserInterface.getUI().showMessage("You need at least "+woodCost+" wood for the plan.");
			throw new ActionCancelException();
		}
		
		for (Building building: plan){
			town.addBuilding(building);
			if (building instanceof Farm){
				((Farm)building).plant(ExpeditionGame.getCurrentGame().getGameTime());
			}
		}
		
		builders.reduceGood("WOOD", woodCost);
	}


	public static void setBuildings(Building[] buildings) {
		for (Building building: buildings){
			buildingsMap.put(building.getId(), building);
			buildingsList.add(building);
		}
	}
	

	public static void getPlanCost(List<Building> plan, Expedition builders, OutParameter netTimeCost, OutParameter woodCost) throws ActionCancelException{
		int woodCostInt = 0;
		int timeCost = 0;
		int minDays = 0;
		for (Building building: plan){
			woodCostInt += building.getWoodCost();
			timeCost += building.getBuildTimeCost();
			if (building.getMinBuildDays() > minDays){
				minDays = building.getMinBuildDays(); 
			}
		}
		//Check resources availability 
		if (builders.getItemCount("WOOD") < woodCostInt){
			UserInterface.getUI().showMessage("You need at least "+woodCostInt+" wood for the plan.");
			throw new ActionCancelException();
		}
		
		int netTimeCostInt = (int)Math.round ((double)timeCost / (double)builders.getBuildingCapacity());
		
		int daysCost = (int)Math.ceil((double)netTimeCostInt / (double)DayShiftAgent.TICKS_PER_DAY);
		
		if (daysCost < minDays){
			daysCost = minDays;
			netTimeCostInt = daysCost * DayShiftAgent.TICKS_PER_DAY;
		}
		
		netTimeCost.setIntValue(netTimeCostInt);
		woodCost.setIntValue(woodCostInt);
	}

	public static List<Building> getBuildingsList() {
		return buildingsList;
	}
}
