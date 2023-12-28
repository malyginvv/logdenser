package pro.malygin.logdenser.distance;

import java.util.ArrayList;
import java.util.List;

public class EditDistanceBuilder {

    private final List<Edit> edits;

    public EditDistanceBuilder() {
        edits = new ArrayList<>();
    }

    public EditDistanceBuilder addEdit(Edit edit) {
        edits.add(edit);
        return this;
    }

    public EditDistanceBuilder addEdit(int index, EditType editType) {
        return addEdit(new Edit(index, editType));
    }

    public EditDistance build() {
        return new EditDistance(edits.size(), edits);
    }
}
