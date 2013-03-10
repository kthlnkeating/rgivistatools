package com.raygroupintl.vista.tools.returntype;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.token.MVersion;
import com.raygroupintl.vista.repository.RepositoryInfo;
import com.raygroupintl.vista.repository.VistaPackage;
import com.raygroupintl.vista.tools.MRARoutineFactory;

public class RoutineLoader {
	
	RepositoryInfo repositoryInfo;
	
	private Map<String,Routine> loadedRoutines = new HashMap<String,Routine>();
	
	public RoutineLoader(MVersion version) {
		repositoryInfo = RepositoryInfo.getInstance(MRARoutineFactory.getInstance(version));
	}
	
	public void loadRoutines(Collection<String> routineNames) {
		for (String routineName: routineNames) {
			loadRoutine(routineName);
		}
	}

	public void loadRoutine(String routineName) {
		if (routineName == null || loadedRoutines.get(routineName) != null)
			return;
		
		VistaPackage vp = repositoryInfo.getPackageFromRoutineName(routineName);
		loadedRoutines.put(
				routineName, //key: routine name
				vp.getRoutineFactory().getRoutineNode(vp.getPathForRoutine(routineName)) //value: routine parse tree
				);
	}
	
	/**
	 * Returns a subset of the cache. For every valid
	 * @param keySet
	 * @return
	 */
	public Set<Routine> getSubset(Collection<String> keySet) {
		Set<Routine> subset = new HashSet<Routine>();
		for (String key : keySet) {
			subset.add(loadedRoutines.get(key));
		}
		return subset;
	}
	
	public Routine getRoutine(String routineName) {
		return loadedRoutines.get(routineName);
	}
}
