package flashcard.organizer;

/**
 * Factory for card ordering strategies.
 */
public final class CardOrganizerFactory {

    private CardOrganizerFactory() {
    }

    /**
     * Creates an organizer for the selected order type.
     *
     * @param orderType selected type
     * @return organizer instance
     */
    public static CardOrganizer create(final OrderType orderType) {
        return switch (orderType) {
            case RANDOM -> new RandomCardOrganizer();
            case WORST_FIRST -> new WorstFirstSorter();
            case RECENT_MISTAKES_FIRST -> new RecentMistakesFirstSorter();
        };
    }
}
