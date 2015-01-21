package io.core9.commerce.tradetracker;

import io.core9.core.executor.Executor;
import io.core9.core.plugin.Core9Plugin;
import io.core9.plugin.widgets.datahandler.DataHandlerFactory;

public interface TradeTrackerDataHandler<T extends TradeTrackerDataHandlerConfig> extends DataHandlerFactory<T>, Core9Plugin, Executor {

	public static final String SESSION_KEY = "viaTradeTracker";
	public static final String SESSION_PRODUCT_KEY = "commerce.tracking.tradetracker.campaignID";
	
}
