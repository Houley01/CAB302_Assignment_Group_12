package controlpanel;

import javax.swing.*;
import java.awt.*;

public class createBillboards {
    static JInternalFrame window = new JInternalFrame( "Create Billboard", false, false, true);
    public static JInternalFrame createBillboards() {
        window.setSize(600, 300);
        window.setLocation((controlPanel.WINDOWWIDTH/2) - 300, (controlPanel.WINDOWHEIGHT/2) - 200);
//        window.setLayout(new GridLayout(3,2));
        return window;
    }
}
