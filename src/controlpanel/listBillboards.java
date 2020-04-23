package controlpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class listBillboards extends JFrame {
    static JInternalFrame window = new JInternalFrame( "List Billboards", false, false, true);
    public static JInternalFrame listBillboards() {
        window.setSize(600, 300);
        window.setLocation((controlPanel.WINDOWWIDTH/2) - 300, (controlPanel.WINDOWHEIGHT/2) - 200);
//        window.setLayout(new GridLayout(3,2));
        return window;
    }

}
