package flashcard.organizer;

/**
 * Supported card ordering strategies.
 */
public enum OrderType {
    RANDOM("random"),
    WORST_FIRST("worst-first"),
    RECENT_MISTAKES_FIRST("recent-mistakes-first");

    private final String cliValue;

    OrderType(final String cliValue) {
        this.cliValue = cliValue;
    }

    /**
     * Parses a CLI value into an order type.
     *
     * @param rawValue raw option value
     * @return matching order type
     */
    public static OrderType fromCliValue(final String rawValue) {
        for (OrderType type : values()) {
            if (type.cliValue.equalsIgnoreCase(rawValue)) {
                return type;
            }
        }
        throw new IllegalArgumentException(
                "Invalid order: " + rawValue + ". Supported values: random, worst-first, recent-mistakes-first.");
    }

    @Override
    public String toString() {
        return cliValue;
    }
}
