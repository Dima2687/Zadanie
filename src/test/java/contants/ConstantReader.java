package contants;

import org.aeonbits.owner.ConfigFactory;

public class ConstantReader {
    public static <T> T reader(Class<? extends MyConfig> clazz) {
        return (T) ConfigFactory.create(clazz);
    }
}
