package flashcard.cli;

/**
 * Signals invalid command line arguments.
 */
public final class CliParsingException extends Exception {

    /**
     * Creates a new exception with a message.
     *
     * @param message validation message
     */
    public CliParsingException(final String message) {
        super(message);
    }
}
