#
# Copyright (C) 2008 The Android Open Source Project
# Copyright (C) 2019 The Derp-CAF Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_SRC_FILES := $(call all-java-files-under, app)
LOCAL_PACKAGE_NAME := Derp-Ota
LOCAL_CERTIFICATE := platform
LOCAL_USE_AAPT2 := true
LOCAL_SDK_VERSION := current
LOCAL_RESOURCE_DIR := $(LOCAL_PATH)/app/src/main/res
LOCAL_FULL_MANIFEST_FILE := $(LOCAL_PATH)/app/src/main/AndroidManifest.xml
LOCAL_STATIC_ANDROID_LIBRARIES := \
        android-support-v4 \
        android-support-v7-appcompat \
        android-support-v13
include $(BUILD_PACKAGE)
include $(call all-makefiles-under,$(LOCAL_PATH))
