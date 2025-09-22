package utilcalc.cli;

import static utilcalc.cli.application.ApplicationRunner.run;
import static utilcalc.cli.parser.InputArgumentParser.parseArgument;

import utilcalc.cli.model.AppConfiguration;

public class Application {
    public static void main(String[] args) {

        if (args.length == 0) {
            System.err.println("No parameter entered. Input file is required.");
            System.exit(1);
        }

        try {
            AppConfiguration appConfig = parseArgument(args);
            run(appConfig);
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }

        System.out.println("Report completed!");
    }
}
