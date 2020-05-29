package com.custom.rac.datamanagement.util;

import java.util.regex.Pattern;

public class MyCharNumber {
	
	private String str;
	
	public MyCharNumber(String str) {
		//去除数字，只保留字母
		this.str = Pattern.compile("[\\d]").matcher(str).replaceAll("").toUpperCase();
	}
	
	public int getASCII() {
		return 0;
	}
	
	public String getStr() {
		return str;
	}
	
	/**
	 * 将字符串转换成26进制的数
	 * @return
	 */
	public int getValue() {
		
		String ascii = stringToAscii(str);
		
		String[] asciis = ascii.split(",");
		
		int sumValue = 0;
		for(int i = 0; i < asciis.length; i++) {
			int by = asciis.length - 1 - i;
			sumValue += Math.pow(26, by) * (Integer.parseInt(asciis[i])-64);
		}
		
		return sumValue;
	}

	public static void main(String[] args) {
		MyCharNumber mcn5 = new MyCharNumber("AC");
		System.out.println(mcn5.getValue() + ", " + (mcn5.getValue() == 29));
		
		MyCharNumber mcn1 = new MyCharNumber("A");
		System.out.println(mcn1.getValue() + ", " + (mcn1.getValue() == 1));
		
		MyCharNumber mcn2 = new MyCharNumber("C");
		System.out.println(mcn2.getValue() + ", " + (mcn2.getValue() == 3));
		
		MyCharNumber mcn4 = new MyCharNumber("Z");
		System.out.println(mcn4.getValue() + ", " + (mcn4.getValue() == 26));
		
		MyCharNumber mcn3 = new MyCharNumber("AA");
		System.out.println(mcn3.getValue() + ", " + (mcn3.getValue() == 27));
		
		MyCharNumber mcn7 = new MyCharNumber("AZ");
		System.out.println(mcn7.getValue() + ", " + (mcn7.getValue() == 52));
		
		MyCharNumber mcn8 = new MyCharNumber("DB");
		System.out.println(mcn8.getValue() + ", " + (mcn8.getValue() == 26 * 4 +2));
		
		MyCharNumber mcn9 = new MyCharNumber("DTD");
		System.out.println(
			mcn9.getValue() + 
			", " + 
				(mcn9.getValue() == 26 * 26 * 4 + 26 * 20 + 4));
	}
	
	
	/** 
     * 字符串转换为Ascii 
     * @param value 
     * @return 
     */ 
    public static String stringToAscii(String value)  
    {  
        StringBuffer sbu = new StringBuffer();  
        char[] chars = value.toCharArray();   
        for (int i = 0; i < chars.length; i++) {  
            if(i != chars.length - 1)  
            {  
                sbu.append((int)chars[i]).append(",");  
            }  
            else {  
                sbu.append((int)chars[i]);  
            }  
        }  
        return sbu.toString(); 
    } 
    
    /** 
     * Ascii转换为字符串 
     * @param value 
     * @return 
     */ 
    public static String asciiToString(String value) 
    { 
        StringBuffer sbu = new StringBuffer(); 
        String[] chars = value.split(","); 
        for (int i = 0; i < chars.length; i++) { 
            sbu.append((char) Integer.parseInt(chars[i])); 
        } 
        return sbu.toString(); 
    } 

}
