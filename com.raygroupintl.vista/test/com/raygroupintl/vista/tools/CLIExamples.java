package com.raygroupintl.vista.tools;

import org.junit.Test;

public class CLIExamples {
	@Test
	public void testAll() {
		MRoutineAnalyzer.main(new String[]{"fanout", "-o", "C:\\Sandbox\\j_fo_all.txt"});				
		MRoutineAnalyzer.main(new String[]{"fanout", "-o", "C:\\Sandbox\\j_fo_gmpl.txt", "-p", "GMPL"});				
		MRoutineAnalyzer.main(new String[]{"fanout", "-o", "C:\\Sandbox\\j_fo_sd.txt", "-p", "SD"});				
		
		MRoutineAnalyzer.main(new String[]{"fanin", "-o", "C:\\Sandbox\\j_fi_all.txt",
				"-mf", "C:\\Users\\Afsin\\git\\VistA-FOIA\\Scripts\\ZGI.m",
				"-mf", "C:\\Users\\Afsin\\git\\VistA-FOIA\\Scripts\\ZGO.m",
				"-pe", "DENTAL RECORD MANAGER"});				
		MRoutineAnalyzer.main(new String[]{"fanin", "-o", "C:\\Sandbox\\j_fi_gmpl.txt", "-p", "GMPL"});				
		MRoutineAnalyzer.main(new String[]{"fanin", "-o", "C:\\Sandbox\\j_fi_sd.txt", "-p", "SD"});				
		
		MRoutineAnalyzer.main(new String[]{"option", "-o", "C:\\Sandbox\\j_opt_all.txt", "-pe", "DENTAL RECORD MANAGER"});				
		MRoutineAnalyzer.main(new String[]{"option", "-o", "C:\\Sandbox\\j_opt_gmpl.txt", "-p", "GMPL"});				
		MRoutineAnalyzer.main(new String[]{"option", "-o", "C:\\Sandbox\\j_opt_sd.txt", "-p", "SD"});				
		
		MRoutineAnalyzer.main(new String[]{"rpc", "-o", "C:\\Sandbox\\j_rpc_all.txt", "-pe", "DENTAL RECORD MANAGER"});				
		MRoutineAnalyzer.main(new String[]{"rpc", "-o", "C:\\Sandbox\\j_rpc_gmpl.txt", "-p", "GMPL"});				
		MRoutineAnalyzer.main(new String[]{"rpc", "-o", "C:\\Sandbox\\j_rpc_sd.txt", "-p", "SD"});				
		
		MRoutineAnalyzer.main(new String[]{"usesglb", "-o", "C:\\Sandbox\\j_uses_gmpl.dat", "-p", "GMPL", "-ownf", "C:\\Sandbox\\Ownership.csv"});				
		MRoutineAnalyzer.main(new String[]{"usesglb", "-o", "C:\\Sandbox\\j_uses_sd.dat", "-p", "SD", "-ownf", "C:\\Sandbox\\Ownership.csv"});				

		MRoutineAnalyzer.main(new String[]{"usedglb", "-o", "C:\\Sandbox\\j_used_gmpl.dat", "-p", "GMPL", "-ownf", "C:\\Sandbox\\Ownership.csv"});				
		MRoutineAnalyzer.main(new String[]{"usedglb", "-o", "C:\\Sandbox\\j_used_sd.dat", "-p", "SD", "-ownf", "C:\\Sandbox\\Ownership.csv"});				
		
		MRoutineAnalyzer.main(new String[]{"filemancall", "-o", "C:\\Sandbox\\j_fmc_all.txt"});	  //SPNRPC4 is due to error			

		MRoutineAnalyzer.main(new String[]{"parsetreesave", "-ptd", "C:\\Sandbox\\serial"});		

		MRoutineAnalyzer.main(new String[]{"entry", "-p", "OR", "-r", "ORQQPL.*", "-o", "C:\\Sandbox\\j_einfo_cprspl_tags.txt"});
		
		MRoutineAnalyzer.main(new String[]{"entryinfo", "-i", "C:\\Sandbox\\j_einfo_cprspl_tags.txt", "-o", "C:\\Sandbox\\j_einfo_cprspl_0.txt",
					"-ptd", "C:\\Sandbox\\serial", "-f", "0"});
		MRoutineAnalyzer.main(new String[]{"entryinfo", "-i", "C:\\Sandbox\\j_einfo_cprspl_tags.txt", "-o", "C:\\Sandbox\\j_einfo_cprspl_1.txt",
					"-ptd", "C:\\Sandbox\\serial", "-f", "1"});
		MRoutineAnalyzer.main(new String[]{"entryinfo", "-i", "C:\\Sandbox\\j_einfo_cprspl_tags.txt", "-o", "C:\\Sandbox\\j_einfo_cprspl_2.txt",
					"-ptd", "C:\\Sandbox\\serial", "-f", "2"});
		MRoutineAnalyzer.main(new String[]{"entryinfo", "-i", "C:\\Sandbox\\j_einfo_cprspl_tags.txt", "-o", "C:\\Sandbox\\j_einfo_cprspl_3.txt",
					"-ptd", "C:\\Sandbox\\serial", "-f", "3"});


		MRoutineAnalyzer.main(new String[]{"fanin", "-o", "C:\\Sandbox\\j_einfo_gmplfi_tags.txt", "--rawformat", "-p", "GMPL"});

		MRoutineAnalyzer.main(new String[]{"entryinfo", "-i", "C:\\Sandbox\\j_einfo_gmplfi_tags.txt", "-o", "C:\\Sandbox\\j_einfo_gmplfi_0.txt",
					"-ptd", "C:\\Sandbox\\serial", "-f", "0"});
		MRoutineAnalyzer.main(new String[]{"entryinfo", "-i", "C:\\Sandbox\\j_einfo_gmplfi_tags.txt", "-o", "C:\\Sandbox\\j_einfo_gmplfi_1.txt",
					"-ptd", "C:\\Sandbox\\serial", "-f", "1"});
		MRoutineAnalyzer.main(new String[]{"entryinfo", "-i", "C:\\Sandbox\\j_einfo_gmplfi_tags.txt", "-o", "C:\\Sandbox\\j_einfo_gmplfi_2.txt",
					"-ptd", "C:\\Sandbox\\serial", "-f", "2"});
		
		
		MRoutineAnalyzer.main(new String[]{"fanin", "-o", "C:\\Sandbox\\j_einfo_sdfi_tags.txt", "--rawformat", "-p", "SD"});

		MRoutineAnalyzer.main(new String[]{"entryinfo", "-i", "C:\\Sandbox\\j_einfo_sdfi_tags.txt", "-o", "C:\\Sandbox\\j_einfo_sdfi_0.txt",
					"-ptd", "C:\\Sandbox\\serial", "-f", "0"});
		MRoutineAnalyzer.main(new String[]{"entryinfo", "-i", "C:\\Sandbox\\j_einfo_sdfi_tags.txt", "-o", "C:\\Sandbox\\j_einfo_sdfi_1.txt",
					"-ptd", "C:\\Sandbox\\serial", "-f", "1"});
		MRoutineAnalyzer.main(new String[]{"entryinfo", "-i", "C:\\Sandbox\\j_einfo_sdfi_tags.txt", "-o", "C:\\Sandbox\\j_einfo_sdfi_2.txt",
					"-ptd", "C:\\Sandbox\\serial", "-f", "2"});
		
		
		
		
		//MRoutineAnalyzer.main(new String[]{"fanin", "-o", "C:\\Sandbox\\j_einfo_fi_tags.txt", "--rawformat",
		//		"-mf", "C:\\Users\\Afsin\\git\\VistA-FOIA\\Scripts\\ZGI.m",
		//		"-mf", "C:\\Users\\Afsin\\git\\VistA-FOIA\\Scripts\\ZGO.m",
		//		"-pe", "DENTAL RECORD MANAGER"});				

		//MRoutineAnalyzer.main(new String[]{"entryinfo", "-i", "C:\\Sandbox\\j_einfo_fi_tags.txt", "-o", "C:\\Sandbox\\j_einfo_fi_0.txt",
		//			"-ptd", "C:\\Sandbox\\serial", "-f", "0"});
		//MRoutineAnalyzer.main(new String[]{"entryinfo", "-i", "C:\\Sandbox\\j_einfo_fi_tags.txt", "-o", "C:\\Sandbox\\j_einfo_fi_1.txt",
		//			"-ptd", "C:\\Sandbox\\serial", "-f", "1"});
		//MRoutineAnalyzer.main(new String[]{"entryinfo", "-i", "C:\\Sandbox\\j_einfo_fi_tags.txt", "-o", "C:\\Sandbox\\j_einfo_fi_2.txt",
		//			"-ptd", "C:\\Sandbox\\serial", "-f", "2"});
		//MRoutineAnalyzer.main(new String[]{"entryinfo", "-i", "C:\\Sandbox\\j_einfo_fi_tags.txt", "-o", "C:\\Sandbox\\j_einfo_fi_3.txt",
		//			"-ptd", "C:\\Sandbox\\serial", "-f", "3"});
	}
}
