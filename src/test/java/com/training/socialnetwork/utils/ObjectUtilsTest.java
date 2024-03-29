package com.training.socialnetwork.utils;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.BeanUtils;

import com.training.socialnetwork.dto.request.user.UserUpdateRequestDto;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.util.mapper.ObjectUtils;

public class ObjectUtilsTest {

	@InjectMocks
    private ObjectUtils objectUtils;
	
	@Test
    public void copyPropertiesTest() {
		ObjectUtils objectMapper = new ObjectUtils();
        UserUpdateRequestDto userUpdateDto = new UserUpdateRequestDto();
        User userToUpdate = new User();
        
        objectMapper.copyProperties(userUpdateDto, userToUpdate);
        
        BeanUtils.copyProperties(userUpdateDto, userToUpdate, objectMapper.getNullPropertyNames(userUpdateDto));
    }

    @Test
    public void getNullPropertyNamesTest() {
    	ObjectUtils objectMapper = new ObjectUtils();
    	UserUpdateRequestDto userUpdateDto = new UserUpdateRequestDto();
        
        objectMapper.getNullPropertyNames(userUpdateDto);
    }
    
    @Test
    public void testGetDefaulterWithEmptyString() {
    	objectUtils = new ObjectUtils();
        User user = new User();
        user.setUsername("");
        user.setUserId(0);
        
        objectUtils.getDefaulter(user);
    }
}
