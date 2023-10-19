package dev.amrv.sge.event;

import dev.amrv.sge.SGE;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class TestConsumer implements EventListener {

    @Listen(EventTest.class)
    public void consume(EventTest e) {
        System.out.println("T0: " + e.message);
    }

    @Listen(EventTest.class)
    public void consume2(EventTest e) {
        System.out.println("T1: " + e.message);
    }

    public static void consume3(EventTest e) {
        System.out.println("T2: " + e.message);
    }

    public static void consume4(EventTestSub e) {
        System.out.println("T2: " + e.message + "(" + e.cc + ")");
    }

    @Listen(EventTestSub.class)
    public void consume5(EventTest e) {
        System.out.println("T3: " + e.message);
    }

    public static void main(String[] args) throws InterruptedException {
        SGE system = null;

        EventSystem es = new EventSystem(system, 5);

        es.addListener(new TestConsumer());
        es.addListener(EventTest.class, TestConsumer::consume3);
        es.addListener(EventTestSub.class, TestConsumer::consume4);
        es.queueEvent(new EventTest("Prueba"));
        es.queueEvent(new EventTest("Prueba 2"));
        es.queueEvent(new EventTest("Prueba 3"));
        es.queueEvent(new EventTest("Prueba 4"));

        es.start();
        Thread.sleep(1000);
//        es.stop();
    }

}
