package mappingEarthquakes;

import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PGraphics;

/** Implements a visual marker for land earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Pranav Mehendiratta
 *
 */
public class LandQuakeMarker extends EarthquakeMarker {
	
	
	public LandQuakeMarker(PointFeature quake) {
		
		// calling EarthquakeMarker constructor
		super(quake);
		
		// setting field in earthquake marker
		isOnLand = true;
	}


	@Override
	public void drawEarthquake(PGraphics pg, float x, float y) {
		
		// Draw a centered circle for land quakes
		pg.ellipse(x, y, (float)(1.5*this.getRadius()), (float)(1.5*this.getRadius()));
		if(getAge().equals("Past Day")){
			pg.fill(0, 0, 0);
			pg.line(x-(float)(1.5*this.getRadius())/2 , y-(float)(1.5*this.getRadius())/2,
					x+(float)(1.5*this.getRadius())/2 , y+(float)(1.5*this.getRadius())/2);
			pg.line(x+(float)(1.5*this.getRadius())/2 , y-(float)(1.5*this.getRadius())/2,
					x-(float)(1.5*this.getRadius())/2 , y+(float)(1.5*this.getRadius())/2);
			}
	}
	

	// Get the country the earthquake is in
	public String getCountry() {
		return (String) getProperty("country");
	}



		
}