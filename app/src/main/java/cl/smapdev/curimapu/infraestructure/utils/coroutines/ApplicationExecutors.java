package cl.smapdev.curimapu.infraestructure.utils.coroutines;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.MainThread;
import java.lang.Runnable;
import java.util.concurrent.Executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApplicationExecutors {
    private final ExecutorService background;
    private final Executor mainThread;

    public ExecutorService getBackground(){
        return background;
    }

    public Executor getMainThread() {
        return mainThread;
    }

    public ApplicationExecutors() {
        this.background = Executors.newSingleThreadExecutor();
        this.mainThread = new MainTreadExecutor();
    }

    public void shutDownBackground() {
        this.background.shutdown();
    }

    public static class MainTreadExecutor  implements  Executor {
        private Handler mainTreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            mainTreadHandler.post(command);
        }


    }

}
