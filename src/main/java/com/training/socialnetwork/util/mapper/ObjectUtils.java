package com.training.socialnetwork.util.mapper;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

@Component
public class ObjectUtils {

	public void copyProperties(Object source, Object destination) {
		BeanUtils.copyProperties(source, destination, getNullPropertyNames(source));
	}

	public String[] getNullPropertyNames(Object source) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
		Set<String> emptyNames = new HashSet<>();
		for (java.beans.PropertyDescriptor pd : pds) {
			Object srcValue = src.getPropertyValue(pd.getName());
			if (srcValue == null)
				emptyNames.add(pd.getName());
		}
		String[] result = new String[emptyNames.size()];
		return emptyNames.toArray(result);
	}
	
	public Object getDefaulter(Object obj) {
	    Arrays.stream(obj.getClass().getDeclaredFields()).map(f -> {
	        f.setAccessible(true);
	        try {
	            if (Objects.equals(f.get(obj), "") || Objects.equals(f.get(obj), 0) || Objects.equals(f.get(obj), Arrays.asList(0))) {
	                f.set(obj, null);
	            }
	        } catch (IllegalArgumentException | IllegalAccessException e) {
	            return null;
	        }
	        return f;
	    }).collect(Collectors.toList());
	    return obj;
	}
	
	public boolean checkNull(Object obj) throws IllegalAccessException {
		for (Field f : obj.getClass().getDeclaredFields()) {
			f.setAccessible(true);
	        if (f.get(obj) != null) {
	        	return false;
	        }
		}
	    return true; 
	}
}
