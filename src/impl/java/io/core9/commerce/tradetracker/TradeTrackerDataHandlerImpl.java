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
public class TradeTrackerDataHandlerImpl<T extends TradeTrackerDataHandlerConfig> implements TradeTrackerDataHandler<T> {
	
	public static final String NAME = "TradeTracker - Conversion";
	
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
				Map<String,Object> result = new HashMap<String, Object>();
				if(session.getAttribute(TradeTrackerDataHandlerConfig.SESSION_KEY) != null) {
					result.put(TradeTrackerDataHandlerConfig.SESSION_KEY, true);
				} else {
					result.put(TradeTrackerDataHandlerConfig.SESSION_KEY, false);
				}
				return result;
			}


			@SuppressWarnings("unchecked")
			@Override
			public T getOptions() {
				// TODO Auto-generated method stub
				return (T) options;
			}
			
		};
	}

	
}
