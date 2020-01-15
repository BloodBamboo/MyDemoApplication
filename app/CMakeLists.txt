# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

#批量引用文件
#file(GLOB allCpp *.cpp) ---allCpp变量名称， *.cpp文件路径 /a/b/*.cpp

add_library( # Sets the name of the library.
        native-lib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        src/main/cpp/native-lib.c
        src/main/cpp/native_test.cpp
        #        ${allCpp} 批量文件使用
        )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        native-lib
        test-lib
        add

        #libavcodec libavfilter libavformat libavutil libswresample libswscale
        #需要依赖顺序，否则报错
        avformat avcodec avfilter avutil swresample swscale

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})


#设置so库路径
set(my_lib_path ${CMAKE_SOURCE_DIR}/libs)
#讲第三方库作为动态库引用
add_library(test-lib
        SHARED
        IMPORTED)
#指名第三方库的绝对路径, 早期写法 ANDROID_ABI 根据当前配置平台进行自动匹配 与 CMAKE_ANDROID_ARCH_ABI一样
set_target_properties(test-lib
        PROPERTIES IMPORTED_LOCATION
        ${my_lib_path}/${ANDROID_ABI}/libtest-lib.so)


#日志打印
#MESSAGE("路径  " ${DIR})
#这是旧的ffmpeg库路径和头文件
#include_directories(libs/include)
#add_library(avcodec-56
#        SHARED
#        IMPORTED)
#set_target_properties(avcodec-56
#        PROPERTIES IMPORTED_LOCATION
#        ${DIR}/armeabi/libavcodec-56.so)


add_library(add
        STATIC
        #        SHARED
        IMPORTED)
set_target_properties(add
        PROPERTIES IMPORTED_LOCATION
        ${my_lib_path}/${CMAKE_ANDROID_ARCH_ABI}/libadd.a)

#设置cpp路径
set(cpp_path ${CMAKE_SOURCE_DIR}/src/main/cpp)
#引入头文件
include_directories(${cpp_path}/include)
#引入ffmpeg库文件，设置内部方式引入，-L指定目录 -l指定库文件
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${cpp_path}/${CMAKE_ANDROID_ARCH_ABI}")


