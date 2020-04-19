package controlpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class listBillboards extends JFrame {
    static JInternalFrame window = new JInternalFrame( "List Billboards");
    public static JInternalFrame listBillboards() {
        window.setSize(600, 300);
        window.setLocation((controlPanel.WINDOWWIDTH/2) - 125, (controlPanel.WINDOWHEIGHT/2) - 100);
        window.setLayout(new GridLayout(3,2));
        return window;
    }

}
