package net.slashie.expedition.ui.oryx;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JTextArea;

import net.slashie.libjcsi.CharKey;

import net.slashie.expedition.domain.Expedition;
import net.slashie.expedition.game.ExpeditionGame;
import net.slashie.expedition.ui.ExpeditionDisplay;
import net.slashie.serf.game.SworeGame;
import net.slashie.serf.ui.UserInterface;
import net.slashie.serf.ui.oryxUI.AddornedBorderTextArea;
import net.slashie.serf.ui.oryxUI.SwingSystemInterface;
import net.slashie.utils.ImageUtils;
import net.slashie.utils.PropertyFilters;

public class OryxExpeditionDisplay extends ExpeditionDisplay{
	private SwingSystemInterface si;
	
	private String IMG_TITLE;  
	public static Font FNT_TEXT;
	public static Font FNT_TITLE;
	public static Font FNT_DIALOGUEIN;
	public static Font FNT_MONO;
	private static BufferedImage IMG_PICKER;
	private static BufferedImage IMG_BORDERS;
	
	public static Color COLOR_BOLD;
	
	private void initProperties(Properties p){
		IMG_TITLE = p.getProperty("IMG_TITLE");
		COLOR_BOLD = PropertyFilters.getColor(p.getProperty("COLOR_BOLD"));
		
		try {
			IMG_PICKER = PropertyFilters.getImage(p.getProperty("IMG_PICKER"), p.getProperty("IMG_PICKER_BOUNDS"));
			IMG_BORDERS = PropertyFilters.getImage(p.getProperty("IMG_BORDERS"), p.getProperty("IMG_BORDERS_BOUNDS"));
			FNT_TITLE = PropertyFilters.getFont(p.getProperty("FNT_TITLE"), p.getProperty("FNT_TITLE_SIZE"));
			FNT_TEXT = PropertyFilters.getFont(p.getProperty("FNT_TEXT"), p.getProperty("FNT_TEXT_SIZE"));
			FNT_DIALOGUEIN  = FNT_TEXT;
			FNT_MONO = PropertyFilters.getFont(p.getProperty("FNT_MONO"), p.getProperty("FNT_MONO_SIZE"));
		} catch (FontFormatException ffe){
			SworeGame.crash("Error loading the font", ffe);
		} catch (IOException ioe){
			SworeGame.crash("Error loading the font", ioe);
		} catch (Exception e){
			SworeGame.crash("Error loading images", e);
		}
	}
	
	private AddornedBorderTextArea addornedTextArea;

	public OryxExpeditionDisplay(SwingSystemInterface si, Properties p){
		initProperties(p);
		this.si = si;
		try {
			//BufferedImage BORDERS = ImageUtils.createImage(IMG_BORDERS);
			int tileSize = PropertyFilters.inte(p.getProperty("TILE_SIZE"));
			BufferedImage b1 = ImageUtils.crearImagen(IMG_BORDERS, 34,1,tileSize,tileSize);
			BufferedImage b2 = ImageUtils.crearImagen(IMG_BORDERS, 1,1,tileSize,tileSize);
			BufferedImage b3 = ImageUtils.crearImagen(IMG_BORDERS, 100, 1, tileSize,tileSize);
			BufferedImage b4 = ImageUtils.crearImagen(IMG_BORDERS, 67,1,tileSize,tileSize);
			addornedTextArea = new AddornedBorderTextArea(
					b1,
					b2,
					b3,
					b4,
					new Color(187,161,80),
					new Color(92,78,36),
					tileSize, tileSize);
			addornedTextArea.setVisible(false);
			addornedTextArea.setEnabled(false);
			addornedTextArea.setForeground(Color.WHITE);
			addornedTextArea.setBackground(Color.BLACK);
			addornedTextArea.setFont(FNT_DIALOGUEIN);
			addornedTextArea.setOpaque(false);
		}
		 catch (Exception e){
			 SworeGame.crash("Error loading UI data", e);
		 }
		 si.add(addornedTextArea);
	}
	
