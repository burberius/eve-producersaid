package net.troja.eve.producersaid;

/*
 * ========================================================================
 * Eve Producer's Aid
 * ------------------------------------------------------------------------
 * Copyright (C) 2015 Jens Oberender <j.obi@troja.net>
 * ------------------------------------------------------------------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * ========================================================================
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.troja.eve.producersaid.data.EveCentralPrice;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EveCentral {
    private static final Logger LOGGER = LoggerFactory.getLogger(EveCentral.class);
    private static final String USER_AGENT = "Producer's Aid Java tool - https://github.com/burberius/producersaid - ";
    private static final String BASE_URL = "http://api.eve-central.com/api/marketstat/json?usesystem=30000142";
    private static final String URL_TYPE = "&typeid=";
    private static final long CACHE_TIME = 1000 * 60 * 60; // 1 hour
    private static final String NODE_BUY = "buy";
    private static final String NODE_SELL = "sell";
    private static final String NODE_FORQUERY = "forQuery";
    private static final String NODE_MIN = "min";
    private static final String NODE_MAX = "max";
    private static final String NODE_5PERCENT = "fivePercent";
    private static final String NODE_VOLUME = "volume";
    private static final String NODE_GENERATED = "generated";
    private static final String NODE_TYPES = "types";

    private final HashMap<Integer, EveCentralPrice> priceCache;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private String fileName = null;

    public EveCentral() {
        priceCache = new HashMap<>();
    }

    public EveCentralPrice getPrice(final int typeId) {
        if (!priceCache.containsKey(typeId)) {
            downloadPrices(Arrays.asList(typeId));
        }
        return priceCache.get(typeId);
    }

    public Map<Integer, EveCentralPrice> getPrices(final List<Integer> typeIds) {
        final Map<Integer, EveCentralPrice> queryPrices = queryPrices(typeIds);
        if (queryPrices.size() < typeIds.size()) {
            final List<Integer> downloadIds = new ArrayList<Integer>();
            for (final Integer id : typeIds) {
                if (!queryPrices.containsKey(id)) {
                    downloadIds.add(id);
                }
            }
            queryPrices.putAll(downloadPrices(downloadIds));
        }
        return queryPrices;
    }

    private Map<Integer, EveCentralPrice> queryPrices(final List<Integer> typeIds) {
        final Map<Integer, EveCentralPrice> allPresent = new HashMap<Integer, EveCentralPrice>();
        final List<Integer> downloadIds = new ArrayList<>();
        for (final int id : typeIds) {
            final EveCentralPrice price = priceCache.get(id);
            if (price == null) {
                downloadIds.add(id);
                continue;
            }
            if (price.getTime() < (System.currentTimeMillis() - CACHE_TIME)) {
                downloadIds.add(id);
                continue;
            }
            allPresent.put(id, price);
        }
        if (downloadIds.size() > 0) {
            allPresent.putAll(downloadPrices(downloadIds));
        }
        return allPresent;
    }

    private Map<Integer, EveCentralPrice> downloadPrices(final List<Integer> typeIds) {
        final String urlString = getUrl(typeIds);
        Map<Integer, EveCentralPrice> prices = null;
        try {
            prices = parsePrices(getData(urlString));
        } catch (final IOException e) {
            LOGGER.error("Could not download eve central prices: " + e.getMessage(), e);
            return null;
        }
        priceCache.putAll(prices);
        LOGGER.info("Downloaded " + prices.size() + " prices from Eve Central");
        return prices;
    }

    private String getData(final String url) throws IOException {
        if (fileName == null) {
            final CloseableHttpClient httpclient = HttpClients.createDefault();
            try {
                final HttpGet request = new HttpGet(url);
                request.setHeader("User-Agent", USER_AGENT);
                final HttpResponse response = httpclient.execute(request);
                final int status = response.getStatusLine().getStatusCode();
                if ((status >= 200) && (status < 300)) {
                    final HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new IOException("Download error: " + status + " " + response.getStatusLine().getReasonPhrase());
                }
            } finally {
                httpclient.close();
            }
        } else {
            return new String(Files.readAllBytes(Paths.get(fileName)));
        }
    }

    private Map<Integer, EveCentralPrice> parsePrices(final String content) {
        final Map<Integer, EveCentralPrice> map = new HashMap<>();
        try {
            final JsonNode node = objectMapper.readTree(content);
            final Iterator<JsonNode> iter = node.elements();
            while (iter.hasNext()) {
                final EveCentralPrice price = convertToObject(iter.next());
                map.put(price.getTypeId(), price);
            }

        } catch (final IOException e) {
            LOGGER.error("Could not parse data", e);
        }
        return map;
    }

    private EveCentralPrice convertToObject(final JsonNode node) {
        final EveCentralPrice price = new EveCentralPrice();
        final JsonNode buy = node.path(NODE_BUY);
        price.setTypeId(buy.path(NODE_FORQUERY).path(NODE_TYPES).get(0).asInt());
        price.setTime(buy.path(NODE_GENERATED).asLong());
        price.setBuyMax((float) buy.path(NODE_MAX).asDouble());
        price.setBuy5Percent((float) buy.path(NODE_5PERCENT).asDouble());
        price.setBuyVolume(buy.path(NODE_VOLUME).asLong());
        final JsonNode sell = node.path(NODE_SELL);
        price.setSellMin((float) sell.path(NODE_MIN).asDouble());
        price.setSell5Percent((float) sell.path(NODE_5PERCENT).asDouble());
        price.setSellVolume(sell.path(NODE_VOLUME).asLong());
        return price;
    }

    private String getUrl(final List<Integer> typeIds) {
        final StringBuilder url = new StringBuilder(BASE_URL);
        for (final Integer value : typeIds) {
            url.append(URL_TYPE).append(value);
        }
        return url.toString();
    }

    public void setFilename(final String name) {
        fileName = name;
    }

}
