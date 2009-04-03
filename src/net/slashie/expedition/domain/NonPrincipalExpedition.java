package net.slashie.expedition.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import net.slashie.expedition.game.ExpeditionGame;
import net.slashie.expedition.item.ItemFactory;
import net.slashie.expedition.ui.ExpeditionUserInterface;
import net.slashie.libjcsi.ConsoleSystemInterface;
import net.slashie.serf.game.Equipment;
import net.slashie.serf.game.Player;
import net.slashie.serf.level.AbstractFeature;
import net.slashie.serf.ui.UserInterface;
import net.slashie.serf.ui.consoleUI.CharAppearance;
import net.slashie.util.Pair;
import net.slashie.utils.Position;
import net.slashie.utils.Util;

public class NonPrincipalExpedition extends Expedition{
	private String classifierId;
	private boolean isHostile = true;
	private int initialPower;
	
	public void calculateInitialPower(){
		initialPower = getPower();
	}
	
	public boolean isHostile() {
		return isHostile;
	}

	public void setHostile(boolean isHostile) {
		this.isHostile = isHostile;
	}

	public NonPrincipalExpedition(ExpeditionGame game, String classifierId) {
		super(game);
		this.classifierId = classifierId;
	}
	
	@Override
	public String getClassifierID() {
		return classifierId;
	}
	
	@Override
	public String getDescription() {
		return getTotalUnits()+" "+ getName();
	}

	
	
	public void checkDeath(){
		if (getTotalUnits() <= 0){
			GoodsCache cache = new GoodsCache(ExpeditionGame.getCurrentGame());
			cache.setPosition(new Position(getPosition()));
			List<Pair<String, Integer>> prizeList = getPrizesFor(initialPower);
			for (Pair<String,Integer> prize: prizeList){
				cache.addItem(ItemFactory.createItem(prize.getA()), prize.getB());
			}
			//((ExpeditionUserInterface)UserInterface.getUI()).transferFromExpedition(cache);
			AbstractFeature previousFeature = getLevel().getFeatureAt(getPosition());
			if (previousFeature != null && 
					previousFeature instanceof GoodsCache &&
					((GoodsCache)previousFeature).isInfiniteCapacity()){
				((GoodsCache)previousFeature).addAllGoods(cache);
			} else {
				getLevel().addFeature(cache);
			}
			die();
		}
	}

	private final static Pair[] prizes = new Pair[]{
		new Pair<String, Integer>("GOLD_NUGGET",1),
		new Pair<String, Integer>("GOLD_BRACELET",2),
		new Pair<String, Integer>("NATIVE_ARTIFACT",3),
		new Pair<String, Integer>("NATIVE_FOOD",6)
	};
	
	private List<Pair<String, Integer>> getPrizesFor(int initialPower) {
		int waves = (int)Math.round((double)initialPower / 100.0d);
		List<Pair<String,Integer>> ret = new ArrayList<Pair<String,Integer>>();
		for (int i = 0; i < waves; i++){
			Pair<String, Integer> prize = prizes[Util.rand(0, 3)];
			ret.add(new Pair<String, Integer>(prize.getA(), prize.getB()*Util.rand(5,8)));
		}
		return ret;
	}
}