	public int showTitleScreen(){
		((ExpeditionOryxUI)UserInterface.getUI()).messageBox.setVisible(false);
		((ExpeditionOryxUI)UserInterface.getUI()).persistantMessageBox.setVisible(false);
		si.setFont(FNT_TEXT);
		si.drawImage(IMG_TITLE);
		//si.drawImage(215,60,IMG_TITLE_NAME);
		si.printAtPixel(220, 555, "Expedition v"+ExpeditionGame.getVersion()+", Developed by Santiago Zapata 2009-2010", Color.WHITE);
		si.printAtPixel(285, 570, "Artwork by Christopher Barrett, 2010", Color.WHITE);
		si.printAtPixel(220, 585, "Music by Dominik Markzuk and Anonymous MIDI Composers", Color.WHITE);
		CharKey x = new CharKey(CharKey.NONE);
    	int choice = 0;
    	si.saveBuffer();
    	out: while (true) {
    		si.restore();
    		si.drawImage(294, 354+choice*20, IMG_PICKER);
    		si.printAtPixel(362,368, "a. Create Expedition", Color.WHITE);
    		si.printAtPixel(344,388, "b. Resume Expedition", Color.WHITE);
    		si.printAtPixel(350,408, "c. Quit", Color.WHITE);
    		si.refresh();
			while (x.code != CharKey.A && x.code != CharKey.a &&
					x.code != CharKey.B && x.code != CharKey.b &&
					x.code != CharKey.C && x.code != CharKey.c &&
					x.code != CharKey.UARROW && x.code != CharKey.DARROW &&
					x.code != CharKey.SPACE && x.code != CharKey.ENTER)
				x = si.inkey();
			switch (x.code){
			case CharKey.A: case CharKey.a:
				return 0;
			case CharKey.B: case CharKey.b:
				return 1;
			case CharKey.C: case CharKey.c:
				return 2;
			case CharKey.D: case CharKey.d:
				return 3;
			case CharKey.E: case CharKey.e:
				return 4;
			case CharKey.F: case CharKey.f:
				return 5;
			case CharKey.G: case CharKey.g:
				return 6;
			case CharKey.UARROW:
				if (choice > 0)
					choice--;
				break;
			case CharKey.DARROW:
				if (choice < 6)
					choice++;
				break;
			case CharKey.SPACE: case CharKey.ENTER:
				return choice;
			}
			x.code = CharKey.NONE;
		}
	}
	
	public void showIntro(Expedition e){
	}
	

	public void showHelp(){
		si.saveBuffer();
		si.cls();
		si.print(6, 1, " == Commands ==", Color.CYAN);
		si.print(6, 3, "  /---\\     ", Color.WHITE);
		si.print(6, 4, "  |789|  Move Around using the numpad or", Color.WHITE);
		si.print(6, 5, "  |4 6|  The directional keys.", Color.WHITE);
		si.print(6, 6, "  |123|", Color.WHITE);
		si.print(6, 7, "  \\---/", Color.WHITE);
		si.print(6, 8, "  ", Color.WHITE);
		si.print(6, 9, " == In the overworld ==", Color.CYAN);
		si.print(6,10, "  ", Color.WHITE);
		si.print(6,11, "  a: Arm / Disarm expedition", Color.WHITE);
		si.print(6,12, "  b: Build a Settlement", Color.WHITE);
		si.print(6,13, "  d: Create Equipment Cach�", Color.WHITE);
		si.print(6,14, "  f: Fire Ranged Attack", Color.WHITE);
		si.print(6,15, "  i: Show inventory", Color.WHITE);
		si.print(6,15, "  l: Look around", Color.WHITE);
		si.print(6,16, "  r: Repair damaged ships", Color.WHITE);
		si.print(6,17, "  R: Reset dead' reckon counter", Color.WHITE);
		si.print(6,18, "  S: Save Game", Color.WHITE);
		si.print(6,19, "  Q: Quit", Color.WHITE);
		
		si.print(6,21, "  ", Color.WHITE);
		si.print(6,22, "  Press Space to continue", Color.CYAN);
		si.refresh();

		si.waitKey(CharKey.SPACE);
		   
		si.restore();
		si.refresh();
	}
	
