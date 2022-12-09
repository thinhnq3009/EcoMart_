/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eco.app.entity;

import eco.app.helper.Convertor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Lenovo
 */
public class EntityHelper {

    /**
     * Return Object[] contains data of <code>fieldName</code> passed in
     * +
     * 
     * @param e
     * @param fieldName
     * @return
     */
    public static Object[] getData(Entity e, String... fieldName) {

        if (fieldName == null || fieldName.length == 0) {
            return null;
        }

        Field[] fields = e.getClass().getDeclaredFields();

        if (fieldName[0].equals("all")) {
            Object value[] = new Object[fields.length + 1];
            value[0] = e.getId();
            int counter = 1;
            for (Field field : fields) {
                try {
                    value[counter] = field.get(e);
                } catch (IllegalAccessException | IllegalArgumentException er) {
                    value[counter] = "Can't get";
                } finally {
                    counter++;
                }
            }

            return value;
        }

        int indexId = Arrays.asList(fieldName).indexOf("id");
        int len = fieldName.length;
        // len += indexId != -1 ? 1 : 0;

        Object[] value = new Object[len];
        int counter = 0;

        // Set ID
        if (indexId != -1) {
            value[indexId] = e.getId();
        }

        // Start taking data in field through the name of the fields
        for (String name : fieldName) {

            boolean toCurrency = false;
            boolean toDate = false;
            boolean toTime = false;
            boolean toDateTime = false;

            if (name.endsWith("$")) {
                name = name.replace("$", "");
                toCurrency = true;
            }

            if (name.endsWith("<date>")) {
                name = name.replace("<date>", "");
                toDate = true;
            }

            if (name.endsWith("<time>")) {
                name = name.replace("<time>", "");
                toTime = true;
            }

            if (name.endsWith("<datetime>")) {
                name = name.replace("<datetime>", "");
                toDateTime = true;
            }

            counter += indexId == counter ? 1 : 0;

            for (Field field : fields) {
                if (field.getName().equals(name)) {
                    try {
                        Object object = field.get(e);
                        if (toCurrency) {
                            value[counter] = Convertor.formatCurrency(object);
                        } else if (toDate) {
                            value[counter] = Convertor.formatDate(object);
                        } else if (toTime) {
                            value[counter] = Convertor.formatTime(object);
                        } else if (toDateTime) {
                            value[counter] = Convertor.formatDateTime(object);
                        } else {
                            value[counter] = object;
                        }
                    } catch (IllegalAccessException | IllegalArgumentException er) {
                        value[counter] = "Can't get";
                    } finally {
                        counter++;
                    }

                    break;
                }

            }
        }

        return value;
    }

    public static Entity find(List list, int id) {
        for (Object o : list) {
            if (o instanceof Entity e) {
                if (e.getId() == id) {
                    return e;
                }
            }
        }
        return null;
    }

}
