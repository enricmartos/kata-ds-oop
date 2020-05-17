package restaurant;

public interface SeatingStore {

    Table getBestTableCandidate(CustomerGroup group);

    void saveLocation(Table table, CustomerGroup group);

    void removeCustomerGroupInsideRestaurant(CustomerGroup group);

    CustomerGroup getByCustomerGroupIdInsideRestaurant(int customerGroupId);
}