	public void init(SwingSystemInterface syst){
		si = syst;
	}
	
	
	public int showSavedGames(File[] saveFiles){
		si.drawImage(IMG_TITLE);
		if (saveFiles == null || saveFiles.length == 0){
			si.print(3,6, "No adventurers available",Color.WHITE);
			si.print(4,8, "[Space to Cancel]",Color.WHITE);
			si.refresh();
			si.waitKey(CharKey.SPACE);
			return -1;
		}
			
		si.print(3,6, "Pick an adventurer",Color.WHITE);
		for (int i = 0; i < saveFiles.length; i++){
			String saveFileName = saveFiles[i].getName();
			si.print(5,7+i, (char)(CharKey.a+i+1)+ " - "+ saveFileName.substring(0,saveFileName.indexOf(".sav")), COLOR_BOLD);
		}
		si.print(3,9+saveFiles.length, "[Space to Cancel]", Color.WHITE);
		si.refresh();
		CharKey x = si.inkey();
		while ((x.code < CharKey.a || x.code > CharKey.a+saveFiles.length-1) && x.code != CharKey.SPACE){
			x = si.inkey();
		}
		if (x.code == CharKey.SPACE)
			return -1;
		else
			return x.code - CharKey.a;
	}
	
	
	public void showTextBox(String text, int consoleX, int consoleY, int consoleW, int consoleH){
		addornedTextArea.setBounds(consoleX, consoleY, consoleW, consoleH);
		addornedTextArea.setText(text);
		addornedTextArea.setVisible(true);
		si.waitKey(CharKey.SPACE);
		addornedTextArea.setVisible(false);
	}
	
	public void showTextBox(String title, String text, int consoleX, int consoleY, int consoleW, int consoleH){
		showTextBox (title+" "+text, consoleX, consoleY, consoleW, consoleH);
	}
	
	public void showTextBoxNoWait(String text, int consoleX, int consoleY, int consoleW, int consoleH){
		addornedTextArea.setBounds(consoleX, consoleY, consoleW, consoleH);
		addornedTextArea.setText(text);
		addornedTextArea.setVisible(true);
	}
	
	public void clearTextBox(){
		addornedTextArea.setVisible(false);	
	}
	
	public boolean showTextBoxPrompt(String text, int consoleX, int consoleY, int consoleW, int consoleH){
		addornedTextArea.setBounds(consoleX, consoleY, consoleW, consoleH);
		addornedTextArea.setText(text);
		addornedTextArea.setVisible(true);
		CharKey x = new CharKey(CharKey.NONE);
		while (x.code != CharKey.Y && x.code != CharKey.y && x.code != CharKey.N && x.code != CharKey.n)
			x = si.inkey();
		boolean ret = (x.code == CharKey.Y || x.code == CharKey.y);
		addornedTextArea.setVisible(false);
		return ret;
	}
	
	public void showTextBox(String text, int consoleX, int consoleY, int consoleW, int consoleH, Font f){
		addornedTextArea.setBounds(consoleX, consoleY, consoleW, consoleH);
		addornedTextArea.setText(text);
		addornedTextArea.setFont(f);
		addornedTextArea.setVisible(true);
		si.waitKey(CharKey.SPACE);
		addornedTextArea.setVisible(false);
	}

	private int readAlphaToNumber(int numbers){
		while (true){
			CharKey key = si.inkey();
			if (key.code >= CharKey.A && key.code <= CharKey.A + numbers -1){
				return key.code - CharKey.A;
			}
			if (key.code >= CharKey.a && key.code <= CharKey.a + numbers -1){
				return key.code - CharKey.a;
			}
		}
	}
	
	//private Color TRANSPARENT_BLUE = new Color(100,100,100,200);
	
	public void showScreen(Object pScreen){
		si.saveBuffer();
		String screenText = (String) pScreen;
		showTextBox(screenText, 430, 70,340,375);
		//si.waitKey(CharKey.SPACE);
		si.restore();
	}

	public static JTextArea createTempArea(int xpos, int ypos, int w, int h){
		JTextArea ret = new JTextArea();
		ret.setOpaque(false);
		ret.setForeground(Color.WHITE);
		ret.setVisible(true);
		ret.setEditable(false);
		ret.setFocusable(false);
		ret.setBounds(xpos, ypos, w, h);
		ret.setLineWrap(true);
		ret.setWrapStyleWord(true);
		ret.setFont(FNT_TEXT);
		return ret;
	}
	
	
	
}