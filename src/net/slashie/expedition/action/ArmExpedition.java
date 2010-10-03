package net.slashie.expedition.action;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.slashie.expedition.domain.Armor;
import net.slashie.expedition.domain.Expedition;
import net.slashie.expedition.domain.ExpeditionUnit;
import net.slashie.expedition.domain.GoodType;
import net.slashie.expedition.domain.Weapon;
import net.slashie.expedition.game.ExpeditionGame;
import net.slashie.expedition.item.ItemFactory;
import net.slashie.expedition.world.ExpeditionMicroLevel;
import net.slashie.serf.action.Action;
import net.slashie.serf.game.Equipment;
import net.slashie.serf.ui.UserInterface;

public class ArmExpedition extends Action{
	private boolean actionCancelled;
	@Override
	public void execute() {
		actionCancelled = false;
		boolean isPlayer = getExpedition() == ExpeditionGame.getCurrentGame().getPlayer(); 
		if (!getExpedition().isArmed()){
			if ( isPlayer && !UserInterface.getUI().promptChat("Arm Expedition: Are you sure?")){
				actionCancelled = true;
				return;
			}
			
			List<Equipment> units = getExpedition().getUnarmedUnits();
			Collections.sort(units, new Comparator<Equipment>() {
				public int compare(Equipment arg0, Equipment arg1) {
					return ((ExpeditionUnit)arg1.getItem()).getAttack().getMax() - ((ExpeditionUnit)arg0.getItem()).getAttack().getMax();
				}
			});
			for (Equipment unit: units){
				String[] preferredWeapons = ((ExpeditionUnit)unit.getItem()).getWeaponTypes();
				for (String weaponType: preferredWeapons){
					int available = getExpedition().getItemCount(weaponType);
					int unitsToArm = available > unit.getQuantity() ? unit.getQuantity() : available;
					getExpedition().reduceGood(weaponType, unitsToArm);
					//Split equipment in armed and disarmed
					if (unitsToArm > 0){
						getExpedition().reduceQuantityOf(unit.getItem(), unitsToArm);
						//ExpeditionUnit newUnit = (ExpeditionUnit)ItemFactory.createItem(unit.getItem().getFullID());
						ExpeditionUnit newUnit = (ExpeditionUnit)((ExpeditionUnit)unit.getItem()).clone();
						newUnit.setArm((Weapon)ItemFactory.createItem(weaponType));
						getExpedition().addItem(newUnit, unitsToArm);
					}
				}
			}
			
			units = getExpedition().getUnarmoredUnits();
			Collections.sort(units, new Comparator<Equipment>() {
				public int compare(Equipment arg0, Equipment arg1) {
					return ((ExpeditionUnit)arg1.getItem()).getDefense().getMax() - ((ExpeditionUnit)arg0.getItem()).getDefense().getMax();
				}
			});
			for (Equipment unit: units){
				String[] preferredArmors = ((ExpeditionUnit)unit.getItem()).getArmorTypes();
				for (String armorType: preferredArmors){
					int available = getExpedition().getItemCount(armorType);
					int unitsToArm = available > unit.getQuantity() ? unit.getQuantity() : available;
					getExpedition().reduceGood(armorType, unitsToArm);
					//Split equipment in armored and unarmored
					if (unitsToArm > 0){
						getExpedition().reduceQuantityOf(unit.getItem(), unitsToArm);
						//ExpeditionUnit newUnit = (ExpeditionUnit)ItemFactory.createItem(unit.getItem().getFullID());
						ExpeditionUnit newUnit = (ExpeditionUnit)((ExpeditionUnit)unit.getItem()).clone();
						newUnit.setArmor((Armor)ItemFactory.createItem(armorType));
						getExpedition().addItem(newUnit, unitsToArm);
					}
				}
			}
			getExpedition().setArmed(true);
		} else {
			if (isPlayer && !UserInterface.getUI().promptChat("Disarm Expedition: Are you sure?")){
				actionCancelled = true;
				return;
			}
				
			List<Equipment> units = getExpedition().getGoods(GoodType.PEOPLE);
			for (Equipment unit: units){
				ExpeditionUnit eUnit = ((ExpeditionUnit)unit.getItem()); 
				boolean useOffShore = isPlayer && getExpedition().getLocation() instanceof ExpeditionMicroLevel && ((ExpeditionMicroLevel)getExpedition().getLocation()).isDock(); 
				if (eUnit.getWeapon() != null || eUnit.getArmor() != null){
					int quantity = unit.getQuantity();
					Weapon weapon = eUnit.getWeapon();
					Armor armor = eUnit.getArmor();
					getExpedition().reduceQuantityOf(eUnit, quantity);
					eUnit.setArm(null);
					eUnit.setArmor(null);
					
					if (useOffShore){
						getExpedition().addItemOffshore(eUnit, quantity);
						if (weapon != null)
							getExpedition().addItemOffshore(weapon, quantity);
						if (armor != null)
							getExpedition().addItemOffshore(armor, quantity);

					} else {
						getExpedition().addItem(eUnit, quantity);
						if (weapon != null)
							getExpedition().addItem(weapon, quantity);
						if (armor != null)
							getExpedition().addItem(armor, quantity);
					}
				}
			}
			getExpedition().setArmed(false);
		}
		
	}
	
	private Expedition getExpedition(){
		return (Expedition) performer;
	}
	
	@Override
	public String getID() {
		return "ArmExpedition";
	}
	
	@Override
	public int getCost() {
		if (actionCancelled)
			return 0;
		return getExpedition().getTotalUnits();
	}

}
