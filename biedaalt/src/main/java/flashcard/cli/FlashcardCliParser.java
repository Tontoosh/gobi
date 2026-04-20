package flashcard.cli;

import flashcard.organizer.OrderType;
import java.io.PrintStream;
import java.util.Arrays;

/**
 * Parses command line arguments for the flashcard application.
 */
public final class FlashcardCliParser {

    /**
     * Parses the provided command line arguments.
     *
     * @param args raw command line arguments
     * @return parsed configuration
     * @throws CliParsingException when the arguments are invalid
     */
    public FlashcardConfiguration parse(final String[] args) throws CliParsingException {
        if (Arrays.asList(args).contains("--help")) {
            return new FlashcardConfiguration(true, null, OrderType.RANDOM, 1, false);
        }

        if (args.length == 0) {
            throw new CliParsingException("Missing required argument: <cards-file>");
        }

        if (args[0].startsWith("--")) {
            throw new CliParsingException("Expected <cards-file> before options.");
        }

        final String cardsFile = args[0];
        OrderType orderType = OrderType.RANDOM;
        int repetitions = 1;
        boolean invertCards = false;

        int index = 1;
        while (index < args.length) {
            final String argument = args[index];
            switch (argument) {
                case "--order" -> {
                    if (index + 1 >= args.length) {
                        throw new CliParsingException("Missing value for --order.");
                    }
                    try {
                        orderType = OrderType.fromCliValue(args[index + 1]);
                    } catch (IllegalArgumentException exception) {
                        throw new CliParsingException(exception.getMessage());
                    }
                    index += 2;
                }
                case "--repetitions" -> {
                    if (index + 1 >= args.length) {
                        throw new CliParsingException("Missing value for --repetitions.");
                    }
                    repetitions = parsePositiveInteger(args[index + 1], "--repetitions");
                    index += 2;
                }
                case "--invertCards" -> {
                    invertCards = true;
                    index += 1;
                }
                default -> {
                    if (argument.startsWith("--")) {
                        throw new CliParsingException("Unknown option: " + argument);
                    }
                    throw new CliParsingException("Unexpected positional argument: " + argument);
                }
            }
        }

        return new FlashcardConfiguration(false, cardsFile, orderType, repetitions, invertCards);
    }

    /**
     * Prints usage information to the provided stream.
     *
     * @param stream output stream
     */
    public void printUsage(final PrintStream stream) {
        stream.println("Usage: flashcard <cards-file> [options]");
        stream.println("Options:");
        stream.println("  --help                         Show help information");
        stream.println("  --order <order>                Card order. Default: random");
        stream.println("                                 Choices: random, worst-first, recent-mistakes-first");
        stream.println("  --repetitions <num>            Required correct answers per card. Default: 1");
        stream.println("  --invertCards                  Swap question and answer sides. Default: false");
    }

    private static int parsePositiveInteger(final String rawValue, final String optionName)
            throws CliParsingException {
        final int value;
        try {
            value = Integer.parseInt(rawValue);
        } catch (NumberFormatException exception) {
            throw new CliParsingException(optionName + " must be a positive integer.");
        }

        if (value <= 0) {
            throw new CliParsingException(optionName + " must be a positive integer.");
        }
        return value;
    }
}
