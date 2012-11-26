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

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.raygroupintl.output.FileWrapper;
import com.raygroupintl.vista.repository.RepositoryInfo;
import com.raygroupintl.vista.repository.VistaPackage;
import com.raygroupintl.vista.repository.VistaPackages;
import com.raygroupintl.vista.repository.visitor.CacheOccuranceWriter;
import com.raygroupintl.vista.repository.visitor.DTFilemanCallWriter;
import com.raygroupintl.vista.repository.visitor.DTUsedGlobalWriter;
import com.raygroupintl.vista.repository.visitor.DTUsesGlobalWriter;
import com.raygroupintl.vista.repository.visitor.EntryWriter;
import com.raygroupintl.vista.repository.visitor.ErrorWriter;
import com.raygroupintl.vista.repository.visitor.FaninWriter;
import com.raygroupintl.vista.repository.visitor.FanoutWriter;
import com.raygroupintl.vista.repository.visitor.OptionWriter;
import com.raygroupintl.vista.repository.visitor.RPCWriter;
import com.raygroupintl.vista.repository.visitor.SerializedRoutineWriter;

public class RepositoryTools extends Tools {
	private static class Fanout extends Tool {		
		public Fanout(CLIParams params) {
			super(params);
		}
		
		@Override
		public void run() {
			FileWrapper fr = this.getOutputFile();
			if (fr != null) {
				RepositoryInfo ri = this.getRepositoryInfo();
				if (ri != null) {
					VistaPackages vps = this.getVistaPackages(ri);
					if (vps != null) {						
						FanoutWriter fow = new FanoutWriter(fr, ri);
						vps.accept(fow);
					}
				}
			}
		}
	}

	private static class Fanin extends Tool {		
		public Fanin(CLIParams params) {
			super(params);
		}
		
		@Override
		public void run() {
			FileWrapper fr = this.getOutputFile();
			if (fr != null) {
				RepositoryInfo ri = this.getRepositoryInfo();
				if (ri != null) {
					VistaPackages vps = this.getVistaPackages(ri);
					if (vps != null) {						
						FaninWriter fow = new FaninWriter(ri, fr, this.params.rawFormat);
						vps.accept(fow);
					}
				}
			}
		}
	}
		
	private static class Option extends Tool {		
		public Option(CLIParams params) {
			super(params);
		}
		
		@Override
		public void run() {
			FileWrapper fr = this.getOutputFile();
			if (fr != null) {
				RepositoryInfo ri = this.getRepositoryInfo();
				if (ri != null) {
					VistaPackages vps = this.getVistaPackages(ri);
					if (vps != null) {						
						OptionWriter ow = new OptionWriter(ri, fr);
						ow.write(vps);
					}
				}
			}
		}
	}
			
	private static class RPC extends Tool {		
		public RPC(CLIParams params) {
			super(params);
		}
		
		@Override
		public void run() {
			FileWrapper fr = this.getOutputFile();
			if (fr != null) {
				RepositoryInfo ri = this.getRepositoryInfo();
				if (ri != null) {
					VistaPackages vps = this.getVistaPackages(ri);
					if (vps != null) {						
						RPCWriter rw = new RPCWriter(ri, fr);
						rw.write(vps);
					}
				}
			}
		}
	}
	
	private static class MError extends Tool {		
		public MError(CLIParams params) {
			super(params);
		}
		
		@Override
		public void run() {
			FileWrapper fr = this.getOutputFile();
			if (fr != null) {
				RepositoryInfo ri = this.getRepositoryInfo();
				if (ri != null) {
					VistaPackages vps = this.getVistaPackages(ri);
					if (vps != null) {						
						ErrorExemptions exemptions = ErrorExemptions.getVistAFOIAInstance();
						ErrorWriter ew = new ErrorWriter(exemptions, fr);
						vps.accept(ew);
					}
				}
			}
		}
	}
	
	private static class RoutineInfo extends Tool {		
		public RoutineInfo(CLIParams params) {
			super(params);
		}
		
		@Override
		public void run() {
			FileWrapper fr = this.getOutputFile();
			if (fr != null) {
				RepositoryInfo ri = this.getRepositoryInfo();
				if (ri != null) {
					VistaPackages vps = this.getVistaPackages(ri);
					if (vps != null) {			
						List<String> routineNames = new ArrayList<String>();
						for (VistaPackage p : vps.getPackages()) {
							List<Path> paths = p.getPaths();
							for (Path path : paths) {
								String routineNameWithExtension = path.getFileName().toString();
								String routineName = routineNameWithExtension.split("\\.")[0];
								routineNames.add(routineName);
							}
						}
						Collections.sort(routineNames);				
						fr.start();
						for (String routineName : routineNames) {
							VistaPackage pkg = ri.getPackageFromRoutineName(routineName);
							String prefix = pkg.getDefaultPrefix();
							String name = pkg.getPackageName();
							String line = routineName + ":" + prefix + ":" + name;
							fr.writeEOL(line);
						}
						fr.stop();
					}
				}
			}
		}
	}
	
	private static class UsesGlobal extends Tool {		
		public UsesGlobal(CLIParams params) {
			super(params);
		}
		
