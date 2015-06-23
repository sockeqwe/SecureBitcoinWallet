LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := OpenMobileApiSample
LOCAL_SRC_FILES := OpenMobileApiSample.cpp

include $(BUILD_SHARED_LIBRARY)
