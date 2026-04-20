package flashcard;

import flashcard.cli.CliParsingException;
import flashcard.cli.FlashcardCliParser;
import flashcard.cli.FlashcardConfiguration;
import flashcard.io.CardDeckLoader;
import flashcard.model.Card;
import flashcard.organizer.CardOrganizer;
import flashcard.organizer.CardOrganizerFactory;
import flashcard.session.StudySession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

/**
 * Entry point for the flashcard command line application.
 */
public final class FlashcardApplication {

    private FlashcardApplication() {
    }

    /**
     * Main entry point.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        final FlashcardCliParser parser = new FlashcardCliParser();
        final FlashcardConfiguration configuration;

        try {
            configuration = parser.parse(args);
        } catch (CliParsingException exception) {
            System.err.println("Error: " + exception.getMessage());
            System.err.println();
            parser.printUsage(System.err);
            System.exit(2);
            return;
        }

        if (configuration.helpRequested()) {
            parser.printUsage(System.out);
            return;
        }

        final List<Card> deck;
        try {
            deck = CardDeckLoader.load(resolvePath(configuration.cardsFile()), configuration.invertCards());
        } catch (IOException | IllegalArgumentException exception) {
            System.err.println("Failed to load cards: " + exception.getMessage());
            System.exit(1);
            return;
        }

        final CardOrganizer organizer = CardOrganizerFactory.create(configuration.orderType());
        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        final PrintWriter writer = new PrintWriter(System.out, true);

        try {
            new StudySession(deck, organizer, configuration.repetitions(), reader, writer).run();
        } catch (IOException exception) {
            System.err.println("I/O error while running the session: " + exception.getMessage());
            System.exit(1);
        }
    }

    private static Path resolvePath(final String rawPath) {
        return Path.of(rawPath).toAbsolutePath().normalize();
    }
}
