package restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SeatingManagerClient {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the maximum capacity of the queue: ");
        int queueCapacity = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter the tables of the restaurant writing their size. Press '0' to move forward");
        List<Table> tables = new ArrayList<>();
        int idCtr = 0;
        while (scanner.hasNextInt()) {
            int tableSize = scanner.nextInt();
            if (tableSize == 0) {
                break;
            }
            if (tableSize <= 1 || tableSize > 6) {
                System.out.println("The number of chairs must be between 2 and 6, both included. Please introduce again the table size: ");
            } else {
                tables.add(new Table(idCtr, tableSize));
                ++idCtr;
            }
        }

        scanner.nextLine(); // Process end of line
//        scanner.nextLine(); // Process breaking char of previous section

        SeatingStore seatingStore = new InMemorySeatingStore(tables);
        SeatingManager seatingManager = new SeatingManagerImpl(queueCapacity, seatingStore);
//        seatingManager.printRestaurantTables();

        System.out.println("Enter the movement writing 'a' (arrival) or 'l' (leaving) - id of customer group - size of customer group." +
                " For instance, a-0-2 (arrival of customer group with id 0 that has 2 people, and l-0 (customer group with id 0 leaves). Press 's' to finish");
        while (scanner.hasNextLine()) {
            String movement = scanner.nextLine();
            if (movement.equals("s")) {
                break;
            }

            String[] parts = movement.split("-");
            String movementType = parts[0];
            int customerGroupId = Integer.valueOf(parts[1]);

//            System.out.println("Movement type: " + movementType);
//            System.out.println("Customer group id: " + customerGroupId);

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
