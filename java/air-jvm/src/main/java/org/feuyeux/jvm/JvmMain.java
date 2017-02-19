package org.feuyeux.jvm;

import org.feuyeux.jvm.hotswap.Ninety;

import java.net.URL;
import java.net.URLClassLoader;

public class JvmMain {
    public static void main(String[] args) throws Exception {
        String hotSwapJar = "/Users/erichan/cooding/feuyeux/quiland2016/java/air-jvm/swap2/target/hopswap2.jar";

        Ninety ninety = new Ninety();
        System.out.println(ninety.show());

        Thread thread = new Thread(() -> {
            try {
                URL[] urls = new URL[]{(new URL("file:" + hotSwapJar))};
                /*使用线程上下文类加载器，可以在执行线程中抛弃双亲委派加载链模式，使用线程上下文里的类加载器加载类*/
                ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                URLClassLoader loader = new URLClassLoader(urls, contextClassLoader.getParent());

                /*
                Thread.currentThread().setContextClassLoader(loader);
                Ninety ninety1 = new Ninety();
                System.out.println(ninety1.show());
                */

                Class cls = loader.loadClass("org.feuyeux.jvm.hotswap.Ninety");
                Object ninety2 = cls.newInstance();
                System.out.println(cls.getMethod("show").invoke(ninety2));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
        thread.join();
    }
}
