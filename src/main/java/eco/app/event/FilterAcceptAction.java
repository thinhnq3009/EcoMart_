package eco.app.event;

/**
 *
 * @author Lenovo
 * @param <E> type of filter
 */
public interface FilterAcceptAction<E> {
    public void accept(E e);
}
