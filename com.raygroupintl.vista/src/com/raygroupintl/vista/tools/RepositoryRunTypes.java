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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raygroupintl.m.parsetree.data.CodeInfo;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.SerializedBlocksSupply;
import com.raygroupintl.m.parsetree.filter.BasicSourcedFanoutFilter;
import com.raygroupintl.m.parsetree.filter.ExcludeFilemanCallFanoutFilter;
import com.raygroupintl.m.parsetree.filter.ExcludeNonPkgCallFanoutFilter;
import com.raygroupintl.m.parsetree.filter.ExcludeNonRtnFanoutFilter;
import com.raygroupintl.m.parsetree.filter.PercentRoutineFanoutFilter;
import com.raygroupintl.m.parsetree.filter.SourcedFanoutFilter;
import com.raygroupintl.m.parsetree.visitor.APIRecorderFactory;
import com.raygroupintl.output.FileWrapper;
import com.raygroupintl.vista.repository.RepositoryInfo;
import com.raygroupintl.vista.repository.VistaPackage;
import com.raygroupintl.vista.repository.VistaPackages;
import com.raygroupintl.vista.repository.visitor.APIOverallRecorder;
import com.raygroupintl.vista.repository.visitor.APIWriter;
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

public class RepositoryRunTypes extends RunTypes {
	private static Map<String, String> REPLACEMENT_ROUTINES = new HashMap<String, String>();
	static {
		REPLACEMENT_ROUTINES.put("%ZOSV", "ZOSVONT");
		REPLACEMENT_ROUTINES.put("%ZIS4", "ZIS4ONT");
		REPLACEMENT_ROUTINES.put("%ZISF", "ZISFONT");
		REPLACEMENT_ROUTINES.put("%ZISH", "ZISHONT");
		REPLACEMENT_ROUTINES.put("%XUCI", "ZISHONT");

		REPLACEMENT_ROUTINES.put("%ZISTCPS", "ZISTCPS");
		REPLACEMENT_ROUTINES.put("%ZTMDCL", "ZTMDCL");
		
		REPLACEMENT_ROUTINES.put("%ZOSVKR", "ZOSVKRO");
		REPLACEMENT_ROUTINES.put("%ZOSVKSE", "ZOSVKSOE");
		REPLACEMENT_ROUTINES.put("%ZOSVKSS", "ZOSVKSOS");
		REPLACEMENT_ROUTINES.put("%ZOSVKSD", "ZOSVKSD");

		REPLACEMENT_ROUTINES.put("%ZTLOAD", "ZTLOAD");
		REPLACEMENT_ROUTINES.put("%ZTLOAD1", "ZTLOAD1");
		REPLACEMENT_ROUTINES.put("%ZTLOAD2", "ZTLOAD2");
		REPLACEMENT_ROUTINES.put("%ZTLOAD3", "ZTLOAD3");
		REPLACEMENT_ROUTINES.put("%ZTLOAD4", "ZTLOAD4");
		REPLACEMENT_ROUTINES.put("%ZTLOAD5", "ZTLOAD5");
		REPLACEMENT_ROUTINES.put("%ZTLOAD6", "ZTLOAD6");
		REPLACEMENT_ROUTINES.put("%ZTLOAD7", "ZTLOAD7");
		
		REPLACEMENT_ROUTINES.put("%ZTM", "ZTM");
		REPLACEMENT_ROUTINES.put("%ZTM0", "ZTM0");
		REPLACEMENT_ROUTINES.put("%ZTM1", "ZTM1");
		REPLACEMENT_ROUTINES.put("%ZTM2", "ZTM2");
		REPLACEMENT_ROUTINES.put("%ZTM3", "ZTM3");
		REPLACEMENT_ROUTINES.put("%ZTM4", "ZTM4");
		REPLACEMENT_ROUTINES.put("%ZTM5", "ZTM5");
		REPLACEMENT_ROUTINES.put("%ZTM6", "ZTM6");
		
		REPLACEMENT_ROUTINES.put("%ZTMS", "ZTMS");
		REPLACEMENT_ROUTINES.put("%ZTMS0", "ZTMS0");
		REPLACEMENT_ROUTINES.put("%ZTMS1", "ZTMS1");
		REPLACEMENT_ROUTINES.put("%ZTMS2", "ZTMS2");
		REPLACEMENT_ROUTINES.put("%ZTMS3", "ZTMS3");
		REPLACEMENT_ROUTINES.put("%ZTMS4", "ZTMS4");
		REPLACEMENT_ROUTINES.put("%ZTMS5", "ZTMS5");
		REPLACEMENT_ROUTINES.put("%ZTMS7", "ZTMS7");
		REPLACEMENT_ROUTINES.put("%ZTMSH", "ZTMSH");

		REPLACEMENT_ROUTINES.put("%DT", "DIDT");
		REPLACEMENT_ROUTINES.put("%DTC", "DIDTC");
		REPLACEMENT_ROUTINES.put("%RCR", "DIRCR");

		REPLACEMENT_ROUTINES.put("%ZTER", "ZTER");
		REPLACEMENT_ROUTINES.put("%ZTER1", "ZTER1");

		REPLACEMENT_ROUTINES.put("%ZTPP", "ZTPP");
		REPLACEMENT_ROUTINES.put("%ZTP1", "ZTP1");
		REPLACEMENT_ROUTINES.put("%ZTPTCH", "ZTPTCH");
		REPLACEMENT_ROUTINES.put("%ZTRDE", "ZTRDE");
		REPLACEMENT_ROUTINES.put("%ZTMOVE", "ZTMOVE");
		
		REPLACEMENT_ROUTINES.put("%ZIS", "ZIS");
		REPLACEMENT_ROUTINES.put("%ZIS1", "ZIS1");
		REPLACEMENT_ROUTINES.put("%ZIS2", "ZIS2");
		REPLACEMENT_ROUTINES.put("%ZIS3", "ZIS3");
		REPLACEMENT_ROUTINES.put("%ZIS5", "ZIS5");
		REPLACEMENT_ROUTINES.put("%ZIS6", "ZIS6");
		REPLACEMENT_ROUTINES.put("%ZIS7", "ZIS7");
		REPLACEMENT_ROUTINES.put("%ZISC", "ZISC");
		REPLACEMENT_ROUTINES.put("%ZISP", "ZISP");
		REPLACEMENT_ROUTINES.put("%ZISS", "ZISS");
		REPLACEMENT_ROUTINES.put("%ZISS1", "ZISS1");
		REPLACEMENT_ROUTINES.put("%ZISS2", "ZISS2");
		REPLACEMENT_ROUTINES.put("%ZISTCP", "ZISTCP");
		REPLACEMENT_ROUTINES.put("%ZISUTL", "ZISUTL");
	}
	
