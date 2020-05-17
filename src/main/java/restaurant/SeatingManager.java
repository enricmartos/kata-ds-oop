package restaurant;

public interface SeatingManager {

    void arrives(CustomerGroup group);

    void leaves(CustomerGroup group);

    Table locate(CustomerGroup group);

    CustomerGroup getByCustomerGroupId(int customerGroupId);

}
