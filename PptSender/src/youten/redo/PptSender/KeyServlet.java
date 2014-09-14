
package youten.redo.PptSender;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class KeyServlet extends HttpServlet {
    private static final long serialVersionUID = 7057169368901522854L;
    
    private static final String KEY_KEY = "key"; // keycode
    private static final String KEY_SHIFT = "shift";
    private static final String KEY_ALT = "alt";
    private static final String KEY_CTRL = "ctrl";
    private static final String VALUE_1 = "1"; // shift, ctrl, altが有効なことを示す。

    public static final String LS = System.getProperty("line.separator");

    private KeyEventSender mKeyEventSender;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        StringBuffer sb = new StringBuffer("params:");
        int keycode = -1;
        String keyStr = req.getParameter(KEY_KEY);
        if (keyStr != null) {
            try {
                keycode = Integer.parseInt(keyStr);
            } catch (NumberFormatException e) {
                // ignore;
            }
        }
        boolean withShift = VALUE_1.equals(req.getParameter(KEY_SHIFT));
        boolean withAlt = VALUE_1.equals(req.getParameter(KEY_ALT));
        boolean withCtrl = VALUE_1.equals(req.getParameter(KEY_CTRL));

        if ((mKeyEventSender != null) && (keycode > 0)) {
            try {
                mKeyEventSender.send(keycode, withShift, withAlt, withCtrl);
            } catch (IllegalArgumentException e) {
                // ignore;
            }
        }
        sb.append("keycode=").append(keycode);
        sb.append(" shift=").append(withShift);
        sb.append(" alt=").append(withAlt);
        sb.append(" ctrl=").append(withCtrl);

        responseHtml(resp, KeyServlet.class.getSimpleName(), sb.toString());
    }

    public void setKeyEventSender(KeyEventSender keyEventSender) {
        mKeyEventSender = keyEventSender;
    }

    public KeyEventSender getKeyEventSender() {
        return mKeyEventSender;
    }

    /**
     * 単純なHtmlを返す。
     *
     * @param resp
     * @param title
     * @param message
     * @throws IOException
     */
    private static void responseHtml(HttpServletResponse resp, String title, String message)
            throws IOException {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html");
        StringBuilder sb = new StringBuilder();
        sb.append("<html>").append(LS);
        sb.append("<head><title>").append(title).append("</title></head>").append(LS);
        sb.append("<body><h1>").append(title).append("</h1>").append(LS);
        sb.append("<p>").append(message).append("</p>").append(LS);
        sb.append("<a href=\"/key?key=38\">Up</a><br>").append(LS);
        sb.append("<a href=\"/key?key=40\">Down</a><br>").append(LS);
        sb.append("<a href=\"/key?key=116&shift=1\">Shift+F5</a>(keynote)<br>").append(LS);
        sb.append("<a href=\"/key?key=27\">ESC</a><br>").append(LS);
        sb.append("</body></html>").append(LS);

        resp.getWriter().println(sb.toString());
    }
}
