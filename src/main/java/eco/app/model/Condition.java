package eco.app.model;

/**
 *
 * @author ThinhNQ
 * @param <T>
 */
public class Condition<T> {

    private String name;
    private Comparison<T> comparison;

    public Condition(String name, Comparison<T> comparison) {
        this.name = name;
        this.comparison = comparison;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Comparison<T> getCondition() {
        return comparison;
    }

    public void setComparison(Comparison<T> comparison) {
        this.comparison = comparison;
    }

    @Override
    public String toString() {
        return name;
    }

}