		@Override
		public void run() {
			FileWrapper fr = this.getOutputFile();
			if (fr != null) {
				RepositoryInfo ri = this.getRepositoryInfo();
				if (ri != null) {
					VistaPackages vps = this.getVistaPackages(ri);
					if (vps != null) {						
						DTUsesGlobalWriter dtug = new DTUsesGlobalWriter(ri, fr);
						vps.accept(dtug);
					}
				}
			}
		}
	}
			
	private static class UsedGlobal extends Tool {		
		public UsedGlobal(CLIParams params) {
			super(params);
		}
		
		@Override
		public void run() {
			FileWrapper fr = this.getOutputFile();
			if (fr != null) {
				RepositoryInfo ri = this.getRepositoryInfo();
				if (ri != null) {
					VistaPackages vps = this.getVistaPackages(ri);
					if (vps != null) {						
						DTUsedGlobalWriter dtug = new DTUsedGlobalWriter(ri, fr);
						vps.accept(dtug);
					}
				}
			}
		}
	}
			
	private static class FilemanCallGlobal extends Tool {		
		public FilemanCallGlobal(CLIParams params) {
			super(params);
		}
		
		@Override
		public void run() {
			FileWrapper fr = this.getOutputFile();
			if (fr != null) {
				RepositoryInfo ri = this.getRepositoryInfo();
				if (ri != null) {
					VistaPackages vps = this.getVistaPackages(ri);
					if (vps != null) {						
						DTFilemanCallWriter dtfmcw = new DTFilemanCallWriter(ri, fr);
						vps.accept(dtfmcw);
					}
				}
			}
		}
	}
			
	private static class ParseTreeSave extends Tool {		
		public ParseTreeSave(CLIParams params) {
			super(params);
		}
		
		@Override
		public void run() {
			RepositoryInfo ri = this.getRepositoryInfo();
			if (ri != null) {
				VistaPackages vps = this.getVistaPackages(ri);
				if (vps != null) {						
					SerializedRoutineWriter srw = new SerializedRoutineWriter(this.params.parseTreeDirectory);
					vps.accept(srw);
				}
			}
		}
	}

	private static class Entry extends Tool {		
		public Entry(CLIParams params) {
			super(params);
		}
		
		@Override
		public void run() {
			FileWrapper fr = this.getOutputFile();
			if (fr != null) {
				RepositoryInfo ri = this.getRepositoryInfo();
				if (ri != null) {
					VistaPackages vps = this.getVistaPackages(ri);
					if (vps != null) {	
						EntryWriter ew = new EntryWriter(fr);
						for (String r : this.params.routines) {
							ew.addRoutineNameFilter(r);
						}					
						vps.accept(ew);
					}
				}
			}
		}
	}

	private static class CacheUsage extends Tool {		
		public CacheUsage(CLIParams params) {
			super(params);
		}
		
		@Override
		public void run() {
			FileWrapper fr = this.getOutputFile();
			if (fr != null) {
				RepositoryInfo ri = this.getRepositoryInfo();
				if (ri != null) {
					VistaPackages vps = this.getVistaPackages(ri);
					if (vps != null) {	
						CacheOccuranceWriter cow = new CacheOccuranceWriter(fr);
						vps.accept(cow);
					}
				}
			}
		}
	}

	@Override
	protected void updateTools(Map<String, MemberFactory> tools) {
		tools.put("fanout", new MemberFactory() {				
			@Override
			public Tool getInstance(CLIParams params) {
				return new Fanout(params);
			}
		});
		tools.put("fanin", new MemberFactory() {				
			@Override
			public Tool getInstance(CLIParams params) {
				return  new Fanin(params);
			}
		});
		tools.put("option", new MemberFactory() {				
			@Override
			public Tool getInstance(CLIParams params) {
				return new Option(params);
			}
		});
		tools.put("rpc", new MemberFactory() {				
			@Override
			public Tool getInstance(CLIParams params) {
				return new RPC(params);
			}
		});
		tools.put("usesglb", new MemberFactory() {				
			@Override
			public Tool getInstance(CLIParams params) {
				return new UsesGlobal(params);
			}
		});
		tools.put("usedglb", new MemberFactory() {				
			@Override
			public Tool getInstance(CLIParams params) {
				return new UsedGlobal(params);
			}
		});
		tools.put("parsetreesave", new MemberFactory() {				
			@Override
			public Tool getInstance(CLIParams params) {
				return new ParseTreeSave(params);
			}
		});
		tools.put("entry", new MemberFactory() {				
			@Override
			public Tool getInstance(CLIParams params) {
				return new Entry(params);
			}
		});
		tools.put("entryinfo", new MemberFactory() {				
			@Override
			public Tool getInstance(CLIParams params) {
				return new EntryCodeInfoTool(params);
			}
		});
		tools.put("error", new MemberFactory() {				
			@Override
			public Tool getInstance(CLIParams params) {
				return new MError(params);
			}
		});
		tools.put("routineinfo", new MemberFactory() {				
			@Override
			public Tool getInstance(CLIParams params) {
				return new RoutineInfo(params);
			}
		});
		tools.put("filemancall", new MemberFactory() {				
			@Override
			public Tool getInstance(CLIParams params) {
				return new FilemanCallGlobal(params);
			}
		});
		tools.put("cacheusage", new MemberFactory() {				
			@Override
			public Tool getInstance(CLIParams params) {
				return new CacheUsage(params);
			}
		});
	}
}