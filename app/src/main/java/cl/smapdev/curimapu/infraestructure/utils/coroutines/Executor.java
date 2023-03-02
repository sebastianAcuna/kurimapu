package cl.smapdev.curimapu.infraestructure.utils.coroutines;

public interface Executor {
    public void execute(Runnable runnable);
}
