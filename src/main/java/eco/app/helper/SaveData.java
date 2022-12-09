/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eco.app.helper;

import eco.app.entity.Rank;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Lenovo
 */
public class SaveData {

    public static String NAME_SHOP = "Eco Mart";

    public static Color TEXT = new Color(255, 255, 255);

    /*
    Background color 
     */
    public static Color BG_MENU_L = new Color(51, 102, 255);
    public static Color BG_MENU_R = new Color(20, 186, 244);
    public static Color BG_HEADER = new Color(81, 202, 245);
    public static Color BG_CONTAINER = new Color(184, 232, 252);
    public static Color BG_CONTENT = new Color(184, 232, 252);

    public static Color BG_DANGER = new Color(235, 143, 152);

    /*
    Button Color
     */
    public static Color BTN_DANGER = new Color(220, 53, 69);
    public static Color BTN_SUCCESS = new Color(40, 167, 69);
    public static Color BTN_WARNING = new Color(255, 193, 7);
    public static Color BTN_DEFAULT = new Color(23, 162, 184);
    public static Color BTN_RIPPLE_EFFECT = new Color(125, 229, 237);

    /*
    Menu Item
     */
    public static Color MN_ITEM_HOVER_EFFECT = new Color(0, 0, 204);
    /*
    table header
     */
    public static Color TBL_HEADER = new Color(51, 163, 255);
    public static Color TBL_TEXT_COLOR_HEADER = Color.BLACK;
    public static Color TBL_SELECTED_BG_COLOR = new Color(204, 204, 255);
    public static Color TBL_SELECTED_FG_COLOR = new Color(102, 102, 102);
    public static Color GRIL_TABLE_COLOR = new Color(153, 153, 255);

    /*
    tabbed 
     */
    public static Color TABBED = new Color(93, 167, 219);

    /*
    textFiled
     */
    public static Color TXT_UNDER_LINE = new Color(231, 231, 0);
    public static Color TXT_RIPPLE_EFFECT = new Color(0, 153, 255);

    /*
    Product item
     */
    public static Color PRODUCT_ITEM = new Color(124, 227, 247);
    public static Dimension SIZE = new Dimension(125, 125);

    /*
    DATE
     */
    public static String PATTERN_DATE = "dd/MM/yyyy";
    public static String PATTERS_TIME = "hh:mm";
    public static String PATTERS_DATETIME = "dd/MM/yyyy hh:mm";


    /*
    RANK 
     */
    public final static List<Rank> RANKS = new ArrayList<>();

    static {
        RANKS.add(new Rank("Bronze", 0, 0.001f));
        RANKS.add(new Rank("Silver", 1000000, 0.0013f));
        RANKS.add(new Rank("Gold", 2500000, 0.0018f));
        RANKS.add(new Rank("Platinum", 5000000, 0.0025f));
        RANKS.add(new Rank("Dianond", 8000000, 0.003f));
        Collections.sort(RANKS, (Rank o2, Rank o1) -> o1.getMinSpent() - o2.getMinSpent());
    }
}
