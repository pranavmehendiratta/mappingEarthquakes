package mappingEarthquakes;

import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PGraphics;

/** Implements a visual marker for ocean earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Pranav Mehendiratta
 *
 */
public class OceanQuakeMarker extends EarthquakeMarker {
	
	public OceanQuakeMarker(PointFeature quake) {
		super(quake);
		
		// setting field in earthquake marker
		isOnLand = false;
	}
	
	@Override
	public void drawEarthquake(PGraphics pg, float x, float y) {
		// Drawing a centered square for Ocean earthquakes
		pg.rect(x-(float)(1.5*this.getRadius()) , y-(float)(1.5*this.getRadius())
				, (float)(1.5*this.getRadius()), (float)(1.5*this.getRadius()));
		if(getAge().equals("Past Day")){
			pg.fill(0, 0, 0);
			pg.line(x-(float)(1.5*this.getRadius()) , y-(float)(1.5*this.getRadius()),
					x , y);
			pg.line(x , y-(float)(1.5*this.getRadius()),
					x-(float)(1.5*this.getRadius()) , y);
		}	
	}
}
