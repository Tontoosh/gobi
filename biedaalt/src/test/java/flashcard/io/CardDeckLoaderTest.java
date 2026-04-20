package flashcard.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import flashcard.model.Card;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class CardDeckLoaderTest {

    @Test
    void loadsCardsAndSupportsEscapedCharacters() throws IOException {
        final Path file = Files.createTempFile("cards", ".txt");
        Files.writeString(file,
                "# comment\n"
                        + "Capital of Mongolia|Ulaanbaatar\n"
                        + "Pipe question\\||pipe \\|\n"
                        + "\n",
                StandardCharsets.UTF_8);

        final List<Card> cards = CardDeckLoader.load(file, false);

        assertEquals(2, cards.size());
        assertEquals("Capital of Mongolia", cards.get(0).question());
        assertEquals("Ulaanbaatar", cards.get(0).answer());
        assertEquals("Pipe question|", cards.get(1).question());
        assertEquals("pipe |", cards.get(1).answer());
    }

    @Test
    void rejectsInvalidCardLines() throws IOException {
        final Path file = Files.createTempFile("bad-cards", ".txt");
        Files.writeString(file, "broken line without separator\n", StandardCharsets.UTF_8);

        assertThrows(IllegalArgumentException.class, () -> CardDeckLoader.load(file, false));
    }
}
