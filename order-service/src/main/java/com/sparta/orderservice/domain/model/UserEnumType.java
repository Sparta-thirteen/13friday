package com.sparta.orderservice.domain.model;

public enum UserEnumType {
    MASTER, HUB_MANAGER, DELIVERY_AGENT, PARTNER;


//
//    CUSTOMER("ROLE_CUSTOMER"),
//    OWNER("ROLE_OWNER"),
//    MANAGER("ROLE_MANAGER"),
//    MASTER("ROLE_MASTER");


    public static class Authority {
        public static final String MASTER = "ROLE_MASTER";

        public static final String HUB_MANAGER = "ROLE_HUB_MANAGER";
        public static final String DELIVERY_AGENT = "ROLE_DELIVERY_AGENT";
        public static final String PARTNER = "ROLE_PARTNER";
    }
}
