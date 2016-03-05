import java.lang.reflect.Method;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 方法区溢出：
        方法区用于存放Class的相关信息，如类名，访问修饰符，常量池，字段描述，方法描述等。
        对于这个区域的测试，大概思路是运行时产生大量的类去填满方法区，直到溢出，本例使用CGLib直接操作字节码，
        生成大量动态类
 *-XX:PermSize=10M -XX:MaxPermSize=10M 
 */
public class JavaMethodAreaOOM {
        public static void main(String[] args) {
                while(true){
                        Enhancer enhancer = new Enhancer();
                        enhancer.setSuperclass(OOMObject.class);
                        enhancer.setUseCache(false);
                        enhancer.setCallback(new MethodInterceptor() {
                                public Object intercept(Object obj, Method method, Object[] args,
                                                MethodProxy proxy) throws Throwable {
                                        return proxy.invokeSuper(obj, args);
                                }
                        });
                        enhancer.create();
                }
        }
        static class OOMObject{
        }
}