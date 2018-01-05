package com.redhat.training.msa.hola.ft;

import javax.inject.Inject;

import org.eclipse.microprofile.faulttolerance.ExecutionContext;
import org.eclipse.microprofile.faulttolerance.FallbackHandler;
import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.annotation.Metric;

public class AlohaServiceFallback implements FallbackHandler<String>{
	
	@Inject
	@Metric(name = "failureCount", description = "Total chained endpoint failures encountered", 
    		displayName="HolaResource#failureCount", absolute=true)
	private Counter failedCount;
	
	@Override
	public String handle(ExecutionContext context) {
		failedCount.inc();
		return "Aloha fallback";
	}
}
