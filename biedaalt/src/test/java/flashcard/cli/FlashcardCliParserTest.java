package flashcard.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import flashcard.organizer.OrderType;
import org.junit.jupiter.api.Test;

class FlashcardCliParserTest {

    private final FlashcardCliParser parser = new FlashcardCliParser();

    @Test
    void parsesAllSupportedOptions() throws CliParsingException {
        final FlashcardConfiguration configuration = parser.parse(new String[]{
                "sample-cards.txt",
                "--order", "recent-mistakes-first",
                "--repetitions", "3",
                "--invertCards"
        });

        assertFalse(configuration.helpRequested());
        assertEquals("sample-cards.txt", configuration.cardsFile());
        assertEquals(OrderType.RECENT_MISTAKES_FIRST, configuration.orderType());
        assertEquals(3, configuration.repetitions());
        assertTrue(configuration.invertCards());
    }

    @Test
    void helpOptionWinsEvenWhenCombinedWithOtherArguments() throws CliParsingException {
        final FlashcardConfiguration configuration = parser.parse(new String[]{
                "sample-cards.txt",
                "--order", "random",
                "--help",
                "--unknown"
        });

        assertTrue(configuration.helpRequested());
        assertNull(configuration.cardsFile());
        assertEquals(OrderType.RANDOM, configuration.orderType());
        assertEquals(1, configuration.repetitions());
        assertFalse(configuration.invertCards());
    }

    @Test
    void rejectsMissingCardsFileWhenHelpIsNotRequested() {
        final CliParsingException exception =
                assertThrows(CliParsingException.class, () -> parser.parse(new String[]{}));

        assertEquals("Missing required argument: <cards-file>", exception.getMessage());
    }

    @Test
    void rejectsUnknownOptionWithHelpfulMessage() {
        final CliParsingException exception =
                assertThrows(CliParsingException.class, () -> parser.parse(new String[]{
                        "sample-cards.txt",
                        "--not-real"
                }));

        assertEquals("Unknown option: --not-real", exception.getMessage());
    }
}
