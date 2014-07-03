package com.sample;

import java.util.ArrayList;
import java.util.List;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.event.KnowledgeRuntimeEventManager;
import org.kie.internal.logger.KnowledgeRuntimeLogger;
import org.kie.internal.logger.KnowledgeRuntimeLoggerFactory;

/**
 * This is a sample class to launch a rule.
 */
public class DroolsTest {

	private static String[] names = new String[] { "foo", "bar", "baz" };
	private static String[] roles = new String[] { "devel", "devel", "manager" };
	private static int[] workingHours = new int[] { 5, 8, 12 };
	private static int[] salary = new int[] { 10, 10, 10 };

	public static final void main(String[] args) {

		KnowledgeRuntimeLogger logger = null;
		try {
			// load up the knowledge base
			KieServices ks = KieServices.Factory.get();
			KieContainer kContainer = ks.getKieClasspathContainer();
			KieSession kSession = kContainer.newKieSession("ksession-rules");
			logger = KnowledgeRuntimeLoggerFactory.newFileLogger((KnowledgeRuntimeEventManager) kSession,
					"c:/temp/drools");

			// go !
			List<Person> persons = new ArrayList<Person>();
			for (int i = 0; i < names.length; i++) {
				Person p = new Person();
				p.setName(names[i]);
				p.setRole(roles[i]);
				p.setSalary(salary[i]);
				p.setWorkingHours(workingHours[i]);
				persons.add(p);
				kSession.insert(p);
			}
			kSession.fireAllRules();

			System.out.println("---------------------------------");
			Person person = persons.get(0);
			person.setWorkingHours(15);
			kSession.update(kSession.getFactHandle(person), person);
			kSession.fireAllRules();
			System.out.println("---------------------------------");

			for (Person p : persons) {
				System.out.println(p);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			logger.close();
		}
	}

	public static class Person {

		private String name;
		private String role;
		private int salary;
		private int raise;
		private int workingHours;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setRole(String role) {
			this.role = role;
		}

		public String getRole() {
			return role;
		}

		public int getWorkingHours() {
			return workingHours;
		}

		public void setWorkingHours(int workingHours) {
			this.workingHours = workingHours;
		}

		public void setSalary(int salary) {
			this.salary = salary;
		}

		public int getSalary() {
			return salary + raise;
		}

		public void setRaise(int raise) {
			this.raise = raise;
		}

		public int getRaise() {
			return raise;
		}

		@Override
		public String toString() {
			return name + "[role:" + role + "][hours:" + workingHours
					+ "][salary:" + getSalary() + "]";
		}
	}

}
