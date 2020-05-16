package restaurant;

import java.util.Objects;

public class Table {
    private final int id;
    private final int size; // number of chairs around this table (2, 3, 4, 5 or 6)

    public Table(int id, int size) {
        this.id = id;
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public int getSize() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        return id == table.id &&
                size == table.size;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, size);
    }
}
