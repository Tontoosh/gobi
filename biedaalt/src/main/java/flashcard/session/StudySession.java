package flashcard.session;

import flashcard.achievement.Achievement;
import flashcard.achievement.AchievementEvaluator;
import flashcard.model.Card;
import flashcard.organizer.CardOrganizer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Runs an interactive flashcard study session.
 */
public final class StudySession {

    private final List<Card> deck;
    private final CardOrganizer organizer;
    private final int repetitions;
    private final BufferedReader reader;
    private final PrintWriter writer;
    private final AchievementEvaluator achievementEvaluator;
    private final Map<Card, CardStats> statistics;
    private final List<RoundSummary> rounds;

    /**
     * Creates a session.
     *
     * @param deck loaded cards
     * @param organizer ordering strategy
     * @param repetitions required correct answers per card
     * @param reader input reader
     * @param writer output writer
     */
    public StudySession(final List<Card> deck,
                        final CardOrganizer organizer,
                        final int repetitions,
                        final BufferedReader reader,
                        final PrintWriter writer) {
        this.deck = List.copyOf(deck);
        this.organizer = organizer;
        this.repetitions = repetitions;
        this.reader = reader;
        this.writer = writer;
        this.achievementEvaluator = new AchievementEvaluator();
        this.statistics = createStatistics(deck);
        this.rounds = new ArrayList<>();
    }

    /**
     * Runs the interactive study session until every card is mastered.
     *
     * @throws IOException when console I/O fails
     */
    public void run() throws IOException {
        writer.printf("Loaded %d cards.%n", deck.size());
        writer.printf("Required correct answers per card: %d%n", repetitions);

        List<Card> currentOrder = new ArrayList<>(deck);
        RoundSummary previousRound = RoundSummary.empty();
        int roundNumber = 1;

        while (true) {
            final List<Card> pendingCards = pendingCards(currentOrder);
            if (pendingCards.isEmpty()) {
                break;
            }

            final StudyProgress progress = new StudyProgress(Map.copyOf(statistics), previousRound);
            final List<Card> roundOrder = organizer.organize(pendingCards, progress);
            final RoundSummary roundSummary = runRound(roundNumber, roundOrder);
            rounds.add(roundSummary);
            previousRound = roundSummary;
            currentOrder = new ArrayList<>(roundOrder);
            printRoundSummary(roundSummary);
            roundNumber++;
        }

        printCompletion();
    }

    private RoundSummary runRound(final int roundNumber, final List<Card> roundOrder) throws IOException {
        writer.println();
        writer.printf("Round %d%n", roundNumber);
        writer.printf("Cards this round: %d%n", roundOrder.size());

        final Set<Integer> incorrectCardIds = new LinkedHashSet<>();
        final long startedAt = System.nanoTime();

        for (Card card : roundOrder) {
            writer.printf("Q: %s%n", card.question());
            writer.print("> ");
            writer.flush();

            final String userAnswer = reader.readLine();
            if (userAnswer == null) {
                throw new IOException("Standard input was closed.");
            }

            final boolean correct = answersMatch(userAnswer, card.answer());
            final CardStats cardStats = statistics.get(card);
            cardStats.recordAttempt(correct);

            if (correct) {
                writer.println("Correct.");
            } else {
                incorrectCardIds.add(card.id());
                writer.printf("Wrong. Correct answer: %s%n", card.answer());
            }
        }

        final long elapsed = System.nanoTime() - startedAt;
        return new RoundSummary(roundNumber, roundOrder, incorrectCardIds, elapsed);
    }

    private void printRoundSummary(final RoundSummary roundSummary) {
        writer.printf("Round %d summary: %d correct, %d wrong, %.2f sec/card%n",
                roundSummary.roundNumber(),
                roundSummary.correctCount(),
                roundSummary.incorrectCount(),
                roundSummary.averageSecondsPerCard());
    }

    private void printCompletion() {
        writer.println();
        writer.println("Session complete.");
        final Set<Achievement> achievements =
                achievementEvaluator.evaluate(rounds, statistics.values());

        if (achievements.isEmpty()) {
            writer.println("Unlocked achievements: none");
            return;
        }

        writer.println("Unlocked achievements:");
        for (Achievement achievement : achievements) {
            writer.printf("- %s: %s%n", achievement.title(), achievement.description());
        }
    }

    private List<Card> pendingCards(final List<Card> currentOrder) {
        final List<Card> pending = new ArrayList<>();
        for (Card card : currentOrder) {
            if (!statistics.get(card).isMastered(repetitions)) {
                pending.add(card);
            }
        }
        return pending;
    }

    private static Map<Card, CardStats> createStatistics(final List<Card> deck) {
        final Map<Card, CardStats> stats = new LinkedHashMap<>();
        for (Card card : deck) {
            stats.put(card, new CardStats());
        }
        return stats;
    }

    private static boolean answersMatch(final String userAnswer, final String expectedAnswer) {
        return normalize(userAnswer).equals(normalize(expectedAnswer));
    }

    private static String normalize(final String value) {
        return value.trim().replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
    }
}
