package net.slashie.expedition.domain;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import net.slashie.expedition.domain.Expedition.MovementMode;
import net.slashie.expedition.game.ExpeditionGame;
import net.slashie.expedition.item.ItemFactory;
import net.slashie.expedition.ui.ExpeditionUserInterface;
import net.slashie.expedition.ui.console.ExpeditionConsoleUI;
import net.slashie.serf.action.Actor;
import net.slashie.serf.ui.AppearanceFactory;
import net.slashie.serf.ui.UserInterface;
import net.slashie.utils.Util;

public class Town extends GoodsCache{
	private String name;
	private Expedition founderExpedition;
	private Date foundedIn;
	
	public Town(ExpeditionGame game) {
		super(game);
		setAppearanceId("TOWN");
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
	
	@Override
	public void onStep(Actor a) {
		/*if (a instanceof Expedition && !(a instanceof NonPrincipalExpedition)){
			switch (UserInterface.getUI().switchChat("What do you want to do?", "Nothing")){
			case 0:
    			break;
    		}
		}*/
		((ExpeditionUserInterface)UserInterface.getUI()).showBlockingMessage("The "+getTitle()+" of "+getName()+" XXX " +
				"Founded on "+ DateFormat.getDateInstance(DateFormat.MEDIUM).format(foundedIn)+" by "+founderExpedition.getExpeditionaryTitle()+" XXX "+
				"Current Population: "+getPopulation()
				
		);
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

	private int getPopulation() {
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
			addItem(ItemFactory.createItem("COLONIST"), growth);
		}
	}
	
	public String getTitle(){
		if (isCity())
			return "city";
		if (isTown())
			return "town";
		return "village";
	}
	
	

}
