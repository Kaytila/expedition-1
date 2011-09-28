package net.slashie.expedition.domain;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.slashie.expedition.domain.Expedition.DeathCause;
import net.slashie.expedition.game.ExpeditionGame;
import net.slashie.expedition.ui.ExpeditionUserInterface;
import net.slashie.expedition.world.FoodConsumer;
import net.slashie.expedition.world.FoodConsumerDelegate;
import net.slashie.serf.action.Actor;
import net.slashie.serf.baseDomain.AbstractItem;
import net.slashie.serf.game.Equipment;
import net.slashie.serf.level.AbstractFeature;
import net.slashie.serf.ui.Appearance;
import net.slashie.serf.ui.AppearanceFactory;
import net.slashie.serf.ui.UserInterface;

public class GoodsCache extends AbstractFeature implements FoodConsumer, UnitContainer, ItemContainer{
	private static final long serialVersionUID = 1L;
	
	private FoodConsumerDelegate foodConsumerDelegate;

	private String nonEmptyAppearanceId; 
	
	public GoodsCache(boolean abstractCache) {
		setAppearanceId("GOODS_CACHE");
		foodConsumerDelegate = new FoodConsumerDelegate(this);
	}
	
	public GoodsCache(ExpeditionGame game, String appearanceId) {
		this(game, appearanceId, null);
	}
	
	public GoodsCache(ExpeditionGame game, String appearanceId, String nonEmptyAppearanceId) {
		setAppearanceId(appearanceId);
		foodConsumerDelegate = new FoodConsumerDelegate(this);
		game.addFoodConsumer(this);
		this.nonEmptyAppearanceId = nonEmptyAppearanceId;
	}
	
	@Override
	public Appearance getAppearance() {
		if (nonEmptyAppearanceId != null && getItems().size() > 0)
			return AppearanceFactory.getAppearanceFactory().getAppearance(nonEmptyAppearanceId);
		else {
			return super.getAppearance();
		}
	}
	
	public GoodsCache() {
		super();
	}

	public int getItemCount(String string) {
		int goodCount = 0;
		List<Equipment> inventory = getInventory();
		for (Equipment equipment: inventory){
			if (equipment.getItem().getFullID().equals(string)){
				goodCount += equipment.getQuantity();
			}
		}
		return goodCount;
	}
	
	public int getVehicleCount(String fullId) {
		return getItemCount(fullId);
	}
	
	public int getItemCountBasic(String string) {
		int goodCount = 0;
		List<Equipment> inventory = getInventory();
		for (Equipment equipment: inventory){
			if (((ExpeditionItem)equipment.getItem()).getBaseID().equals(string)){
				goodCount += equipment.getQuantity();
			}
		}
		return goodCount;
	}
	
	public List<Equipment> getItemsWithBaseID(String baseId) {
		List<Equipment> ret = new ArrayList<Equipment>();
		List<Equipment> inventory = getInventory();
		for (Equipment equipment: inventory){
			if (((ExpeditionItem)equipment.getItem()).getBaseID().equals(baseId)){
				ret.add(equipment);
			}
		}
		return ret;
	}
	
	public List<Equipment> getItems() {
		return items;
	}

	private List<Equipment> items = new ArrayList<Equipment>();
	private Map<String, Equipment> itemsHash = new Hashtable<String, Equipment>(); 
	
	public void addAllGoods(Expedition expedition){
		for (Equipment equipment: expedition.getInventory()){
			if (equipment.getQuantity() > 0) {
				Equipment current = itemsHash.get(equipment.getItem().getFullID());
				if (current == null){
					Equipment new_ = equipment.clone();
					itemsHash.put(equipment.getItem().getFullID(), new_);
					items.add(new_);
				} else {
					current.increaseQuantity(equipment.getQuantity());
				}
			}
		}
	}
	
	public void addAllGoods(GoodsCache cache){
		for (Equipment equipment: cache.getInventory()){
			addItem((ExpeditionItem)equipment.getItem(), equipment.getQuantity());
		}
	}
	
