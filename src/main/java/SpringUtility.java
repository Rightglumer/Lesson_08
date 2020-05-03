import javax.naming.ldap.Control;
import javax.swing.*;
import java.awt.*;

public class SpringUtility {
    public static final int SL_PAD = 5;

    public static final int SU_TOPLEFT = 0;
    public static final int SU_TOPLEFT_BASE = 1;
    public static final int SU_BOTTOMRIGHT = 2;
    public static final int SU_BOTTOMRIGHT_BASE = 3;
    public static final int SU_BOTTOMBASE_RIGHT = 4;
    public static final int SU_TOP_LEFTBASE = 5;

    SpringLayout layout;
    Container contentPane;

    public SpringUtility(SpringLayout layout, Container contentPane){
        this.contentPane = contentPane;
        this.layout = layout;
    }

    public void setRelative(Component obj, Component obj1, Component obj2, int position) throws IllegalStateException {
        String posObjY;
        String posObj1Y;
        String posObjX;
        String posObj2X;
        int pad = SL_PAD;
        switch (position){
            case (SU_TOPLEFT) : posObjY = SpringLayout.NORTH; posObj1Y = SpringLayout.SOUTH;
                                posObjX = SpringLayout.WEST; posObj2X = SpringLayout.EAST; break;
            case (SU_TOPLEFT_BASE) : posObjY = SpringLayout.NORTH; posObj1Y = SpringLayout.NORTH;
                                posObjX = SpringLayout.WEST; posObj2X = SpringLayout.WEST; break;
            case (SU_TOP_LEFTBASE) : posObjY = SpringLayout.NORTH; posObj1Y = SpringLayout.SOUTH;
                                posObjX = SpringLayout.WEST; posObj2X = SpringLayout.WEST; break;
            case (SU_BOTTOMRIGHT) : posObjY = SpringLayout.SOUTH; posObj1Y = SpringLayout.NORTH;
                                posObjX = SpringLayout.EAST; posObj2X = SpringLayout.WEST; pad = 1; break;
            case (SU_BOTTOMBASE_RIGHT) : posObjY = SpringLayout.SOUTH; posObj1Y = SpringLayout.SOUTH;
                                posObjX = SpringLayout.EAST; posObj2X = SpringLayout.WEST; pad = 1; break;
            case (SU_BOTTOMRIGHT_BASE) : posObjY = SpringLayout.SOUTH; posObj1Y = SpringLayout.SOUTH;
                                posObjX = SpringLayout.EAST; posObj2X = SpringLayout.EAST; pad = 1; break;
            default:
                throw new IllegalStateException("Unexpected value: " + position);
        }
        layout.putConstraint(posObjY, obj, pad, posObj1Y, obj1);
        layout.putConstraint(posObjX, obj, pad, posObj2X, obj2);
        contentPane.add(obj);
    }
}
