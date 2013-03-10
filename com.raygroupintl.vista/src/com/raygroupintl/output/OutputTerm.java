package com.raygroupintl.output;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OutputTerm extends Terminal {

	private final static Logger LOGGER = Logger.getLogger(OutputTerm.class.getName());
	private String eol = Utility.getEOL();
	
	OutputStream outputStream;
	
	public OutputTerm(OutputStream outputStream) {
		super();
		
		this.outputStream = outputStream;
	}

	@Override
	public boolean write(String data) {
		try {
			if (this.outputStream != null) {
				this.outputStream.write(data.getBytes());
				return true;
			} else {
				LOGGER.log(Level.SEVERE, "Attempt to write to stream");												
			}
		}  catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error writing to stream");						
		}
		return false;
	}

	@Override
	public boolean writeEOL() {
		return this.write(this.eol);
	}

}
