package restaurant;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InMemorySeatingStore implements SeatingStore {
    private Map<Table, Set<CustomerGroup>> map;

    public InMemorySeatingStore(List<Table> tables) {
        this.map = new LinkedHashMap<>();
        for (Table table: tables) {
            map.put(table, new LinkedHashSet<>());
        }
    }

    @Override
    public Table getBestTableCandidate(CustomerGroup group) {
        Table bestTableCandidate = null;
        for (Map.Entry<Table, Set<CustomerGroup>> entry: map.entrySet()) {
            Set<CustomerGroup> currentGroupSet = entry.getValue();
            Table currentTable = entry.getKey();
            if (isTableAvailableForCustomerGroup(currentTable, currentGroupSet, group) && getOccupiedChairsByCustomerGroupSet(currentGroupSet) == 0) {
                return currentTable;
            } else if (isTableAvailableForCustomerGroup(currentTable, currentGroupSet, group)) {
                bestTableCandidate = currentTable;
            }
        }

        return bestTableCandidate;
    }

    @Override
    public void saveLocation(Table table, CustomerGroup group) {
        Set<CustomerGroup> currentGroupSet = map.get(table);
        currentGroupSet.add(group);
        map.put(table, currentGroupSet);
        System.out.println("Customer group " + group.getId() + " of size " + group.getSize() + " has been located to Table "
                + table.getId() + " that has size " + table.getSize() + " and current available chairs of "
                + getAvailableChairsByTableAndCustomerGroupSet(table, map.get(table)));
    }

    @Override
    public void removeCustomerGroupInsideRestaurant(CustomerGroup group) {
        for (Map.Entry<Table, Set<CustomerGroup>> entry: map.entrySet()) {
            Set<CustomerGroup> currentGroupSet = entry.getValue();
            Table currentTable = entry.getKey();
            if (currentGroupSet.contains(group)) {
                currentGroupSet.remove(group);
                map.put(currentTable, currentGroupSet);
            }
        }
    }

    @Override
    public CustomerGroup getByCustomerGroupIdInsideRestaurant(int customerGroupId) {
        for (Map.Entry<Table, Set<CustomerGroup>> entry: map.entrySet()) {
            Set<CustomerGroup> currentGroupSet = entry.getValue();
            for (CustomerGroup customerGroup: currentGroupSet) {
                if (customerGroup.getId() == customerGroupId) {
                    return customerGroup;
                }
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
}
