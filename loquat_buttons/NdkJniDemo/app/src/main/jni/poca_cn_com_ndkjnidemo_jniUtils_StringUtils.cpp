//
// Created by WilliamJiang on 16/6/16.
//

#include "poca_cn_com_ndkjnidemo_jniUtils_StringUtils.h"

JNIEXPORT jstring JNICALL Java_poca_cn_com_ndkjnidemo_jniUtils_StringUtils_getStringFromC
        (JNIEnv *env, jclass obj) {
    return env->NewStringUTF("this string from c");
}