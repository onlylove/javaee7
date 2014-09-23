package com.chenzr.javaee.ejb.impl;

import java.util.UUID;

import javax.ejb.Stateless;

import com.chenzr.javaee.ejb.HelloI;

@Stateless
public class HelloWorld implements HelloI {

	@Override
	public String sayHello(String msg) {
		System.out.println(msg);
		return UUID.randomUUID().toString();
	}

}
