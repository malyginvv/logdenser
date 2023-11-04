package dev.mvv.distance;

import java.util.List;

/**
 * Represents the result of an edit distance calculation between two sequences.
 * <p>
 * Edit distance is a measure of the similarity between two sequences, and it quantifies the minimum
 * number of edit operations (insertions, deletions, substitutions) required to make the sequences identical.
 *
 * @param distance The calculated edit distance between the two sequences.
 * @param edits    A list edit operations required to transform one sequence into another.
 */
public record EditDistance(int distance, List<Edit> edits) {
}
