import java.util.ArrayList;
import java.util.List;

/*
 * -XX:PermSize=10M -XX:MaxPermSize=10M
 */
public class RuntimeConstantPoolOOM {
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		int i = 0;
		while (true) {
			System.out.println(i);
			list.add(String.valueOf(i++).intern());
		}
	}
}
