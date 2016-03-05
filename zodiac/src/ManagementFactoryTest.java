import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

public class ManagementFactoryTest {
	public static void main(String[] args) {
		iterateMethods(ManagementFactory.getClassLoadingMXBean());
		iterateMethods(ManagementFactory.getMemoryMXBean());
		iterateMethods(ManagementFactory.getThreadMXBean());
		iterateMethods(ManagementFactory.getRuntimeMXBean());
		iterateMethods(ManagementFactory.getCompilationMXBean());
		iterateMethods(ManagementFactory.getOperatingSystemMXBean());
		listMethods(ManagementFactory.getMemoryPoolMXBeans());
		listMethods(ManagementFactory.getMemoryManagerMXBeans());
		listMethods(ManagementFactory.getGarbageCollectorMXBeans());
	}

	private static void listMethods(List<?> mxBeanList) {
		System.out.println("----");
		for (Object mxBean : mxBeanList) {
			iterateMethods(mxBean);
		}
		System.out.println("----");
	}

	private static void iterateMethods(Object mxBean) {
		System.out.println(mxBean + ":");
		Method[] declaredMethods = mxBean.getClass().getDeclaredMethods();
		for (Method method : declaredMethods) {
			method.setAccessible(true);
			String methodName = method.getName();
			if (methodName.startsWith("get") && Modifier.isPublic(method.getModifiers())) {
				Object value;
				try {
					value = method.invoke(mxBean);
				} catch (Exception e) {
					value = e;
				} // try
				System.out.println(methodName + " = " + value);
			} // if
		} // for
		System.out.println();
	}
}