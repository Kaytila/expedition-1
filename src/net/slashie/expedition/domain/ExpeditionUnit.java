package net.slashie.expedition.domain;

import java.util.ArrayList;
import java.util.List;

import net.slashie.expedition.domain.Armor.ArmorType;
import net.slashie.expedition.domain.Weapon.WeaponType;
import net.slashie.expedition.item.Mount;
import net.slashie.serf.game.Equipment;
import net.slashie.serf.text.EnglishGrammar;
import net.slashie.serf.ui.Appearance;
import net.slashie.serf.ui.AppearanceFactory;
import net.slashie.util.Pair;
import net.slashie.utils.roll.Roll;

@SuppressWarnings("serial")
public class ExpeditionUnit extends Vehicle{
	public enum ContractType {
		LIFETIME,
		MONTHLY,
		JOIN_AND_SPLIT
	}
	
	@Override
	public Appearance getAppearance() {
		if (!isMounted()){
			return super.getAppearance();
		} else {
			return AppearanceFactory.getAppearanceFactory().getAppearance(getBaseID()+"_MOUNTED");
		}
	}
	
	private String name;
	private int resistance;  //TODO: Make this affect battle performance
	private int dailyFoodConsumption;
	private int dailyWaterConsumption;
	private int baseHitChance;
	private Roll baseAttack;
	private Roll baseDefense;
	
	private int evadeChance;
	private int hitChance;
	private Roll compositeAttack;
	private Roll compositeDefense;
	private boolean isRangedAttack;
	
	private WeaponType[] weaponTypes;
	private ArmorType[] armorTypes;
	private Weapon weapon;
	private Armor armor;
	private Mount mount;

	private boolean isSettled;
	
