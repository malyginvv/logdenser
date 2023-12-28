package pro.malygin.logdenser.distance;

/**
 * Represents an edit operation performed on a sequence of tokens.
 * It captures both the index of the element being edited and the type of the edit operation.
 *
 * @param index    The index at which the edit operation occurs in the sequence.
 * @param editType The type of the edit operation
 */
public record Edit(int index, EditType editType) {
}
