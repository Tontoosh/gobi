package flashcard.io;

import flashcard.model.Card;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads flashcards from a text file.
 */
public final class CardDeckLoader {

    private CardDeckLoader() {
    }

    /**
     * Loads cards from a UTF-8 text file.
     *
     * @param filePath path to the cards file
     * @param invertCards whether cards should be inverted
     * @return loaded cards
     * @throws IOException when file reading fails
     */
    public static List<Card> load(final Path filePath, final boolean invertCards) throws IOException {
        if (!Files.exists(filePath)) {
            throw new IllegalArgumentException("Картын файл олдсонгүй: " + filePath);
        }

        final List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        final List<Card> cards = new ArrayList<>();
        int nextId = 1;

        for (int index = 0; index < lines.size(); index++) {
            final String line = lines.get(index);
            final String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                continue;
            }

            final int separatorIndex = findSeparator(line);
            if (separatorIndex < 0) {
                throw new IllegalArgumentException(
                        (index + 1) + "-р мөрийн формат буруу. Асуулт|хариу байх ёстой.");
            }

            final String question = unescape(line.substring(0, separatorIndex).trim());
            final String answer = unescape(line.substring(separatorIndex + 1).trim());
            if (question.isBlank() || answer.isBlank()) {
                throw new IllegalArgumentException(
                        (index + 1) + "-р мөрийн формат буруу. Асуулт ба хариу шаардлагатай.");
            }

            Card card = new Card(nextId, question, answer);
            if (invertCards) {
                card = card.inverted();
            }
            cards.add(card);
            nextId++;
        }

        if (cards.isEmpty()) {
            throw new IllegalArgumentException("Картын файл хоосон эсвэл зөв форматтай карт байхгүй байна.");
        }

        return cards;
    }

    private static int findSeparator(final String line) {
        boolean escaped = false;
        for (int index = 0; index < line.length(); index++) {
            final char current = line.charAt(index);
            if (escaped) {
                escaped = false;
                continue;
            }
            if (current == '\\') {
                escaped = true;
                continue;
            }
            if (current == '|') {
                return index;
            }
        }
        return -1;
    }

    private static String unescape(final String value) {
        final StringBuilder result = new StringBuilder(value.length());
        boolean escaped = false;

        for (int index = 0; index < value.length(); index++) {
            final char current = value.charAt(index);
            if (escaped) {
                result.append(current);
                escaped = false;
            } else if (current == '\\') {
                escaped = true;
            } else {
                result.append(current);
            }
        }

        if (escaped) {
            result.append('\\');
        }
        return result.toString();
    }
}
