@FunctionalInterface
public interface RegenerationStrategy {
    RegenerativeFoodSource regenerate(RegenerativeFoodSource foodSource);
}
