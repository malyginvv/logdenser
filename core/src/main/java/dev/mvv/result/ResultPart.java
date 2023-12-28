package dev.mvv.result;

/**
 * Represents a part or component of a result. This interface serves as a
 * common contract for classes that provide various parts of a larger result, which may include details,
 * segments, or elements that collectively contribute to the complete result.
 */
public interface ResultPart {

    /**
     * Indicates that this part contains only a static string and not a collection.
     *
     * @return contains only a static string
     */
    boolean isStatic();
}
