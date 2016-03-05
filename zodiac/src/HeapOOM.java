import java.util.ArrayList;
import java.util.List;

/**
 * Args:
 * -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
 * 
 * -verbose:gc -Xms20M -Xmx20M -XX:+HeapDumpOnOutOfMemoryError
 * stand-alone Memory Analyzer:
 * http://www.eclipse.org/mat/downloads.php
 */
public class HeapOOM {
	static class OOMObject {

	}

	public static void main(String[] args) {
		List<OOMObject> list = new ArrayList<OOMObject>();
		while (true) {
			list.add(new OOMObject());
		}
	}
}