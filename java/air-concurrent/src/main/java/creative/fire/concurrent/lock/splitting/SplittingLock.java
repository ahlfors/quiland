package creative.fire.concurrent.lock.splitting;

import java.util.HashSet;
import java.util.Set;

public class SplittingLock implements Runnable {
	private final Set<String> users = new HashSet<>();
	private final Set<String> queries = new HashSet<>();
	private int opNum;

	public SplittingLock(int on) {
		opNum = on;
	}

	public synchronized void addUser1(String u) {
		users.add(u);
	}

	public synchronized void addQuery1(String q) {
		queries.add(q);
	}

	public void addUser(String u) {
		synchronized (users) {
			//System.out.println(Thread.currentThread()+" Add:" + u);
			users.add(u);
		}
	}

	public void addQuery(String q) {
		synchronized (queries) {
			//System.out.println(Thread.currentThread()+" Query:" + q);
			queries.add(q);
		}
	}

	@Override
	public void run() {
		for (int i = 0; i < opNum; i++) {
			String user = new String("user");
			user += i;
			//addUser1(user);
			addUser(user);

			String query = new String("query");
			query += i;
			//addQuery1(query);
			addQuery(query);
		}
	}
}
