#include "net_java_games_input_EventInterface.h"
#include "eventInterface.c"

/*
 * Class:     net_java_games_input_EventInterface
 * Method:    eventInit
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_net_java_games_input_EventInterface_eventInit
  (JNIEnv *, jclass){
  	eventInit();
  }

/*
 * Class:     net_java_games_input_EventInterface
 * Method:    getNumDevices
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_net_java_games_input_EventInterface_getNumDevices
  (JNIEnv *, jclass) {
  	return getNumDevices();
  }

/*
 * Class:     net_java_games_input_EventInterface
 * Method:    getFFEnabled
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_net_java_games_input_EventInterface_getFFEnabled
  (JNIEnv *, jclass, jint device) {
  	return getFFEnabled(device);
  }

/*
 * Class:     net_java_games_input_EventInterface
 * Method:    getName
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_net_java_games_input_EventInterface_getName
  (JNIEnv *env, jclass, jint device) {
    return env->NewStringUTF(getName(device));
  }

/*
 * Class:     net_java_games_input_EventInterface
 * Method:    rumble
 * Signature: (IF)V
 */
JNIEXPORT void JNICALL Java_net_java_games_input_EventInterface_rumble
  (JNIEnv *, jclass, jint device, jfloat force) {
  	rumble(device, force);
  }


/*
 * Class:     net_java_games_input_EventInterface
 * Method:    getBusType
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_java_games_input_EventInterface_getBusType
  (JNIEnv *, jclass, jint device){
  	return getBusType(device);
  }

/*
 * Class:     net_java_games_input_EventInterface
 * Method:    getVendorID
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_java_games_input_EventInterface_getVendorID
  (JNIEnv *, jclass, jint device){
  	return getVendorID(device);
  }

/*
 * Class:     net_java_games_input_EventInterface
 * Method:    getProductID
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_java_games_input_EventInterface_getProductID
  (JNIEnv *, jclass, jint device){
  	return getProductID(device);
  }

/*
 * Class:     net_java_games_input_EventInterface
 * Method:    getVersion
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_java_games_input_EventInterface_getVersion
  (JNIEnv *, jclass, jint device){
  	getVersion(device);
  }

/*
 * Class:     net_java_games_input_EventInterface
 * Method:    getSupportedRelAxes
 * Signature: (I[I)V
 */
JNIEXPORT void JNICALL Java_net_java_games_input_EventInterface_getSupportedRelAxes
  (JNIEnv *env, jclass, jint device, jintArray axesData){
    jint *axisReturns = env->GetIntArrayElements(axesData, 0);
    getSupportedRelAxes(device, axisReturns);
    env->ReleaseIntArrayElements(axesData, axisReturns, 0);  	
  }

/*
 * Class:     net_java_games_input_EventInterface
 * Method:    getSupportedAbsAxes
 * Signature: (I[I)V
 */
JNIEXPORT void JNICALL Java_net_java_games_input_EventInterface_getSupportedAbsAxes
  (JNIEnv *env, jclass, jint device, jintArray axesData){
  	jint *axisReturns = env->GetIntArrayElements(axesData, 0);
  	getSupportedAbsAxes(device, axisReturns);
  	env->ReleaseIntArrayElements(axesData, axisReturns, 0);
  }

/*
 * Class:     net_java_games_input_EventInterface
 * Method:    getSupportedButtons
 * Signature: (I[I)V
 */
JNIEXPORT void JNICALL Java_net_java_games_input_EventInterface_getSupportedButtons
  (JNIEnv *env, jclass, jint device, jintArray buttonData){
  	jint *buttonReturns = env->GetIntArrayElements(buttonData, 0);
  	getSupportedButtons(device, buttonReturns);
  	env->ReleaseIntArrayElements(buttonData, buttonReturns, 0);
}
/*
 * Class:     net_java_games_input_EventInterface
 * Method:    poll
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_java_games_input_EventInterface_poll
  (JNIEnv *, jclass, jint device){
  	return poll(device);
  }

/*
 * Class:     net_java_games_input_EventInterface
 * Method:    getPolledData
 * Signature: (I[I[I[I)V
 */
JNIEXPORT void JNICALL Java_net_java_games_input_EventInterface_getPolledData
  (JNIEnv *env, jclass, jint device, jintArray relAxisData, jintArray absAxisData, jintArray buttonData) {
    jint *buttonElements = env->GetIntArrayElements(buttonData, 0);
    jint *relAxesElements = env->GetIntArrayElements(relAxisData, 0);
    jint *absAxesElements = env->GetIntArrayElements(absAxisData, 0);
    
    getPolledData(device, relAxesElements, absAxesElements, buttonElements);
    
    env->ReleaseIntArrayElements(buttonData, buttonElements, 0);
    env->ReleaseIntArrayElements(relAxisData, relAxesElements, 0);
    env->ReleaseIntArrayElements(absAxisData, absAxesElements, 0);    
  }

/*
 * Class:     net_java_games_input_EventInterface
 * Method:    getAbsAxisMinimum
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_net_java_games_input_EventInterface_getAbsAxisMinimum
  (JNIEnv *, jclass, jint device, jint axis) {
  	return getAbsAxisMinimum(device,axis);
  }

/*
 * Class:     net_java_games_input_EventInterface
 * Method:    getAbsAxisMaximum
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_net_java_games_input_EventInterface_getAbsAxisMaximum
  (JNIEnv *, jclass, jint device, jint axis) {
  	return getAbsAxisMaximum(device,axis);
  }

/*
 * Class:     net_java_games_input_EventInterface
 * Method:    getAbsAxisFuzz
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_net_java_games_input_EventInterface_getAbsAxisFuzz
  (JNIEnv *, jclass, jint device, jint axis) {
  	return getAbsAxisFuzz(device,axis);
  }

/*
 * Class:     net_java_games_input_EventInterface
 * Method:    isValidDevice
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_java_games_input_EventInterface_isValidDevice
  (JNIEnv *, jclass, jint device) {
  	return isValidDevice(device);
  }

/*
 * Class:     net_java_games_input_EventInterface
 * Method:    cleanup
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_java_games_input_EventInterface_cleanup
  (JNIEnv *, jclass, jint device) {
  	cleanup(device);
  }

/*
 * Class:     net_java_games_input_EventInterface
 * Method:    getNumberRelAxes
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_java_games_input_EventInterface_getNumberRelAxes
  (JNIEnv *, jclass, jint device) {
  	return getNumberRelAxes(device);
  }

/*
 * Class:     net_java_games_input_EventInterface
 * Method:    getNumberAbsAxes
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_java_games_input_EventInterface_getNumberAbsAxes
  (JNIEnv *, jclass, jint device) {
  	return getNumberAbsAxes(device);
  }

/*
 * Class:     net_java_games_input_EventInterface
 * Method:    getNumberButtons
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_java_games_input_EventInterface_getNumberButtons
  (JNIEnv *, jclass, jint device) {
  	return getNumberButtons(device);
  }
