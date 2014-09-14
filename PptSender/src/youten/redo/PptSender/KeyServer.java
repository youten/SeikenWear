
package youten.redo.PptSender;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class KeyServer {
    private int mPort;
    private Server mServer;
    private KeyEventSender mKeyEventSender;

    /**
     * コンストラクタ
     *
     * @param port Jettyを起動するport
     */
    public KeyServer(int port, KeyEventSender keyEventSender) {
        mKeyEventSender = keyEventSender;
        mPort = port;
    }

    /**
     * Serverを開始する。
     */
    public synchronized void start() {
        if ((mServer != null) && (mServer.isStarted())) {
            return;
        }
        if (mServer == null) {
            // setup Servlet Handler
            ServletContextHandler servletHandler = new ServletContextHandler(
                    ServletContextHandler.SESSIONS);
            // KeyServletを "/key"に割り当て
            KeyServlet keyServlet = new KeyServlet();
            keyServlet.setKeyEventSender(mKeyEventSender);
            servletHandler.addServlet(new ServletHolder(keyServlet), "/key");

            // setup Server and Handler List
            HandlerList handlerList = new HandlerList();
            handlerList.addHandler(servletHandler);
            mServer = new Server(mPort);
            mServer.setHandler(handlerList);
        }

        try {
            mServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Serverを停止する。
     */
    public synchronized void stop() {
        if ((mServer == null) || (mServer.isStopped())) {
            return;
        }
        try {
            mServer.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Serverが起動しているかどうか
     *
     * @return 起動していたらtrueを、それ以外はfalseを返す。
     */
    public synchronized boolean isStarted() {
        if (mServer == null) {
            return false;
        }
        return mServer.isStarted();
    }
}