	public int getCarryable(ExpeditionItem item){
		return -1;
	}
	
	@Override
	public int getCurrentlyCarrying() {
		if (getCarryCapacity() == -1){
			return -1;
		} else {
			return (int)Math.round(((double)getCurrentWeight()/(double)getCarryCapacity())*100.0d);
		}
	}
	
	private int getCurrentWeight(){
		int currentlyCarrying = 0;
		List<Equipment> inventory = getItems();
		for (Equipment equipment: inventory){
			if (!(equipment.getItem() instanceof Vehicle)){
				currentlyCarrying += ((ExpeditionItem)equipment.getItem()).getWeight() * equipment.getQuantity();
			}
		}
		return currentlyCarrying;
	}

	@Override
	public AbstractFeature featureDestroyed(Actor actor) {
		return null;
	}

	@Override
	public void counterFinished(String counterId) {
	}

	@Override
	public boolean extendedInfoAvailable() {
		return false;
	}

	@Override
	public String getClassifierID() {
		return "Mound";
	}

	@Override
	public boolean isInvisible() {
		return false;
	}
	
	@Override
	public void onStep(Actor a) {
		if (a instanceof Expedition && !(a instanceof NonPrincipalExpedition)){
			switch (UserInterface.getUI().switchChat("Goods Cache","What do you want to do?", "Fetch Equipment", "Cach� Equipment", "Carry all", "Do Nothing")){
			case 0:
				((ExpeditionUserInterface)UserInterface.getUI()).transferFromCache("Select the goods to transfer", null, this);
    			break;
			case 1:
				((ExpeditionUserInterface)UserInterface.getUI()).transferFromExpedition(this);
				break;
			case 2:
				Expedition expedition = (Expedition)a;
				expedition.addAllItemsForced(getInventory());
				removeAllItems();
				if (destroyOnEmpty())
					getLevel().destroyFeature(this);
				forceNotSolid = true;
				break;
			case 3:
				// Do nothing;
			}
		}
	}
	
	private boolean forceNotSolid = false;
	
	@Override
	public boolean isSolid() {
		if (forceNotSolid)
			return false;
		else 
			return ExpeditionGame.getCurrentGame().getExpedition().getMovementMode().isLandMovement();
	}

	public boolean canCarry(ExpeditionItem item, int quantity) {
		return true;
	}

	@Override
	public void addItem(ExpeditionItem item, int quantity) {
		String toAddID = item.getFullID();
		Equipment equipmentx = itemsHash.get(toAddID);
		if (equipmentx == null){
			equipmentx = new Equipment(item, quantity);
			itemsHash.put(toAddID, equipmentx);
			items.add(equipmentx);
		} else {
			equipmentx.increaseQuantity(quantity);
		}

	}
	
	@Override
	public String getDescription() {
		return "Ground";
	}

	@Override
	public int getTotalUnits() {
		int totalUnits = 0;
		List<Equipment> inventory = getItems();
		for (Equipment equipment: inventory){
			if (equipment.getItem() instanceof ExpeditionUnit){
				totalUnits += equipment.getQuantity();
			}
		}
		return totalUnits;
	}
	
	public void checkDeath() {
		if (getItems().size() == 0){
			die();
		}
	}
	
	public void consumeFood() {
		foodConsumerDelegate.consumeFood();
	}
	
	public int getDailyFoodConsumption() {
		return foodConsumerDelegate.getDailyFoodConsumption();
	}
	
	public List<Equipment> getInventory() {
		return getItems();
	}
	
	public double getFoodConsumptionMultiplier() {
		return 1;
	}
	
