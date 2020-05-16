package restaurant;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SeatingManager {
    private BlockingQueue<CustomerGroup> queue;

    private final List<Table> tables;

    private Map<Table, Set<CustomerGroup>> map;


    public SeatingManager(List<Table> tables, int queueCapacity) {
        this.queue = new ArrayBlockingQueue<>(queueCapacity);
        this.tables = tables;
        this.map = new LinkedHashMap<>();
        for (Table table: tables) {
            map.put(table, new LinkedHashSet<>());
        }
    }

    // Group arrives and wants to be seated.
    // Locates the group to a table if there is enough space, puts the group into the queue otherwise
    public void arrives(CustomerGroup group) {
        Table assignedTable = locate(group);
        if (assignedTable == null) {
            queue.add(group);
            System.out.println("No available tables for the customer group " + group.getId() + " of size " + group.getSize()
                    + ". It has been added to the queue with size " + queue.size());
        }

    }

    private CustomerGroup getByCustomerGroupId(int customerGroupId) {
        for (Map.Entry<Table, Set<CustomerGroup>> entry: map.entrySet()) {
            Set<CustomerGroup> currentGroupSet = entry.getValue();
            for (CustomerGroup customerGroup: currentGroupSet) {
                if (customerGroup.getId() == customerGroupId) {
                    return customerGroup;
                }
            }
        }
        for (CustomerGroup group: queue) {
            if (group.getId() == customerGroupId) {
                return group;
            }
        }
        return null;
    }

    /* Whether seated or not, the group leaves the restaurant. */
    public void leaves(CustomerGroup group) {
        for (Map.Entry<Table, Set<CustomerGroup>> entry: map.entrySet()) {
            Set<CustomerGroup> currentGroupSet = entry.getValue();
            Table currentTable = entry.getKey();
            if (currentGroupSet.contains(group)) {
                currentGroupSet.remove(group);
                map.put(currentTable, currentGroupSet);
            }
        }
        queue.remove(group);
        System.out.println("Customer group " + group.getId() + " of size " + group.getSize() + " has left the restaurant.");
        tryToReduceQueue();
    }

    /* Return the table at which the group is seated, or null if
   they are not seated (whether they're waiting or already left). */
    public Table locate(CustomerGroup group) {
        for (Map.Entry<Table, Set<CustomerGroup>> entry: map.entrySet()) {
            Set<CustomerGroup> currentGroupSet = entry.getValue();
            Table currentTable = entry.getKey();
            if (isTableAvailableForCustomerGroup(currentTable, currentGroupSet, group)) {
                currentGroupSet.add(group);
                map.put(currentTable, currentGroupSet);
                System.out.println("Customer group " + group.getId() + " of size " + group.getSize() + " has been located to Table "
                        + currentTable.getId() + " that has size " + currentTable.getSize() + " and current available chairs of "
                        + getAvailableChairsByTableAndCustomerGroupSet(currentTable, map.get(currentTable)));
                return currentTable;
            }
        }
        return null;
    }

    private boolean isTableAvailableForCustomerGroup(Table table, Set<CustomerGroup> groupSet, CustomerGroup group) {
        return getAvailableChairsByTableAndCustomerGroupSet(table, groupSet) >= group.getSize();
    }

    private int getOccupiedChairsByCustomerGroupSet(Set<CustomerGroup> groupSet) {
        int occupiedChairsByCustomerGroupSet = 0;
        for (CustomerGroup group: groupSet) {
            occupiedChairsByCustomerGroupSet += group.getSize();
        }
        return occupiedChairsByCustomerGroupSet;
    }

    private int getAvailableChairsByTableAndCustomerGroupSet(Table table, Set<CustomerGroup> groupSet) {
        return table.getSize() - getOccupiedChairsByCustomerGroupSet(groupSet);
    }

    private void tryToReduceQueue() {
        for (CustomerGroup group: queue) {
            Table assignedTable = locate(queue.element());
            if (assignedTable == null) {
                System.out.println("No available tables for the Customer group of size " + queue.element().getSize()
                        + ". It has been added to the queue with size " + queue.size());
            } else {
                queue.remove(group);
            }
        }
    }

    private void printRestaurantTables() {
        for (Table table: tables) {
            System.out.println("Table with id " + table.getId() + " has size " + table.getSize() + ".\n");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the maximum capacity of the queue: ");
        int queueCapacity = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter the tables of the restaurant writing their size. Press any letter to finish");
        List<Table> tables = new ArrayList<>();
        int idCtr = 0;
        while (scanner.hasNextInt()) {
            int tableSize = scanner.nextInt();
            if (tableSize <= 1 || tableSize > 6) {
                System.out.println("The number of chairs must be between 2 and 6, both included. Please introduce again the table size: ");
            } else {
                tables.add(new Table(idCtr, tableSize));
                ++idCtr;
            }
        }

        scanner.nextLine(); // Process end of line
        scanner.nextLine(); // Process breaking char of previous section

        SeatingManager seatingManager = new SeatingManager(tables, queueCapacity);
        seatingManager.printRestaurantTables();

        System.out.println("Enter the movement writing 'a' (arrival) or 'l' (leaving) - id of customer group - size of customer group. Press Ctrl + C to leave ");
        while (scanner.hasNextLine()) {
            String movement = scanner.nextLine();
            String[] parts = movement.split("-");
            String movementType = parts[0];
            int customerGroupId = Integer.valueOf(parts[1]);

            System.out.println("Movement type: " + movementType);
            System.out.println("Customer group id: " + customerGroupId);

            if (movementType.equals("a")) {
                int customerGroupSize = Integer.valueOf(parts[2]);
                CustomerGroup group = new CustomerGroup(customerGroupId, customerGroupSize);
                seatingManager.arrives(group);
            } else if (movementType.equals("l")) {
                CustomerGroup group = seatingManager.getByCustomerGroupId(customerGroupId);
                seatingManager.leaves(group);
            }
        }

    }


}
