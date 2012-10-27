//---------------------------------------------------------------------------
// Copyright 2012 Ray Group International
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//---------------------------------------------------------------------------

package com.raygroupintl.vista.tools;

import java.io.IOException;
import java.nio.file.Path;

import com.raygroupintl.m.parsetree.ErrorNode;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.struct.MError;
import com.raygroupintl.m.token.MRoutine;
import com.raygroupintl.m.token.MTFSupply;
import com.raygroupintl.m.token.MVersion;
import com.raygroupintl.m.token.TFRoutine;
import com.raygroupintl.parser.SyntaxErrorException;
import com.raygroupintl.parsergen.ParseException;
import com.raygroupintl.vista.repository.RoutineFactory;

public class MRARoutineFactory implements RoutineFactory {
	private TFRoutine tokenFactory;
	
	public MRARoutineFactory(TFRoutine tokenFactory) {
		this.tokenFactory = tokenFactory;
	}
	
	@Override
	public Node getNode(Path path) {
		try {
			MRoutine mr = this.tokenFactory.tokenize(path);
			Routine node = mr.getNode();
			return node;
		} catch (SyntaxErrorException e) {
			return new ErrorNode(MError.ERR_BLOCK_STRUCTURE);
		} catch (IOException e) {
			return new ErrorNode(MError.ERR_ROUTINE_PATH);
		}
	}	
	
	@Override
	public Routine getRoutineNode(Path path) {
		try {
			MRoutine mr = this.tokenFactory.tokenize(path);
			Routine node = mr.getNode();
			return node;
		} catch (SyntaxErrorException e) {
			MError err = new MError(MError.ERR_BLOCK_STRUCTURE);
			MRALogger.logError(err.getText());
		} catch (IOException e) {
			MError err = new MError(MError.ERR_ROUTINE_PATH);
			MRALogger.logError(err.getText());
		}
		return null;
	}	
	
	public static MRARoutineFactory getInstance(MVersion version) {
		try {
			MTFSupply supply = MTFSupply.getInstance(version);
			TFRoutine tf = new TFRoutine(supply);
			return new MRARoutineFactory(tf);
		} catch (ParseException e) {
			MRALogger.logError("Unable to load M parser definitions.");
			return null;
		}
	}
}

