package io.core9.commerce.tradetracker;

import io.core9.module.auth.AuthenticationPlugin;
import io.core9.module.auth.Session;
import io.core9.plugin.server.request.Request;
import io.core9.plugin.widgets.datahandler.DataHandler;
import io.core9.plugin.widgets.datahandler.DataHandlerFactoryConfig;

import java.util.HashMap;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;

@PluginImplementation
public class TradeTrackerRedirectDataHandlerImpl<T extends TradeTrackerDataHandlerConfig> implements TradeTrackerRedirectDataHandler<T> {
	
	public static final String NAME = "TradeTracker - Redirect";
	
	@InjectPlugin
	private AuthenticationPlugin auth;

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Class<? extends DataHandlerFactoryConfig> getConfigClass() {
		return TradeTrackerDataHandlerConfig.class;
	}

	@Override
	public DataHandler<T> createDataHandler(DataHandlerFactoryConfig options) {
		return new DataHandler<T>() {

			@Override
			public Map<String, Object> handle(Request req) {
				Session session = auth.getUser(req).getSession();
				session.setAttribute(TradeTrackerDataHandlerConfig.SESSION_KEY, true);
				
				return new HashMap<String,Object>();
			}

			@SuppressWarnings("unchecked")
			@Override
			public T getOptions() {
				return (T) options;
			}
		};
	}

}
