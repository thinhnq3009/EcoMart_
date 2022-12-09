package eco.app.model;

import java.util.List;
import eco.app.event.FilterAcceptAction;
import java.util.ArrayList;

/**
 *
 * @author ThinhNQ
 * @param <E> type of filler
 */
public class FilterMoldel<E> {

    /**
     * List item will select in combobox
     */
    private List<E> allElements;

    private List<FilterAcceptAction<E>> listAcceptAction = new ArrayList<>();

    public FilterMoldel() {
        allElements = new ArrayList<>();
    }

    public FilterMoldel(List<E> allElements) {
        this.allElements = allElements;
    }

    public void filling(Comparison<E> comparison) {
        for (E e : getAllElements()) {
            if (comparison.compare(e)) {
                accept(e);
            }
        }
    }

    public void accept(E e) {
        for (FilterAcceptAction<E> faa : listAcceptAction) {
            faa.accept(e);
        }
    }

    public List<E> getAllElements() {
        return allElements;
    }

    public void setAllElements(List<E> allElements) {
        this.allElements = allElements;
    }

    public void addFilterAcceptAction(FilterAcceptAction<E> acceptAction) {
        listAcceptAction.add(acceptAction);
    }

}
