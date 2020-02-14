/**
 * Copyright (c) 2014-present, Facebook, Inc. All rights reserved.
 *
 * You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
 * copy, modify, and distribute this software in source code or binary form for use
 * in connection with the web services and APIs provided by Facebook.
 *
 * As with any software that integrates with the Facebook platform, your use of
 * this software is subject to the Facebook Developer Principles and Policies
 * [http://developers.facebook.com/policy/]. This copyright notice shall be
 * included in all copies or substantial portions of the software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.facebook.internal.instrument.crashreport;

import com.facebook.FacebookSdk;
import com.facebook.internal.instrument.InstrumentData;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public class CrashShieldHandler {

    private static final Set<Object> sCrashingObjects
            = Collections.newSetFromMap(new WeakHashMap<Object, Boolean>());

    public static void handleThrowable(Throwable t, Object o) {
        sCrashingObjects.add(o);
        if (FacebookSdk.getAutoLogAppEventsEnabled()) {
            InstrumentData instrumentData = new InstrumentData(t, InstrumentData.Type.CrashShield);
            instrumentData.save();
        }
    }

    public static boolean isObjectCrashing(Object o) {
        return sCrashingObjects.contains(o);
    }

    public static void methodFinished(Object o) { }

    public static void reset() {
        resetCrashingObjects();
    }

    public static void resetCrashingObjects() {
        sCrashingObjects.clear();
    }
}
