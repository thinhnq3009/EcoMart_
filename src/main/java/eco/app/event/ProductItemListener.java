

package eco.app.event;

import eco.app.entity.Product;
import javax.swing.JTextField;

/**
 *
 * @author ThinhNQ 
 */
public interface ProductItemListener {
    public void onClick(Product product, JTextField quantityField);
}
