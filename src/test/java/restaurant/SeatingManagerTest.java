package restaurant;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SeatingManagerTest {
    private static SeatingManager seatingManager;

    @BeforeClass
    public static void beforeClass() {
        List<Table> tables = new ArrayList<>();
        Table table1 = new Table(0, 6);
        Table table2 = new Table(1, 2);

        tables.add(table1);
        tables.add(table2);
        seatingManager = new SeatingManager(tables, 3);
    }

    @Test
    public void shouldArriveAndLocateCustomerGroupInTable() {
        CustomerGroup group0 = new CustomerGroup(0,6);
        seatingManager.arrives(group0);
        CustomerGroup group1 = new CustomerGroup(1, 2);
        seatingManager.arrives(group1); // Now all the tables are full

        CustomerGroup group2 = new CustomerGroup(2, 1);
        seatingManager.arrives(group2); // Now we add group to the queue
        CustomerGroup group3 = new CustomerGroup(3, 1);
        seatingManager.arrives(group3); // Now we add group to the queue
        CustomerGroup group4 = new CustomerGroup(4, 5);
        seatingManager.arrives(group4); // Now we add group to the queue
        seatingManager.leaves(group0); // Now the previous groups in the queue can be located
        seatingManager.leaves(group2);

    }


}
