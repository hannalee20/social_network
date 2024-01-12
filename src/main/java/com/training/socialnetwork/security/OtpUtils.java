package com.training.socialnetwork.security;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.training.socialnetwork.util.constant.Constant;

@Component
public class OtpUtils {

	private static final Integer EXPIRE_MINS = 4;
	private LoadingCache<String, Integer> otpCache;
	
	public OtpUtils() {
		super();
		otpCache = CacheBuilder.newBuilder()
				.expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES)
				.build(new CacheLoader<String, Integer>() {
					public Integer load(String key) {
						return 0;
					}
				});
	}
	
	public int generateOtp(String key) throws Exception {
		Random random =  new Random();
		int otp = 100000 + random.nextInt(900000);
		
		if(otp == -1) {
			throw new Exception(Constant.SERVER_ERROR);
		}
		otpCache.put(key, otp);
		
		return otp;
	}
	
	public int getOtp(String key) {
		try {
			return otpCache.get(key);
		} catch (Exception e) {
			return 0;
		}
	}
	
	public void clearOtp(String key) {
		otpCache.invalidate(key);
	}
	
	public boolean validateOtp(String key, int otp) {
		int otpFromCache = getOtp(key);
		
		if (otpFromCache > 0 && otpFromCache == otp) {
			clearOtp(key);
			return true;
		}
		return false;
	}
}
