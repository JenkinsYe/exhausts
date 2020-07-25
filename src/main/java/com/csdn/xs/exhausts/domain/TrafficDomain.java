package com.csdn.xs.exhausts.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 交通小脑数据统计实体类
 * @author YJJ
 * @Date: Created in 14:53 2020-06-17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrafficDomain {
    Long id;

    String channelName;

    String channelID;

    Date time;

    Integer flow;

    String flowString;
}
