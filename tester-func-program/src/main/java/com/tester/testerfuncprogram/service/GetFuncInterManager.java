package com.tester.testerfuncprogram.service;

import com.tester.testerfuncprogram.interfaces.GetFunction;
import org.springframework.stereotype.Service;

@Service("getFuncInterManager")
public class GetFuncInterManager {

    public GetFuncInterManager getMySelf(){
        return getMySelf(GetFuncInterManager::gett);
    }

    private <T> GetFuncInterManager getMySelf (GetFunction<T> t){
        return new GetFuncInterManager();
    }

    private static GetFuncInterManager gett(){
        return new GetFuncInterManager();
    }
}
