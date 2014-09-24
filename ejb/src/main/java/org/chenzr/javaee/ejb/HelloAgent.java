package org.chenzr.javaee.ejb;

//
import java.util.*;
import java.io.*;
import java.net.*;
//RI imports
//
import javax.management.ObjectName;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;

import javax.management.Attribute;

public class HelloAgent {

	public HelloAgent() {
	}

	public static void main(String[] args) {

		// CREATE the MBeanServer
		//
		System.out.println("\n\tCREATE the MBeanServer.");
		MBeanServer server = MBeanServerFactory.createMBeanServer();

		String domain = server.getDefaultDomain();
		ObjectName object_name = null;
		// 使用HelloDynamic 如果为Hello 就是使用我们上章介绍的StandardMbean了
		String mbeanName = "HelloDynamic";
		try {

			object_name = new ObjectName(domain + ":type=" + mbeanName);

			server.createMBean(mbeanName, object_name);

			Attribute stateAttribute = new Attribute("Name", "I am big one");
			server.setAttribute(object_name, stateAttribute);

			String name = (String) server.getAttribute(object_name, "Name");
			System.out.println("name now is " + name);

			server.invoke(object_name, "print", null, null);
		} catch (Exception e) {
			System.out.println("\t!!! Could not create the Hello agent !!!");
			e.printStackTrace();
			return;
		}
	}
}