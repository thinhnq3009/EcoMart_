package eco.app.helper;

import eco.app.dao.BrandDao;
import eco.app.dao.CategoryDao;
import eco.app.entity.Brand;
import eco.app.entity.Category;
import eco.app.entity.Employee;
import eco.app.event.ValidateActionAdapter;
import eco.app.myswing.TextFieldCustom;
import java.awt.Color;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ThinhNQ
 */
public class ShareData {

    // Hành động kiểm tra dữ liệu chung cho các text field
    public static ValidateActionAdapter validateAction = new ValidateActionAdapter() {
        @Override
        public void invalidAction(TextFieldCustom t) {
            t.setBackground(SaveData.BG_DANGER);
        }

        @Override
        public void validAction(TextFieldCustom t) {
            t.setBackground(Color.WHITE);
        }

    };

    public static Employee USER_LOGIN;

    public static List<Category> CATEGORIES;
    public static List<Brand> BRANDS;

    static {

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    CATEGORIES = new CategoryDao().getAll();
                    BRANDS = new BrandDao().getAll();

                } catch (Exception ex) {
                    Logger.getLogger(ShareData.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

        thread.start();

    }

//    static {
//        try {
//            USER_LOGIN = new EmployeeDao().findById(1).get(0);
//        } catch (Exception ex) {
//            Logger.getLogger(ShareData.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}
