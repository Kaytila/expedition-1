package net.slashie.expedition.domain;
import org.apache.log4j.Logger;
import net.slashie.expedition.item.StorageType;

public class Water extends ExpeditionItem
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final static Logger logger = Logger.getRootLogger();

	private int unitsFedPerGood;

	public int getUnitsFedPerGood()
	{
		return unitsFedPerGood;
	}

	public Water(String classifierId, String description, String pluralDescription, String longDescription, int weight,
			int unitsFedPerGood, int palosStoreValue, int baseTradingValue, StorageType storageType)
	{
		super(classifierId, description, pluralDescription, longDescription, classifierId, weight, GoodType.SUPPLIES,
				palosStoreValue, baseTradingValue, storageType);
		this.unitsFedPerGood = unitsFedPerGood;
	}
}
