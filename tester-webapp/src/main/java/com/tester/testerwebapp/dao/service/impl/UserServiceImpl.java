package com.tester.testerwebapp.dao.service.impl;

import com.tester.testercommon.model.request.IdAndNameRequest;
import com.tester.testerwebapp.dao.domain.UserDomain;
import com.tester.testerwebapp.dao.mapper.UserMapper;
import com.tester.testerwebapp.dao.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @Author 温昌营
 * @Date
 */
@Slf4j
@Service
public class UserServiceImpl extends BaseServiceImpl<Long, UserDomain> implements UserService, InitializingBean {

    @Resource
    private UserMapper userMapper;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.setBaseMapper(userMapper);
    }


    @Override
    public UserDomain selectUserId(Long id) {
        return userMapper.selectUserId(id);
    }

    @Override
    public List<UserDomain> listByIds(Set<Long> ids) {
        return userMapper.listByIds(ids);
    }

    @Override
    public List<UserDomain> listByName(IdAndNameRequest request) {
        return userMapper.listByName(request);
    }

    @Override
    public int batchSaveUser(List<UserDomain> list){
        return userMapper.batchSaveUser(list);
    }


}

