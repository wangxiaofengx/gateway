package com.zy.gis.factory;

import com.zy.gis.filter.OGCShareFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractNameValueGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class OGCShareGatewayFilterFactory extends AbstractNameValueGatewayFilterFactory {

    private static final Logger log = LoggerFactory.getLogger(OGCShareGatewayFilterFactory.class);

    private static String filterName = null;

    public static String getFilterName() {
        return filterName;
    }

    private final OGCShareFilter ogcShareFilter;

    public OGCShareGatewayFilterFactory(OGCShareFilter ogcShareFilter) {
        this.ogcShareFilter = ogcShareFilter;
        filterName = this.name();
    }

    @Override
    public GatewayFilter apply(NameValueConfig config) {
        return ogcShareFilter;
    }
}
