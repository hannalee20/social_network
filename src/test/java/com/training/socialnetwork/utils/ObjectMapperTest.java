package com.training.socialnetwork.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;

import com.training.socialnetwork.dto.request.user.UserUpdateDto;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.util.mapper.ObjectMapper;

public class ObjectMapperTest {

	@Test
    public void testCopyProperties() {
		ObjectMapper objectMapper = new ObjectMapper();
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        User userToUpdate = new User();
        
        objectMapper.copyProperties(userUpdateDto, userToUpdate);
        
        BeanUtils.copyProperties(userUpdateDto, userToUpdate, objectMapper.getNullPropertyNames(userUpdateDto));
    }

    @Test
    public void testGetNullPropertyNames() {
    	ObjectMapper objectMapper = new ObjectMapper();
    	UserUpdateDto userUpdateDto = new UserUpdateDto();
        
        objectMapper.getNullPropertyNames(userUpdateDto);
    }
}
