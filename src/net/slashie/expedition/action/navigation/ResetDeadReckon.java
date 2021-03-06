package net.slashie.expedition.action.navigation;

import net.slashie.expedition.domain.Expedition;
import net.slashie.serf.action.Action;

public class ResetDeadReckon extends Action{

	@Override
	public void execute() {
		((Expedition)performer).resetDeducedReckonWest();
	}

	@Override
	public String getID() {
		return "ResetDeadReckon";
	}

}
