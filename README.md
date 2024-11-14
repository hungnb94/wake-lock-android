# Wake Lock Example on Android

In Android, a Wake Lock is a powerful mechanism that allows your application to control the power state of the device, ensuring that the CPU or screen stays active while your app performs essential tasks. This is especially useful for apps that need to maintain network connections, run background tasks, or keep the device awake for a specific period, such as during long data uploads, real-time navigation, or media playback.

In this example, we’ll demonstrate how to acquire and release a Partial Wake Lock in an Android app. A Partial Wake Lock keeps the CPU running but allows the screen and other resources to turn off, helping to save battery while maintaining network or background activity.
## Example Code

The following code illustrates a basic Android class that acquires a Partial Wake Lock, holds it for a specified duration, and then releases it. This prevents the device from going into sleep mode while the app completes its background task.
## Step 1: Request Permission

First, in the AndroidManifest.xml, request the necessary permission to use Wake Locks:
```xml
<uses-permission android:name="android.permission.WAKE_LOCK" />
```

## Step 2: Acquire and Release Wake Lock

In the WakeLockExample.java file, we use the PowerManager service to acquire and release a Wake Lock.
```java
import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

public class WakeLockExample {
    private PowerManager.WakeLock wakeLock;

    // Method to acquire the Wake Lock
    public void acquireWakeLock(Context context) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::WakeLockExample");
            wakeLock.acquire(10 * 60 * 1000L /* 10 minutes */);
            Log.d("WakeLockExample", "Wake Lock acquired.");
        }
    }

    // Method to release the Wake Lock
    public void releaseWakeLock() {
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
            Log.d("WakeLockExample", "Wake Lock released.");
        }
    }
}
```

## Explanation

- Acquire Wake Lock: The acquireWakeLock() method initializes the Wake Lock with a PARTIAL_WAKE_LOCK flag, allowing only the CPU to stay awake. The acquire() method is given a timeout of 10 minutes, after which the lock will automatically release if not done manually.
- Release Wake Lock: The releaseWakeLock() method checks if the Wake Lock is held and releases it, freeing the CPU to go back to its usual sleep mode.

## Important Notes

- Battery Impact: While a Partial Wake Lock is active, the CPU stays on, which can increase battery usage. Use Wake Locks sparingly and release them as soon as possible to preserve battery.
- Timeout: Setting a timeout in acquire() helps avoid excessive battery drain if the lock isn't explicitly released.
- Best Practices: Always wrap Wake Lock acquisition in a try-finally block, or use an onDestroy() method in a Service to ensure it’s released properly.

This example serves as a basic introduction to using Wake Locks on Android.