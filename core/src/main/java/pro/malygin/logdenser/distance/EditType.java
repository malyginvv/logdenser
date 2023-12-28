package pro.malygin.logdenser.distance;

/**
 * Represents different types of edits.
 * These can be used to describe changes in a sequence of tokens.
 */
public enum EditType {

    /**
     * Represents an insertion edit where an element is inserted into the sequence.
     */
    INSERTION,

    /**
     * Represents a deletion edit where an element is deleted from the sequence.
     */
    DELETION,

    /**
     * Represents a substitution edit where an element is replaced with another element in the sequence.
     */
    SUBSTITUTION
}