	private static class Fanout extends RunType {		
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

	private static class Fanin extends RunType {		
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
		
	private static class Option extends RunType {		
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
			
	private static class RPC extends RunType {		
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
	
	private static class MError extends RunType {		
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
	
	private static class RoutineInfo extends RunType {		
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
	
	private static class UsesGlobal extends RunType {		
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
			
	private static class UsedGlobal extends RunType {		
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
			
	private static class FilemanCallGlobal extends RunType {		
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
			
	private static class ParseTreeSave extends RunType {		
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

	private static class Entry extends RunType {		
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

	private static class EntryInfo extends RunType {		
		public EntryInfo(CLIParams params) {
			super(params);
		}
		
		private int getFanoutFlag() {
			try {
				int result = Integer.parseInt(this.params.flag);
				if (result < 0) return 0;
				if (result > 3) return 3;
				return result;
			} catch(Throwable t) {
			}
			return 0;
		}
		
		private SourcedFanoutFilter getFilter(RepositoryInfo ri) {
			int flag = this.getFanoutFlag();
			switch (flag) {
				case 1:
					return new ExcludeFilemanCallFanoutFilter(ri);
				case 2:
					return new ExcludeNonPkgCallFanoutFilter(ri);
				case 3:
					return new ExcludeNonRtnFanoutFilter();
				default:
					PercentRoutineFanoutFilter filter = new PercentRoutineFanoutFilter();
					return new BasicSourcedFanoutFilter(filter);
			}
		}
		
