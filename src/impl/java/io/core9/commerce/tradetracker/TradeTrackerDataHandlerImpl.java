package io.core9.commerce.tradetracker;

import io.core9.module.auth.AuthenticationPlugin;
import io.core9.module.auth.Session;
import io.core9.plugin.server.Cookie;
import io.core9.plugin.server.Server;
import io.core9.plugin.server.request.Request;
import io.core9.plugin.template.closure.ClosureTemplateEngine;
import io.core9.plugin.widgets.datahandler.DataHandler;
import io.core9.plugin.widgets.datahandler.DataHandlerFactoryConfig;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;

@PluginImplementation
public class TradeTrackerDataHandlerImpl<T extends TradeTrackerDataHandlerConfig> implements TradeTrackerDataHandler<T> {
	
	public static final String NAME = "Commerce Tracking - TradeTracker";
	
	@InjectPlugin
	private AuthenticationPlugin auth;
	
	@InjectPlugin
	private Server server;

	@InjectPlugin
	private ClosureTemplateEngine engine;
	
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
		
		@SuppressWarnings("unchecked")
		T config = (T) options;
		
		return new DataHandler<T>() {

			private String getFirstOrDefault(Deque<String> items, String fallback) {
				return items != null && items.size() > 0 ? items.getFirst() : fallback;
			}

			@Override
			public Map<String, Object> handle(Request req) {
				Map<String,Object> result = new HashMap<String, Object>();
				Session session = auth.getUser(req).getSession();
				if(config.isRedirectWidget(req)) {
					session.setAttribute(SESSION_KEY, true);
					redirect(req, session);
				} else if(session.getAttribute(SESSION_KEY) != null) {
					result.put(SESSION_KEY, true);
					result.put("campaignID", session.getAttribute(SESSION_KEY));
					//TODO Not implemented
					result.put("productID", session.getAttribute(SESSION_KEY));
					session.removeAttribute(SESSION_KEY);
				} else {
					result.put(SESSION_KEY, false);
				}
				return result;
			}

			/**
			 * Redirects the user to the TradeTracker system
			 * @param req
			 * @param session
			 */
			private void redirect(Request req, Session session) {
				String campaignID = config.getCampaignID();
				String materialID = config.getMaterialID();
				String affiliateID = config.getAffiliateID();
				String redirectURL = config.getRedirectURL();
				String reference = config.getReference();

				boolean canRedirect = true;
				Map<String, Deque<String>> params = req.getQueryParams();
				if (params.get("campaignID") != null) {
					campaignID = getFirstOrDefault(params.get("campaignID"), campaignID);
					materialID = getFirstOrDefault(params.get("materialID"), materialID);
					affiliateID = getFirstOrDefault(params.get("affiliateID"), affiliateID);
					redirectURL = getFirstOrDefault(params.get("redirectURL"), redirectURL);
				} else if (params.get("tt") != null) {
					String[] trackingData = getFirstOrDefault(params.get("tt"), "").split("_");

					campaignID = trackingData.length >= 1 ? trackingData[0] : "";
					materialID = trackingData.length >= 2 ? trackingData[1] : "";
					affiliateID = trackingData.length >= 3 ? trackingData[2] : "";
					reference = trackingData.length >= 4 ? trackingData[3] : "";

					redirectURL = getFirstOrDefault(params.get("r"), "");
				} else {
					canRedirect = false;
				}
				if (canRedirect) {
					try {
						// Calculate MD5 checksum.
						MessageDigest md = MessageDigest.getInstance("MD5");
						String MD5String = "CHK_" + campaignID + "::" + materialID + "::" + affiliateID + "::"
								+ reference;

						byte[] ba_md5string = MD5String.getBytes();
						byte[] ba_md5string_encoded = md.digest(ba_md5string);

						StringBuffer sb = new StringBuffer();

						for (int i = 0; i < ba_md5string_encoded.length; ++i) {
							sb.append(Integer.toHexString((ba_md5string_encoded[i] & 0xFF) | 0x100).substring(1, 3));
						}

						String MD5Checksum = sb.toString();

						// Calculate unix time stamp.
						long timeStamp = System.currentTimeMillis() / 1000;

						// Set session/cookie arguments.
						String cookieName = "TT2_" + campaignID;
						String cookieValue = materialID + "::" + affiliateID + "::" + reference + "::" + MD5Checksum + "::" + timeStamp;

						// Create tracking cookie.
						Cookie trackingCookie = server.newCookie(cookieName, cookieValue);
						trackingCookie.setMaxAge(60 * 60 * 24 * config.getCookieDays());
						trackingCookie.setPath("/");

						if (config.getCookieDomain() != null && !config.getCookieDomain().equals("")) {
							trackingCookie.setDomain("." + config.getCookieDomain());
						}

						req.getResponse().addCookie(trackingCookie);

						// Set session data.
						session.setAttribute(cookieName, cookieValue);
						session.setAttribute(SESSION_KEY, campaignID);

						// Set track-back URL.
						String trackBackURL = "http://tc.tradetracker.net/?c=" + campaignID + "&m=" + materialID
								+ "&a=" + affiliateID + "&r=" + java.net.URLEncoder.encode(reference, "UTF-8") + "&u="
								+ java.net.URLEncoder.encode(redirectURL, "UTF-8");

						// Redirect to TradeTracker.
						req.getResponse().putHeader("P3P: CP", "IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT");
						req.getResponse().sendRedirect(301, trackBackURL);
					} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
						e.printStackTrace();
						req.getResponse().end();
					}
				}
			}

			@Override
			public T getOptions() {
				return config;
			}
			
		};
	}
}

