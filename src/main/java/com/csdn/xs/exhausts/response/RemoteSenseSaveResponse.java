package com.csdn.xs.exhausts.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author YJJ
 * @Date: Created in 10:53 2019-12-12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoteSenseSaveResponse {

    private boolean success = false;

    private String msg;
}