	public void reduceQuantityOf(AbstractItem item, int quantity) {
		for (int i = 0; i < items.size(); i++){
			Equipment equipment = (Equipment) items.get(i);
			if (equipment.getItem().equals(item)){
				equipment.reduceQuantity(quantity);
				if (equipment.getQuantity() < 0){
					// This should never happen
					System.out.println("WARNING: Invalid scenario has just happened. Please contact us with this information:");
					equipment.setQuantity(0);
					try {
						throw new RuntimeException();
					} catch (RuntimeException e){
						e.printStackTrace();
					}
				}
				if (equipment.isEmpty()){
					items.remove(equipment);
					itemsHash.remove(equipment.getItem().getFullID());
				}
				return;
			}
		}
	}
	
	public void reduceQuantityOf(String itemId, int quantity) {
		for (int i = 0; i < items.size(); i++){
			Equipment equipment = (Equipment) items.get(i);
			if (equipment.getItem().getFullID().equals(itemId)){
				equipment.reduceQuantity(quantity);
				if (equipment.getQuantity() < 0){
					// This should never happen
					System.out.println("WARNING: Invalid scenario has just happened. Please contact us with this information:");
					equipment.setQuantity(0);
					try {
						throw new RuntimeException();
					} catch (RuntimeException e){
						e.printStackTrace();
					}
				}
				if (equipment.isEmpty()){
					items.remove(equipment);
					itemsHash.remove(equipment.getItem().getFullID());
				}
				return;
			}
		}
	}
	
	public boolean isInfiniteCapacity(){
		return true;
	}
	
	@Override
	public int getCarryCapacity() {
		return -1;
	}
	
	@Override
	public int getTotalShips() {
		return 0;
	}
	
	public void killUnits(int quantity) {
		foodConsumerDelegate.killUnits(quantity);
		if (destroyOnEmpty() && getItems().size() == 0){
			getLevel().destroyFeature(this);
		}
	}

	public boolean destroyOnEmpty() {
		return true;
	}
	
	@Override
	public void addUnits(ExpeditionUnit unit, int quantity) {
		addItem(unit, quantity);
	}
	
	@Override
	public void reduceUnits(ExpeditionUnit unit, int quantity, DeathCause cause) {
		reduceQuantityOf(unit, quantity);
	}
	
	public int getGoodTypeCount(GoodType goodType) {
		int acum = 0;
		for (Equipment e: getInventory()){
			ExpeditionItem g = (ExpeditionItem) e.getItem();
			if (g.getGoodType() == goodType)
				acum += e.getQuantity();
		}
		return acum;
	}
	
	public List<Equipment> getGoods(GoodType goodType) {
		List<Equipment> ret = new ArrayList<Equipment>();
		for (Equipment e: getInventory()){
			ExpeditionItem g = (ExpeditionItem) e.getItem();
			if (g.getGoodType() == goodType)
				ret.add(new Equipment(e.getItem(), e.getQuantity()));
		}
		return ret;
	}

	public void addAllItems(List<Equipment> items) {
		for (Equipment equipment: items){
			addItem((ExpeditionItem)equipment.getItem(), equipment.getQuantity());
		}
	}
	
	public void reduceAllItems(List<Equipment> items){
		for (Equipment equipment: items){
			reduceQuantityOf(equipment.getItem().getFullID(), equipment.getQuantity());
		}
	}
	
	protected void removeAllItems(){
		items.clear();
	}
	
	
	public int getFoodDays(){
		return foodConsumerDelegate.getFoodDays();
	}
	
	@Override
	public int getCurrentFood() {
		int currentFood = 0;
		List<Equipment> inventory = getInventory();
		for (Equipment equipment: inventory){
			if (equipment.getItem() instanceof Food){
				Food good = (Food)equipment.getItem();
				currentFood += good.getUnitsFedPerGood() * equipment.getQuantity();
			}
		}
		return currentFood;
	}

	public int getWaterDays() {
		return 0;
	}
	
	@Override
	public boolean isPeopleContainer() {
		return true;
	}
	
	@Override
	public boolean requiresUnitsToContainItems() {
		return false;
	}
	
	@Override
	public String getTypeDescription() {
		return "Ground";
	}
}
