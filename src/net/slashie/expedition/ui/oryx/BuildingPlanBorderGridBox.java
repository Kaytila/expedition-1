package net.slashie.expedition.ui.oryx;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.concurrent.BlockingQueue;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Timer;

import net.slashie.expedition.game.ExpeditionGame;
import net.slashie.expedition.ui.ExpeditionUserInterface;
import net.slashie.libjcsi.CharKey;
import net.slashie.serf.ui.UserInterface;
import net.slashie.serf.ui.oryxUI.GFXUserInterface;
import net.slashie.serf.ui.oryxUI.SwingSystemInterface;
import net.slashie.utils.swing.BorderedGridBox;
import net.slashie.utils.swing.CallbackActionListener;
import net.slashie.utils.swing.CallbackKeyListener;
import net.slashie.utils.swing.CleanButton;
import net.slashie.utils.swing.CustomGFXMenuItem;
import net.slashie.utils.swing.GFXMenuItem;

@SuppressWarnings("serial")
public class BuildingPlanBorderGridBox extends BorderedGridBox{
	private ExpeditionCleanButton okButton;
	private BuildingCustomGFXMenuItem selectedBuilding;
	private int requiredWood;
	
	// Splitter attributes
	private CleanButton quantitySplitterUp;
	private CleanButton quantitySplitterDown;
	private MouseMotionAdapter mml;
	private CallbackKeyListener<String> cbkl;
	
	public BuildingPlanBorderGridBox(
			// Standard parameters, sent to super()
			BufferedImage border1, BufferedImage border2,
			BufferedImage border3, BufferedImage border4,
			SwingSystemInterface g, Color backgroundColor, Color borderIn,
			Color borderOut, int borderWidth, int outsideBound, int inBound,
			int insideBound, final int itemHeight, final int itemWidth, int gridX, int gridY, 
			BlockingQueue<String> selectionHandler,
			CleanButton closeButton
			) {
		super(border1, border2, border3, border4, g, backgroundColor, borderIn,
				borderOut, borderWidth, outsideBound, inBound, insideBound, itemHeight,
				itemWidth, gridX, gridY, null, closeButton, ExpeditionOryxUI.BTN_SPLIT_UP, ExpeditionOryxUI.BTN_SPLIT_DOWN, ExpeditionOryxUI.BTN_SPLIT_UP_HOVER, ExpeditionOryxUI.BTN_SPLIT_DOWN_HOVER, ExpeditionOryxUI.HAND_CURSOR);
		initializeSplitters();
				
		okButton = new ExpeditionCleanButton(4, "Build");
		okButton.setVisible(true);
		okButton.setLocation(615,370);
		si.add(okButton);
		
		okButton.addActionListener(new CallbackActionListener<String>(selectionHandler){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (hoverDisabled)
					return;
				
				if (requiredWood > ExpeditionGame.getCurrentGame().getExpedition().getItemCount("WOOD")){
					((ExpeditionUserInterface)UserInterface.getUI()).showBlockingMessage("You don't have enough wood to execute this plan");
					return;
				}
				
				try {
					handler.put("OK");
				} catch (InterruptedException e1) {}
				si.recoverFocus();
			}
		});
		
		final int pageElements = gridX * gridY;
		
		cbkl = new CallbackKeyListener<String>(selectionHandler){
			@Override
			public void keyPressed(KeyEvent e) {
				if (hoverDisabled)
					return;
				
				int code = SwingSystemInterface.charCode(e);
				
				if (code == CharKey.SPACE || code == CharKey.ESC || code == CharKey.ENTER){
					if (requiredWood > ExpeditionGame.getCurrentGame().getExpedition().getItemCount("WOOD")){
						((ExpeditionUserInterface)UserInterface.getUI()).showBlockingMessage("You don't have enough wood to execute this plan");
						return;
					}
					try {
						handler.put("OK");
					} catch (InterruptedException e1) {}
					si.recoverFocus();
				} else if (code == CharKey.UARROW || code == CharKey.N8){
					if (requiredWood + selectedBuilding.getBuilding().getWoodCost() > ExpeditionGame.getCurrentGame().getExpedition().getItemCount("WOOD")){
						return;
					}
					selectedBuilding.add();
					requiredWood += selectedBuilding.getBuilding().getWoodCost();
					draw(true);
				} else if (code == CharKey.DARROW || code == CharKey.N2){
					if (selectedBuilding.getQuantity() > 0)
						requiredWood -= selectedBuilding.getBuilding().getWoodCost();
					selectedBuilding.remove();
					draw(true);
				} else if (code == CharKey.LARROW || code == CharKey.N4){
					rePag();
				} else if (code == CharKey.RARROW || code == CharKey.N6){
					avPag();
				} else if (code >= CharKey.A && code <= CharKey.A + pageElements-1 && code <= CharKey.A + items.size() - 1) {
					selectedBuilding = (BuildingCustomGFXMenuItem) shownItems.get(code-CharKey.A);
					int selectedIndex = code-CharKey.A;
					selectedItem = getSelectedItemByKeyboard(selectedIndex);
					draw(true);
				} else if (code >= CharKey.a && code <= CharKey.a + pageElements-1 && code <= CharKey.a + items.size() - 1) {
					selectedBuilding = (BuildingCustomGFXMenuItem) shownItems.get(code-CharKey.a);
					int selectedIndex = code-CharKey.a;
					selectedItem = getSelectedItemByKeyboard(selectedIndex);
					draw(true);
				}
			}
		};
		
		String[] legends = legend.split("XXX");
		int fontSize = getFont().getSize();
		final int lineHeight = (int)Math.round(fontSize*1.5);
		final int legendLines = legends.length > 0 ? legends.length: 1;
		
