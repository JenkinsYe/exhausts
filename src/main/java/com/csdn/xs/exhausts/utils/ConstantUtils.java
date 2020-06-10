package com.csdn.xs.exhausts.utils;

import com.csdn.xs.exhausts.domain.LocationDomain;

import java.util.HashMap;

/**
 * @author YJJ
 * @Date: Created in 11:04 2019-12-12
 */
public class ConstantUtils {

    public static final String IMG_PATH = "/root/imgs";

    public static final String IMG_VDB_PATH = "/imgs_vdb";

    /** 新城路地点信息 两台设备编号: 19116和19108*/
    private static final LocationDomain location1 = new LocationDomain(120.305313d, 30.184383d,
            "浙江省杭州市萧山区新城路");

    /** 奔竞大道地点信息 两台设备编号: 19102和19105*/
    private static final LocationDomain location2 = new LocationDomain(120.230859d, 30.224070d,
            "浙江省杭州市萧山区奔竞大道");

    public static final HashMap<Integer, LocationDomain> locationMap = new HashMap<Integer, LocationDomain>() {
        {
            put(1, location1);
            put(19116, location1);
            put(19108, location1);
            put(19102, location2);
            put(19105, location2);
        }
    };

    public static LocationDomain getLocationByFixture(Integer fixture) {
        if (locationMap.containsKey(fixture))
            return locationMap.get(fixture);
        return null;
    }
}
