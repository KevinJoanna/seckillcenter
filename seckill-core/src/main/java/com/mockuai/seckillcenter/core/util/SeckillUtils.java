package com.mockuai.seckillcenter.core.util;


import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mockuai.seckillcenter.common.api.SeckillResponse;
import com.mockuai.seckillcenter.common.constant.ResponseCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;


public final class SeckillUtils {

    public static int getCombinedPlatformDBValue(String param) {

        Preconditions.checkArgument(StringUtils.isNotBlank(param), "\"平台环境\"的值为空!");

        Iterable<String> platforms = Splitter.on(",").omitEmptyStrings().trimResults().split(param);

        int combinedValue = 0;

        for (String tmp : platforms) {

            combinedValue |= getPlatformDBValue(Integer.valueOf(tmp).intValue());

        }

        return combinedValue;

    }


    private static int getPlatformDBValue(int platform) {

        Preconditions.checkArgument(platform > 0, "\"平台环境\"的值应该是一个大于零的数。plateform = " + platform);

        Preconditions.checkArgument(platform < 31, "\"平台环境\"的值应该是一个小于等于30的数。plateform = " + platform);

        return (int) Math.pow(2.0D, platform - 1);

    }


    public static String getDispPlatform(int platform) {

        String binaryStr = StringUtils.reverse(Integer.toBinaryString(platform));

        StringBuffer dispValue = new StringBuffer();

        for (int i = 0; i < binaryStr.length(); i++) {

            if (binaryStr.charAt(i) == '1') {

                dispValue.append(i + 1).append(",");

            }

        }

        return dispValue.substring(0, dispValue.length() - 1);

    }


    public static <K, V> void copyProperties(K orig, V dest) {

        Assert.notNull(orig, "source object must not be null");

        Assert.notNull(dest, "target object must not be null");


        Method[] sourceMethods = orig.getClass().getDeclaredMethods();

        for (Method sourceMethod : sourceMethods) {

            String srcMethodName = sourceMethod.getName();

            if (StringUtils.startsWith(srcMethodName, "set")) {

                Method targetMethod = null;

                try {

                    targetMethod = dest.getClass().getMethod(srcMethodName, sourceMethod.getParameterTypes());

                } catch (Exception e) {

                    continue;

                }


                String getMethodName = "get" + srcMethodName.substring(3);

                try {

                    targetMethod.invoke(dest, new Object[]{orig.getClass().getMethod(getMethodName, new Class[0]).invoke(orig, new Object[0])});

                } catch (Exception e) {

                    throw new RuntimeException("copy properties error.orig:" + JSONObject.toJSONString(orig) + ",dest:" + JSONObject.toJSONString(dest));

                }

            }

        }

    }


    public static <T> SeckillResponse<T> getSuccessResponse() {
        return new SeckillResponse(ResponseCode.SUCCESS);
    }


    public static <T> SeckillResponse<T> getSuccessResponse(T module, int total) {
        SeckillResponse response = new SeckillResponse(module, total);
        response.setResCode(ResponseCode.SUCCESS.getCode());
        response.setMessage(ResponseCode.SUCCESS.getMessage());
        return response;
    }

    public static <T> SeckillResponse<T> getSuccessResponse(T module) {
        return getSuccessResponse(module, 1);
    }

    public static <T> SeckillResponse<T> getFailResponse(int errorCode, String msg) {
        return new SeckillResponse(errorCode, msg);
    }

    public static <T> SeckillResponse<T> getFailResponse(ResponseCode responseCode) {
        return new SeckillResponse<T>(responseCode);
    }

    public static void main(String[] args) {

        String a = "1,3,5";

        String b = "6";


        System.out.println(getCombinedPlatformDBValue(a));

        System.out.println(getCombinedPlatformDBValue(b));


        System.out.println(getCombinedPlatformDBValue(b) & getCombinedPlatformDBValue(a));


        List<Integer> list = Lists.newArrayList(new Integer[]{Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5)});

        for (Iterator inte = list.iterator(); inte.hasNext(); ) {

            Integer aaa = (Integer) inte.next();

            if (aaa.intValue() == 1) {

                inte.remove();

            }
        }

        for (Integer intea : list)

            if (intea.intValue() == 2)
                list.remove(intea);
    }
}