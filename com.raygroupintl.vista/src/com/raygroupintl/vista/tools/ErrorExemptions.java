package com.raygroupintl.vista.tools;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.vista.struct.MLineLocation;

public class ErrorExemptions {
	private Map<String, Set<MLineLocation>> exemptions = new HashMap<String, Set<MLineLocation>>();
		
	public void add(String routineName, MLineLocation lineLocation) {
		Set<MLineLocation> locations = this.exemptions.get(routineName);
		if (locations == null) {
			locations = new HashSet<>();
			this.exemptions.put(routineName, locations);
		}		
		locations.add(lineLocation);
	}
	
	public void add(String routineName, String tag, int offset) {
		this.add(routineName, new MLineLocation(tag, offset));
	}
	
	public boolean contains(String routineName, MLineLocation lineLocation) {
		Set<MLineLocation> locations = this.exemptions.get(routineName);
		if (locations != null) {
			return locations.contains(lineLocation);
		}		
		return false;	
	}

	public boolean contains(String routineName, String tag, int offset) {
		return contains(routineName, new MLineLocation(tag, offset));	
	}
	
	public Set<MLineLocation> get(String routine) {
		return this.exemptions.get(routine);
	}
}
