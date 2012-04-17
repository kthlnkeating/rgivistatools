package com.raygroupintl.vista.tools;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.vista.struct.MLineLocation;

public class ErrorExemptions {
	private Map<String, Set<MLineLocation>> lines;
	private Set<String> routines;
	
	public void addLine(String routineName, MLineLocation lineLocation) {
		if (this.lines == null) {
			this.lines =  new HashMap<String, Set<MLineLocation>>();
		}
		Set<MLineLocation> locations = this.lines.get(routineName);
		if (locations == null) {
			locations = new HashSet<>();
			this.lines.put(routineName, locations);
		}		
		locations.add(lineLocation);
	}
	
	public void addLine(String routineName, String tag, int offset) {
		this.addLine(routineName, new MLineLocation(tag, offset));
	}
	
	public void addRoutine(String routineName) {
		if (this.routines == null) {
			this.routines =  new HashSet<String>();
		}
		this.routines.add(routineName);
	}
	
	public boolean containsLine(String routineName, MLineLocation lineLocation) {
		if (this.lines != null) {
			Set<MLineLocation> locations = this.lines.get(routineName);
			if (locations != null) {
				return locations.contains(lineLocation);
			}
		}
		return false;	
	}

	public boolean containsLine(String routineName, String tag, int offset) {
		return containsLine(routineName, new MLineLocation(tag, offset));	
	}
	
	public boolean containsRoutine(String routineName) {
		return (this.routines != null) && this.routines.contains(routineName);	
	}
	
	public Set<MLineLocation> getLines(String routine) {
		return this.lines.get(routine);
	}
}
