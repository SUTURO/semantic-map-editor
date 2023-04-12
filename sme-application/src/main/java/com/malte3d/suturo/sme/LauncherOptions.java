package com.malte3d.suturo.sme;

import com.malte3d.suturo.commons.messages.Messages;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;

/**
 * Parses and defines the CLI arguments
 */
@Slf4j
@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LauncherOptions {

    private static final Option OPT_DEBUG = Option.builder()
            .option("d")
            .longOpt("debug")
            .desc("Enable debug mode")
            .build();

    private static final Option OPT_HELP = Option.builder()
            .option("h")
            .longOpt("help")
            .desc("Print help information")
            .build();

    boolean debugMode;

    public static LauncherOptions parse(@NonNull String[] args) {

        Options options = new Options();
        options.addOption(OPT_HELP);
        options.addOption(OPT_DEBUG);

        try {

            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption(OPT_HELP))
                printHelp(options);

            return new LauncherOptions(cmd.hasOption(OPT_DEBUG));

        } catch (ParseException e) {
            printHelp(options);
            log.error("Error while parsing the command line arguments. {}", e.getMessage());
            System.exit(1);
        }

        throw new IllegalStateException();
    }

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(Messages.getString("Application.Name"), options, true);
    }
}
