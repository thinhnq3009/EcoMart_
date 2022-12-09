package eco.app.interfaces;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Lenovo
 * @param <E>
 */
public interface Statistic<E> {

    public void addRow(E e);

    public void fillTable(List<E> list);
    
    public void filter(Date from, Date to);

    public void initEvent();

}
