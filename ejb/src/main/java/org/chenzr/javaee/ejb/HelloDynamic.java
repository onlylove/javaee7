package org.chenzr.javaee.ejb;

//
import javax.management.*;
import java.lang.reflect.Constructor;
import java.util.Iterator;

/**
 * @author Sunny Peng
 * @version 1.0
 */

public class HelloDynamic implements DynamicMBean {

	// 这是我们的属性名称
	private String name = "";

	private MBeanInfo mBeanInfo = null;
	private String className = this.getClass().getName();
	private String description = "Simple implementation of a dynamic MBean.";

	private MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[1];
	private MBeanConstructorInfo[] constructors = new MBeanConstructorInfo[1];
	private MBeanOperationInfo[] operations = new MBeanOperationInfo[1];
	MBeanNotificationInfo[] mBeanNotificationInfoArray = new MBeanNotificationInfo[0];

	public HelloDynamic() {

		// 设定一个属性
		attributes[0] = new MBeanAttributeInfo("Name", "java.lang.String",
				"Name: name string.", true, true, false);

		// 设定构造函数
		Constructor[] thisconstructors = this.getClass().getConstructors();
		constructors[0] = new MBeanConstructorInfo(
				"HelloDynamic(): Constructs a HelloDynamic object",
				thisconstructors[0]);

		// operate method 我们的操作方法是print
		MBeanParameterInfo[] params = null;
		operations[0] = new MBeanOperationInfo("print",
				"print(): print the name", params, "void",
				MBeanOperationInfo.INFO);

		mBeanInfo = new MBeanInfo(className, description, attributes,
				constructors, operations, mBeanNotificationInfoArray);

	}

	@Override
	public Object getAttribute(String attribute_name) {
		if (attribute_name.equals("Name"))
			return name;
		return null;
	}

	@Override
	public AttributeList getAttributes(String[] attributeNames) {
		AttributeList resultList = new AttributeList();

		// if attributeNames is empty, return an empty result list
		if (attributeNames.length == 0)
			return resultList;

		for (int i = 0; i < attributeNames.length; i++) {
			try {
				Object value = getAttribute((String) attributeNames[i]);
				resultList.add(new Attribute(attributeNames[i], value));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return resultList;

	}

	@Override
	public void setAttribute(Attribute attribute) {
		String Name = attribute.getName();
		Object value = attribute.getValue();

		try {
			if (Name.equals("Name")) {
				// if null value, try and see if the setter returns any
				// exception
				if (value == null) {
					name = null;
				}
				// if non null value, make sure it is assignable to the
				// attribute
				else if ((Class.forName("java.lang.String"))
						.isAssignableFrom(value.getClass())) {
					{
						name = (String) value;
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public AttributeList setAttributes(AttributeList attributes) {
		AttributeList resultList = new AttributeList();

		// if attributeNames is empty, nothing more to do
		if (attributes.isEmpty())
			return resultList;

		// for each attribute, try to set it and add to the result list if
		// successfull
		for (Iterator i = attributes.iterator(); i.hasNext();) {
			Attribute attr = (Attribute) i.next();
			try {
				setAttribute(attr);
				String name = attr.getName();
				Object value = getAttribute(name);
				resultList.add(new Attribute(name, value));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return resultList;

	}

	public Object invoke(String operationName, Object params[],
			String signature[]) throws MBeanException, ReflectionException {

		// Check for a recognized operation name and call the corresponding
		// operation
		if (operationName.equals("print")) {
			// 具体实现我们的操作方法print
			System.out.println("Hello, " + name + ", this is HellDynamic!");
			return null;
		} else {
			// unrecognized operation name:
			throw new ReflectionException(new NoSuchMethodException(
					operationName), "Cannot find the operation "
					+ operationName + " in " + className);
		}

	}

	@Override
	public MBeanInfo getMBeanInfo() {
		return mBeanInfo;
	}
}
