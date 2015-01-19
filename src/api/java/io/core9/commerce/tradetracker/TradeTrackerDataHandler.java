package io.core9.commerce.tradetracker;

import io.core9.core.plugin.Core9Plugin;
import io.core9.plugin.widgets.datahandler.DataHandlerFactory;

public interface TradeTrackerDataHandler<T extends TradeTrackerDataHandlerConfig> extends DataHandlerFactory<T>, Core9Plugin {

}
