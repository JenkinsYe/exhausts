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

    public static HashMap<String, Object> vehicleMap = new HashMap<String, Object>() {
        {
            put("0", "未知");
            put("1", "小型汽车");
            put("2", "大型汽车");
            put("3", "使馆汽车");
            put("4", "领馆汽车");
            put("5", "境外汽车");
            put("6", "外籍汽车");
            put("7", "低速汽车");
            put("8", "拖拉机");
            put("9", "挂车");
            put("10", "教练汽车");
            put("11", "临时行驶车");
            put("12", "警用汽车");
            put("13", "警用摩托车");
            put("14", "两、三轮摩托车");
            put("15", "轻便摩托车");
            put("16", "机动车");
            put("17", "公交车");
            put("18", "摩托车");
            put("19", "客车");
            put("20", "大货车");
            put("21", "中货车");
            put("22", "轿车");
            put("23", "面包车");
            put("24", "小货车");
            put("256", "非机动车");
            put("257", "自行车");
            put("258", "三轮车");
            put("512", "行人");
            put("513", "军用车");
            put("514", "槽罐车");
        }
    };
}
