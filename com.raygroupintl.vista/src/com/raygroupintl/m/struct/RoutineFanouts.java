package com.raygroupintl.m.struct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RoutineFanouts {
	private String routineName;
	private Map<LineLocation, List<Fanout>> fanouts; 

	public RoutineFanouts(String routineName) {
		this.routineName = routineName;
	}
	
	public void add(LineLocation location, Fanout fanout) {
		if (this.fanouts == null) {
			this.fanouts = new HashMap<LineLocation, List<Fanout>>();
		} 		
		List<Fanout> foutsAtLocation = this.fanouts.get(location);
		if (foutsAtLocation == null) {
			foutsAtLocation = new ArrayList<Fanout>();
			this.fanouts.put(location, foutsAtLocation);
		}
		foutsAtLocation.add(fanout);
	}
	
	public String getRoutineName() {
		return this.routineName;
	}
	
	public Set<LineLocation> getFanoutLocations() {
		if (this.fanouts == null) {
			return Collections.emptySet();
		} else {
			return this.fanouts.keySet();
		}
	}
	
	public List<Fanout> getFanouts(LineLocation location) {
		if (this.fanouts != null) {
			List<Fanout> result = this.fanouts.get(location);
			if (result != null) {
				return result;
			}
		}
		return Collections.emptyList();
	}
}
