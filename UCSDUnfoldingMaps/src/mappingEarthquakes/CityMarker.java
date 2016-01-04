package mappingEarthquakes;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;

/** Implements a visual marker for cities on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Pranav Mehendiratta
 *
 */
public class CityMarker extends SimplePointMarker {
	
	public CityMarker(Location location) {
		super(location);
	}
	
	public CityMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
	}

	/**
	 * Implementation of method to draw marker on the map.
	 */
	public void draw(PGraphics pg, float x, float y) {
		
		// Saves previous drawing style
		pg.pushStyle();
		
		// Code to draw a triangle to represent the CityMarker
		pg.fill(255,0,0);
		pg.triangle(x, y-6, x-6, y+6, x+6, y+6);
		
		// Restores previous drawing style
		pg.popStyle();
	}
	
	/* Local getters for some city properties. 
	 *  */
	public String getCity()
	{
		return getStringProperty("name");
	}
	
	public String getCountry()
	{
		return getStringProperty("country");
	}
	
	public float getPopulation()
	{
		return Float.parseFloat(getStringProperty("population"));
	}
	
}