	public void setArmor(Armor armor) {
		this.armor = armor;
		updateCompositeVariables();
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public WeaponType[] getWeaponTypes() {
		return weaponTypes;
	}

	public ArmorType[] getArmorTypes() {
		return armorTypes;
	}

	/**
	 * Returns the perceived power for the unit
	 * @return
	 */
	public int getPower(){
		return (getAttack().getMax()*3+getDefense().getMax()*2)*(isRangedAttack()?2:1);
	}
	
	public Roll getAttack(){
		return compositeAttack;
	}
	
	public Roll getDefense(){
		return compositeDefense;
	}
	
	public int getDailyFoodConsumption() {
		/*if (weapon != null)
			return dailyFoodConsumption + weapon.getBurden();
		else*/
			return dailyFoodConsumption;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getResistance() {
		return resistance;
	}

	public void setResistance(int resistance) {
		this.resistance = resistance;
	}

	public ExpeditionUnit(String classifierId, 
			String description, 
			String pluralDescription,
			String longDescription,
			int weight, 
			int carryCapacity,
			Roll baseAttack, 
			Roll baseDefense,
			int resistance,
			int baseHitChance,
			int evadeChance,
			int dailyFoodConsumption,
			int dailyWaterConsumption,
			WeaponType[] weaponTypes, ArmorType[] armorTypes, int palosStoreValue) {
		super(classifierId, description, pluralDescription, longDescription, weight, false, false, 1, carryCapacity, 1, true, GoodType.PEOPLE, palosStoreValue, 999999999);
		// A life is too expensive to trade with it (Slavery isn't yet added)
		this.name = description;
		this.baseAttack = baseAttack;
		this.baseDefense = baseDefense;
		this.baseHitChance = baseHitChance;
		this.evadeChance = evadeChance;
		this.resistance = resistance;
		this.dailyFoodConsumption = dailyFoodConsumption;
		this.dailyWaterConsumption = dailyWaterConsumption;
		this.weaponTypes = weaponTypes;
		this.armorTypes = armorTypes;
		updateCompositeVariables();
	}

	public void setArm(Weapon createItem) {
		weapon = createItem;
		updateCompositeVariables();
	}
	
	public String getStatusModifiersString(){
		String ret = "";
		if (getMount() != null){
			ret += "Mounted ";
		}
		if (isWounded){
			ret += "Wounded ";
		}
		return ret;
	}
	
	public void updateCompositeVariables(){
		fullId = super.getBaseID();
		unitWeight = super.getWeight();
		fullDescription = "";
		compositeAttack = new Roll(baseAttack);
		compositeDefense = new Roll(baseDefense);
		
		if (getMount() != null){
			fullDescription += "Mounted ";
			compositeAttack.addModifier(2);
			unitWeight += mount.getWeight();
		}
		
		if (isWounded){
			fullDescription += "Wounded ";
		}
		
		if (armor != null){
			//fullDescription += "+"+armor.getDefense().getMax()+" ";
			fullDescription += armor.getShortDescription()+" ";
		}

		if (weapon != null){
			fullId += ","+weapon.getFullID();
			fullDescription += weapon.getDescription()+" ";
			unitWeight += weapon.getWeight();
			if (weapon.isTool()){
				compositeAttack = new Roll(weapon.getAttack());
				hitChance = weapon.getHitChance();
			} else {
				compositeAttack.addModifierRoll(weapon.getAttack());
				hitChance = (int) Math.round( (baseHitChance + weapon.getHitChance())/2.0d);
			}
			if (weapon.isRanged()){
				isRangedAttack = true;
			} else {
				isRangedAttack = false;
			}
		} else {
			hitChance = baseHitChance;
		}
		fullDescriptionBase = fullDescription;
		fullDescription += getDescription();
		if (armor != null){
			fullId += ";"+armor.getFullID();
			//menuDescription += "("+armor.getShortDescription()+")";
			unitWeight += armor.getWeight();
			compositeDefense = new Roll(armor.getDefense());
		}
		if (mount != null){
			fullId += ";MOUNT:"+mount.getFullID();
		}
		if (isWounded){
			fullId += ",WOUNDED";
		}
	}
	
	private String fullDescription;
	
	@Override
	public String getFullDescription() {
		return fullDescription;
	}
	
	private String fullId;
	@Override
	public String getFullID() {
		return fullId;
	}
	
	private int unitWeight;
	private boolean isWounded;
	private String fullDescriptionBase;
	@Override
	public int getWeight() {
		return unitWeight;
	}
	
	public Armor getArmor() {
		return armor;
	}

	
	public int getHitChance() {
		return hitChance;
	}

	public int getEvadeChance() {
		return evadeChance;
	}

	public boolean isRangedAttack() {
		return isRangedAttack;
	}

	public boolean isWounded() {
		return isWounded;
	}

	public void setWounded(boolean isWounded) {
		this.isWounded = isWounded;
		updateCompositeVariables();
	}
	
	@Override
	public String getPluralDescription() {
		return fullDescriptionBase + super.getPluralDescription();
	}
	
	public static Pair<String, Integer> getUnitsString(
			List<Pair<ExpeditionUnit, Integer>> list) {
		int i = 0;
		int unitCount = 0;
		String unitsString = "";
		for (Pair<ExpeditionUnit, Integer> killInfo: list){
			if (killInfo.getB() == 0){
				i++;
				continue;
			}
			if (killInfo.getB() == 1){
				unitsString += EnglishGrammar.a(killInfo.getA().getFullDescription())+" "+killInfo.getA().getFullDescription();
			} else
				unitsString += killInfo.getB()+" "+killInfo.getA().getPluralDescription();
			if (i == list.size()-2)
				unitsString += " and ";
			else if (i == list.size()-1)
				;
			else if (list.size()>1)
				unitsString += ", ";
			i++;
			unitCount += killInfo.getB();
		}
		return new Pair<String, Integer>(unitsString, unitCount);
	}

	public static Pair<String, Integer> getUnitsStringFromEquipment (List<Equipment> list) {
		int i = 0;
		int unitCount = 0;
		String unitsString = "";
		for (Equipment killInfo: list){
			if (killInfo.getQuantity() == 0){
				i++;
				continue;
			}
			if (killInfo.getQuantity() == 1){
				String unitDescription = ((ExpeditionUnit)killInfo.getItem()).getFullDescription();
				unitsString += EnglishGrammar.a(unitDescription)+" "+unitDescription;
			} else
				unitsString += killInfo.getQuantity()+" "+((ExpeditionUnit)killInfo.getItem()).getPluralDescription();
			if (i == list.size()-2)
				unitsString += " and ";
			else if (i == list.size()-1)
				;
			else if (list.size()>1)
				unitsString += ", ";
			i++;
			unitCount += killInfo.getQuantity();
		}
		return new Pair<String, Integer>(unitsString, unitCount);
	}

	public Mount getMount() {
		return mount;
	}

	public void setMount(Mount mount) {
		this.mount = mount;
		updateCompositeVariables();
	}

	public boolean isMounted() {
		return getMount() != null;
	}
	
	public ContractType getContractType(){
		return ContractType.LIFETIME;
	}

	public List<Appearance> getAvailableWeaponAppearances() {
		WeaponType[] weaponTypes = getWeaponTypes();
		List<Appearance> ret = new ArrayList<Appearance>();
		for (WeaponType weaponType: weaponTypes){
			if (weaponType.equals(""))
				continue;
			ret.add(AppearanceFactory.getAppearanceFactory().getAppearance("DIALOG_"+weaponType.getAppearanceId()));
		}
		return ret;
	}

	public List<Appearance> getAvailableArmorAppearances() {
		ArmorType[] armorTypes = getArmorTypes();
		List<Appearance> ret = new ArrayList<Appearance>();
		for (ArmorType armorType: armorTypes){
			if (armorType.equals(""))
				continue;
			ret.add(AppearanceFactory.getAppearanceFactory().getAppearance("DIALOG_"+armorType.getAppearanceId()));
		}
		return ret;
	}


	public String getWeaponDescription() {
		if (getWeapon() == null){
			return "Unarmed";
		}else {
			return getWeapon().getDescription();
		}
	}
	
	public String getArmorDescription(){
		if (getArmor() == null){
			return "In Clothes";
		} else {
			return getArmor().getDescription();
		}
	}

	
	public boolean isSettled() {
		return isSettled;
	}
	
	public int getDailyWaterConsumption() {
		return dailyWaterConsumption;
	}
}

	
