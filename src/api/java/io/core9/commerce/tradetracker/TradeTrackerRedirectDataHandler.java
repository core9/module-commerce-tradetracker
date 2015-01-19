package io.core9.commerce.tradetracker;

import io.core9.core.plugin.Core9Plugin;
import io.core9.plugin.widgets.datahandler.DataHandlerFactory;

public interface TradeTrackerRedirectDataHandler<T extends TradeTrackerDataHandlerConfig> extends Core9Plugin, DataHandlerFactory<T> {

}
