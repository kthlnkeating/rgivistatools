package com.raygroupintl.vista.tools;

import org.junit.Test;

public class CLIExamples {
	@Test
	public void testAll() {
		String outputPath = "C:\\Sandbox"; 
		String pathPrefix = "C:\\Sandbox\\m_";		
		MRoutineAnalyzer.main(new String[]{"fanout", "-o", pathPrefix + "fo_all.txt"});				
		MRoutineAnalyzer.main(new String[]{"fanout", "-o", pathPrefix + "fo_gmpl.txt", "-p", "GMPL"});				
		MRoutineAnalyzer.main(new String[]{"fanout", "-o", pathPrefix + "fo_sd.txt", "-p", "SD"});				
		
		MRoutineAnalyzer.main(new String[]{"fanin", "-o", pathPrefix + "fi_all.txt",
				"-mf", "C:\\Users\\Afsin\\git\\VistA-FOIA\\Scripts\\ZGI.m",
				"-mf", "C:\\Users\\Afsin\\git\\VistA-FOIA\\Scripts\\ZGO.m",
				"-pe", "DENTAL RECORD MANAGER"});				
		MRoutineAnalyzer.main(new String[]{"fanin", "-o", pathPrefix + "fi_gmpl.txt", "-p", "GMPL"});				
		MRoutineAnalyzer.main(new String[]{"fanin", "-o", pathPrefix + "fi_sd.txt", "-p", "SD"});				
		
		MRoutineAnalyzer.main(new String[]{"option", "-o", pathPrefix + "opt_all.txt", "-pe", "DENTAL RECORD MANAGER"});				
		MRoutineAnalyzer.main(new String[]{"option", "-o", pathPrefix + "opt_gmpl.txt", "-p", "GMPL"});				
		MRoutineAnalyzer.main(new String[]{"option", "-o", pathPrefix + "opt_sd.txt", "-p", "SD"});				
		
		MRoutineAnalyzer.main(new String[]{"rpc", "-o", pathPrefix + "rpc_all.txt", "-pe", "DENTAL RECORD MANAGER"});				
		MRoutineAnalyzer.main(new String[]{"rpc", "-o", pathPrefix + "rpc_gmpl.txt", "-p", "GMPL"});				
		MRoutineAnalyzer.main(new String[]{"rpc", "-o", pathPrefix + "rpc_sd.txt", "-p", "SD"});				
		
		MRoutineAnalyzer.main(new String[]{"usesglb", "-o", pathPrefix + "uses_gmpl.txt", "-p", "GMPL", "-ownf", outputPath + "\\Ownership.csv"});				
		MRoutineAnalyzer.main(new String[]{"usesglb", "-o", pathPrefix + "uses_sd.txt", "-p", "SD", "-ownf", outputPath + "\\Ownership.csv"});				

		MRoutineAnalyzer.main(new String[]{"usedglb", "-o", pathPrefix + "used_gmpl.txt", "-p", "GMPL", "-ownf", outputPath + "\\Ownership.csv"});				
		MRoutineAnalyzer.main(new String[]{"usedglb", "-o", pathPrefix + "used_sd.txt", "-p", "SD", "-ownf", outputPath + "\\Ownership.csv"});				
		
		MRoutineAnalyzer.main(new String[]{"filemancall", "-o", pathPrefix + "fmc_all.txt"});	  //SPNRPC4 is due to error			

		MRoutineAnalyzer.main(new String[]{"parsetreesave", "-ptd", outputPath + "\\serial"});		

		MRoutineAnalyzer.main(new String[]{"entry", "-p", "OR", "-r", "ORQQPL.*", "-o", pathPrefix + "einfo_cprspl_tags.txt"});
		
		MRoutineAnalyzer.main(new String[]{"entryinfo", "-i", pathPrefix + "einfo_cprspl_tags.txt", "-o", pathPrefix + "einfo_cprspl_0.txt",
					"-ptd", outputPath + "\\serial", "-f", "0"});
		MRoutineAnalyzer.main(new String[]{"entryinfo", "-i", pathPrefix + "einfo_cprspl_tags.txt", "-o", pathPrefix + "einfo_cprspl_1.txt",
					"-ptd", outputPath + "\\serial", "-f", "1"});
		MRoutineAnalyzer.main(new String[]{"entryinfo", "-i", pathPrefix + "einfo_cprspl_tags.txt", "-o", pathPrefix + "einfo_cprspl_2.txt",
					"-ptd", outputPath + "\\serial", "-f", "2"});
		MRoutineAnalyzer.main(new String[]{"entryinfo", "-i", pathPrefix + "einfo_cprspl_tags.txt", "-o", pathPrefix + "einfo_cprspl_3.txt",
					"-ptd", outputPath + "\\serial", "-f", "3"});


		MRoutineAnalyzer.main(new String[]{"fanin", "-o", pathPrefix + "einfo_gmplfi_tags.txt", "--rawformat", "-p", "GMPL"});

		MRoutineAnalyzer.main(new String[]{"entryinfo", "-i", pathPrefix + "einfo_gmplfi_tags.txt", "-o", pathPrefix + "einfo_gmplfi_0.txt",
					"-ptd", outputPath + "\\serial", "-f", "0"});
		MRoutineAnalyzer.main(new String[]{"entryinfo", "-i", pathPrefix + "einfo_gmplfi_tags.txt", "-o", pathPrefix + "einfo_gmplfi_1.txt",
					"-ptd", outputPath + "\\serial", "-f", "1"});
		MRoutineAnalyzer.main(new String[]{"entryinfo", "-i", pathPrefix + "einfo_gmplfi_tags.txt", "-o", pathPrefix + "einfo_gmplfi_2.txt",
					"-ptd", outputPath + "\\serial", "-f", "2"});
		MRoutineAnalyzer.main(new String[]{"entryinfo", "-i", pathPrefix + "einfo_gmplfi_tags.txt", "-o", pathPrefix + "einfo_gmplfi_3.txt",
				"-ptd", outputPath + "\\serial", "-f", "3"});
		
		
		MRoutineAnalyzer.main(new String[]{"fanin", "-o", pathPrefix + "einfo_sdfi_tags.txt", "--rawformat", "-p", "SD"});

		MRoutineAnalyzer.main(new String[]{"entryinfo", "-i", pathPrefix + "einfo_sdfi_tags.txt", "-o", pathPrefix + "einfo_sdfi_0.txt",
					"-ptd", outputPath + "\\serial", "-f", "0"});
		MRoutineAnalyzer.main(new String[]{"entryinfo", "-i", pathPrefix + "einfo_sdfi_tags.txt", "-o", pathPrefix + "einfo_sdfi_1.txt",
					"-ptd", outputPath + "\\serial", "-f", "1"});
		MRoutineAnalyzer.main(new String[]{"entryinfo", "-i", pathPrefix + "einfo_sdfi_tags.txt", "-o", pathPrefix + "einfo_sdfi_2.txt",
					"-ptd", outputPath + "\\serial", "-f", "2"});		
		MRoutineAnalyzer.main(new String[]{"entryinfo", "-i", pathPrefix + "einfo_sdfi_tags.txt", "-o", pathPrefix + "einfo_sdfi_3.txt",
				"-ptd", outputPath + "\\serial", "-f", "3"});
		
		
		
/*		MRoutineAnalyzer.main(new String[]{"entry", "-p", "GMPL", 
					"-r", "GMPLAPI.*", "-r", "GMPLSITE.*", "-r", "GMPLDAL.*", "-r", "GMPLEXT.*", 
					"-o", pathPrefix + "einfo_gmplapi_tags.txt"});
		
		MRoutineAnalyzer.main(new String[]{"entryinfo", "-i", pathPrefix + "einfo_gmplapi_tags.txt", "-o", pathPrefix + "einfo_gmplapi_0.txt",
					"-ptd", outputPath + "\\serial", "-f", "0"});
		MRoutineAnalyzer.main(new String[]{"entryinfo", "-i", pathPrefix + "einfo_gmplapi_tags.txt", "-o", pathPrefix + "einfo_gmplapi_1.txt",
					"-ptd", outputPath + "\\serial", "-f", "1"});
		MRoutineAnalyzer.main(new String[]{"entryinfo", "-i", pathPrefix + "einfo_gmplapi_tags.txt", "-o", pathPrefix + "einfo_gmplapi_2.txt",
					"-ptd", outputPath + "\\serial", "-f", "2"});
		MRoutineAnalyzer.main(new String[]{"entryinfo", "-i", pathPrefix + "einfo_gmplapi_tags.txt", "-o", pathPrefix + "einfo_gmplapi_3.txt",
					"-ptd", outputPath + "\\serial", "-f", "3"});
*/
		//MRoutineAnalyzer.main(new String[]{"fanin", "-o", pathPrefix + "einfo_fi_tags.txt", "--rawformat",
		//		"-mf", "C:\\Users\\Afsin\\git\\VistA-FOIA\\Scripts\\ZGI.m",
		//		"-mf", "C:\\Users\\Afsin\\git\\VistA-FOIA\\Scripts\\ZGO.m",
		//		"-pe", "DENTAL RECORD MANAGER"});				

		//MRoutineAnalyzer.main(new String[]{"entryinfo", "-i", pathPrefix + "einfo_fi_tags.txt", "-o", pathPrefix + "einfo_fi_0.txt",
		//			"-ptd", outputPath + "\\serial", "-f", "0"});
		//MRoutineAnalyzer.main(new String[]{"entryinfo", "-i", pathPrefix + "einfo_fi_tags.txt", "-o", pathPrefix + "einfo_fi_1.txt",
		//			"-ptd", outputPath + "\\serial", "-f", "1"});
		//MRoutineAnalyzer.main(new String[]{"entryinfo", "-i", pathPrefix + "einfo_fi_tags.txt", "-o", pathPrefix + "einfo_fi_2.txt",
		//			"-ptd", outputPath + "\\serial", "-f", "2"});
		//MRoutineAnalyzer.main(new String[]{"entryinfo", "-i", pathPrefix + "einfo_fi_tags.txt", "-o", pathPrefix + "einfo_fi_3.txt",
		//			"-ptd", outputPath + "\\serial", "-f", "3"});
	}
}
