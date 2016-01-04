package mappingEarthquakes;

import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Pranav  Mehendiratta
 * Date: January 3, 2016
 * */
public class EarthquakeCityMap extends PApplet {
	
	// You can ignore this.  It's to get rid of eclipse warnings
	private static final long serialVersionUID = 1L;

	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	
	// The files containing city names and info and country names and info
	private String cityFile = "city-data.json";
	private String countryFile = "countries.geo.json";
	
	// The map
	private UnfoldingMap map;
	
	// Markers for each city
	private List<Marker> cityMarkers;
	
	// Markers for each earthquake
	private List<Marker> quakeMarkers;

	// A List of country markers
	private List<Marker> countryMarkers;
	
	public void setup() {		
		// Initializing canvas and map tiles
		size(900, 700, OPENGL);
		map = new UnfoldingMap(this, 200, 50, 650, 600, new Google.GoogleMapProvider());
		
		// Giving user the ability to pan in and out(making map more interactive)
		MapUtils.createDefaultEventDispatcher(this, map);
		
		// Reading in earthquake data and geometric properties
	    // loading country features and markers
		List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		
		// reading in city data
		List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
		cityMarkers = new ArrayList<Marker>();
		
		for(Feature city : cities) {
		  cityMarkers.add(new CityMarker(city));
		}
	    
		// reading in earthquake RSS feed
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    quakeMarkers = new ArrayList<Marker>();
	    
	    for(PointFeature feature : earthquakes) {
		 
	      //check if LandQuake
		  if(isLand(feature)) {
		    quakeMarkers.add(new LandQuakeMarker(feature));
		  }
		  
		  // OceanQuakes
		  else {
		    quakeMarkers.add(new OceanQuakeMarker(feature));
		  }
	    }

	    // Using for debugging
	    printQuakes();
	 		
	    // Adding markers to map
	    map.addMarkers(quakeMarkers);
	    map.addMarkers(cityMarkers);
	    
	}  // End setup
	
	
	public void draw() {
		background(0);
		map.draw();
		addKey();
		
	}
	
	// helper method to draw key in GUI
	private void addKey() {	
		
		// Creating the box for key
		fill(255, 250, 240);
		rect(25, 50, 150, 250);
		
		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("Earthquake Key", 50, 75);
		
		fill(255, 0, 0);
		triangle(58, 90, 50, 102, 66, 102);
		fill(0);
		textSize(12);
		text("City Marker", 75, 96);
		
		fill(255, 255, 255);
		ellipse(58, 115, 12, 12);
		fill(0);
		textSize(12);
		text("Land Quake", 75, 115);
		
		fill(255, 255, 255);
		rect(53 ,130 ,10 ,10);
		fill(0);
		textSize(12);
		text("Ocean Quake", 75, 135);
		text("Size ~ Magnitude", 50, 155);
		
		fill(255, 255, 0);
		ellipse(58, 180, 12, 12);
		fill(0);
		textSize(12);
		text("Shallow", 75, 180);
		
		fill(0, 0, 255);
		ellipse(58, 200, 12, 12);
		fill(0);
		textSize(12);
		text("Intermediate", 75, 200);
		
		fill(255, 0, 0);
		ellipse(58, 220, 12, 12);
		fill(0);
		textSize(12);
		text("Deep", 75, 220);
	}

	// Checking whether this quake occurred on land. If it did, it sets the 
	// "country" property of its PointFeature to the country where it occurred
	// and returns true. 
	private boolean isLand(PointFeature earthquake) {
		
		for(Marker country : countryMarkers){
			if(isInCountry(earthquake, country))
				return true;
		}
		
		return false;
	}
	
	// prints countries with number of earthquakes
	private void printQuakes() 
	{
		System.out.println(quakeMarkers.size());
		int totalNumOfOceanQuakes= 0;
		int totalNumOfLandQuakes= 0;
		for(Marker country : countryMarkers){
			int numOfLandQuakes = 0;
			for(Marker landQuake : quakeMarkers){
				if(landQuake instanceof LandQuakeMarker){
					if(country.getProperty("name").equals
							(((LandQuakeMarker) landQuake).getCountry())){
						numOfLandQuakes++;				
					}
				}
			}
			System.out.println(country.getProperty("name") + " : " + numOfLandQuakes);
			totalNumOfLandQuakes += numOfLandQuakes;
		}
		System.out.println("Land Quakes : " + totalNumOfLandQuakes);
		totalNumOfOceanQuakes = quakeMarkers.size() - totalNumOfLandQuakes;
		System.out.println("Ocean Quakes : " + totalNumOfOceanQuakes);
	}
	
	// helper method to test whether a given earthquake is in a given country
	// This will also add the country property to the properties of the earthquake 
	// feature if it's in one of the countries.
	private boolean isInCountry(PointFeature earthquake, Marker country) {
		
		// getting location of feature
		Location checkLoc = earthquake.getLocation();

		// some countries represented it as MultiMarker
		// looping over SimplePolygonMarkers which make them up to use isInsideByLoc
		if(country.getClass() == MultiMarker.class) {
				
			// looping over markers making up MultiMarker
			for(Marker marker : ((MultiMarker)country).getMarkers()) {
					
				// checking if inside
				if(((AbstractShapeMarker)marker).isInsideByLocation(checkLoc)) {
					earthquake.addProperty("country", country.getProperty("name"));
						
					// return if is inside one
					return true;
				}
			}
		}
			
		// check if inside country represented by SimplePolygonMarker
		else if(((AbstractShapeMarker)country).isInsideByLocation(checkLoc)) {
			earthquake.addProperty("country", country.getProperty("name"));
			
			return true;
		}
		return false;
	}

}
