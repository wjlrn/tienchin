package org.javaboy.tienchin.common.constant;

public interface TienchinConstants {
    int CLUE_TYPE = 0;
    int BUSSINESS_TYPE = 1;

    /**
     * 下次跟进时间，单位是小时
     */
    int NEXT_FOLLOW_TIME = 24;

    int CLUE_ALLOCATED = 1;
    int CLUE_FOLLOING = 2;
    int CLUE_RECOVERY = 3;
    int CLUE_INVALIDATE = 4;

    int BUSINESS_ALLOCATED = 1;
    int BUSINESS_FOLLOING = 2;
    int BUSINESS_RECOVERY = 3;

    String ADMIN_USERNAME = "admin";
    Long ADMIN_ID = 1l;
    Long ADMIN_DEPT_ID = 103l;
}
