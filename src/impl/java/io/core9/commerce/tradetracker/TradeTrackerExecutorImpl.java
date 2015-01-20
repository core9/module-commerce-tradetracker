package io.core9.commerce.tradetracker;

import io.core9.plugin.template.closure.ClosureTemplateEngine;

import java.io.IOException;
import java.io.InputStreamReader;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;

import com.google.common.io.CharStreams;

@PluginImplementation
public class TradeTrackerExecutorImpl implements TradeTrackerExecutor {

	@InjectPlugin
	private ClosureTemplateEngine engine;

	@Override
	public void execute() {
		try {
			engine.addString("io.core9.commerce.tracking.tradetracker", 
					CharStreams.toString(new InputStreamReader(this.getClass().getResourceAsStream("/tradetracker/tradetracker.soy"))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
