
package youten.redo.PptSender;

//public class PptSender extends JFrame implements ActionListener {
public class PptSender {
    private static final int DEFAULT_PORT = 8088;
    // private static final int WINDOW_WIDTH = 180;
    // private static final int WINDOW_HEIGHT = 100;
    // private static final int WINDOW_MARGIN = 20;
    //
    // private JButton mUpButton = new JButton("Up");
    // private JButton mDownButton = new JButton("Down");
    // private JButton mShiftF5Button = new JButton("Shift+F5");
    // private JButton mEscButton = new JButton("Esc");

    private KeyEventSender mKeyEventSender = new KeyEventSenderImpl();
    private KeyServer mServer;

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        if ((args != null) && (args.length > 0)) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                // ignore;
            }
        }
        PptSender pptSender = new PptSender();
        pptSender.init(port);
        // pptSender.setVisible(true);
    }

    // @Override
    // public void actionPerformed(ActionEvent e) {
    // if (e.getSource() == mUpButton) {
    // mKeyEventSender.send(KeyEvent.VK_UP, false, false, false);
    // } else if (e.getSource() == mDownButton) {
    // mKeyEventSender.send(KeyEvent.VK_DOWN, false, false, false);
    // } else if (e.getSource() == mShiftF5Button) {
    // mKeyEventSender.send(KeyEvent.VK_F5, true, false, false);
    // } else if (e.getSource() == mEscButton) {
    // mKeyEventSender.send(KeyEvent.VK_ESCAPE, false, false, false);
    // } else {
    // // unknown
    // }
    // }

    private void init(int port) {

        // setTitle("PptSender");
        // Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); // 画面全体のサイズ
        // setBounds(d.width - WINDOW_WIDTH - WINDOW_MARGIN, d.height - WINDOW_HEIGHT - WINDOW_MARGIN,
        // WINDOW_WIDTH, WINDOW_HEIGHT);
        // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // setFocusableWindowState(false);
        // setAlwaysOnTop(true);
        // setResizable(false);
        //
        // JPanel panel = new JPanel();
        // getContentPane().add(panel);
        //
        // panel.setLayout(new GridLayout(2, 2));
        // panel.add(mUpButton);
        // panel.add(mDownButton);
        // panel.add(mShiftF5Button);
        // panel.add(mEscButton);
        // mUpButton.addActionListener(this);
        // mDownButton.addActionListener(this);
        // mShiftF5Button.addActionListener(this);
        // mEscButton.addActionListener(this);

        mServer = new KeyServer(port, mKeyEventSender);
        mServer.start();
    }
}
