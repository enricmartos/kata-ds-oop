package restaurant;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SeatingManagerImpl implements SeatingManager {
    private BlockingQueue<CustomerGroup> queue;
    private SeatingStore seatingStore;

    public SeatingManagerImpl(int queueCapacity, SeatingStore seatingStore) {
        this.queue = new ArrayBlockingQueue<>(queueCapacity);
        this.seatingStore = seatingStore;
    }

    /* Group arrives and wants to be seated.
    Locates the group to a table if there is enough space, puts the group into the queue otherwise */
    @Override
    public void arrives(CustomerGroup group) {
        Table assignedTable = locate(group);
        if (assignedTable == null) {
            queue.add(group);
            System.out.println("No available tables for the customer group " + group.getId() + " of size " + group.getSize()
                    + ". It has been added to the queue with size " + queue.size());
        }
    }

    // Whether seated or not, the group leaves the restaurant.
    @Override
    public void leaves(CustomerGroup group) {
        seatingStore.removeCustomerGroupInsideRestaurant(group);
        queue.remove(group);
        System.out.println("Customer group " + group.getId() + " of size " + group.getSize() + " has left the restaurant.");
        tryToReduceQueue();
    }

    /* Return the table at which the group is seated, or null if
   they are not seated (whether they're waiting or already left). */
    @Override
    public Table locate(CustomerGroup group) {
        Table bestTableCandidate = seatingStore.getBestTableCandidate(group);
        if (bestTableCandidate != null) {
            seatingStore.saveLocation(bestTableCandidate, group);
            return bestTableCandidate;
        }
        return null;
    }

    @Override
    public CustomerGroup getByCustomerGroupId(int customerGroupId) {
        CustomerGroup group = seatingStore.getByCustomerGroupIdInsideRestaurant(customerGroupId);
        if (group != null) {
            return group;
        } else if (getByCustomerGroupIdInQueue(customerGroupId) != null) {
            return getByCustomerGroupIdInQueue(customerGroupId);
        }
        return null;
    }

    private void tryToReduceQueue() {
        for (CustomerGroup group : queue) {
            Table assignedTable = locate(queue.element());
            if (assignedTable == null) {
                System.out.println("No available tables for the Customer group of size " + queue.element().getSize()
                        + ". It has been added to the queue with size " + queue.size());
            } else {
                queue.remove(group);
            }
        }
    }

//    public void printRestaurantTables() {
//        for (Table table : tables) {
//            System.out.println("Table with id " + table.getId() + " has size " + table.getSize() + ".\n");
//        }
//    }

    private CustomerGroup getByCustomerGroupIdInQueue(int customerGroupId) {
        for (CustomerGroup group : queue) {
            if (group.getId() == customerGroupId) {
                return group;
            }
        }
        return null;
    }


}
