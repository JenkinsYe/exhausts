package com.csdn.xs.exhausts.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.util.Date;

/**
 * @author YJJ
 * @Date: Created in 13:42 2020-06-17
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UtilsTest {


    @Test
    public void dateParseTest() {
        String dateString = "2020-06-17 15";
        Date date = null;
        try {
            date = DateUtils.dateStringToDate(dateString, "yyyy-MM-dd HH");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        log.info(date.toString());
    }
}
