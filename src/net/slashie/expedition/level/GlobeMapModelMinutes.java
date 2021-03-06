package net.slashie.expedition.level;

import java.util.HashMap;
import java.util.Map;

import net.slashie.utils.Position;

public class GlobeMapModelMinutes extends GlobeModel{
	private final static int LONGITUDE_EQUATOR_NAUTICAL_MILES = 60; 
	
	// Note: This is calibrated for the 8colorsAmericaAndAtlantic map
	@Override
	public int transformLongIntoX(int longMinutes){
		return (int)Math.round((double)longMinutes * 0.3324d) + 3377; 
	}

	// Note: This is calibrated for the 8colorsAmericaAndAtlantic map
	@Override
	public int transformLatIntoY(int latMinutes){
		return (int)Math.round((double)latMinutes * -0.3338d) + 1580; 
	}
	
	/**
	 * Steps left and right are 3 minutes at equator, and more minutes as aproaching the poles
	 */
	private static Map<Integer, Integer> _getLongitudeScaleCache = new HashMap<Integer, Integer>();
	@Override
	public int getLongitudeScale(int latitudeMinutes) {
		double latitudeDegrees = latitudeMinutes / 60.0d;
		Integer ret = _getLongitudeScaleCache.get(latitudeMinutes); 
		if (ret == null){
			//ret = (int) Math.floor(3.0d / Math.cos(latitudeDegrees * (Math.PI / 180.0d)));
			ret = (int) Math.round(3.0d / Math.cos(latitudeDegrees * (Math.PI / 180.0d)));
			_getLongitudeScaleCache.put(latitudeMinutes, ret);
		}
		return ret;
	}
	
	@Override
	public int transformYIntoLat(int y) {
		return (int)Math.floor((double)(y-1580)/ -0.3338d);
	}

	@Override
	public int transformXIntoLong(int x) {
		return (int)Math.floor((double)(x - 3377)/ 0.3324d);
	}

	@Override
	public int getLatitudeHeight() {
		return 3;
	}

	/**
	 * Transform a longitude minutes value into a distance in nautical miles
	 * @param q
	 * @return
	 */
	@Override	
	public double transformLongIntoNauticalMiles(int latitudeMinutesPosition, int longitudeMinutesValue) {
		double latitudeDegrees = latitudeMinutesPosition / 60.0d;
		double nauticalMiles = Math.cos(latitudeDegrees * (Math.PI / 180.0d));
		nauticalMiles *= LONGITUDE_EQUATOR_NAUTICAL_MILES;
		nauticalMiles /= 60.0d;
		nauticalMiles *= longitudeMinutesValue;
		return nauticalMiles;
	}

	@Override
	public boolean isValidCoordinate(int longMinutes, int latMinutes) {
		return ! (longMinutes <= -180*60 ||
				latMinutes <= -90 * 60  ||
				longMinutes >= 180 * 60 ||
				latMinutes >= 90*60);
	}
	
	@Override
	public int getLongitudeDegrees(int longitude) {
		return (int)Math.round(longitude / 60.0d);
	}
	
	@Override
	public int getLatitudeDegrees(int latitude) {
		return (int)Math.round(latitude / 60.0d);
	}
	
	@Override
	public int getMilesDistance(Position position, Position position2) {
		// TODO Use a real long distance calculator, for now use a ver approximate flat distance :P
		int longDistance = position.x - position2.x;
		double longitudeMiles = transformLongIntoNauticalMiles(position.y, longDistance);
		int latDistance = position.y - position2.y;
		latDistance *= LONGITUDE_EQUATOR_NAUTICAL_MILES;
		return (int) Math.sqrt(Math.pow(longitudeMiles,2) + Math.pow(latDistance,2));
	}
}
