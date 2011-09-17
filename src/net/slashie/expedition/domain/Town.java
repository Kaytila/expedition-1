package net.slashie.expedition.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.slashie.expedition.action.BuildBuildings;
import net.slashie.expedition.action.Hibernate;
import net.slashie.expedition.game.ExpeditionGame;
import net.slashie.expedition.item.ItemFactory;
import net.slashie.expedition.town.Building;
import net.slashie.expedition.town.Farm;
import net.slashie.expedition.town.Building.SpecialCapability;
import net.slashie.expedition.ui.ExpeditionUserInterface;
import net.slashie.serf.action.Actor;
import net.slashie.serf.ui.UserInterface;
import net.slashie.utils.Util;

@SuppressWarnings("serial")
public class Town extends GoodsCache{
	
	private static final String[] TOWN_ACTIONS = new String[] { 
		"Transfer equipment and people",
		"Construct building on settlement",
		"Inhabit settlement",
		"Pass through the settlement",
		"Do nothing"
	};
	private String name;
	protected Expedition founderExpedition;
	protected Date foundedIn;
	private List<Building> buildings = new ArrayList<Building>();
	
	public int getPopulationCapacity(){
		int ret = 0;
		for (Building building: buildings){
			ret += building.getPopulationCapacity();
		}
		return ret;
	}
	
	public Town(ExpeditionGame game) {
		super(game, "TOWN");
		founderExpedition = game.getExpedition();
		foundedIn = game.getGameTime().getTime();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getDescription() {
		return getName();
	}
	
	@Override
	public String getClassifierID() {
		return "Town"+name;
	}
	
	public String getLongDescription(){
		return "The "+getTitle()+" of "+getName(); 
	}
	
	@Override
	public void onStep(Actor a) {
		if (a != ExpeditionGame.getCurrentGame().getExpedition()){
			return;
		}
		if (!ExpeditionGame.getCurrentGame().getExpedition().getMovementMode().isLandMovement()){
			return;
		}
		((ExpeditionUserInterface)UserInterface.getUI()).showCityInfo(this);
		townAction(UserInterface.getUI().switchChat(getLongDescription(),"What do you want to do", getTownActions()), (Expedition)a);
		((ExpeditionUserInterface)UserInterface.getUI()).afterTownAction();
	}
	
	protected void townAction(int switchChat, Expedition expedition) {
		switch (switchChat){
		case 0:
			((ExpeditionUserInterface)UserInterface.getUI()).transferFromExpedition(this);
			break;
		case 1:
			// Build
			BuildBuildings buildAction = new BuildBuildings();
			buildAction.setTown(this);
			expedition.setNextAction(buildAction);
			break;
		case 2: 
			// Inhabit
			if (getPopulation() + expedition.getTotalUnits() + 1 <= getPopulationCapacity()){
				Hibernate hibernate = new Hibernate(7, true);
				expedition.setPosition(getPosition().x(), getPosition().y(), getPosition().z());
				expedition.setNextAction(hibernate);
			} else {
				expedition.getLevel().addMessage(getDescription()+" can't host all of your expedition.");
			}
		case 3:
			expedition.setPosition(getPosition());
			break;
		case 4:
			break;
		}
	}

	public void showBlockingMessage(String message) {
		((ExpeditionUserInterface)UserInterface.getUI()).showBlockingMessage(message);
	}

	protected String[] getTownActions() {
		return TOWN_ACTIONS;
	}

	@Override
	public void consumeFood() {
		//Do nothing, this must be handled differently
	}
	
	@Override
	public boolean isInfiniteCapacity() {
		return false;
	}
	
	public int getSize(){
		return (getPopulation() / 1000)+1;
	}

	public int getPopulation() {
		return getTotalUnits();
	}

	public boolean isTown() {
		return getSize() > 5 && getSize() < 20;
	}

	public boolean isCity() {
		return getSize() > 20;
	}
	
	public void tryGrowing(){
		//This is called each 30 days
		if (Util.chance(95)){
			int growth = (int)Math.round(getPopulation() * ((double)Util.rand(1, 5)/100.0d));
			if (growth > 0){
				if (getPopulation() + growth > getPopulationCapacity()){
					growth = getPopulationCapacity() - getPopulation(); 
				}
				if (growth > 0){
					addItem(ItemFactory.createItem("COLONIST"), growth);
				}
			}
		}
	}
	
	public String getTitle(){
		if (isCity())
			return "city";
		if (isTown())
			return "town";
		return "village";
	}
	
	public boolean canCarry(ExpeditionItem item, int quantity) {
		if (item instanceof ExpeditionUnit){
			int currentUnits = getTotalUnits();
			if (currentUnits + quantity > getPopulationCapacity()){
				return false;
			}
		}
		return true;
	}
	
	@Override
	
	 public int getCarryable(ExpeditionItem item) {
		if (item instanceof ExpeditionUnit){
			return getPopulationCapacity() - getTotalUnits();
		} else {
			return super.getCarryable(item);
		}
	}

	public void addBuilding(Building building) {
		buildings.add(building);
	}
	
	@Override
	public boolean destroyOnEmpty() {
		return false;
	}

	public Expedition getFounderExpedition() {
		return founderExpedition;
	}

	public Date getFoundedIn() {
		return foundedIn;
	}

	public List<Building> getBuildings() {
		return buildings;
	}

	
	public void forageFood() {
		// Get max foraged food storage
		int maxForagedStorage = getMaxForagedStorage(); 
		
		// Get currently stored food
		int currentFood = getItemCount("FRUIT");
		
		// Get food foraging capacity
		int foragingCapacity = getTotalUnits();
		
		maxForagedStorage -= currentFood;
		
		// Store food
		if (foragingCapacity > maxForagedStorage){
			foragingCapacity = maxForagedStorage;
		}
		
		if (foragingCapacity > 0){
			String food = "FRUIT";
			ExpeditionItem foodSample = ItemFactory.createItem(food);
			addItem(foodSample, foragingCapacity);
		}
	}

	private int getMaxForagedStorage() {
		int acum = 0;
		for (Building building: buildings){
			Integer forageStorageCapacity = (Integer) building.getSpecialCapability(SpecialCapability.FORAGED_FOOD_STORAGE);
			if (forageStorageCapacity == null)
				continue;
			acum += forageStorageCapacity;
		}
		return acum;
	}

	public void checkCrops() {
		for (Building building: buildings){
			if (building instanceof Farm)
				((Farm)building).checkCrop(ExpeditionGame.getCurrentGame().getGameTime(), this);
		}
	}

	@Override
	public boolean requiresUnitsToContainItems() {
		return false;
	}

	@Override
	public String getTypeDescription() {
		if (isCity())
			return "City";
		if (isTown())
			return "Town";
		return "Village";
	}
	
}
