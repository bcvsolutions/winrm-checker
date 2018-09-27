package eu.bcvsolutions.winrm.checker;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.http.client.config.AuthSchemes;

import io.cloudsoft.winrm4j.client.WinRmClientContext;
import io.cloudsoft.winrm4j.winrm.WinRmTool;
import io.cloudsoft.winrm4j.winrm.WinRmToolResponse;

/**
 * Main class. So far behaves like script - everything is created in the main().
 * TODO: create separate class for the checker - not needed for the first version
 * @author fiisch
 *
 */
public class CheckerMain {
	
	public static final String DEFAULT_AUTH_METHOD = "Basic";
	public static final String DEFAULT_REMOTE_COMMAND = "Winrm id";
	//public static final String DEFAULT_REMOTE_COMMAND = "Winrm id –r:machinename";
	
	public static final int DEFAULT_CONNECTION_RETRIES = 3;

	public static void main(String[] args) {
		// create logger
		CheckerLogger log = new CheckerLogger();
		
		// create Options object from commons cli - rules for available options
		Options cliOpts = new Options();
		
		final Option hostOpt = Option.builder("h")
				.hasArg(true)
				.desc("Target hostname or IP address.")
				.required(true)
				.type(String.class)
				.build();
		cliOpts.addOption(hostOpt);
		
		final Option portOpt = Option.builder("p")
				.hasArg(true)
				.desc("Target port.")
				.required(true)
				.type(Integer.class)
				.build();
		cliOpts.addOption(portOpt);
		
		final Option useHttpsOpt = Option.builder("s")
				.hasArg(false)
				.desc("Use HTTPS.")
				.required(false)
				.type(Boolean.class)
				.build();
		cliOpts.addOption(useHttpsOpt);
		
		final Option ignoreCertProblemOpt = Option.builder("k")
				.hasArg(false)
				.desc("Ignore HTTPS certificate problems.")
				.required(false)
				.type(Boolean.class)
				.build();
		cliOpts.addOption(ignoreCertProblemOpt);
		
		final Option authOpt = Option.builder("a")
				.hasArg(true)
				.desc("Authentication method. Default: " + CheckerMain.DEFAULT_AUTH_METHOD + ". Implemented: Basic.")
				.required(false)
				.type(String.class)
				.build();
		cliOpts.addOption(authOpt);
		
		final Option userOpt = Option.builder("u")
				.hasArg(true)
				.desc("Username.")
				.required(true)
				.type(String.class)
				.build();
		cliOpts.addOption(userOpt);
		
		final Option passOpt = Option.builder("w")
				.hasArg(true)
				.desc("Password.")
				.required(true)
				.type(String.class)
				.build();
		cliOpts.addOption(passOpt);
		
		final Option cmdOpt = Option.builder("c")
				.hasArg(true)
				.desc("Non-PS command to execute. Default: " + CheckerMain.DEFAULT_REMOTE_COMMAND)
				.required(false)
				.type(String.class)
				.build();
		cliOpts.addOption(cmdOpt);
		
		// parse options from command line, exit on error
		CommandLineParser cliParser = new DefaultParser();
		CommandLine cli = null;
		try {
			cli = cliParser.parse(cliOpts, args);
		} catch (ParseException e) {
			System.out.println("Unable to parse command line arguments.");
			System.out.println(e.getMessage());
			System.out.println();
			new HelpFormatter().printHelp("java -jar winrm-checker-jar-with-dependencies.jar", cliOpts);
			System.exit(1);
		}
		
		assert cli!=null;
		
		// get arguments from command line
		String host = cli.getOptionValue("h");
		int port = Integer.parseInt(cli.getOptionValue("p"));
		boolean useHttps = cli.hasOption("s"); 
		boolean ignoreCertProblem = cli.hasOption("k");
		String auth = CheckerMain.DEFAULT_AUTH_METHOD; 
		if (cli.hasOption("a")) {
			auth = cli.getOptionValue("a");
		}
		String user = cli.getOptionValue("u");
		String pass = cli.getOptionValue("w");
		String cmd = cli.getOptionValue("c");
		
		log.log("Host: " + host);
		log.log("Port: " + port);
		log.log("HTTPS: " + useHttps);
		log.log("Ignore certificate problems: " + ignoreCertProblem);
		log.log("Authentication: " + auth);
		log.log("Username: " + user);
		log.log("Password: " + pass);
		log.log("Remote command: " + cmd);
		
		// initialize winrm4j and use it
		log.log("Initializing winrm4j context...");
		WinRmClientContext rmctx = WinRmClientContext.newInstance();
		WinRmTool.Builder rmbuilder = WinRmTool.Builder.builder(host, user, pass);
		switch(auth) {
			case "Basic":
				rmbuilder.setAuthenticationScheme(AuthSchemes.BASIC);
				break;
			default:
				log.log("Authentication method '"+ auth +"' not implemented. Defaulting.");
				rmbuilder.setAuthenticationScheme(AuthSchemes.BASIC);
				break;
		}
		rmbuilder.port(port);
		rmbuilder.useHttps(useHttps);
		rmbuilder.disableCertificateChecks(ignoreCertProblem);
		rmbuilder.context(rmctx);
		WinRmTool rmtool = rmbuilder.build();
		rmtool.setRetriesForConnectionFailures(CheckerMain.DEFAULT_CONNECTION_RETRIES);
		log.log("Done.");
		
		//run command, get response
		if (cmd == null) {
			log.log("Remote command not specified. Defaulting.");
			cmd = CheckerMain.DEFAULT_REMOTE_COMMAND;
		}
		log.log("Executing command '" + cmd + "'...");
		WinRmToolResponse rmresponse = rmtool.executeCommand(cmd);
		log.log("Done.");
		log.log("Response STDOUT: " + rmresponse.getStdOut());
		log.log("Response STDERR: " + rmresponse.getStdErr());
		log.log("Response status code: " + rmresponse.getStatusCode());
		
		//cleanup
		rmctx.shutdown();

	}
	
}
