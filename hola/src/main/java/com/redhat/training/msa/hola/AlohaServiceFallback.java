package com.redhat.training.msa.hola;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.microprofile.faulttolerance.ExecutionContext;
import org.eclipse.microprofile.faulttolerance.FallbackHandler;

public class AlohaServiceFallback implements FallbackHandler<List<String>>{

	@Inject
	private HolaResource holaService;
	
	@Override
	public List<String> handle(ExecutionContext context) {
		List<String> hellos = new ArrayList<String>();
		hellos.add(holaService.hola());
		hellos.add("aloha fallback");
		return hellos;
	}
}
