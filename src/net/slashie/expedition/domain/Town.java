package net.slashie.expedition.domain;

import net.slashie.expedition.domain.Expedition.MovementMode;
import net.slashie.expedition.game.ExpeditionGame;
import net.slashie.expedition.ui.ExpeditionUserInterface;
import net.slashie.serf.action.Actor;
import net.slashie.serf.ui.AppearanceFactory;
import net.slashie.serf.ui.UserInterface;

public class Town extends GoodsCache{
	private String name;
	
	public Town(ExpeditionGame game) {
		super(game);
		setAppearanceId("TOWN");

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
		if (a instanceof Expedition){
			switch (UserInterface.getUI().switchChat("What do you want to do?", "Nothing")){
			case 0:
    			break;
    		}
		}
	}
	
	@Override
	public void consumeFood() {
		//Do nothing, this must be handled differently
	}

}
