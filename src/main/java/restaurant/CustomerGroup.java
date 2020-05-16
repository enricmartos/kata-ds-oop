package restaurant;

import java.util.Objects;

public class CustomerGroup {
    private final int id;
    private final int size; // number of people in the group

    public CustomerGroup(int id, int size) {
        this.id = id;
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public int getSize() {
        return size;
    }
}
