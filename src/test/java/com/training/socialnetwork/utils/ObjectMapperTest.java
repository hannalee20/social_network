package com.training.socialnetwork.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;

import com.training.socialnetwork.dto.request.user.UserUpdateRequestDto;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.util.mapper.ObjectMapperUtils;

public class ObjectMapperTest {

	@Test
    public void testCopyProperties() {
		ObjectMapperUtils objectMapper = new ObjectMapperUtils();
        UserUpdateRequestDto userUpdateDto = new UserUpdateRequestDto();
        User userToUpdate = new User();
        
        objectMapper.copyProperties(userUpdateDto, userToUpdate);
        
        BeanUtils.copyProperties(userUpdateDto, userToUpdate, objectMapper.getNullPropertyNames(userUpdateDto));
    }

    @Test
    public void testGetNullPropertyNames() {
    	ObjectMapperUtils objectMapper = new ObjectMapperUtils();
    	UserUpdateRequestDto userUpdateDto = new UserUpdateRequestDto();
        
        objectMapper.getNullPropertyNames(userUpdateDto);
    }
}