		mml = new MouseMotionAdapter(){
			public void mouseMoved(MouseEvent e) {
				draw(true);
				SelectedItem selectedItem = getSelectedItemByClick(e.getPoint(), legendLines, lineHeight);
				if (selectedItem != null){
					selectedBuilding = (BuildingCustomGFXMenuItem) shownItems.get(selectedItem.selectedIndex);
					// Move splitters and count label
					int xpos = selectedItem.cursorX * itemWidth + getLocation().x + getBorderWidth();
					int ypos = selectedItem.cursorY * itemHeight + getLocation().y + getBorderWidth() + (legendLines + 1) * lineHeight;
					quantitySplitterUp.setLocation(xpos + ExpeditionOryxUI.STANDARD_ITEM_WIDTH + 3, ypos + 17 - 16);
					quantitySplitterDown.setLocation(xpos + ExpeditionOryxUI.STANDARD_ITEM_WIDTH + 3, ypos + 44 - 16 - 8);
					quantitySplitterUp.setVisible(true);
					quantitySplitterDown.setVisible(true);
					GFXMenuItem item = (GFXMenuItem) shownItems.get(selectedItem.selectedIndex);
					((CustomGFXMenuItem) item).drawTooltip(si, xpos, ypos, selectedItem.selectedIndex);
					si.commitLayer(getDrawingLayer(), true);
					si.setCursor(getHandCursor());
					
				} else {
					// No grid selected
					quantitySplitterUp.setVisible(false);
					quantitySplitterDown.setVisible(false);
				}
			}
			
		};
		
		si.addKeyListener(cbkl);
		si.addMouseMotionListener(mml);
	}

	private void initializeSplitters() {
		quantitySplitterUp = new CleanButton(ExpeditionOryxUI.BTN_SPLIT_UP, ExpeditionOryxUI.BTN_SPLIT_UP_HOVER, null, ((GFXUserInterface)UserInterface.getUI()).getHandCursor());
		quantitySplitterUp.setVisible(false);
		quantitySplitterUp.setLocation(512,221);
		final Action increaseQuantityAction = new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (requiredWood + selectedBuilding.getBuilding().getWoodCost() > ExpeditionGame.getCurrentGame().getExpedition().getItemCount("WOOD")){
					return;
				}
				selectedBuilding.add();
				requiredWood += selectedBuilding.getBuilding().getWoodCost();
				draw(true);
			}
		};
		final Timer increaseQuantityTimer = new Timer(100, increaseQuantityAction);

		quantitySplitterUp.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e){
				if (requiredWood + selectedBuilding.getBuilding().getWoodCost() > ExpeditionGame.getCurrentGame().getExpedition().getItemCount("WOOD")){
					return;
				}
				selectedBuilding.add();
				requiredWood += selectedBuilding.getBuilding().getWoodCost();
				//quantityLabel.setText(selectedBuilding.getQuantity()+"");
				draw(true);
				increaseQuantityTimer.start();
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				increaseQuantityTimer.stop();
			}
		});
		
		quantitySplitterDown = new CleanButton(ExpeditionOryxUI.BTN_SPLIT_DOWN, ExpeditionOryxUI.BTN_SPLIT_DOWN_HOVER, null, ((GFXUserInterface)UserInterface.getUI()).getHandCursor());
		quantitySplitterDown.setVisible(false);
		quantitySplitterDown.setLocation(512,243);
		
		final Action decreaseQuantityAction = new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (selectedBuilding.getQuantity() > 0)
					requiredWood -= selectedBuilding.getBuilding().getWoodCost();
				selectedBuilding.remove();
				draw(true);
			}
		};
		final Timer decreaseQuantityTimer = new Timer(100, decreaseQuantityAction);

		quantitySplitterDown.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e){
				if (selectedBuilding.getQuantity() > 0)
					requiredWood -= selectedBuilding.getBuilding().getWoodCost();
				selectedBuilding.remove();
				draw(true);
				decreaseQuantityTimer.start();
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				decreaseQuantityTimer.stop();
			}
		});
		si.add(quantitySplitterUp);
		si.add(quantitySplitterDown);
		
	}

	@Override
	public void kill() {
		super.kill();
		si.remove(quantitySplitterUp);
		si.remove(quantitySplitterDown);
		si.remove(okButton);
		//si.remove(quantityLabel);
		si.removeKeyListener(cbkl);
		si.removeMouseMotionListener(mml);
	}

	@Override
	protected Cursor getDefaultCursor() {
		return ExpeditionOryxUI.POINTER_CURSOR;
	}
	
	@Override
	protected Cursor getHandCursor() {
		return ExpeditionOryxUI.HAND_CURSOR;
	}
	
	@Override
	public int getDrawingLayer() {
		return ExpeditionOryxUI.UI_WIDGETS_LAYER;
	}
	
	@Override
	public void draw(boolean refresh) {
		super.draw(false);
		si.printAtPixel(getDrawingLayer(), 600, 238, "Available Wood", ExpeditionOryxUI.TITLE_COLOR);
		si.printAtPixel(getDrawingLayer(), 640, 238+24*1, ExpeditionGame.getCurrentGame().getExpedition().getItemCount("WOOD")+"", Color.WHITE);
		si.printAtPixel(getDrawingLayer(), 600, 238+24*2, "Required Wood", ExpeditionOryxUI.TITLE_COLOR);
		si.printAtPixel(getDrawingLayer(), 640, 238+24*3, requiredWood+"", Color.WHITE);
		if (refresh)
			si.commitLayer(getDrawingLayer(), true);
	}
}
