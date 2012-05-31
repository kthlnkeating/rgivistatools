package com.raygroupintl.m.parsetree.visitor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.raygroupintl.m.parsetree.Fanout;
import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.RoutinePackage;
import com.raygroupintl.m.struct.LineLocation;
import com.raygroupintl.m.token.TRoutine;

public class FanoutWriter extends FanoutRecorder {
	private final static Logger LOGGER = Logger.getLogger(FanoutWriter.class.getName());

	private FileOutputStream os;
	private String eol = TRoutine.getEOL();
	private int packageCount;
	
	@Override
	protected void visitRoutinePackage(RoutinePackage routinePackage) {
		try {
			String line = "--------------------------------------------------------------" + this.eol + this.eol;
			this.os.write(line.getBytes());
			++this.packageCount;
			this.os.write((String.valueOf(this.packageCount) + ". " + routinePackage.getPackageName() + this.eol + this.eol).getBytes());
			super.visitRoutinePackage(routinePackage);			
			this.os.write(line.getBytes());
		} catch (IOException ex) {
			LOGGER.log(Level.SEVERE, "IO Exception");
		}
	}

	@Override
	public void visitRoutine(Routine routine) {
		super.visitRoutine(routine);
		try {
			Map<LineLocation, List<Fanout>> fanouts = this.getRoutineFanouts();
			for (List<Fanout> fouts : fanouts.values()) {
				for (Fanout fout : fouts) {
					this.os.write(("    "  + fout.toString() + this.eol).getBytes());
				}
			}	
		} catch (IOException ex) {
			LOGGER.log(Level.SEVERE, "Unable to write");
		}
	}

	public void write(String outputName, List<RoutinePackage> packages) throws IOException {
		File file = new File(outputName);
		this.os = new FileOutputStream(file);
		for (RoutinePackage p : packages) {
			this.visitRoutinePackage(p);
		}
		this.os.close();
		this.os = null;
	}	
}
