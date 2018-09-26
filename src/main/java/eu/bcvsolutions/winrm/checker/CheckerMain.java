package eu.bcvsolutions.winrm.checker;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CheckerMain {

	public static void main(String[] args) {
		// create logger
		CheckerLogger log = new CheckerLogger();
		
		// create Options object from commons cli - rules for available options
		Options cliOpts = new Options();
		
		final Option hostOpt = Option.builder("h").hasArg(true).desc("Target hostname or IP address.").required(true).type(String.class).build();
		cliOpts.addOption(hostOpt);
		final Option portOpt = Option.builder("p").hasArg(true).desc("Target port.").required(true).type(Integer.class).build();
		cliOpts.addOption(portOpt);
		final Option useHttpsOpt = Option.builder("s").hasArg(false).desc("Use HTTPS.").required(false).type(Boolean.class).build();
		cliOpts.addOption(useHttpsOpt);
		final Option ignoreCertProblemOpt = Option.builder("k").hasArg(false).desc("Ignore HTTPS certificate problems.").required(false).type(Boolean.class).build();
		cliOpts.addOption(ignoreCertProblemOpt);
		
		// parse options from command line, exit on error
		CommandLineParser cliParser = new DefaultParser();
		try {
			CommandLine cli = cliParser.parse(cliOpts, args);
		} catch (ParseException e) {
			System.out.println("Unable to parse command line arguments.");
			System.out.println(e.getMessage());
			System.out.println();
			HelpFormatter hf = new HelpFormatter();
			hf.printHelp("java -jar winrm-checker-jar-with-dependencies.jar", cliOpts);
			System.exit(1);
		}
		
		// initialize winrm4j and use it

	}
	
}
