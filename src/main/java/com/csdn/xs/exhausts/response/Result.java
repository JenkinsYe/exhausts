package com.csdn.xs.exhausts.response;

import java.util.HashMap;

/**
 * @author YJJ
 * @Date: Created in 10:08 2020-06-10
 */
public class Result {
        private Long code;
        private String msg;
        private HashMap<String, Object> data;

        public Long getCode() {
            return code;
        }

        public void setCode(Long code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public HashMap<String, Object> getData() {
            return data;
        }

        public void setData(HashMap<String, Object> data) {
            this.data = data;
        }

        public Result success() {
            this.code = 200l;
            this.msg = "成功";
            return this;
        }

        public Result success(HashMap<String, Object> data) {
            this.code = 200l;
            this.msg = "成功";
            this.data = data;
            return this;
        }

        public Result fail() {

            this.code = 400l;
            this.msg = "失败";
            return this;
        }
}
