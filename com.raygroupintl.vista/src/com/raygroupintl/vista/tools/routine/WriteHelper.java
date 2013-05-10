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

package com.raygroupintl.vista.tools.routine;

import java.util.Set;

import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.tool.RoutineEntryLinks;
import com.raygroupintl.m.tool.routine.fanout.RoutineEntryLinksCollection;
import com.raygroupintl.output.Terminal;
import com.raygroupintl.output.TerminalFormatter;

class WriteHelper {
	private static void write(RoutineEntryLinksCollection result, Terminal t, TerminalFormatter tf) {
		Set<String> rns = result.getRoutineNames();
		for (String rn : rns) {
			RoutineEntryLinks rfs = result.getRoutineEntryLinks(rn);
			Set<String> labels = rfs.getRoutineEntryLabels();
			for (String label : labels) {
				t.writeEOL(" " + label + "^" + rn);	
				Set<EntryId> fouts = rfs.getLinkedEntries(label);
				if ((fouts == null) || (fouts.size() == 0)) {
					t.writeEOL("  --");				
				} else {
					for (EntryId f : fouts) {
						t.writeEOL("  " + f.toString2());
					}
				}
			}
		
		}
	}
	
	public static void write(RoutineEntryLinksCollection result, Terminal t) {
		if (t.start()) {
			TerminalFormatter tf = new TerminalFormatter();
			tf.setTab(12);
			WriteHelper.write(result, t, tf);
			t.stop();
		}						
		
	}
}
