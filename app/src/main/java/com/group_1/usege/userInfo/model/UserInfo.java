package com.group_1.usege.userInfo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private String userId;
    private String email;
    //Use kb unit
    private Long deletedImgCount;
    private Long imgCount;
    private Long albumCount;
    private Long usedSpace;
    private Long maxSpace;
    private String plan;
    private int planOrder;
    private String purchasedPlanDate;
}
