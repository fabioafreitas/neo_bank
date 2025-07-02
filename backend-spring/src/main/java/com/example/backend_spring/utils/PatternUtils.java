package com.example.backend_spring.utils;

public class PatternUtils {
	public static final String ACCESS_PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{8,}$";
	public static final String TRANSACTION_PASSWORD_PATTERN = "^\\d{6}$";
}
