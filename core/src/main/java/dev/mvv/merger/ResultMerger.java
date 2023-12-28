package dev.mvv.merger;

import dev.mvv.result.FullResult;
import dev.mvv.result.MultipleOptions;
import dev.mvv.result.ResultPart;
import dev.mvv.result.SameResults;
import dev.mvv.result.StaticPart;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

/**
 * Merges multiple condensed results saving all available options.
 * <p>
 * Example: given the following list of results:
 * <pre>
 * "Lorem &lt;ipsum|ipsu|ipsumm&gt; {3}"
 * "Lorem &lt;ip|ipsum&gt; {2}"
 * "Lorem sit {4}"
 * </pre>
 * will be merged to
 * <pre>
 * "Lorem &lt;ipsum|ipsu|ipsumm|ip&gt; {5}"
 * "Lorem sit {4}"
 * </pre>
 */
public class ResultMerger {

    /**
     * Merges multiple condensed results.
     *
     * @param sameResults Input results, may or may not contain unique result templates.
     * @return Merged results.
     */
    public @NotNull List<@NotNull SameResults> merge(@NotNull Collection<@NotNull SameResults> sameResults) {
        Map<String, List<SameResults>> templates = new LinkedHashMap<>();
        for (SameResults sameResult : sameResults) {
            templates.computeIfAbsent(sameResult.fullResult().template(), key -> new ArrayList<>()).add(sameResult);
        }

        List<SameResults> list = new ArrayList<>();
        for (List<SameResults> results : templates.values()) {
            var first = results.get(0);
            var multipleOptions = multipleOptions(first);
            int totalCount = first.count();
            for (int i = 1; i < results.size(); i++) {
                addOptions(results.get(i), multipleOptions);
                totalCount += results.get(i).count();
            }
            list.add(new SameResults(fromOptions(first.fullResult(), multipleOptions), totalCount));
        }
        return list;
    }

    private Map<Integer, Collection<String>> multipleOptions(SameResults sameResults) {
        Map<Integer, Collection<String>> result = new HashMap<>();
        List<? extends ResultPart> parts = sameResults.fullResult().parts();
        for (int i = 0; i < parts.size(); i++) {
            ResultPart part = parts.get(i);
            if (part instanceof MultipleOptions multipleOptions) {
                result.put(i, new LinkedHashSet<>(multipleOptions.options()));
            }
        }
        return result;
    }

    private void addOptions(SameResults sameResults, Map<Integer, Collection<String>> options) {
        List<? extends ResultPart> parts = sameResults.fullResult().parts();
        for (int i = 0; i < parts.size(); i++) {
            ResultPart part = parts.get(i);
            if (part instanceof MultipleOptions multipleOptions) {
                options.computeIfPresent(i, (index, existingOptions) -> {
                    existingOptions.addAll(multipleOptions.options());
                    return existingOptions;
                });
            }
        }
    }

    private FullResult fromOptions(FullResult staticSource, Map<Integer, Collection<String>> options) {
        List<? extends ResultPart> parts = staticSource.parts();
        List<ResultPart> result = new ArrayList<>(parts.size());
        for (int i = 0; i < parts.size(); i++) {
            ResultPart part = parts.get(i);
            if (part instanceof StaticPart staticPart) {
                result.add(staticPart);
            } else {
                result.add(new MultipleOptions(new ArrayList<>(options.getOrDefault(i, emptyList()))));
            }
        }
        return new FullResult(result);
    }
}
