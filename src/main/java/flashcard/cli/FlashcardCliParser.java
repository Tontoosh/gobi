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
            throw new CliParsingException("Шаардлагатай аргумент дутуу: <карт-файл>");
        }

        if (args[0].startsWith("--")) {
            throw new CliParsingException("<карт-файл> сонголтуудаас өмнө байх ёстой.");
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
                        throw new CliParsingException("--order-н утга дутуу.");
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
                        throw new CliParsingException("--repetitions-н утга дутуу.");
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
                        throw new CliParsingException("Тодорхойгүй сонголт: " + argument);
                    }
                    throw new CliParsingException("Гэнэтийн аргумент: " + argument);
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
        stream.println("Хэрэглээ: flashcard <карт-файл> [сонголтууд]");
        stream.println("Сонголтууд:");
        stream.println("  --help                         Тусламжийн мэдээлэл харуулах");
        stream.println("  --order <дараалал>             Картын дараалал. Өгөгдмөл: random");
        stream.println("                                 Сонголтууд: random, worst-first, recent-mistakes-first");
        stream.println("  --repetitions <тоо>            Картын шаардлагатай зөв хариу. Өгөгдмөл: 1");
        stream.println("  --invertCards                  Асуулт ба хариуг солих. Өгөгдмөл: false");
    }

    private static int parsePositiveInteger(final String rawValue, final String optionName)
            throws CliParsingException {
        final int value;
        try {
            value = Integer.parseInt(rawValue);
        } catch (NumberFormatException exception) {
            throw new CliParsingException(optionName + " эерэг бүхэл тоо байх ёстой.");
        }

        if (value <= 0) {
            throw new CliParsingException(optionName + " эерэг бүхэл тоо байх ёстой.");
        }
        return value;
    }
}
