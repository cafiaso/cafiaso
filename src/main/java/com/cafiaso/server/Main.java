package com.cafiaso.server;

import com.cafiaso.server.network.server.SocketServer;
import com.cafiaso.server.server.Server;
import com.cafiaso.server.server.ServerImpl;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 25565;

    public static void main(String[] args) {
        Options options = new Options();

        Option helpOption = Option.builder()
                .longOpt("help")
                .desc("Show this help message")
                .build();

        Option hostOption = Option.builder()
                .option("h")
                .longOpt("host")
                .desc("The host to listen on")
                .hasArg()
                .build();

        Option portOption = Option.builder()
                .option("p")
                .longOpt("port")
                .desc("The port to listen on")
                .hasArg()
                .converter(Integer::parseInt)
                .build();

        options.addOption(helpOption);
        options.addOption(hostOption);
        options.addOption(portOption);

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine line = parser.parse(options, args);

            if (line.hasOption(helpOption)) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("Server", options);

                return;
            }

            String host = line.getOptionValue(hostOption, DEFAULT_HOST);
            int port = line.getParsedOptionValue(portOption, DEFAULT_PORT);

            Server server = new ServerImpl(new SocketServer());
            server.start(host, port);

            // Add a shutdown hook to stop the server gracefully
            Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
        } catch (ParseException e) {
            LOGGER.error("Failed to parse command line arguments", e);
        }
    }
}