		private void auxRun(RepositoryInfo ri, FileWrapper fr, BlocksSupply<CodeInfo> blocks) {
			APIWriter apiw = new APIWriter(fr, blocks, REPLACEMENT_ROUTINES);
			SourcedFanoutFilter filter = this.getFilter(ri);
			apiw.setFilter(filter);
			if (this.params.inputFile != null) {
				apiw.write(this.params.inputFile);					
			} else {
				apiw.writeEntries(this.params.entries);
			}			
		}
		
		@Override
		public void run() {
			FileWrapper fr = this.getOutputFile();
			if (fr != null) {
				RepositoryInfo ri = this.getRepositoryInfo();
				if (ri != null) {
					if ((this.params.parseTreeDirectory == null) || this.params.parseTreeDirectory.isEmpty()) {
						APIOverallRecorder api = new APIOverallRecorder(ri);
						VistaPackages vps = new VistaPackages(ri.getAllPackages());
						vps.accept(api);
						BlocksSupply<CodeInfo> blocks = api.getBlocks();
						this.auxRun(ri, fr, blocks);
					} else {
						BlocksSupply<CodeInfo> blocks = new SerializedBlocksSupply<CodeInfo>(this.params.parseTreeDirectory, new APIRecorderFactory(ri));
						this.auxRun(ri, fr, blocks);
					}
				}
			}
		}
	}	
	
	private static class CacheUsage extends RunType {		
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
	protected Map<String, RunType.Factory> createRunTypes() {
		Map<String, RunType.Factory> result = new HashMap<String, RunType.Factory>();
		result.put("fanout", new RunType.Factory() {				
			@Override
			public RunType getInstance(CLIParams params) {
				return new Fanout(params);
			}
		});
		result.put("fanin", new RunType.Factory() {				
			@Override
			public RunType getInstance(CLIParams params) {
				return  new Fanin(params);
			}
		});
		result.put("option", new RunType.Factory() {				
			@Override
			public RunType getInstance(CLIParams params) {
				return new Option(params);
			}
		});
		result.put("rpc", new RunType.Factory() {				
			@Override
			public RunType getInstance(CLIParams params) {
				return new RPC(params);
			}
		});
		result.put("usesglb", new RunType.Factory() {				
			@Override
			public RunType getInstance(CLIParams params) {
				return new UsesGlobal(params);
			}
		});
		result.put("usedglb", new RunType.Factory() {				
			@Override
			public RunType getInstance(CLIParams params) {
				return new UsedGlobal(params);
			}
		});
		result.put("parsetreesave", new RunType.Factory() {				
			@Override
			public RunType getInstance(CLIParams params) {
				return new ParseTreeSave(params);
			}
		});
		result.put("entry", new RunType.Factory() {				
			@Override
			public RunType getInstance(CLIParams params) {
				return new Entry(params);
			}
		});
		result.put("entryinfo", new RunType.Factory() {				
			@Override
			public RunType getInstance(CLIParams params) {
				return new EntryInfo(params);
			}
		});
		result.put("error", new RunType.Factory() {				
			@Override
			public RunType getInstance(CLIParams params) {
				return new MError(params);
			}
		});
		result.put("routineinfo", new RunType.Factory() {				
			@Override
			public RunType getInstance(CLIParams params) {
				return new RoutineInfo(params);
			}
		});
		result.put("filemancall", new RunType.Factory() {				
			@Override
			public RunType getInstance(CLIParams params) {
				return new FilemanCallGlobal(params);
			}
		});
		result.put("cacheusage", new RunType.Factory() {				
			@Override
			public RunType getInstance(CLIParams params) {
				return new CacheUsage(params);
			}
		});
		return result;
	}
}