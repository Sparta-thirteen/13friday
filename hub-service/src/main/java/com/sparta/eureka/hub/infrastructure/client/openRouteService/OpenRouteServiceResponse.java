package com.sparta.eureka.hub.infrastructure.client.openRouteService;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OpenRouteServiceResponse {
        private List<Feature> features;

        @Getter
        @Setter
        public static class Feature {
            private Properties properties;

            @Getter
            @Setter
            public static class Properties {
                private Summary summary;

                @Getter
                @Setter
                public static class Summary {
                    private double distance;
                    private double duration;
                }
            }
        }

}
