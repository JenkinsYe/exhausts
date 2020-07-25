package com.csdn.xs.exhausts.webservice;

import com.csdn.xs.exhausts.domain.RemoteSenseDomain;
import com.csdn.xs.exhausts.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

/**
 * @author YJJ
 * @Date: Created in 11:02 2019-12-08
 */
@WebService
@Deprecated
public class RemoteSenseUploadService {

    @Autowired
    private DataService dataService;

    @WebMethod
    public void uploadRemoteSense(List<RemoteSenseDomain> domains) {
        dataService.insertAllRemoteSense(domains);
    }
}
