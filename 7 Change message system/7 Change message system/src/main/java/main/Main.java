package main;

import accountServer.AccountServer;
import accountServer.AccountServerI;
import accountServer.AccountService;
import accountServer.UserProfile;
import chat.WebSocketChatServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import resources.ResourceServerController;
import resources.ResourceServerControllerMBean;
import resources.ResourceServerI;
import resources.TestResource;
import servlets.ResourcesPageServlet;
import servlets.SignInServlet;
import servlets.SignUpServlet;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class Main {

    static final Logger logger = LogManager.getLogger(Main.class.getName());
    private static final int THREADS_NUMBER = 10;

    public static void main(String[] args) throws Exception {
        int port = Integer.valueOf(8080);

        logger.info("Starting at http://127.0.0.1:" + port);
        ResourceServerI resourceServer = new TestResource();
        AccountServerI accountServer = new AccountServer(10);
        ResourceServerControllerMBean serverStatistics = new ResourceServerController(resourceServer);
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("Admin:type=ResourceServerController");
        mbs.registerMBean(serverStatistics, name);

        Server server = new Server(port);
        AccountService accountService = new AccountService();
        accountService.addNewUser(new UserProfile("admin"));
        accountService.addNewUser(new UserProfile("test"));
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new ResourcesPageServlet(resourceServer)), ResourcesPageServlet.PAGE_URL);
        context.addServlet(new ServletHolder(new SignUpServlet(accountService)), "/signup");
        context.addServlet(new ServletHolder(new SignInServlet(accountService)), "/signin");
        context.addServlet(new ServletHolder(new WebSocketChatServlet()), "/chat");
        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("static");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});
        server.setHandler(handlers);

        server.start();
        System.out.println("Server started");
        logger.info("Server started");

        server.join();
        for (int i = 0; i < THREADS_NUMBER; ++i) {
            Thread thread = new RandomSequenceExample();
            System.out.println("Start: " + thread.getName());
            thread.start();
        }
    }

    private static class RandomSequenceExample extends Thread {
        public void run() {
            System.out.println("Run: " + this.getName());
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    private static class SerialSequenceExample extends Thread {
        private static int currentMax = 1;
        private int mainId;
        private final static Object waitObject = new Object();

        public SerialSequenceExample(int id) {
            this.mainId = id;
        }

        public void run() {
            try {
                System.out.println("Run: " + mainId);
                synchronized (waitObject) {
                    while (mainId > currentMax) {
                        waitObject.wait();
                    }

                    currentMax++;
                    waitObject.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
