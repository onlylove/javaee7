package com.chenzr.javaee.ejb;

import javax.ejb.Remote;

@Remote
public interface HelloI {

	String sayHello(String msg);
	
}
