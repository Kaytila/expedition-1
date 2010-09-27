package net.slashie.expedition.locations;

import net.slashie.serf.level.Unleasher;
import net.slashie.serf.levelGeneration.StaticPattern;

public class Spain extends StaticPattern {

	public Spain () {
		
		this.cellMap = new String [][]{{
			"---------------------------------------------------....................",
			"-----------------------------------------------........h...............",
			"--------------------------------------------...................h.......",
			"--------------------------------v--------,,###......h..................",
			"---------------------------------------,,,,,,####......................",
			"--------------------------v---------hhhhhhhhh,,,###........hh..........",
			"---------------------------------v--h3h1h2hhh,h,,,####X.......,........",
			"--------------------v--v---------6==,,,,,,,,,,,,,,,,,7X.........h......",
			"---------------------------,,,------,,h4hhh,hh5h,,,,######.............",
			"----------------------===,,,,hhh,h,,,,,,,,,,,hhh,,h,,,,,,####..........",
			"------------v---------,,,,,,,,,,,,,hhhh,,hh,,,hhh,h,,h,,,...###........",
			"---------------v----hhh,h,,,,,,,hhhhhh,,,hh,,,,,hh,hh,,,,,....##.......",
			"------------------,hhh,,,,,,,hh,hhhh,h,,,hh,,h,h,,,,,,,h,,,....###.....",
			"---------------hhh,,,,,,h,h,,,h,hhh,h,,,h....hhh,,,,,hhhhhh,,h.hh#.....",
			"-----------...##h,,,,,,,hhh,h,h,...,,,,hh....h,,,,h,,hhhh,,,h..hh#.....",
			"--------.......##,h,,,h,hhh,,,,,hhhhh,,,,,....,,hhh,hhhh,,,hhhhh##.....",
			"-------.....h...##h,hhh,hhhhh,,hhhhhhh,,,hhhh,,hhhhh,,,,,,#######......",
			"----.............#,,,,,,hhhhh,,hhhh,,,h,,,hhhh,,,,,,,,hhh##............",
			"---.....h........######,,,,,,,,,,,,,,,,,,,,,,,,,,,,,hhhh##.............",
			"-..............h......########,,h,#####-------####hhhh###......h,......",
			".....h.......................######.....-----....######........h.......",
			"...........h...h.......h..............--------.........................",
			".....................h..h..............-------....h....................",
			".........h.............h..h..............................h.............",
			".........h.......................h.....................................",
			"...............h.........................h.............................",
			".......................................................h...............",
			".............................h.........................................",
			".......................................................................",
			".......................................................................",

			
			/*
			"----------------................................h................................",
			"----------------..................................................h..............",
			"----------------.................................................................",
			"----------------.....................h...........................................",
			"----------------.........................................h.......................",
			"----------------.........h.....................................h.................",
			"----------------................................h..............h...............h.",
			"----------------............h....................................................",
			"----------------................................h..............h.......h.........",
			"-------------....h..............h................................................",
			"-------------..........................h............h............................",
			"-----------------..h...###..............................###......................",
			"---------------------..####################################.......h..............",
			"-----------------------##,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,##......................",
			"-------------------------,hh,hh,,hh,hh,,hhh,hhh,,hhh,hhh,#,......................",
			"-----------------==------,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,#,,,....................",
			"-------------v----=--....,,.h1,h,hh,2h,,h4h,h5h,,,...h.,.........................",
			"-------------6=======..,,,,,,,,,,,,,,,,,,,,,,,,,,,,S,,,,,7,,,,,,,,,,........h....",
			"------------------=---...-hh,h3hh,hhh,hhhh,,hh.hh,.....,##.........h.............",
			"----------------===----.....,,,,,,,,,,,,,,,,,.......h,,,,#,,,,,.,................",
			"---------------------......h-----..hh,,hhh..h.h..hh,...,,#.......................",
			"-----------------...----##...............................#.......................",
			"-------------------....-###################################...h..................",
			"-------------...........##.................,,,,,,,......###...,,,,,..............",
			"-----.-----...............................h..,,,,,,,,..,,,,,,,,,,,,,,,,,,,,,,,,,.",
			"----------......................................,,,,,,,,,,............h..........",
			"-------.---...................h..........................h.......................",
			"-------..........................................................................",
			"--..............................................h................................",
			"....................................h............................................",*/
			
			
		}};
		
/*		this.cellMap = new String [][]{{
			"..----.................................................................",
			"..----.................................................................",
			".-----.................................................................",
			".---.-----.............................................................",
			"..----.................................................................",
			"..----.................................................................",
			".-----....................................................####.........",
			".---.-----...........-------------------------------...####..####......",
			".---.------........---------------------------------####........#......",
			".---..-------..--------####################--------##--...hhhhh,##.....",
			".---...----------#######,,,,,,,,,,,,,,,,,,####-----##,,,..hhhhh,,#.....",
			".---....-----#####,,,,,,,hhhhhhh,........,,,#-----###,,hhhhhhhh,,####..",
			".---....---###,,,,hhh,,hhhhhhhhh,,hhhhhhh,,,#######,,,,hhhhhhhh,,,,,#..",
			"..---...####,,,,hhhhh,,hhhhhhhh..,hhhhhhhh,,,,,S,,,,,,,,,,7hhhh,,,###..",
			"..----###,,,,,hhhhhhh,,hhhhhh,.hhhhhhh,,,,,,#######,,,,hhhhhhhh,,##....",
			"..---##,,,,hhhhhh3hhh,,hhh1h,,,h5hh,,,,,,,,,#-----##,,,hhhhhhhh###.....",
			"...--#,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,hhh,,hh,#------#,,,,,,hhhhh,#......",
			"...--#,,hhhhh,h4hhhhhh2hhh,,,hhhhhhhh,,,hhh,#------##,,,,,hhhhh,#......",
			"....-#,,hhhhh,hhhhhhhhhhhhhh,,hhhhhh,,,,,hh,#------.####,,,,,,,,#......",
			".----#,,hhhh,,hhhhhhhhhhhhhh,,hhhhhh,,hhhhh,#######....####,,####......",
			"..---#,,hhhh,,hhhhhhhhhhhhhh,,,hhhhh,,,,,hh,#------.......####.........",
			"..---#,,,hhh,,hhhhhhhhhhhhhh,,,hhhhhhhh,,hh,#------....................",
			".----###,,,,,,hhhhhhhhhhhhhh,,,,,,,,,,,,,,,,#------....................",
			"..-----##,,,,,hhhhhhhhhhhh,,,...,,,,,,,,,,,,=====6---..................",
			"...-----###,,,,,,,,,,,,,,,,,,,,,,,###########----v-----................",
			"..--------#########################----------------------..............",
			"..---------------------------------------------------------............",
			"...-----------------------------------------------------------.........",
			"----------------------------------------------------------------.......",
			"-----------------------------------------------------------------......",
			"...----------------------------------------------------------------....",
			"...........................................................------------",
			".............................................................----------",
			"-----------------------------------------------------------------------",
		}};*/
		
		charMap.put(".", "SPAIN_GRASS");
		charMap.put("X", "SPAIN_GRASS_BLOCKED");
		charMap.put(",", "SPAIN_FLOOR");
		charMap.put("-", "SPAIN_WATER");
		charMap.put("#", "SPAIN_WALL");
		charMap.put("h", "SPAIN_HOUSE");
		charMap.put("v", "SPAIN_WATER ITEM CARRACK");
		charMap.put("=", "SPAIN_DOCKS");
		charMap.put("a", "SPAIN_CASTLE");
		charMap.put("c", "SPAIN_COLUMN");
		charMap.put("1", "GOODS_STORE");
		charMap.put("2", "WEAPONS_STORE");
		charMap.put("3", "PORT");
		charMap.put("4", "MERCHANT");
		charMap.put("5", "GUILD");
		charMap.put("6", "DEPARTURE EXIT WORLD");
		charMap.put("7", "CASTLE_GATE EXIT SPAIN_CASTLE");
		charMap.put("S", "SPAIN_FLOOR EXIT _START");
		charMap.put("E", "GRASS EXPEDITION NATIVE_EXPEDITION");

		unleashers = new Unleasher[]{};
	}

	@Override
	public String getDescription() {
		return "Palos, Andalusia";
	}

}
